/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.subscription.adminSubcription;

import dal.PaymentTransactionDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Employee;
import model.dto.AdminTransactionDTO;
import utils.UrlConstants;

/**
 *
 * @author dat20
 */
@WebServlet(name = "AdminSubscription", urlPatterns = {UrlConstants.URL_ADMIN + "/subscription"})
public class AdminSubscription extends HttpServlet {

    private PaymentTransactionDAO paymentTransactionDAO = new PaymentTransactionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        Employee emp = (Employee) session.getAttribute("admin");

        if (emp == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // 1. Lấy thông tin từ bộ lọc (Giao diện gửi lên qua method GET)
        String search = request.getParameter("search");
        String status = request.getParameter("status");
        String type = request.getParameter("type");

        try {
            // 2. Gọi DAO lấy danh sách giao dịch vé tháng
            // Lưu ý: Đảm bảo hàm này trong DAO trả về List<AdminSubscriptionDTO>
            List<AdminTransactionDTO> listTransactions = paymentTransactionDAO.getTransactionsWithFilter(search, status, type);

            // 3. Đẩy danh sách và đếm số lượng sang JSP
            request.setAttribute("listTransactions", listTransactions);
            request.setAttribute("totalCount", listTransactions.size());

        } catch (Exception e) {
            System.out.println("Error at AdminSubscription Servlet: " + e.getMessage());
            request.setAttribute("errorMessage", "Đã có lỗi xảy ra khi tải dữ liệu giao dịch.");
        }

        // 4. Chuyển hướng tới file JSP
        request.getRequestDispatcher("/WEB-INF/views/subscription/admin/list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}
