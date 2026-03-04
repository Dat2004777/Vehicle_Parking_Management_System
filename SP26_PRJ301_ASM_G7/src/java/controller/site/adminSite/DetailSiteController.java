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
import model.ParkingArea;
import model.ParkingSite;
import model.PriceConfig;
import model.dto.SaveSiteDataDTO;
import model.dto.SiteFormDataDTO;
import model.dto.VehicleConfigDTO;
import utils.UrlConstants;
import utils.ValidationUtils;

/**
 *
 * @author dat20
 */
@WebServlet(name = "DetailSiteController", urlPatterns = {UrlConstants.URL_ADMIN + "/site/detail"})
public class DetailSiteController extends HttpServlet {

    private SiteDAO siteDAO = new SiteDAO();
    private EmployeeDAO employeeDao = new EmployeeDAO();
    private VehicleDAO vehicleDao = new VehicleDAO();
    private PriceConfigsDAO priceConfigsDAO = new PriceConfigsDAO();
    private AreaDAO areaDAO = new AreaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String siteIdStr = request.getParameter("siteId");
        int validSiteId = ValidationUtils.requireValidInt(siteIdStr, "SiteId không đúng, vui lòng nhập lại");

        ParkingSite parkingSite = siteDAO.getSiteById(validSiteId);

        SiteFormDataDTO formData = new SiteFormDataDTO(
                ParkingSite.Region.values(),
                ParkingSite.State.values(),
                vehicleDao.getAllVehicle(),
                employeeDao.getAllEmployeeForDetailSite(validSiteId)
        );

        List<VehicleConfigDTO> vehicleConfigDTOs = vehicleDao.getAllVehicleForDetailSite(validSiteId);

        request.setAttribute("formData", formData);
        request.setAttribute("vehicleConfigDTOs", vehicleConfigDTOs);
        request.setAttribute("parkingSite", parkingSite);
        request.getRequestDispatcher("/WEB-INF/views/site/admin/detail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession httpSession = request.getSession();

        String siteIdStr = request.getParameter("siteId");
        String siteName = request.getParameter("siteName");
        String siteAddress = request.getParameter("siteAddress");
        String siteRegion = request.getParameter("siteRegion");
        String siteManagerStr = request.getParameter("siteManager");
        String siteState = request.getParameter("siteState");

        String[] vehicleTypes = request.getParameterValues("vehicleType");
        String[] capacities = request.getParameterValues("capacity");
        String[] hourlyPrices = request.getParameterValues("hourlyPrice");
        String[] monthlyPrices = request.getParameterValues("monthlyPrice");

        int validSiteId = 0; // Khởi tạo bên ngoài để catch có thể dùng

        try {
            validSiteId = ValidationUtils.requireValidInt(siteIdStr, "siteId không tìm thấy, vui lòng kiểm tra lại");

            String validName = ValidationUtils.requireNonEmpty(siteName, "Tên bãi xe không được để trống!");
            String validAddress = ValidationUtils.requireNonEmpty(siteAddress, "Địa chỉ bãi xe không được để trống!");

            ParkingSite.Region validRegion = ValidationUtils.requireValidEnum(ParkingSite.Region.class, siteRegion, "Vui lòng chọn khu vực hợp lệ!");
            ParkingSite.State validState = ValidationUtils.requireValidEnum(ParkingSite.State.class, siteState, "Trạng thái bãi xe không hợp lệ!");

            int siteManager = 0;
            if (siteManagerStr != null && !siteManagerStr.trim().isEmpty()) {
                try {
                    siteManager = Integer.parseInt(siteManagerStr.trim());
                } catch (NumberFormatException e) {
                    siteManager = 0;
                }
            }

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

                // Ép kiểu Sức chứa (Yêu cầu từ 0 đến 10.000 xe)
                int capacity = ValidationUtils.requireIntGreaterThan(capacities[i], -1, 10000, "Sức chứa ở dòng " + (i + 1) + " không hợp lệ");

                // Xử lý giá tiền: Cắt bỏ các chữ 'đ', dấu chấm, phẩy rồi mới validate
                String cleanHourly = hourlyPrices[i].replaceAll("[^0-9]", "");
                int hourlyPrice = ValidationUtils.requireIntGreaterThan(cleanHourly, -1, 10000000, "Giá theo giờ ở dòng " + (i + 1) + " không hợp lệ");

                String cleanMonthly = monthlyPrices[i].replaceAll("[^0-9]", "");
                int monthlyPrice = ValidationUtils.requireIntGreaterThan(cleanMonthly, -1, 100000000, "Giá theo tháng ở dòng " + (i + 1) + " không hợp lệ");

                ParkingArea parkingArea = new ParkingArea(validSiteId, vehicleId, capacity);
                areaList.add(parkingArea);

                PriceConfig priceConfig = new PriceConfig(validSiteId, vehicleId, "hourly", hourlyPrice);
                priceList.add(priceConfig);

                PriceConfig priceConfig2 = new PriceConfig(validSiteId, vehicleId, "monthly", monthlyPrice);
                priceList.add(priceConfig2);
            }

            ParkingSite parkingSite = new ParkingSite(validSiteId, validName, validAddress, validRegion, validState, siteManager);

            employeeDao.updateSiteIdForNewEmployeeAndRemoveForOldEmployee(parkingSite, siteManager);

            siteDAO.updateParkingSite(parkingSite);

            //Xoa truoc roi add lai config moi
            priceConfigsDAO.deletePriceConfigsBySiteId(validSiteId);
            areaDAO.deleteAreaBySiteId(validSiteId);

            // TODO: Gọi DAO lưu vào ParkingAreas và PriceConfigs bằng vehicleId, capacity, hourlyPrice, monthlyPrice
            priceConfigsDAO.insertPriceConfigs(priceList);
            areaDAO.insertAreas(areaList);

            // 4. Nếu mọi thứ thành công, chuyển hướng về trang danh sách
            httpSession.setAttribute("successMessage", "Cập nhật bãi xe thành công");
            response.sendRedirect(httpSession.getAttribute("ctx") + "/site");
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", e.getMessage());

            // Tạo danh sách configs tạm thời từ input
            List<VehicleConfigDTO> savedConfigs = new ArrayList<>();
            if (vehicleTypes != null) {
                for (int i = 0; i < vehicleTypes.length; i++) {
                    if (vehicleTypes[i] == null || vehicleTypes[i].trim().isEmpty()) {
                        continue;
                    }
                    try {
                        VehicleConfigDTO config = new VehicleConfigDTO();
                        config.setVehicleTypeId(Integer.parseInt(vehicleTypes[i]));
                        config.setCapacity(Integer.parseInt(capacities[i]));
                        config.setHourlyPrice(Long.parseLong(hourlyPrices[i].replaceAll("[^0-9]", "")));
                        config.setMonthlyPrice(Long.parseLong(monthlyPrices[i].replaceAll("[^0-9]", "")));
                        savedConfigs.add(config);
                    } catch (Exception ex) {
                    }
                }
            }

            // Gom vào SaveAddSiteDataDTO (Dùng chung cho cả trang Add và Detail/Update)
            SaveSiteDataDTO savedData = new SaveSiteDataDTO(
                    siteName, siteAddress, siteRegion, siteManagerStr, siteState, savedConfigs
            );
            request.setAttribute("savedData", savedData);

            // BẮT BUỘC: Nạp lại toàn bộ dữ liệu của các thẻ Select (DTO)
            SiteFormDataDTO formData = new SiteFormDataDTO(
                    ParkingSite.Region.values(),
                    ParkingSite.State.values(),
                    vehicleDao.getAllVehicle(),
                    employeeDao.getAllEmployeeForDetailSite(validSiteId)
            );
            request.setAttribute("formData", formData);
            
            // Quan trọng: Phải nạp lại đối tượng bãi xe gốc hoặc siteId để trang Detail không bị trống ID
            request.setAttribute("siteId", siteIdStr);
            
            // Chuyển hướng về lại trang Form kèm theo câu thông báo lỗi
            request.getRequestDispatcher("/WEB-INF/views/site/admin/detail.jsp").forward(request, response);
        }
    }

}
