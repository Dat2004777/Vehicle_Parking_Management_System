package controller.service;

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
import model.ParkingSession;
import utils.ParkingUtils;
import utils.ValidationUtils;

@WebServlet(name = "EstimatePriceAPI", urlPatterns = {"/api/parking/estimate-price"})
public class EstimatePriceAPI extends HttpServlet {
    PriceConfigsDAO priceDAO = new PriceConfigsDAO();
    SessionDAO sessionDAO = new SessionDAO();
    
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
                    String cardId = ValidationUtils.requireNonEmpty(request.getParameter("cardId"), "Thiếu cardId");
                    ParkingSession session = sessionDAO.getActiveSession(cardId);
                    if (session == null) {
                        throw new IllegalArgumentException("Không tìm thấy xe!");
                    }

                    // Lấy đơn giá và mốc thời gian
                    long sessionHourlyPrice = priceDAO.getBasePrice(siteId, session.getVehicleTypeId(), "hourly");
                    LocalDateTime expectedOut = sessionDAO.getExpectedTimeOut(cardId);
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
            response.getWriter().write("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"success\": false, \"message\": \"Lỗi hệ thống trong quá trình xử lý.\"}");
        }
    }
}
