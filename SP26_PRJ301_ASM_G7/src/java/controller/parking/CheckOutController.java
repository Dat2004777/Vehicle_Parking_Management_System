package controller.parking;

import model.Employee;
import dal.CardDAO;
import dal.SessionDAO;
import dal.AreaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import model.ParkingCard;
import model.ParkingSession;
import utils.UrlConstants;
import utils.ValidationUtils;

@WebServlet(name = "CheckOutController", urlPatterns = {UrlConstants.URL_STAFF + "/parking/checkout"})
public class CheckOutController extends HttpServlet {

    private final CardDAO cardDAO = new CardDAO();
    private final SessionDAO sessionDAO = new SessionDAO();
    private final AreaDAO areaDAO = new AreaDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        Employee emp = (Employee) session.getAttribute("staff");

        if (emp == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String cardId = request.getParameter("cardId");
        String licensePlate = request.getParameter("licensePlate");

        try {
            // Gọi hàm Helper private bên dưới
            String resultMessage = processCheckOut(
                    cardId.trim(),
                    licensePlate.trim().toUpperCase(),
                    emp.getSiteId(),
                    emp.getEmployeeId()
            );

            session.setAttribute("successMsg", resultMessage);

        } catch (Exception e) {
            session.setAttribute("errorMsg", e.getMessage());
        }

        String activeTab = request.getParameter("actionType");
        session.setAttribute("activeTab", activeTab);
        session.setAttribute("oldCardId", cardId);
        session.setAttribute("oldPlate", licensePlate);

        response.sendRedirect(request.getContextPath() + "/staff/dashboard");
    }

    // =========================================================================
    // HÀM CHÍNH XỬ LÝ CHECK-OUT
    // =========================================================================
    private String processCheckOut(String cardId, String licensePlate, int siteId, int staffId) throws Exception {

        // 1. Chuẩn hóa và kiểm tra dữ liệu đầu vào
        String cleanPlate = validateAndCleanCheckOutInput(cardId, licensePlate);

        // 2. Kiểm tra tính hợp lệ của Thẻ
        validateCardForCheckOut(cardId, siteId);

        // 3. Lấy phiên đỗ xe đang diễn ra
        ParkingSession currentSession = fetchActiveSession(cardId);

        // 4. Kiểm tra bảo mật: So khớp biển số (Chống cầm nhầm thẻ / Trộm xe)
        verifyLicensePlateMatch(currentSession.getLicensePlate(), cleanPlate);

        // 5. Tính toán phí đỗ xe dựa trên Phân loại vé
        long feeToCollect = calculateCheckoutFee(currentSession);

        // 6. Cập nhật dữ liệu xuống Database và thu hồi thẻ
        executeCheckOutTransaction(currentSession, feeToCollect, cardId, staffId);

        // 7. Trả về thông báo thành công
        return buildCheckOutSuccessMessage(cleanPlate, currentSession.getSessionType(), feeToCollect);
    }

// =========================================================================
// CÁC HÀM PHỤ TRỢ (HELPER METHODS)
// =========================================================================
// Hàm 1: Validate và dọn dẹp chuỗi đầu vào
    private String validateAndCleanCheckOutInput(String cardId, String licensePlate) throws Exception {
        if (cardId == null || cardId.trim().isEmpty() || licensePlate == null || licensePlate.trim().isEmpty()) {
            throw new Exception("Vui lòng nhập đầy đủ Mã thẻ và Biển số!");
        }
        // Dọn dẹp khoảng trắng, dấu chấm để so sánh chính xác với DB
        return ValidationUtils.cleanLicensePlate(licensePlate);
    }

// Hàm 2: Kiểm tra trạng thái thẻ khi ra bãi
    private void validateCardForCheckOut(String cardId, int siteId) throws Exception {
        ParkingCard card = cardDAO.getById(cardId);
        if (card == null) {
            throw new Exception("Thẻ [" + cardId + "] không tồn tại trên hệ thống!");
        }
        if (card.getSiteId() != siteId) {
            throw new Exception("Lỗi: Thẻ này thuộc bãi xe khác, không thể check-out tại đây!");
        }
        if (card.getState() != ParkingCard.State.USING) {
            throw new Exception("Lỗi: Thẻ này hiện không có xe nào đang sử dụng (Chưa check-in)!");
        }
    }

// Hàm 3: Lấy phiên đỗ xe
    private ParkingSession fetchActiveSession(String cardId) throws Exception {
        ParkingSession session = sessionDAO.getActiveSession(cardId);
        if (session == null) {
            throw new Exception("Lỗi hệ thống: Không tìm thấy dữ liệu xe vào cho thẻ này!");
        }
        return session;
    }

// Hàm 4: Xác thực biển số xe
    private void verifyLicensePlateMatch(String dbPlate, String inputPlate) throws Exception {
        String formattedDbPlate = dbPlate.trim().toUpperCase();
        String formattedInputPlate = inputPlate.trim().toUpperCase();

        if (!formattedDbPlate.equals(formattedInputPlate)) {
            throw new Exception("CẢNH BÁO BẢO MẬT: Biển số xe gửi lên [" + formattedInputPlate + "] KHÔNG KHỚP với biển số lúc vào [" + formattedDbPlate + "]!");
        }
    }

// Hàm 5: Tính toán phí đỗ xe
    private long calculateCheckoutFee(ParkingSession session) {
        String type = session.getSessionType(); // "casual", "subscription", "booking"

        // Nếu là vé tháng hoặc đã đặt trước -> Không thu phí tại cổng (Hoặc gọi logic tính phụ phí quá giờ)
        if ("noncasual".equalsIgnoreCase(type)) {
            return 0;
        }

        // Nếu là vé lượt ("casual") -> Tính tiền
        // TODO: Tương lai bạn thay bằng logic tính tiền thực tế:
        // return pricingService.calculateFee(session.getEntryTime(), LocalDateTime.now(), session.getVehicleTypeId());
        return 5000; // Tạm thu đồng giá 5.000đ cho vé lượt
    }

// Hàm 6: Ghi nhận giao dịch kết thúc xuống DB
    private void executeCheckOutTransaction(ParkingSession session, long fee, String cardId, int staffId) throws Exception {
        // Cập nhật thông tin Session
        session.setFeeAmount(fee);
        session.setSessionState("completed");
        session.setExitTime(LocalDateTime.now());
        // session.setStaffOutId(staffId); // Nếu DB của bạn sau này có lưu nhân viên cho xe ra

        boolean isSessionUpdated = sessionDAO.checkOut(session);
        if (!isSessionUpdated) {
            throw new Exception("Lỗi CSDL: Không thể lưu thông tin xe ra!");
        }

        // Giải phóng Thẻ (Trả về trạng thái AVAILABLE)
        cardDAO.updateState(cardId, ParkingCard.State.AVAILABLE);
    }

// Hàm 7: Xây dựng câu thông báo
    private String buildCheckOutSuccessMessage(String licensePlate, String sessionType, long fee) {
        java.text.DecimalFormat formatter = new java.text.DecimalFormat("#,###");
        String formattedFee = formatter.format(fee);

        String typeStr = "subscription".equalsIgnoreCase(sessionType) ? "Vé tháng"
                : ("booking".equalsIgnoreCase(sessionType) ? "Vé đặt trước" : "Vé lượt");

        return "Check-out thành công: <b>" + licensePlate.toUpperCase() + "</b><br>"
                + "Loại thẻ: " + typeStr + "<br>"
                + "Thu phí: <strong class='text-danger fs-5'>" + formattedFee + " VNĐ</strong>";
    }
}
