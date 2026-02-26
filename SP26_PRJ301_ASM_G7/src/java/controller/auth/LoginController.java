/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.auth;

import dal.AccountDAO;
import dal.CustomerDAO;
import dal.EmployeeDAO;
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
import utils.UrlConstants;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "LoginController", urlPatterns = {"/login"})
public class LoginController extends HttpServlet {

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
            out.println("<title>Servlet LoginController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LoginController at " + request.getContextPath() + "</h1>");
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
            request.setAttribute("authMode", "login");
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
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        AccountDAO accDAO = new AccountDAO();
        Account acc = accDAO.checkAccount(username, password);
        
        HttpSession session = request.getSession();
        
        if(acc != null){
            EmployeeDAO empDAO = new EmployeeDAO();
            String contextPath = request.getContextPath();
            session.setAttribute("account",acc);
            switch(acc.getRole()){
                case ADMIN:
                    int adminId = empDAO.getEmployeeId(acc.getAccount_id(), "admin");
                    acc.setEmployeeId(adminId);
                    session.setAttribute("rolePrefix", UrlConstants.URL_ADMIN);
                    session.setAttribute("ctx", contextPath + UrlConstants.URL_ADMIN);
                    response.sendRedirect(contextPath + UrlConstants.URL_ADMIN + "/dashboard");
                    return;
                case STAFF:
                    int staffId = empDAO.getEmployeeId(acc.getAccount_id(), "staff");
                    acc.setEmployeeId(staffId);
                    session.setAttribute("rolePrefix", UrlConstants.URL_STAFF);
                    session.setAttribute("ctx", contextPath + UrlConstants.URL_STAFF);
                    response.sendRedirect(contextPath + UrlConstants.URL_STAFF);
                    return;
                case MANAGER:
                    int managerId = empDAO.getEmployeeId(acc.getAccount_id(), "manager");
                    acc.setEmployeeId(managerId);
                    session.setAttribute("rolePrefix", UrlConstants.URL_MANAGER);
                    session.setAttribute("ctx", contextPath + UrlConstants.URL_MANAGER);
                    response.sendRedirect(contextPath + UrlConstants.URL_MANAGER);
                    return;
                case CUSTOMER:
                    CustomerDAO customerDAO = new CustomerDAO();
                    Customer customer = customerDAO.getCustomerProfile(acc.getAccount_id());
                    session.setAttribute("customer",customer);
                    response.sendRedirect(contextPath);
                    return;
                          
            }
        }
        
        request.setAttribute("errorMessage","Hãy đăng nhập lại");
        request.setAttribute("authMode","login");
        request.setAttribute("username", username);
        request.getRequestDispatcher("/WEB-INF/views/public/login-signup.jsp").forward(request, response);
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
