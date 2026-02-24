//package controller.dashboard;
//
//import model.ScanResultDTO;
//import java.io.IOException;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//@WebServlet(name = "StaffController", urlPatterns = {"/staff/dashboard", "/staff/handle-scan", "/staff/confirm-checkout"})
//public class StaffController extends HttpServlet {
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        // Load trang dashboard mặc định
//        request.getRequestDispatcher("/views/staff/staff-dashboard.jsp").forward(request, response);
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        
//        String path = request.getServletPath();
//        
//        if (path.equals("/staff/handle-scan")) {
//            handleScan(request, response);
//        } else if (path.equals("/staff/confirm-checkout")) {
//            confirmCheckout(request, response);
//        }
//    }
//
//    // --- LOGIC XỬ LÝ CHÍNH ---
//    private void handleScan(HttpServletRequest request, HttpServletResponse response) 
//            throws ServletException, IOException {
//        
//        String cardId = request.getParameter("cardId").trim();
//        String inputPlate = request.getParameter("licensePlate").trim().toUpperCase();
//        ScanResultDTO result = new ScanResultDTO();
//
//        // [MOCK DB]: Giả lập trạng thái thẻ
//        // Nếu mã thẻ chứa "99" -> Đang dùng (IN_USE) -> Xe Ra
//        // Còn lại -> Rảnh (AVAILABLE) -> Xe Vào
//        boolean isCardInUse = cardId.contains("99"); 
//
//        try {
//            if (cardId.isEmpty()) throw new Exception("Vui lòng quẹt thẻ!");
//
//            // === CASE 1: XE RA (CHECK-OUT) ===
//            if (isCardInUse) {
//                // [TODO]: Lấy Session từ DB
//                // ParkingSession session = sessionDAO.findActiveSession(cardId);
//                
//                // Giả lập dữ liệu từ DB
//                String dbPlate = "30A-123.45"; 
//                double fee = 15000;
//                String type = "CASUAL"; // Khách vãng lai
//                
//                result.setStatus("CONFIRM_CHECKOUT");
//                result.setCardId(cardId);
//                result.setLicensePlate(dbPlate); // Biển gốc trong DB
//                result.setInputPlate(inputPlate); // Biển nhân viên vừa nhập
//                result.setFee(fee);
//                result.setDuration("2 giờ 30 phút");
//                result.setCustomerType(type);
//                result.setSessionId("SES_9999");
//
//                // --- LOGIC SECURITY CHECK (ĐỐI CHIẾU BIỂN SỐ) ---
//                if (inputPlate.isEmpty()) {
//                    result.setWarningMessage("⚠️ CẢNH BÁO: Bạn chưa nhập biển số xe ra để đối chiếu!");
//                } else if (!inputPlate.equals(dbPlate)) {
//                    result.setStatus("ERROR"); // Chặn luôn
//                    throw new Exception("⛔ TRÁO XE PHÁT HIỆN! Biển ra (" + inputPlate + ") khác biển vào (" + dbPlate + ")");
//                } else {
//                    result.setMessage("✅ Biển số trùng khớp. An toàn.");
//                }
//            } 
//            
//            // === CASE 2: XE VÀO (CHECK-IN) ===
//            else {
//                if (inputPlate.isEmpty()) {
//                    throw new Exception("Xe vào bắt buộc phải nhập biển số!");
//                }
//
//                // [TODO]: Logic Booking/Subscription check
//                // Subscription sub = subDAO.findByPlate(inputPlate);
//                // if (sub != null) { ... }
//
//                // [TODO]: Insert Session & Update Card Status
//                
//                result.setStatus("SUCCESS");
//                result.setMessage("Xe vào thành công: " + inputPlate);
//                result.setCustomerType("CASUAL");
//            }
//
//        } catch (Exception e) {
//            result.setStatus("ERROR");
//            result.setMessage(e.getMessage());
//        }
//
//        request.setAttribute("scanResult", result);
//        request.getRequestDispatcher("/views/staff/staff-dashboard.jsp").forward(request, response);
//    }
//
//    private void confirmCheckout(HttpServletRequest request, HttpServletResponse response) 
//            throws ServletException, IOException {
//        ScanResultDTO result = new ScanResultDTO();
//        // [TODO]: Update DB (End Session, Free Card)
//        result.setStatus("SUCCESS");
//        result.setMessage("Đã thu tiền & Mở cổng ra!");
//        
//        request.setAttribute("scanResult", result);
//        request.getRequestDispatcher("/views/staff/staff-dashboard.jsp").forward(request, response);
//    }
//}