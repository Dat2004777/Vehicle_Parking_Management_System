/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.customer;

import dal.AccountDAO;
import dal.CustomerDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;
import model.Customer;
import utils.ValidationUtils;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "ProfileController", urlPatterns = {"/customer-info"})
public class ProfileController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ProfileController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ProfileController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        if (account == null || account.getRole() != Account.RoleEnum.CUSTOMER) {
            response.sendRedirect(request.getContextPath());
            return;
        }

        request.getRequestDispatcher("/WEB-INF/views/customer/info-detail.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect(request.getContextPath() + "/customer-info");
            return;
        }

        HttpSession session = request.getSession();

        Account account = (Account) session.getAttribute("account");

        if (account == null || account.getRole() != Account.RoleEnum.CUSTOMER) {
            response.sendRedirect(request.getContextPath());
            return;
        }

        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            response.sendRedirect(request.getContextPath());
            return;
        }

        switch (action) {
            case "updateProfile":
                handleUpdateProfile(request, response, customer);
                break;
            case "changePassword":
                handleChangePassword(request, response, customer);
                break;
        }
    }

    private void handleUpdateProfile(HttpServletRequest request, HttpServletResponse response, Customer customer) throws ServletException, IOException {
        String userName = request.getParameter("customerName");
        String phoneNumber = request.getParameter("phone");

        boolean hasError = false;
        if (userName == null || userName.isBlank()) {
            request.setAttribute("errorName", "Không được để trống!");
            hasError = true;
        }

        if (phoneNumber == null || phoneNumber.isBlank()) {
            request.setAttribute("errorPhone", "Không được để trống!");
            hasError = true;
        } else if (!ValidationUtils.checkPhone(phoneNumber.trim())) {
            request.setAttribute("errorPhone", "Không đúng định dạng!");
            hasError = true;
        }

        if (hasError) {
            request.getRequestDispatcher("/WEB-INF/views/customer/info-detail.jsp").forward(request, response);
            return;
        }

        // Tách tên về thành Firstname với Lastname
        String[] parts = userName.trim().split("\\s+");
        String lastName = "";
        String firstName = "";

        if (parts.length >= 2) {
            lastName = parts[0];
            firstName = userName.substring(lastName.length()).trim();
        } else {
            lastName = userName;
        }

        //Kiểm tra không thay đổi
        boolean isSameName = customer.getLastname().trim().equals(lastName)
                && customer.getFirstname().trim().equals(firstName);

        boolean isSamePhone = phoneNumber.trim().equals(customer.getPhone().trim());

        if (isSameName && isSamePhone) {
            response.sendRedirect(request.getContextPath() + "/customer-info");
            return;
        }

        CustomerDAO customerDAO = new CustomerDAO();
        boolean updated = customerDAO.updateCustomerInfo(customer, firstName, lastName, phoneNumber);

        if (updated) {
            customer.setFirstname(firstName);
            customer.setLastname(lastName);
            customer.setPhone(phoneNumber);

//            session.setAttribute("customer", customer);
        }

        response.sendRedirect(request.getContextPath() + "/customer-info?success=" + updated);

    }

    private void handleChangePassword(HttpServletRequest request, HttpServletResponse response, Customer customer) throws ServletException, IOException {
        String oldPassword = request.getParameter("oldPass");
        String newPassword = request.getParameter("newPass");
        String confirmNewPasword = request.getParameter("confirmPass");

        boolean hasError = false;

        if (oldPassword == null || oldPassword.isBlank()) {
            request.setAttribute("errorOldPass", "Không được để trống!");
            hasError = true;
        }

        if (newPassword == null || newPassword.isBlank()) {
            request.setAttribute("errorNewPass", "Không được để trống!");
            hasError = true;
        }

        if (confirmNewPasword == null || confirmNewPasword.isBlank()) {
            request.setAttribute("errorConfirmPass", "Không được để trống!");
            hasError = true;
        }

        if (hasError) {
            request.getRequestDispatcher("/WEB-INF/views/customer/info-detail.jsp").forward(request, response);
            return;
        }

        oldPassword = oldPassword.trim();
        newPassword = newPassword.trim();
        confirmNewPasword = confirmNewPasword.trim();

        Account accountSession = (Account) request.getSession().getAttribute("account");
        if (accountSession == null) {
            response.sendRedirect(request.getContextPath());
            return;
        }

        AccountDAO accountDAO = new AccountDAO();

        Account accountOldPass = accountDAO.checkAccount(accountSession.getUsername(), oldPassword);
        if (accountOldPass == null) {
            request.setAttribute("errorOldPass", "Mật khẩu hiện tại không đúng!");
            request.getRequestDispatcher("/WEB-INF/views/customer/info-detail.jsp").forward(request, response);
            return;
        }

        boolean checkNewPass = !newPassword.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
        boolean checkConfirmPass = !confirmNewPasword.equals(newPassword);

        if (checkNewPass) {
            request.setAttribute("errorNewPass", "Mật khẩu phải ≥8 ký tự, gồm hoa, thường, số, ký tự đặc biệt!");
            request.getRequestDispatcher("/WEB-INF/views/customer/info-detail.jsp").forward(request, response);
            return;
        }

        if (checkConfirmPass) {
            request.setAttribute("errorConfirmPass", "Mật khẩu xác nhận không đúng!");
            request.getRequestDispatcher("/WEB-INF/views/customer/info-detail.jsp").forward(request, response);
            return;
        }

        boolean changePassword = accountDAO.changePassword(customer.getAccountId(), newPassword);

        response.sendRedirect(request.getContextPath() + "/customer-info?success=" + changePassword);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
