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
@WebServlet(name = "DetailEmployeeController", urlPatterns = {UrlConstants.URL_ADMIN + "/employee/detail"})
public class DetailEmployeeController extends HttpServlet {

    private AccountDAO accountDAO = new AccountDAO();
    private EmployeeDAO employeeDAO = new EmployeeDAO();
    private SiteDAO siteDAO = new SiteDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        String employeeId = request.getParameter("employeeId");

        try {

            if (employeeId == null || employeeId.trim().isEmpty()) {
                throw new Exception("Mã nhân viên không được để trống");
            }

            int validEmployeeId = ValidationUtils.requireValidInt(employeeId, "Mã nhân viên không hợp lệ");

            Employee employee = employeeDAO.getEmployeeById(validEmployeeId);

            request.setAttribute("listSites", siteDAO.getAllSites()); // Load Bãi xe
            request.setAttribute("employee", employee);
            request.setAttribute("employeeRole", accountDAO.getAccountRole(employee.getAccountId()));
            request.getRequestDispatcher("/WEB-INF/views/employee/admin/detail.jsp").forward(request, response);
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Lỗi thông tin nhân viên");
            response.sendRedirect(session.getAttribute("ctx") + "/employee");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String phone = request.getParameter("phone");
        String siteIdStr = request.getParameter("siteId");
        String employeeIdStr = request.getParameter("employeeId");

        try {
            // 1. Lấy thông tin nhân viên HIỆN TẠI từ DB
            int employeeId = ValidationUtils.requireValidInt(employeeIdStr, "Không xác định được ID nhân viên!");
            Employee oldEmployee = employeeDAO.getEmployeeById(employeeId);

            if (oldEmployee == null) {
                throw new Exception("Không tìm thấy nhân viên trong hệ thống!");
            }

            // 2. Validate dữ liệu cơ bản
            String validFirstName = ValidationUtils.requireNonEmpty(firstName, "Vui lòng điền Họ và tên đệm!");
            String validLastName = ValidationUtils.requireNonEmpty(lastName, "Vui lòng điền Tên!");

            // 3. LOGIC XỬ LÝ SỐ ĐIỆN THOẠI THÔNG MINH
            String validPhone = oldEmployee.getPhone(); // Mặc định giữ nguyên sđt cũ
            boolean isPhoneChanged = !oldEmployee.getPhone().equals(phone); // Kiểm tra xem có đổi sđt không

            if (isPhoneChanged) {
                // Nếu CÓ thay đổi, mới tiến hành validate format và check trùng
                if (!ValidationUtils.checkPhone(phone)) {
                    throw new Exception("Số điện thoại mới không đúng định dạng!");
                }
                if (employeeDAO.isPhoneExist(phone)) {
                    throw new Exception("Số điện thoại mới đã được sử dụng cho người khác!");
                }
                validPhone = phone; // Cập nhật sđt mới để lưu xuống DB

                // QUAN TRỌNG: Cập nhật luôn username trong bảng Accounts
                accountDAO.updateUsername(oldEmployee.getAccountId(), validPhone);
            }

            // 4. Xử lý dữ liệu Site ID (Xử lý chuỗi rỗng hoặc "0")
            int siteId = 0;
            if (siteIdStr != null && !siteIdStr.trim().isEmpty() && !siteIdStr.equals("0")) {
                siteId = ValidationUtils.requireValidInt(siteIdStr, "Bãi xe không hợp lệ");
            }

            // 5. Đóng gói dữ liệu và gọi DAO update
            // (Bạn có thể dùng setter hoặc constructor tùy class Employee của bạn)
            Employee updateEmployee = new Employee(
                    employeeId,
                    oldEmployee.getAccountId(), // Giữ nguyên account ID
                    validFirstName,
                    validLastName,
                    validPhone, // Sẽ là số cũ nếu không đổi, số mới nếu đã đổi
                    siteId
            );

            employeeDAO.updateEmployee(updateEmployee);

            // CHỈ GỠ QUẢN LÝ KHI CÓ SỰ THAY ĐỔI BÃI XE
            if (oldEmployee.getSiteId() != siteId) {
                siteDAO.setNullManagerIdWhenChangeSiteEmployee(employeeId);
            }

            // 4. Báo thành công và chuyển hướng
            session.setAttribute("successMessage", "Cập nhật nhân viên " + lastName + " " + firstName + " thành công!");
            response.sendRedirect(session.getAttribute("ctx") + "/employee");

        } catch (Exception e) {
            e.printStackTrace();

            // Nếu có bất kỳ lỗi gì (thiếu tên, sai sđt, sai pass...), nó sẽ nhảy vào đây
            request.setAttribute("errorMessage", e.getMessage());

            List<ParkingSite> listSites = siteDAO.getAllSites();

            // Giữ lại dữ liệu người dùng đã nhập (Trừ password)
            request.setAttribute("firstName", firstName);
            request.setAttribute("lastName", lastName);
            request.setAttribute("phone", phone);
            request.setAttribute("saveSiteId", siteIdStr);
            request.setAttribute("listSites", listSites);

            // Load lại nhân viên cũ (để giữ ID và các phần không được sửa)
            Employee tempEmp = new Employee();
            try {
                tempEmp.setEmployeeId(Integer.parseInt(employeeIdStr));
                tempEmp.setFirstName(firstName);
                tempEmp.setLastName(lastName);
                tempEmp.setPhone(phone);

                if (siteIdStr != null && !siteIdStr.trim().isEmpty()) {
                    tempEmp.setSiteId(Integer.parseInt(siteIdStr));
                } else {
                    tempEmp.setSiteId(0);
                }
            } catch (Exception ignored) {
            }

            // Forward lại về trang add cùng với thông báo lỗi
            request.getRequestDispatcher("/WEB-INF/views/employee/admin/detail.jsp").forward(request, response);
        }
    }

}
