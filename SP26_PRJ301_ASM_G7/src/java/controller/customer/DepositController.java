/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.customer;

import dal.CustomerDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;
import model.Customer;

/**
 *
 * @author dat20
 */
@WebServlet(name = "DepositController", urlPatterns = {"/deposit"})
public class DepositController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        Customer customer = (Customer) session.getAttribute("customer");

        if (account == null || account.getRole() != Account.RoleEnum.CUSTOMER || customer == null) {
            response.sendRedirect(request.getContextPath() + "/login");
        }else{
            response.sendRedirect(request.getContextPath() +"/customer-info");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("customer");

        // (Filter đã chặn ở ngoài rồi, nhưng cứ check lại cho chắc)
        if (customer == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // Lấy số tiền khách muốn nạp (bỏ các ký tự không phải số)
            String amountStr = request.getParameter("amount").replaceAll("[^0-9]", "");
            long amount = Long.parseLong(amountStr);

            if (amount < 10000) {
                throw new IllegalArgumentException("Số tiền nạp tối thiểu là 10.000đ");
            }

            // Gọi DAO để cộng tiền vào DB
            CustomerDAO customerDAO = new CustomerDAO();
            boolean success = customerDAO.addBalance(customer.getCustomerId(), amount);

            if (success) {
                // QUAN TRỌNG: Cập nhật lại số dư mới vào Session để hiển thị trên giao diện
                customer.setWalletAmount(customer.getWalletAmount()+ amount);
                session.setAttribute("customer", customer);

                session.setAttribute("successMessage", "Nạp thành công " + amount + "đ vào tài khoản!");
            } else {
                session.setAttribute("errorMessage", "Nạp tiền thất bại, vui lòng thử lại.");
            }

        } catch (Exception e) {
            session.setAttribute("errorMessage", "Số tiền không hợp lệ!");
        }

        // Quay về trang ví hoặc trang profile của khách
        response.sendRedirect(request.getContextPath() + "/customer-info");
    }

}
