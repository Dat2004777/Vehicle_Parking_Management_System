/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.site.adminSite;

import dal.AreaDAO;
import dal.EmployeeDAO;
import dal.PriceConfigsDAO;
import dal.SiteDAO;
import dal.VehicleDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import model.Employee;
import model.ParkingArea;
import model.ParkingSite;
import model.PriceConfig;
import model.dto.SaveSiteDataDTO;
import model.dto.SiteFormDataDTO;
import model.dto.VehicleConfigDTO;
import model.dto.VehicleConfigStrDTO;
import utils.UrlConstants;
import utils.ValidationUtils;

/**
 *
 * @author dat20
 */
@WebServlet(name = "AddSiteController", urlPatterns = {UrlConstants.URL_ADMIN + "/site/add"})
public class AddSiteController extends HttpServlet {

    private EmployeeDAO employeeDao = new EmployeeDAO();
    private VehicleDAO vehicleDao = new VehicleDAO();
    private SiteDAO siteDAO = new SiteDAO();
    private PriceConfigsDAO priceConfigsDAO = new PriceConfigsDAO();
    private AreaDAO areaDAO = new AreaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        Employee emp = (Employee) session.getAttribute("admin");

        if (emp == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
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
        HttpSession httpSession = request.getSession();

        String siteName = request.getParameter("siteName");
        String siteAddress = request.getParameter("siteAddress");
        String siteRegion = request.getParameter("siteRegion");
        String siteManagerStr = request.getParameter("siteManager");
        String siteState = request.getParameter("siteState");

        String[] vehicleTypes = request.getParameterValues("vehicleType");
        String[] capacities = request.getParameterValues("capacity");
        String[] hourlyPrices = request.getParameterValues("hourlyPrice");
        String[] monthlyPrices = request.getParameterValues("monthlyPrice");

        try {
            String validName = ValidationUtils.requireNonEmpty(siteName, "Tên bãi xe không được để trống!");
            String validAddress = ValidationUtils.requireNonEmpty(siteAddress, "Địa chỉ bãi xe không được để trống!");

            ParkingSite.Region validRegion = ValidationUtils.requireValidEnum(ParkingSite.Region.class, siteRegion, "Vui lòng chọn khu vực hợp lệ!");
            ParkingSite.State validState = ValidationUtils.requireValidEnum(ParkingSite.State.class, siteState, "Trạng thái bãi xe không hợp lệ!");

            if (vehicleTypes == null || vehicleTypes.length == 0 || capacities == null || hourlyPrices == null || monthlyPrices == null) {
                throw new IllegalArgumentException("Vui lòng cấu hình ít nhất một loại xe và điền đầy đủ thông tin.");
            }

            List<ParkingArea> areaList = new ArrayList<>();
            List<PriceConfig> priceList = new ArrayList<>();

            for (int i = 0; i < vehicleTypes.length; i++) {
                if (vehicleTypes[i] == null || vehicleTypes[i].trim().isEmpty()) {
                    continue; // Bỏ qua nếu người dùng không chọn loại xe ở ô này
                }

                int vehicleId = ValidationUtils.requireValidInt(vehicleTypes[i], "Dữ liệu loại xe bị lỗi.");

                // Ép kiểu Sức chứa (Yêu cầu từ 1 đến 10.000 xe)
                int capacity = ValidationUtils.requireIntInRange(capacities[i], 1, 10000, "Sức chứa ở dòng " + (i + 1) + " không hợp lệ");

                // Xử lý giá tiền: Cắt bỏ các chữ 'đ', dấu chấm, phẩy rồi mới validate
                String cleanHourly = hourlyPrices[i].replaceAll("[^0-9]", "");
                long hourlyPrice = ValidationUtils.requireLongInRange(cleanHourly, 5000, 10000000, "Giá theo giờ ở dòng " + (i + 1) + " không hợp lệ");

                String cleanMonthly = monthlyPrices[i].replaceAll("[^0-9]", "");
                long monthlyPrice = ValidationUtils.requireLongInRange(cleanMonthly, 5000, 100000000, "Giá theo tháng ở dòng " + (i + 1) + " không hợp lệ");

                ParkingArea parkingArea = new ParkingArea(0, vehicleId, capacity);
                areaList.add(parkingArea);

                PriceConfig priceConfig = new PriceConfig(0, vehicleId, "hourly", hourlyPrice);
                priceList.add(priceConfig);

                PriceConfig priceConfig2 = new PriceConfig(0, vehicleId, "monthly", monthlyPrice);
                priceList.add(priceConfig2);
            }

            int siteManager = 0;
            if (siteManagerStr != null && !siteManagerStr.trim().isEmpty()) {
                try {
                    siteManager = Integer.parseInt(siteManagerStr.trim());
                } catch (NumberFormatException e) {
                    siteManager = 0;
                }
            }

            ParkingSite parkingSite = new ParkingSite(validName, validAddress, validRegion, validState, siteManager);
            int siteId = siteDAO.addSiteAndGetId(parkingSite);

            if (siteManager != 0) {
                employeeDao.setSiteIdForEmployee(siteId, siteManager);
            }

            for (ParkingArea parkingArea : areaList) {
                parkingArea.setSiteId(siteId);
            }
            for (PriceConfig priceConfig : priceList) {
                priceConfig.setSiteId(siteId);
            }

            // TODO: Gọi DAO lưu vào ParkingAreas và PriceConfigs bằng vehicleId, capacity, hourlyPrice, monthlyPrice
            priceConfigsDAO.insertPriceConfigs(priceList);
            areaDAO.insertAreas(areaList);

            // 4. Nếu mọi thứ thành công, chuyển hướng về trang danh sách
            httpSession.setAttribute("successMessage", "Thêm bãi xe thành công");
            response.sendRedirect(httpSession.getAttribute("ctx") + "/site");
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", e.getMessage());

            // 1. Tạo danh sách config từ mảng input
            List<VehicleConfigStrDTO> savedConfigs = new ArrayList<>();
            if (vehicleTypes != null) {
                for (int i = 0; i < vehicleTypes.length; i++) {
                    if (vehicleTypes[i] == null || vehicleTypes[i].trim().isEmpty()) {
                        continue;
                    }

                    try {
                        VehicleConfigStrDTO config = new VehicleConfigStrDTO();
                        config.setVehicleTypeId(Integer.parseInt(vehicleTypes[i]));
                        config.setCapacity(capacities[i]);
                        // Giữ nguyên định dạng tiền tệ hoặc parse sạch để bắn ngược lại
                        config.setHourlyPrice(hourlyPrices[i]);
                        config.setMonthlyPrice(monthlyPrices[i]);
                        savedConfigs.add(config);
                    } catch (Exception ex) {
                        // Bỏ qua dòng lỗi parse để giữ các dòng đúng
                    }
                }
            }

            // 2. Gom tất cả vào SaveSiteDataDTO
            SaveSiteDataDTO savedData = new SaveSiteDataDTO(
                    siteName,
                    siteAddress,
                    siteRegion,
                    siteManagerStr,
                    siteState,
                    savedConfigs
            );

            // 3. Chỉ bắn đúng 1 cục data duy nhất lên JSP
            request.setAttribute("savedData", savedData);

            // BẮT BUỘC: Nạp lại toàn bộ dữ liệu của các thẻ Select (DTO)
            SiteFormDataDTO formData = new SiteFormDataDTO(
                    ParkingSite.Region.values(),
                    ParkingSite.State.values(),
                    vehicleDao.getAllVehicle(),
                    employeeDao.getAllEmployeeWithNullSiteId()
            );
            request.setAttribute("formData", formData);

            // Chuyển hướng về lại trang Form kèm theo câu thông báo lỗi
            request.getRequestDispatcher("/WEB-INF/views/site/admin/add.jsp").forward(request, response);
        }

    }
}
