package controller.manager;

import dal.PaymentTransactionDAO;
import model.dto.PaymentHistoryDTO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Employee;
import model.dto.TransactionHistoryDTO;
import utils.UrlConstants;

@WebServlet(name = "ManagerTransactionHistoryController", urlPatterns = {UrlConstants.URL_MANAGER + "/transaction-history"})
public class ManagerTransactionHistoryController extends HttpServlet {

    private PaymentTransactionDAO historyDAO = new PaymentTransactionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // CHỐT CHẶN BẢO MẬT
        Employee emp = (Employee) session.getAttribute("manager");

        if (emp == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String userRole = (String) session.getAttribute("userRole");
        if (!"manager".equals(userRole)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Truy cập bị từ chối!");
            return;
        }

        // Lấy ID bãi xe của Manager
        Integer userSiteId = (Integer) session.getAttribute("userSiteId");
        if (userSiteId == null || userSiteId == 0) {
            response.sendRedirect(request.getContextPath() + UrlConstants.URL_MANAGER + "/dashboard");
            return;
        }

        String search = request.getParameter("search");
        String serviceType = request.getParameter("serviceType");
        String status = request.getParameter("status");

        try {
            // Lọc dữ liệu Lịch sử giao dịch dựa vào userSiteId
            List<PaymentHistoryDTO> list = historyDAO.getTransactionHistoryBySiteId(userSiteId, search, serviceType, status);
            request.setAttribute("listTransactions", list);
            request.setAttribute("totalCount", list.size());
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Lỗi tải dữ liệu lịch sử giao dịch.");
        }

        // Chuyển hướng về JSP của Manager
        request.getRequestDispatcher("/WEB-INF/views/manager/transaction-list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Không thể sửa lịch sử giao dịch!");
    }
}
