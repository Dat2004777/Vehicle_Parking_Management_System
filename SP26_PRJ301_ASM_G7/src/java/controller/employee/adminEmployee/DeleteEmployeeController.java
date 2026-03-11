/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.employee.adminEmployee;

import dal.AccountDAO;
import dal.EmployeeDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Employee;
import utils.UrlConstants;
import utils.ValidationUtils;

/**
 *
 * @author dat20
 */
@WebServlet(name = "DeleteEmployeeController", urlPatterns = {UrlConstants.URL_ADMIN + "/employee/detail/delete"})
public class DeleteEmployeeController extends HttpServlet {

    private EmployeeDAO employeeDAO = new EmployeeDAO();
    private AccountDAO accountDAO = new AccountDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        Employee emp = (Employee) session.getAttribute("admin");

        if (emp == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        String employeeId = request.getParameter("employeeId");

        try {
            int validEmployeeId = ValidationUtils.requireValidInt(employeeId, "Employee Id không hợp lệ");
            employeeDAO.softDeleteEmployeeById(validEmployeeId);

            int accountId = employeeDAO.getEmployeeAcountId(validEmployeeId);
            accountDAO.softDeleteAccount(accountId);

            session.setAttribute("successMessage", "Xóa nhân viên thành công");
            response.sendRedirect(session.getAttribute("ctx") + "/employee");
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Xóa nhân viên thất bại");
            response.sendRedirect(session.getAttribute("ctx") + "/employee");
        }

    }

}
