package controller.parking;

import dal.AreaDAO;
import dal.CardDAO;
import dal.CustomerDAO;
import dal.PaymentTransactionDAO;
import dal.PriceConfigsDAO;
import dal.SubscriptionDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.Customer;
import model.Employee;
import model.ParkingArea;
import model.ParkingCard;
import model.PaymentTransaction;
import model.PriceConfig;
import model.Subscription;
import model.VehicleType;
import model.dto.SubscriptionDataDTO;
import utils.UrlConstants;
import utils.ValidationUtils;

@WebServlet(name = "ParkingSubscriptionController", urlPatterns = {UrlConstants.URL_STAFF + "/subscription"})
public class ParkingSubscriptionController extends HttpServlet {

    private final AreaDAO areaDAO = new AreaDAO();
    private final PriceConfigsDAO priceDAO = new PriceConfigsDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final SubscriptionDAO subDAO = new SubscriptionDAO();
    private final CardDAO cardDAO = new CardDAO();
    private final PaymentTransactionDAO transactionDAO = new PaymentTransactionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Employee emp = (Employee) request.getSession().getAttribute("staff");
        if (emp == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int siteId = emp.getSiteId();
        request.setAttribute("vehicleTypes", getAllVehicleType(siteId));
        List<PriceConfig> priceConfigs = priceDAO.getAllSubscriptionPricesBySite(siteId);
        request.setAttribute("priceConfigs", priceConfigs);
        request.setAttribute("availablePackages", extractUniquePackages(priceConfigs));

        request.getRequestDispatcher("/WEB-INF/views/parking/subscription.jsp").forward(request, response);
    }

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

        // BƯỚC 1: LẤY DỮ LIỆU THÔ VÀ ĐỔ VÀO DTO (Luôn luôn thành công)
        SubscriptionDataDTO formData = extractRawData(request);

        try {
            // BƯỚC 2: KIỂM TRA LỖI (Validation)
            validateData(formData);

            // BƯỚC 3: XỬ LÝ NGHIỆP VỤ (Hàm processSubscription của bạn không cần validate nữa)
            String resultMessage = processSubscription(formData, emp);
            
            session.setAttribute("successMsg", resultMessage);

        } catch (IllegalArgumentException e) {
            // Lúc này formData ĐÃ CÓ CHỨA DỮ LIỆU THÔ, nên bắn ngược về JS sẽ hoạt động 100%
            session.setAttribute("errorMsg", e.getMessage());
            session.setAttribute("oldSubscription", formData); 
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMsg", "Hệ thống bận, vui lòng thử lại sau!");
            session.setAttribute("oldSubscription", formData);
        }

        response.sendRedirect(request.getContextPath() + "/staff/subscription");
    }

    // =========================================================================
    // HÀM CHÍNH XỬ LÝ ĐĂNG KÝ/GIA HẠN
    // =========================================================================
    private String processSubscription(SubscriptionDataDTO data, Employee emp) throws Exception {

        // 1. Lấy cấu hình giá từ DB (Chốt chặn bảo mật)
        PriceConfig config = priceDAO.getConfigById(data.getConfigId());
        if (config == null) {
            throw new Exception("Không tìm thấy cấu hình giá phù hợp!");
        }

        // 2. Xử lý logic khách hàng (Tìm hoặc Tạo mới)
        int customerId = processCustomerLogic(data.getPhone(), data.getFullName());

        // 3. Tính toán thời gian và chi phí
        LocalDateTime startDT = data.getStartDate().atStartOfDay();
        LocalDateTime endDT = calculateEndDate(data.getStartDate(), config.getType());
        long finalPrice = calculateFinalPrice(config.getBasePrice(), data.getActionType());

        // 4. Giao dịch Database (Sử dụng ID khách hàng vừa tìm/tạo)
        executeSubscriptionTransaction(data, customerId, startDT, endDT, finalPrice, emp.getEmployeeId(), emp.getSiteId(), finalPrice);

        // 5. Trả về câu thông báo thành công
        return buildSuccessMessage(data.getPlate(), data.getActionType());
    }

    private void executeSubscriptionTransaction(SubscriptionDataDTO d, int custId, LocalDateTime start, LocalDateTime end, long price, int staffId, int siteId, long amount) throws Exception {
        Subscription s = new Subscription();
        s.setCustomerId(custId);
        s.setCardId(d.getCardId());
        s.setLicensePlate(d.getPlate());
        s.setVehicleTypeId(d.getVehicleTypeId());
        s.setStartDate(start);
        s.setEndDate(end);
        s.setAppliedPrice(price);
        s.setSubState("active");

        int newSubId = subDAO.insertAndReturnId(s);
        if (newSubId == -1) {
            throw new Exception("Không thể lưu thông tin vé vào CSDL!");
        }

        // Nếu tạo mới, cập nhật trạng thái thẻ sang "Đang sử dụng"
        if ("create".equals(d.getActionType())) {
            cardDAO.updateState(d.getCardId(), ParkingCard.State.ASSIGNED);
        }

        try {
            PaymentTransaction txn = new PaymentTransaction();
            txn.setTargetId(newSubId);
            txn.setTotalAmount(amount);
            txn.setTransactionType(PaymentTransaction.TransactionType.SUBSCRIPTION);
            txn.setPaymentStatus("completed");

            if (!transactionDAO.add(txn)) {
                // Log lỗi nếu không lưu được nhưng không làm gián đoạn luồng chính
                System.err.println("[Finance Error] Không thể lưu giao dịch cho Subscription ID: " + newSubId);
            }
        } catch (Exception e) {
            System.err.println("[Critical Error] Lỗi trong quá trình xử lý thanh toán: " + e.getMessage());
        }
    }

    private String buildSuccessMessage(String plate, String actionType) {
        String act = "create".equals(actionType) ? "Đăng ký mới" : "Gia hạn";
        return act + " thành công cho xe: <b>" + plate + "</b>";
    }

    // =========================================================================
    // HÀM KIỂM TRA TRÙNG LẶP (BUSINESS VALIDATION)
    // =========================================================================
    private SubscriptionDataDTO extractRawData(HttpServletRequest request) {
        // Áp dụng đúng ý tưởng dùng Setter của bạn
        SubscriptionDataDTO d = new SubscriptionDataDTO();
        
        d.setActionType(request.getParameter("actionType"));
        d.setPhone(request.getParameter("phone"));
        d.setFullName(request.getParameter("fullName"));
        d.setPlate(request.getParameter("plate"));
        d.setCardId(request.getParameter("cardId"));
        
        // Parse Int an toàn (Nếu lỗi thì set mặc định bằng 0)
        try { d.setVehicleTypeId(Integer.parseInt(request.getParameter("vehicleTypeId"))); } catch (Exception e) { d.setVehicleTypeId(0); }
        try { d.setConfigId(Integer.parseInt(request.getParameter("configId"))); } catch (Exception e) { d.setConfigId(0); }
        try { d.setOldSubId(Integer.parseInt(request.getParameter("oldSubscriptionId"))); } catch (Exception e) { d.setOldSubId(null); }
        
        // Parse Date an toàn
        String dateStr = request.getParameter("startDate");
        if (dateStr != null && !dateStr.isEmpty()) {
            try { d.setStartDate(LocalDate.parse(dateStr)); } catch (Exception e) {}
        }
        
        return d;
    }
    
    private void validateBusinessRules(SubscriptionDataDTO data) throws Exception {

        if ("create".equals(data.getActionType())) {
            // 1. Kiểm tra Biển số đã có vé tháng ACTIVE chưa
            // Giả sử bạn thêm hàm này vào SubscriptionDAO
            boolean isPlateActive = subDAO.hasActiveSubscriptionByPlate(data.getPlate());
            if (isPlateActive) {
                throw new IllegalArgumentException("Xe [" + data.getPlate() + "] hiện đang có vé tháng còn hiệu lực. Vui lòng sử dụng chức năng Gia hạn.");
            }

            // 2. Kiểm tra Thẻ này đã có ai dùng làm vé tháng chưa (Trạng thái thẻ trong bãi)
            Subscription subCardUsed = subDAO.getActiveSubscriptionByCard(data.getCardId());
            if (subCardUsed != null) {
                throw new IllegalArgumentException("Thẻ [" + data.getCardId() + "] đã được đăng ký cho một phương tiện khác!");
            }
        }

        if ("renew".equals(data.getActionType())) {
            if (data.getOldSubId() == null) {
                throw new IllegalArgumentException("Không tìm thấy thông tin vé cũ để thực hiện gia hạn!");
            }
            // Kiểm tra xem vé cũ có thực sự khớp với biển số/thẻ này không
            Subscription oldSub = subDAO.getById(data.getOldSubId());
            if (oldSub == null || !oldSub.getLicensePlate().equals(data.getPlate())) {
                throw new IllegalArgumentException("Dữ liệu gia hạn không đồng nhất. Vui lòng tải lại trang!");
            }
        }
    }
    
    private void validateData(SubscriptionDataDTO d) throws Exception {
        // Check rỗng
        ValidationUtils.requireNonEmpty(d.getActionType(), "Thiếu loại hành động!");
        ValidationUtils.requireNonEmpty(d.getPhone(), "SĐT không được để trống");
        ValidationUtils.requireNonEmpty(d.getFullName(), "Họ tên không được để trống");
        ValidationUtils.requireNonEmpty(d.getPlate(), "Biển số không được để trống");
        ValidationUtils.requireNonEmpty(d.getCardId(), "Mã thẻ không được để trống");

        // Check SĐT
        if (!ValidationUtils.checkPhone(d.getPhone())) {
            throw new IllegalArgumentException("SĐT phải gồm 10 chữ số!");
        }

        // Dọn dẹp biển số (Lúc này mới chuẩn hóa)
        String cleanPlate = ValidationUtils.cleanLicensePlate(d.getPlate());
        d.setPlate(cleanPlate); // Update ngược lại DTO

        // Check format biển số
        if (d.getVehicleTypeId() == 1 && !ValidationUtils.isValidCarPlate(cleanPlate)) {
            throw new IllegalArgumentException("Biển số ô tô sai định dạng!");
        }
        if (d.getVehicleTypeId() == 2 && !ValidationUtils.isValidMotorbikePlate(cleanPlate)) {
            throw new IllegalArgumentException("Biển số xe máy sai định dạng!");
        }

        if (d.getConfigId() == 0) {
            throw new IllegalArgumentException("Vui lòng chọn Gói cước!");
        }
        if (d.getStartDate() == null) {
            throw new IllegalArgumentException("Ngày bắt đầu không được để trống!");
        }

        // Gọi luôn check Trùng lặp DB tại đây
        validateBusinessRules(d);
    }
    // =========================================================================
    // CÁC HÀM PHỤ TRỢ (HELPER METHODS)
    // =========================================================================
    

    private int processCustomerLogic(String phone, String fullName) throws Exception {
        Customer c = customerDAO.getByPhone(phone);
        if (c != null) {
            return c.getCustomerId();
        }

        Customer newCust = new Customer();
        newCust.setPhone(phone);
        String[] parts = fullName.split(" ", 2);
        newCust.setLastname(parts[0]);
        newCust.setFirstname(parts.length > 1 ? parts[1] : "");

        return customerDAO.insertAndReturnId(newCust);
    }

    private long calculateFinalPrice(long basePrice, String actionType) {
        // Tạo mới thì cộng thêm phí thẻ 30k, gia hạn thì không
        return "create".equals(actionType) ? (basePrice) : basePrice;
    }

    // --- Giữ lại các hàm Helper của GET ---
    private List<VehicleType> getAllVehicleType(int siteId) {
        List<ParkingArea> rawAreas = areaDAO.getAreasBySite(siteId);
        List<VehicleType> vtList = new ArrayList<>();
        for (ParkingArea area : rawAreas) {
            vtList.add(new VehicleType(area.getVehicleTypeId(), area.getVehicleTypeName()));
        }
        return vtList;
    }

    private List<String> extractUniquePackages(List<PriceConfig> configs) {
        List<String> packages = new ArrayList<>();
        for (PriceConfig c : configs) {
            if (c.getType() != null && !packages.contains(c.getType())) {
                packages.add(c.getType());
            }
        }
        return packages;
    }

    private LocalDateTime calculateEndDate(LocalDate start, String type) {
        int m = 1;
        if (type != null) {
            switch (type.toLowerCase()) {
                case "monthly":
                    m = 1;
                    break;
                case "quarterly":
                    m = 3;
                    break;
                case "half-yearly":
                    m = 6;
                    break;
                case "yearly":
                    m = 12;
                    break;
            }
        }
        return start.plusMonths(m).atTime(23, 59, 59);
    }
}
