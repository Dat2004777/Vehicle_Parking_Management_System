package controller.parking;

import model.Employee;
import dal.CardDAO;
import dal.SessionDAO;
import dal.AreaDAO;
import dal.EmployeeDAO;
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
//        Employee emp = (Employee) session.getAttribute("employee");
        Employee emp = new EmployeeDAO().getById(2);
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

        response.sendRedirect(request.getContextPath() + "/staff/dashboard");
    }

    // ================= HELPER METHOD: XỬ LÝ LOGIC CHECK-OUT =================
    private String processCheckOut(String cardId, String licensePlate, int siteId, int staffId) throws Exception {

        // 1. Validate dữ liệu đầu vào
        if (cardId == null || cardId.isEmpty() || licensePlate == null || licensePlate.isEmpty()) {
            throw new Exception("Vui lòng nhập đầy đủ Mã thẻ và Biển số!");
        }

        // 2. Lấy thông tin Thẻ
        ParkingCard card = cardDAO.getById(cardId);
        if (card == null) {
            throw new Exception("Thẻ [" + cardId + "] không tồn tại trên hệ thống!");
        }

        if (card.getSiteId() != siteId) {
            throw new Exception("Thẻ này thuộc bãi xe khác, không thể check-out tại đây!");
        }

        if (card.getState() != ParkingCard.State.USING) {
            throw new Exception("Thẻ này hiện không có xe nào đang sử dụng!");
        }

        // 3. Tìm phiên đỗ xe (Session) đang ACTIVE của thẻ này
        // Cần tạo hàm này trong ParkingSessionDAO: getActiveSessionByCard(String cardId)
        ParkingSession currentSession = sessionDAO.getActiveSession(cardId);
        if (currentSession == null) {
            throw new Exception("Lỗi hệ thống: Không tìm thấy dữ liệu xe vào cho thẻ này!");
        }

        // 4. KIỂM TRA BẢO MẬT: So khớp biển số xe (Chống trộm/Cầm nhầm thẻ)
        // Chuyển cả 2 về in hoa và xóa khoảng trắng để so sánh chính xác nhất
        String dbPlate = currentSession.getLicensePlate().trim().toUpperCase();
        String inputPlate = licensePlate.trim().toUpperCase();

        if (!dbPlate.equals(inputPlate)) {
            throw new Exception("CẢNH BÁO: Biển số xe gửi lên [" + inputPlate + "] KHÔNG KHỚP với biển số lúc vào [" + dbPlate + "]!");
        }

        // 5. TÍNH TOÁN TIỀN PHÍ (Dành cho VÉ LƯỢT)
        // Nếu hệ thống của bạn có vé tháng, bạn sẽ kiểm tra session_type ở đây. Hiện tại ta mặc định là tính theo vé lượt.
        long feeToCollect = 0;

        if ("GUEST".equalsIgnoreCase(currentSession.getSessionType())) {
            // Cần tạo hàm này để tính tiền (Dựa vào giờ vào, giờ ra hiện tại, và loại xe)
            // Nếu bạn chưa viết thuật toán tính phí, ta tạm gán một mức giá cố định (Ví dụ: 5000đ)
            feeToCollect = 5000;

            // TODO Tương lai: feeToCollect = pricingService.calculateFee(currentSession.getEntryTime(), currentSession.getVehicleTypeId());
        }

        // 6. CẬP NHẬT DATABASE
        // Cập nhật thông tin cho ParkingSession (Giờ ra, NV cho ra, Tiền phí, Trạng thái)
        currentSession.setFeeAmount(feeToCollect);
        currentSession.setSessionState("COMPLETED");
        // Thời gian ra (exit_time) sẽ được DAO tự động lấy lúc gọi lệnh UPDATE
        
        currentSession.setExitTime(LocalDateTime.now());
        
        boolean isSessionUpdated = sessionDAO.checkOut(currentSession); // Cần tạo hàm này trong SessionDAO

        if (!isSessionUpdated) {
            throw new Exception("Lỗi hệ thống: Không thể lưu thông tin xe ra!");
        }

        // 7. Giải phóng Thẻ (Trả về trạng thái AVAILABLE)
        cardDAO.updateState(cardId, ParkingCard.State.AVAILABLE);

        // 8. Định dạng số tiền để hiển thị (Ví dụ: 5,000 VNĐ)
        java.text.DecimalFormat formatter = new java.text.DecimalFormat("#,###");
        String formattedFee = formatter.format(feeToCollect);

        return "Xe " + inputPlate + " ra bãi thành công. <br>Thu phí: " + formattedFee + " VNĐ";
    }
}
