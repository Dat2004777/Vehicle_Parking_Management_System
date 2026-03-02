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
import java.util.List;
import model.ParkingArea;
import model.ParkingCard;
import model.ParkingSession;
import utils.UrlConstants;
import utils.ValidationUtils;

@WebServlet(name = "CheckInController", urlPatterns = {UrlConstants.URL_STAFF + "/parking/checkin"})
public class CheckInController extends HttpServlet {

    // Khai báo các DAO dùng chung cho class này
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
            response.sendRedirect(session.getAttribute("ctx") + "/login");
            return;
        }

        String cardId = request.getParameter("cardId");
        String licensePlate = request.getParameter("licensePlate");
        String vehicleTypeIdStr = request.getParameter("vehicleTypeId");

        try {
            int vehicleTypeId = ValidationUtils.requireValidInt(vehicleTypeIdStr, "Vehicle type id ");

            // Gọi thẳng hàm Helper private bên dưới
            String resultMessage = processCheckIn(
                    cardId.trim(),
                    licensePlate.trim().toUpperCase(),
                    vehicleTypeId,
                    emp.getSiteId()
            );

            session.setAttribute("successMsg", resultMessage);

        } catch (Exception e) {
            // Tóm gọn mọi lỗi ném ra từ hàm Helper (Ví dụ: "Thẻ không hợp lệ")
            session.setAttribute("errorMsg", e.getMessage());
            session.setAttribute("oldCardId", cardId);
            session.setAttribute("oldPlate", licensePlate);
        }

        String activeTab = request.getParameter("actionType");
        session.setAttribute("activeTab", activeTab);

        response.sendRedirect(request.getContextPath() + "/staff/dashboard");
    }

    // =========================================================================
// HÀM CHÍNH XỬ LÝ CHECK-IN
// =========================================================================
    private String processCheckIn(String cardId, String licensePlate, int vehicleTypeId, int siteId) throws Exception {

        // 1. Chuẩn hóa và kiểm tra dữ liệu đầu vào
        String cleanPlate = validateAndCleanInput(cardId, licensePlate, vehicleTypeId);

        // 2. Kiểm tra tính hợp lệ của Thẻ và trạng thái Xe trong bãi
        validateCardAndVehicle(cardId, cleanPlate, siteId);

        // 3. Xác định phân loại phiên đỗ xe (Vé tháng / Đặt trước / Vé lượt)
        String sessionType = determineSessionType(cardId, cleanPlate, vehicleTypeId);

        // 4. Tìm khu vực đỗ xe còn trống
        ParkingArea targetArea = findAvailableArea(siteId, vehicleTypeId);

        // 5. Ghi nhận phiên đỗ xe xuống Database và khóa thẻ
        executeCheckInTransaction(cardId, cleanPlate, vehicleTypeId, sessionType);

        // 6. Trả về thông báo thành công
        return buildSuccessMessage(cleanPlate, sessionType, targetArea.getAreaName());
    }

// =========================================================================
// CÁC HÀM PHỤ TRỢ (HELPER METHODS)
// =========================================================================
// Hàm 1: Xử lý định dạng và dữ liệu đầu vào
    private String validateAndCleanInput(String cardId, String licensePlate, int vehicleTypeId) throws Exception {
        if (cardId == null || cardId.trim().isEmpty() || licensePlate == null || licensePlate.trim().isEmpty()) {
            throw new Exception("Vui lòng nhập đầy đủ Mã thẻ và Biển số!");
        }
        if (vehicleTypeId == -1) {
            throw new Exception("Bãi xe đã hết chỗ cho loại xe này. \nKhông thể cho xe vào!");
        }

        boolean isValidFormat = false;
        if (vehicleTypeId == 2) { // Xe máy
            isValidFormat = ValidationUtils.isValidMotorbikePlate(licensePlate);
            if (!isValidFormat) {
                throw new Exception("Sai định dạng biển số Xe Máy! (VD: 29H1-12345)");
            }
        } else if (vehicleTypeId == 1) { // Ô tô
            isValidFormat = ValidationUtils.isValidCarPlate(licensePlate);
            if (!isValidFormat) {
                throw new Exception("Sai định dạng biển số Ô Tô! (VD: 30A-12345)");
            }
        }

        return ValidationUtils.cleanLicensePlate(licensePlate);
    }

// Hàm 2: Xác thực Thẻ & Xe
    private void validateCardAndVehicle(String cardId, String licensePlate, int siteId) throws Exception {
        ParkingCard card = cardDAO.getById(cardId);

        if (card == null) {
            throw new Exception("Thẻ [" + cardId + "] không tồn tại trên hệ thống!");
        }
        if (siteId != card.getSiteId()) {
            throw new Exception("Thẻ này không thuộc thẩm quyền của bãi xe hiện tại!");
        }
        if (card.getState() != ParkingCard.State.AVAILABLE) {
            throw new Exception("Thẻ này đang được xe khác sử dụng hoặc đang bị khóa!");
        }

        if (sessionDAO.isVehicleInLot(licensePlate, siteId)) {
            throw new Exception("Xe mang biển số [" + licensePlate + "] đang ở trong bãi, không thể Check-in lần 2!");
        }
    }

// Hàm 3: Xác định Logic Phân quyền vé (Vé tháng / Đặt trước / Vé lượt)
    private String determineSessionType(String cardId, String licensePlate, int vehicleTypeId) throws Exception {
        // Ưu tiên 1: Check Vé tháng
//        Subscription sub = subscriptionDAO.getValidSubscriptionByCard(cardId);
//        if (sub != null) {
//            if (!sub.getLicensePlate().equalsIgnoreCase(licensePlate)) {
//                throw new Exception("Lỗi: Thẻ vé tháng đăng ký cho biển số [" + sub.getLicensePlate() + "], không dùng được cho xe này!");
//            }
//            if (sub.getVehicleTypeId() != vehicleTypeId) {
//                throw new Exception("Lỗi: Thẻ vé tháng này đăng ký cho loại xe khác!");
//            }
//            return "subscription";
//        }
//
//        // Ưu tiên 2: Check Lịch đặt trước
//        Booking booking = bookingDAO.getValidBookingByCard(cardId);
//        if (booking != null) {
//            if (booking.getVehicleTypeId() != vehicleTypeId) {
//                throw new Exception("Lỗi: Lịch đặt trước của thẻ này dành cho loại xe khác!");
//            }
//            return "booking";
//        }

        // Mặc định: Trả về vé lượt nếu không có các điều kiện trên
        return "casual";
    }

// Hàm 4: Thuật toán tìm khu vực trống
    private ParkingArea findAvailableArea(int siteId, int vehicleTypeId) throws Exception {
        List<ParkingArea> areas = areaDAO.getAreasBySite(siteId);

        for (ParkingArea area : areas) {
            if (area.getVehicleTypeId() == vehicleTypeId) {
                int occupied = sessionDAO.countActiveSessionsByArea(area.getAreaId());
                if (occupied < area.getTotalSlots()) {
                    return area; // Trả về ngay khu vực đầu tiên còn trống
                }
            }
        }

        throw new Exception("Xin lỗi, khu vực đỗ cho loại xe này hiện đã không còn chỗ trống thực tế!");
    }

// Hàm 5: Ghi nhận giao dịch xuống Database
    private void executeCheckInTransaction(String cardId, String licensePlate, int vehicleTypeId, String sessionType) throws Exception {
        ParkingSession newSession = new ParkingSession();
        newSession.setCardId(cardId);
        newSession.setLicensePlate(licensePlate);
        newSession.setVehicleTypeId(vehicleTypeId);
        newSession.setSessionType(sessionType);
        newSession.setSessionState("parked");

        boolean isSessionCreated = sessionDAO.checkIn(newSession);
        if (!isSessionCreated) {
            throw new Exception("Lỗi hệ thống CSDL: Không thể tạo phiên đỗ xe!");
        }

        // Cập nhật trạng thái thẻ sang "Đang sử dụng"
        cardDAO.updateState(cardId, ParkingCard.State.USING);
    }

// Hàm 6: Xây dựng câu thông báo thành công cho giao diện
    private String buildSuccessMessage(String licensePlate, String sessionType, String areaName) {
        String typeStr = sessionType.equals("subscription") ? "Vé tháng"
                : (sessionType.equals("booking") ? "Vé đặt trước" : "Vé lượt");

        return "Check-in thành công: <b>" + licensePlate + "</b><br>"
                + "Loại vé: " + typeStr + "<br>"
                + "Vị trí: " + areaName;
    }
}
