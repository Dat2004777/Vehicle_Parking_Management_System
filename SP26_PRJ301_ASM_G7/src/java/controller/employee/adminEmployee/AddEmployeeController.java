/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.employee.adminEmployee;

import dal.AccountDAO;
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
import model.ParkingSite;
import model.dto.AdminListEmployeeDTO;
import utils.UrlConstants;
import utils.ValidationUtils;

/**
 *
 * @author dat20
 */
@WebServlet(name = "AddEmployeeController", urlPatterns = {UrlConstants.URL_ADMIN + "/employee/add"})
public class AddEmployeeController extends HttpServlet {

    private SiteDAO siteDAO = new SiteDAO();
    private AccountDAO accountDAO = new AccountDAO();
    private EmployeeDAO employeeDAO = new EmployeeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        Employee emp = (Employee) session.getAttribute("admin");

        if (emp == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        List<ParkingSite> listSites = siteDAO.getAllSites();

        request.setAttribute("roles", AdminListEmployeeDTO.Role.values());
        request.setAttribute("listSites", listSites);
        request.getRequestDispatcher("/WEB-INF/views/employee/admin/add.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession httpSession = request.getSession();

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String phone = request.getParameter("phone");
        String siteIdStr = request.getParameter("siteId");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        try {
            String validFirstName = ValidationUtils.requireNonEmpty(firstName, "Vui lòng điền Họ và tên đệm!");
            String validLastName = ValidationUtils.requireNonEmpty(lastName, "Vui lòng điền Tên!");

            if (!password.equals(confirmPassword)) {
                throw new Exception("Mật khẩu và Xác nhận mật khẩu không khớp!");
            }

            // 1. Nếu số điện thoại KHÔNG hợp lệ -> Ném lỗi ra ngay lập tức
            if (!ValidationUtils.checkPhone(phone)) {
                throw new Exception("Số điện thoại không hợp lệ!");
            }

            // 2. KIỂM TRA TRÙNG SỐ ĐIỆN THOẠI (Bổ sung mới)
            if (employeeDAO.isPhoneExist(phone)) {
                throw new Exception("Số điện thoại này đã được đăng ký trong hệ thống!");
            }

            // 2. Ép kiểu Site ID (Sửa 0 thành null để DB hiểu là trống)
            Integer siteId = null;
            if (siteIdStr != null && !siteIdStr.trim().isEmpty()) {
                siteId = ValidationUtils.requireValidInt(siteIdStr, "Site Id không hợp lệ");
            }

            // 3. Nếu code chạy được đến dòng này, nghĩa là mọi dữ liệu đều hợp lệ
            int accountId = accountDAO.insertAccount(phone, password, "staff");
            Employee employee = new Employee(
                    accountId,
                    validFirstName,
                    validLastName,
                    phone,
                    siteId);
            employeeDAO.addEmployee(employee);

            // 4. Báo thành công và chuyển hướng
            httpSession.setAttribute("successMessage", "Thêm nhân viên " + lastName + " " + firstName + " thành công!");
            response.sendRedirect(httpSession.getAttribute("ctx") + "/employee");

        } catch (Exception e) {
            // Nếu có bất kỳ lỗi gì (thiếu tên, sai sđt, sai pass...), nó sẽ nhảy vào đây
            request.setAttribute("errorMessage", e.getMessage());

            List<ParkingSite> listSites = siteDAO.getAllSites();

            // Giữ lại dữ liệu người dùng đã nhập (Trừ password)
            request.setAttribute("firstName", firstName);
            request.setAttribute("lastName", lastName);
            request.setAttribute("phone", phone);
            request.setAttribute("saveSiteId", siteIdStr);
            request.setAttribute("roles", AdminListEmployeeDTO.Role.values());
            request.setAttribute("listSites", listSites);

            // Forward lại về trang add cùng với thông báo lỗi
            request.getRequestDispatcher("/WEB-INF/views/employee/admin/add.jsp").forward(request, response);
        }
    }

}
