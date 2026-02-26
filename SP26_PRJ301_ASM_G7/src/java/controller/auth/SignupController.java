/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.auth;

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
import utils.ValidationUtils;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "SignupController", urlPatterns = {"/signup"})
public class SignupController extends HttpServlet {

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
            out.println("<title>Servlet SignupController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SignupController at " + request.getContextPath() + "</h1>");
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
        Account acc = (Account) session.getAttribute("account");

        if (acc == null) {
            request.setAttribute("authMode", "signup");
            request.getRequestDispatcher("/WEB-INF/views/public/login-signup.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath());
        }
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
        String username = request.getParameter("username").trim();
        String firstname = request.getParameter("firstname").trim();
        String lastname = request.getParameter("lastname").trim();
        String email = request.getParameter("email").trim();
        String phone = request.getParameter("phone").trim();
        String password = request.getParameter("password_1").trim();
        String confirmPassword = request.getParameter("password_2").trim();

        AccountDAO accDAO = new AccountDAO();
        CustomerDAO customerDAO = new CustomerDAO();

        boolean isUsernameInvalid = username.contains(" ");
        boolean isUsernameExist = accDAO.getUsername(username);
        boolean isPasswordMismatch = !password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$") && !password.equals(confirmPassword);
        boolean isEmailMismatch = !ValidationUtils.checkEmail(email);
        boolean isEmailExist = customerDAO.isEmailExist(email);
        boolean isPhoneMismatch = !ValidationUtils.checkPhone(phone);

        boolean hasError = false;

        // ===== CHECK USERNAME =====
        if (isUsernameInvalid) {
            request.setAttribute("errorUsername", "Tài khoản không hợp lệ!");
            hasError = true;
        } else if (isUsernameExist) {
            request.setAttribute("errorUsername", "Tài khoản này đã tồn tại!");
            hasError = true;
        }

        // ===== CHECK PASSWORD =====
        if (isPasswordMismatch) {
            request.setAttribute("errorPassConfirm", "Mật khẩu không khớp!");
            hasError = true;
        }

        // ===== CHECK EMAIL =====
        if (isEmailMismatch) {
            request.setAttribute("errorEmail", "Email không hợp lệ!");
            hasError = true;
        } else if (isEmailExist) {
            request.setAttribute("errorEmail", "Email đã tồn tại!");
            hasError = true;
        }

        // ===== CHECK PHONE =====
        if (isPhoneMismatch) {
            request.setAttribute("errorPhone", "SĐT không hợp lệ!");
            hasError = true;
        }

        // ===== NẾU CÓ LỖI → QUAY LẠI SIGNUP =====
        if (hasError) {
            request.setAttribute("authMode", "signup");
            // ===== GỮ GIÁ TRỊ NHẬP LẠI =====
            request.setAttribute("username", username);
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/views/public/login-signup.jsp")
                    .forward(request, response);
            return;
        }

        // ===== ĐĂNG KÝ THÀNH CÔNG =====
        int acc_id = accDAO.insertAccount(username, password, "customer");
        customerDAO.insertCustomer(firstname, lastname, phone, email, acc_id);

        response.sendRedirect(request.getContextPath() + "/signup?success=true");
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
