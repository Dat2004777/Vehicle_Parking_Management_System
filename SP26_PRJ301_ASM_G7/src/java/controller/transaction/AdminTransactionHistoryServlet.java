/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.transaction;

import dal.PaymentTransactionDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.dto.AdminTransactionHistoryDTO;
import utils.UrlConstants;

/**
 *
 * @author dat20
 */
@WebServlet(name = "AdminTransactionHistoryServlet", urlPatterns = {UrlConstants.URL_ADMIN + "/transaction-history"})
public class AdminTransactionHistoryServlet extends HttpServlet {

    private PaymentTransactionDAO historyDAO = new PaymentTransactionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String search = request.getParameter("search");
        String serviceType = request.getParameter("serviceType");
        String status = request.getParameter("status");

        try {
            List<AdminTransactionHistoryDTO> list = historyDAO.getAllTransactionHistory(search, serviceType, status);
            request.setAttribute("listTransactions", list);
            request.setAttribute("totalCount", list.size());
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Lỗi tải dữ liệu lịch sử giao dịch.");
        }

        request.getRequestDispatcher("/WEB-INF/views/transaction-history/admin/list.jsp").forward(request, response);
    }
}
