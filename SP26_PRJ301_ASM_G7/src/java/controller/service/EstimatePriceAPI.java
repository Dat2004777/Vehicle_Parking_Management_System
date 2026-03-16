package controller.service;

import dal.CardDAO;
import dal.PriceConfigsDAO;
import dal.SessionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import model.ParkingCard;
import model.ParkingSession;
import utils.ParkingUtils;
import utils.ValidationUtils;

@WebServlet(name = "EstimatePriceAPI", urlPatterns = {"/api/parking/estimate-price"})
public class EstimatePriceAPI extends HttpServlet {

    PriceConfigsDAO priceDAO = new PriceConfigsDAO();
    SessionDAO sessionDAO = new SessionDAO();
    CardDAO cardDAO = new CardDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Format thời gian để hiển thị đẹp ở Frontend
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        try {
            String type = ValidationUtils.requireNonEmpty(request.getParameter("type"), "Thiếu tham số loại hình (type)");
            int siteId = ValidationUtils.requireValidInt(request.getParameter("siteId"), "Mã bãi xe không hợp lệ");

            long finalPrice = 0;

            // Các biến bổ sung cho Case Session
            String timeInStr = "";
            String durationStr = "";

            switch (type.toLowerCase()) {
                case "subscription":
                    String subType = ValidationUtils.requireNonEmpty(request.getParameter("subType"), "subType trống");
                    int vId1 = ValidationUtils.requireValidInt(request.getParameter("vehicleTypeId"), "Mã xe lỗi");

                    // Lấy giá từ DAO trước
                    long subBasePrice = priceDAO.getBasePrice(siteId, vId1, subType);
                    finalPrice = ParkingUtils.calculateSubscriptionPrice(subBasePrice);
                    break;

                case "booking":
                    LocalDateTime bTimeIn = ValidationUtils.parseTimeParams(request.getParameter("timeIn"), "giờ vào");
                    LocalDateTime bTimeOut = ValidationUtils.parseTimeParams(request.getParameter("timeOut"), "giờ ra");
                    int vId2 = ValidationUtils.requireValidInt(request.getParameter("vehicleTypeId"), "Mã xe lỗi");

                    // Lấy đơn giá giờ từ DAO
                    long bookingHourlyPrice = priceDAO.getBasePrice(siteId, vId2, "hourly");
                    finalPrice = ParkingUtils.calculateBookingPrice(bTimeIn, bTimeOut, bookingHourlyPrice);
                    break;

                case "session":
                    String rawCardId = request.getParameter("cardId");
                    String rawPlate = request.getParameter("plateNumber");

                    // TÁI SỬ DỤNG 4 HÀM HELPER ĐỂ VALIDATE
                    String cleanPlate = validateAndCleanCheckOutInput(rawCardId, rawPlate);

                    ParkingCard card = validateCardForCheckOut(rawCardId, siteId);
                    ParkingSession session = fetchActiveSession(rawCardId);
                    verifyLicensePlateMatch(session.getLicensePlate(), cleanPlate);

                    // Vượt qua 4 ải kiểm tra, tiến hành lấy giá và tính tiền
                    long sessionHourlyPrice = priceDAO.getBasePrice(siteId, session.getVehicleTypeId(), "hourly");
                    LocalDateTime expectedOut = sessionDAO.getExpectedTimeOut(rawCardId);
                    LocalDateTime entryTime = session.getEntryTime();
                    LocalDateTime now = LocalDateTime.now();

                    finalPrice = ParkingUtils.calculateSessionPrice(
                            session.getSessionType(), entryTime, now, expectedOut, sessionHourlyPrice
                    );

                    durationStr = ParkingUtils.formatActualDuration(entryTime, now);
                    timeInStr = entryTime.format(formatter);
                    break;
            }

            // Xây dựng JSON trả về
            String priceFormatted = ParkingUtils.formatCurrency(finalPrice);

            StringBuilder json = new StringBuilder();
            json.append("{");
            json.append("\"success\": true, ");
            json.append("\"price\": \"").append(priceFormatted).append("\"");

            // Nếu là case session thì nối thêm 2 trường thời gian
            if (!timeInStr.isEmpty() && !durationStr.isEmpty()) {
                json.append(", \"timeIn\": \"").append(timeInStr).append("\"");
                json.append(", \"duration\": \"").append(durationStr).append("\"");
            }

            json.append("}");

            response.getWriter().write(json.toString());

        } catch (IllegalArgumentException e) {
            // Dọn dẹp câu thông báo: Xóa các ký tự có thể làm vỡ JSON
            String safeMsg = e.getMessage() != null ? e.getMessage().replace("\"", "'").replace("\\", "") : "Lỗi dữ liệu";
            response.getWriter().write("{\"success\": false, \"message\": \"" + safeMsg + "\"}");

        } catch (Exception e) {
            e.printStackTrace();
            // Đảm bảo tuyệt đối không còn chữ z nào ở đây nhé!
            response.getWriter().write("{\"success\": false, \"message\": \"Lỗi hệ thống trong quá trình xử lý.\"}");
        }
    }

    // Hàm 1: Validate và dọn dẹp chuỗi đầu vào
    private String validateAndCleanCheckOutInput(String cardId, String licensePlate) throws Exception {
        if (cardId == null || cardId.trim().isEmpty() || licensePlate == null || licensePlate.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập đầy đủ Mã thẻ và Biển số!");
        }

        if (!((licensePlate.length() == 9 && ValidationUtils.isValidCarPlate(licensePlate))
                || (licensePlate.length() == 10 && ValidationUtils.isValidMotorbikePlate(licensePlate)))) {
            throw new IllegalArgumentException("Lỗi: Biển số xe sai định dạng!");
        }

        // Dọn dẹp khoảng trắng, dấu chấm để so sánh chính xác với DB
        return ValidationUtils.cleanLicensePlate(licensePlate);
    }

// Hàm 2: Kiểm tra trạng thái thẻ khi ra bãi
    private ParkingCard validateCardForCheckOut(String cardId, int siteId) throws Exception {
        ParkingCard card = cardDAO.getById(cardId);
        if (card == null) {
            throw new IllegalArgumentException("Thẻ [" + cardId + "] không tồn tại trên hệ thống!");
        }
        if (card.getSiteId() != siteId) {
            throw new IllegalArgumentException("Lỗi: Thẻ này thuộc bãi xe khác, không thể check-out tại đây!");
        }
        if (card.getState() == ParkingCard.State.AVAILABLE) {
            throw new IllegalArgumentException("Lỗi: Thẻ này hiện không có xe nào đang sử dụng (Chưa check-in)!");
        }

        return card;
    }

// Hàm 3: Lấy phiên đỗ xe
    private ParkingSession fetchActiveSession(String cardId) throws Exception {
        ParkingSession session = sessionDAO.getActiveSession(cardId);
        if (session == null) {
            throw new IllegalArgumentException("Lỗi hệ thống: Không tìm thấy dữ liệu xe vào cho thẻ này!");
        }
        return session;
    }

// Hàm 4: Xác thực biển số xe
    private void verifyLicensePlateMatch(String dbPlate, String inputPlate) throws Exception {
        String formattedDbPlate = dbPlate.trim().toUpperCase();
        String formattedInputPlate = inputPlate.trim().toUpperCase();

        if (!formattedDbPlate.equals(formattedInputPlate)) {
            throw new IllegalArgumentException("CẢNH BÁO BẢO MẬT: Biển số xe gửi lên [" + formattedInputPlate + "] KHÔNG KHỚP với biển số lúc vào");
        }
    }
}
