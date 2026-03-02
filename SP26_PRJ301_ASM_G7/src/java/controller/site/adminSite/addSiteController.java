/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.site.adminSite;

import dal.EmployeeDAO;
import dal.VehicleDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Employee;
import model.ParkingSite;
import model.Vehicle;
import model.dto.SiteFormDataDTO;
import utils.UrlConstants;

/**
 *
 * @author dat20
 */
@WebServlet(name = "addSiteController", urlPatterns = {UrlConstants.URL_ADMIN + "/site/add"})
public class AddSiteController extends HttpServlet {

    private EmployeeDAO employeeDao = new EmployeeDAO();
    private VehicleDAO vehicleDao = new VehicleDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        List<Vehicle> vehicles = vehicleDao.getAllVehicle();
//        List<Employee> employees = employeeDao.getAllEmployeeWithNullSiteId();
//        ParkingSite.Region[] regions = ParkingSite.Region.values();
//        ParkingSite.State[] states = ParkingSite.State.values();
//
//        request.setAttribute("regions", regions);
//        request.setAttribute("vehicles", vehicles);
//        request.setAttribute("states", states);
//        request.setAttribute("employees", employees);

        // 1. Khởi tạo DTO và nhét toàn bộ dữ liệu vào
        SiteFormDataDTO formData = new SiteFormDataDTO(
                ParkingSite.Region.values(),
                ParkingSite.State.values(),
                vehicleDao.getAllVehicle(),
                employeeDao.getAllEmployeeWithNullSiteId()
        );

        // 2. Chỉ bắn đúng 1 cục "formData" này lên JSP
        request.setAttribute("formData", formData);
        request.getRequestDispatcher("/WEB-INF/views/site/admin/add.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String siteName = request.getParameter("siteName");
        String siteAddress = request.getParameter("siteAddress");
        String siteRegion = request.getParameter("siteRegion");
        String siteManager = request.getParameter("siteManager");
        String siteState = request.getParameter("siteState");
        String[] vehicleTypes = request.getParameterValues("vehicleType");
        String[] capacities = request.getParameterValues("capacity");
        String[] hourlyPrices = request.getParameterValues("hourlyPrice");
        String[] monthlyPrices = request.getParameterValues("monthlyPrice");

        System.out.println(siteName);
        System.out.println(siteAddress);
        System.out.println(siteRegion);
        System.out.println(siteManager);
        System.out.println(siteState);
        
        // 3. Xử lý logic ghép cặp dữ liệu
        if (vehicleTypes != null) {
            // Vòng lặp sẽ chạy tương ứng với số lượng khối cấu hình đã được thêm trên UI
            for (int i = 0; i < vehicleTypes.length; i++) {
                String typeIdStr = vehicleTypes[i];

                // Nếu người dùng để nguyên "Chọn loại xe" (value rỗng) thì bỏ qua khối đó
                if (typeIdStr == null || typeIdStr.trim().isEmpty()) {
                    continue;
                }

                int vehicleTypeId = Integer.parseInt(typeIdStr);
                int capacity = capacities[i].isEmpty() ? 0 : Integer.parseInt(capacities[i]);

                // Xử lý giá tiền (Loại bỏ các ký tự không phải là số như "đ", dấu chấm, dấu phẩy)
                String rawHourly = hourlyPrices[i].replaceAll("[^0-9]", "");
                long hourlyPrice = rawHourly.isEmpty() ? 0 : Long.parseLong(rawHourly);

                String rawMonthly = monthlyPrices[i].replaceAll("[^0-9]", "");
                long monthlyPrice = rawMonthly.isEmpty() ? 0 : Long.parseLong(rawMonthly);

                // TODO: In ra test thử (Hoặc lưu vào DB)
                System.out.println("--- Cấu hình " + (i + 1) + " ---");
                System.out.println("Loại xe ID: " + vehicleTypeId);
                System.out.println("Sức chứa: " + capacity);
                System.out.println("Giá giờ: " + hourlyPrice);
                System.out.println("Giá tháng: " + monthlyPrice);

                // Ở đây bạn có thể gọi DAO để lưu bảng ParkingAreas và PriceConfigs
            }
            // Cuối cùng: Redirect về trang danh sách
            response.sendRedirect(request.getContextPath() + "/admin/site");
        }
    }
}
