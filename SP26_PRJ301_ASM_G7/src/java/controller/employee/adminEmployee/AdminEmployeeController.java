/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.employee.adminEmployee;

import dal.EmployeeDAO;
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
import utils.ValidationUtils;

/**
 *
 * @author dat20
 */
@WebServlet(name = "ListEmployeeController", urlPatterns = {UrlConstants.URL_ADMIN + "/employee"})
public class AdminEmployeeController extends HttpServlet {

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

        String employeeSearch = request.getParameter("employeeSearch");

        List<AdminListEmployeeDTO> listEmployees;
        if (employeeSearch != null && !employeeSearch.trim().isEmpty()) {
            if (employeeSearch.charAt(0) == 'E') {
                int employeeId = ValidationUtils.requireValidInt(employeeSearch.substring(1), "EmployeeId không hợp lệ");
                listEmployees = employeeDAO.employeeSearchByEmployeeId(employeeId);
            } else {
                listEmployees = employeeDAO.employeeSearch(employeeSearch);
            }
        } else {
            listEmployees = employeeDAO.getAllEmployeeWithTheirSite();
        }

        request.setAttribute("roles", AdminListEmployeeDTO.Role.values());
        request.setAttribute("listEmployees", listEmployees);
        request.getRequestDispatcher("/WEB-INF/views/employee/admin/list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
