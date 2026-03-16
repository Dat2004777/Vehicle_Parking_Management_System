package controller.service;

import dal.CustomerDAO;
import dal.SubscriptionDAO;
import java.io.IOException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Customer;
import model.Subscription;

@WebServlet(name = "LookupCustomerAPI", urlPatterns = {"/api/customer/lookup"})
public class LookupCustomerAPI extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String phone = request.getParameter("phone");
        
        if (phone == null || phone.trim().isEmpty()) {
            response.getWriter().write("{\"exists\": false}");
            return;
        }

        CustomerDAO customerDAO = new CustomerDAO();
        SubscriptionDAO subDAO = new SubscriptionDAO();

        try {
            Customer c = customerDAO.getByPhone(phone.trim());
            if (c == null) {
                response.getWriter().write("{\"exists\": false}");
                return;
            }

            StringBuilder json = new StringBuilder();
            json.append("{");
            json.append("\"exists\": true, ");
            // Đảm bảo tên khách hàng không làm gãy chuỗi JSON nếu có ký tự lạ
            String fullName = (c.getFirstname() + " " + c.getLastname()).trim().replace("\"", "\\\"");
            json.append("\"name\": \"").append(fullName).append("\", ");
            json.append("\"vehicles\": [");

            // Lấy danh sách xe dựa trên lịch sử Subscriptions
            List<String> plates = subDAO.getDistinctPlatesByCustomerId(c.getCustomerId());
            for (int i = 0; i < plates.size(); i++) {
                String plate = plates.get(i);
                Subscription latest = subDAO.getLatestByPlate(plate);

                if (latest != null) {
                    json.append("{");
                    json.append("\"plate\": \"").append(plate).append("\", ");
                    json.append("\"typeId\": ").append(latest.getVehicleTypeId()).append(", ");
                    
                    // --- ĐIỂM CẬP NHẬT QUAN TRỌNG CHO FORM GIA HẠN ---
                    json.append("\"subscriptionId\": ").append(latest.getSubscriptionId()).append(", ");
                    json.append("\"expiryDate\": \"").append(latest.getEndDate().toString()).append("\", ");
                    
                    // Logic: Nếu end_date còn xa hơn hiện tại -> Vẫn đang Active
                    boolean isActive = latest.getEndDate().isAfter(java.time.LocalDateTime.now());
                    json.append("\"hasSub\": true, ");
                    json.append("\"subStatus\": \"").append(isActive ? "ACTIVE" : "EXPIRED").append("\"");
                    
                    json.append("}");
                    if (i < plates.size() - 1) json.append(", ");
                }
            }
            json.append("]}");
            
            response.getWriter().write(json.toString());
            
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"exists\": false, \"message\": \"Lỗi Server khi tra cứu\"}");
        }
    }
}