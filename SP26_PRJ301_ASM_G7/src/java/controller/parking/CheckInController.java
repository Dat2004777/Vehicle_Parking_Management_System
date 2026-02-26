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
//        Employee emp = (Employee) session.getAttribute("employee");
        Employee emp = new EmployeeDAO().getById(2);

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
        }

        response.sendRedirect(request.getContextPath() + "/staff/dashboard");
    }

    // ================= HELPER METHOD: XỬ LÝ LOGIC CHECK-IN =================
    private String processCheckIn(String cardId, String licensePlate, int vehicleTypeId, int siteId) throws Exception {

        // 1. Validate dữ liệu đầu vào
        if (cardId == null || cardId.isEmpty() || licensePlate == null || licensePlate.isEmpty()) {
            throw new Exception("Vui lòng nhập đầy đủ Mã thẻ và Biển số!");
        }

        boolean isValidFormat = false;

        // Giả sử: vehicleTypeId == 1 (Xe máy), vehicleTypeId == 2 (Ô tô)
        if (vehicleTypeId == 2) {
            isValidFormat = ValidationUtils.isValidMotorbikePlate(licensePlate);
            if (!isValidFormat) {
                throw new Exception("Sai định dạng biển số Xe Máy! (VD chuẩn: 29H1-12345)");
            }
        } else if (vehicleTypeId == 1) {
            isValidFormat = ValidationUtils.isValidCarPlate(licensePlate);
            if (!isValidFormat) {
                throw new Exception("Sai định dạng biển số Ô Tô! (VD chuẩn: 30A-12345)");
            }
        }

        // Dọn dẹp biển số (xóa dấu chấm, khoảng trắng) trước khi lưu vào DB để dữ liệu đồng nhất 100%
        licensePlate = ValidationUtils.cleanLicensePlate(licensePlate);

        if (vehicleTypeId == -1) {
            throw new Exception("Bãi xe đã hết chỗ cho loại xe này. \nKhông thể cho xe vào!");
        }

        // 2. Kiểm tra Thẻ (Validation)
        ParkingCard card = cardDAO.getById(cardId);
        if (card == null) {
            throw new Exception("Thẻ [" + cardId + "] không tồn tại trên hệ thống!");
        }

        // So sánh siteId (Ép kiểu int sang String để khớp với CardDAO của bạn)
        if (siteId != card.getSiteId()) {
            throw new Exception("Thẻ này không thuộc thẩm quyền của bãi xe hiện tại!");
        }

        // So sánh lisencePlate với xe trong session
        if (sessionDAO.isVehicleInLot(licensePlate, siteId)) {
            throw new Exception("Xe đã tồn tại trong bãi!");
        }

        // Kiểm tra trạng thái thẻ (Phải là AVAILABLE mới được dùng)
        if (card.getState() != ParkingCard.State.AVAILABLE) {
            throw new Exception("Thẻ này đang được xe khác sử dụng hoặc đang bị khóa!");
        }

        // 3. Tìm Khu vực (Area) phù hợp & còn trống cho xe
        // Vì 1 loại xe có thể có nhiều khu (Khu A, Khu B), ta duyệt qua các khu, khu nào còn trống thì xếp xe vào đó.
        List<ParkingArea> areas = areaDAO.getAreasBySite(siteId);
        ParkingArea targetArea = null;

        for (ParkingArea area : areas) {
            if (area.getVehicleTypeId() == vehicleTypeId) {
                int occupied = sessionDAO.countActiveSessionsByArea(area.getAreaId());
                if (occupied < area.getTotalSlots()) {
                    targetArea = area; // Tìm thấy khu vực còn chỗ
                    break;
                }
            }
        }

        if (targetArea == null) {
            // Dù Frontend đã chặn rồi, nhưng Backend vẫn phải chặn lại để chống hack/lỗi đồng bộ
            throw new Exception("Đã hết chỗ trống thực tế cho loại xe này!");
        }

        // 4. Khởi tạo phiên đỗ xe (Parking Session)
        ParkingSession newSession = new ParkingSession();
        newSession.setCardId(cardId);
        newSession.setLicensePlate(licensePlate);
        newSession.setVehicleTypeId(vehicleTypeId);
        // (Thời gian check-in thường được set mặc định là GETDATE() / NOW() ngay trong câu lệnh INSERT của SQL)
        newSession.setSessionType("casual");
        newSession.setSessionState("parked");
        // Thực hiện Insert xuống Database
        boolean isSessionCreated = sessionDAO.checkIn(newSession);

        if (!isSessionCreated) {
            throw new Exception("Lỗi hệ thống: Không thể tạo phiên đỗ xe!");
        }

        // 5. Cập nhật trạng thái Thẻ thành Đang sử dụng (IN_USE)
        cardDAO.updateState(cardId, ParkingCard.State.USING);

        // 6. Trả về thông báo thành công
        return "Check-in thành công: " + licensePlate + " <br>(Được xếp vào " + targetArea.getAreaName() + ")";
    }
}
