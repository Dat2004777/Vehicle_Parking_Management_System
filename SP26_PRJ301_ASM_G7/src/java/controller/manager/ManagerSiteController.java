package controller.manager;

import dal.EmployeeDAO;
import dal.SiteDAO;
import dal.VehicleDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Employee;
import model.ParkingSite;
import model.dto.SiteFormDataDTO;
import model.dto.VehicleConfigDTO;
import utils.UrlConstants;

/**
 * Controller dành riêng cho Manager xem thông tin bãi xe của mình
 */
@WebServlet(name = "ManagerSiteController", urlPatterns = {UrlConstants.URL_MANAGER + "/site"})
public class ManagerSiteController extends HttpServlet {

    private SiteDAO siteDAO = new SiteDAO();
    private EmployeeDAO employeeDao = new EmployeeDAO();
    private VehicleDAO vehicleDao = new VehicleDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // =========================================================
        // CHỐT CHẶN BẢO MẬT: KIỂM TRA ROLE
        // =========================================================
        Employee emp = (Employee) session.getAttribute("manager");

        if (emp == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String userRole = (String) session.getAttribute("userRole");
        if (!"manager".equals(userRole)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Truy cập bị từ chối! Chỉ Quản lý bãi xe mới được vào trang này.");
            return;
        }

        // Lấy site_id của Manager từ Session
        Integer userSiteId = (Integer) session.getAttribute("userSiteId");

        if (userSiteId == null || userSiteId == 0) {
            request.setAttribute("errorMessage", "Bạn chưa được phân công quản lý bãi xe nào. Vui lòng liên hệ Admin!");
            request.getRequestDispatcher("/WEB-INF/views/manager/dashboard.jsp").forward(request, response);
            return;
        }

        // Lấy thông tin bãi xe
        ParkingSite parkingSite = siteDAO.getSiteById(userSiteId);

        // Lấy dữ liệu cấu hình loại xe (Chỉ lấy để hiển thị, không sửa)
        SiteFormDataDTO formData = new SiteFormDataDTO(
                ParkingSite.Region.values(),
                ParkingSite.State.values(),
                vehicleDao.getAllVehicle(),
                employeeDao.getAllEmployeeForDetailSite(userSiteId)
        );
        List<VehicleConfigDTO> vehicleConfigDTOs = vehicleDao.getAllVehicleForDetailSite(userSiteId);

        request.setAttribute("formData", formData);
        request.setAttribute("vehicleConfigDTOs", vehicleConfigDTOs);
        request.setAttribute("parkingSite", parkingSite);

        // Chuyển hướng sang file JSP của Manager
        request.getRequestDispatcher("/WEB-INF/views/manager/site-detail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // MANAGER CHỈ ĐƯỢC XEM, KHÔNG ĐƯỢC CẬP NHẬT Ở ĐÂY
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền cập nhật thông tin bãi xe!");
    }
}
