package controller.manager;

import dal.EmployeeDAO;
import dal.SiteDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Employee;
import model.dto.AdminListEmployeeDTO;
import utils.UrlConstants;

@WebServlet(name = "ManagerEmployeeController", urlPatterns = {UrlConstants.URL_MANAGER + "/employee"})
public class ManagerEmployeeController extends HttpServlet {

    private EmployeeDAO employeeDao = new EmployeeDAO();
    private SiteDAO siteDao = new SiteDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // CHỐT CHẶN BẢO MẬT
        Employee emp = (Employee) session.getAttribute("manager");

        if (emp == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String userRole = (String) session.getAttribute("userRole");
        if (!"manager".equals(userRole)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Truy cập bị từ chối!");
            return;
        }

        // Lấy ID bãi xe của Manager
        Integer userSiteId = (Integer) session.getAttribute("userSiteId");
        if (userSiteId == null || userSiteId == 0) {
            response.sendRedirect(request.getContextPath() + UrlConstants.URL_MANAGER + "/dashboard");
            return;
        }

        String search = request.getParameter("search");

        // Lấy danh sách nhân viên chỉ thuộc bãi xe này
        List<AdminListEmployeeDTO> listEmployees = employeeDao.getEmployeesBySiteId(userSiteId, search);

        request.setAttribute("listEmployees", listEmployees);
        request.setAttribute("siteName", siteDao.getSiteById(userSiteId).getSiteName()); // Truyền tên bãi xe lên UI

        request.getRequestDispatcher("/WEB-INF/views/manager/employee-list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // MANAGER KHÔNG CÓ QUYỀN THÊM XÓA SỬA NHÂN VIÊN (DO ADMIN QUẢN LÝ ACCOUNT)
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền thay đổi nhân sự!");
    }
}
