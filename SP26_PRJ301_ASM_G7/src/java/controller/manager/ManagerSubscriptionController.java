package controller.manager;

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

@WebServlet(name = "ManagerSubscriptionController", urlPatterns = {UrlConstants.URL_MANAGER + "/subscription"})
public class ManagerSubscriptionController extends HttpServlet {

    private PaymentTransactionDAO paymentTransactionDAO = new PaymentTransactionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // CHỐT CHẶN BẢO MẬT: Kiểm tra role Manager
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

        // Lấy thông tin từ bộ lọc (Search, Status, Type)
        String search = request.getParameter("search");
        String status = request.getParameter("status");
        String type = request.getParameter("type");

        try {
            // Lấy danh sách Vé tháng chỉ thuộc bãi xe của Manager này
            List<AdminTransactionDTO> listTransactions = paymentTransactionDAO.getSubscriptionsBySiteId(userSiteId, search, status, type);

            request.setAttribute("listTransactions", listTransactions);
            request.setAttribute("totalCount", listTransactions.size());

        } catch (Exception e) {
            System.out.println("Error at ManagerSubscriptionController: " + e.getMessage());
            request.setAttribute("errorMessage", "Đã có lỗi xảy ra khi tải dữ liệu.");
        }

        // Chuyển hướng tới file JSP trong thư mục manager
        request.getRequestDispatcher("/WEB-INF/views/manager/subscription.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // NGĂN CHẶN THAO TÁC CẬP NHẬT TỪ MANAGER
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền chỉnh sửa vé tháng!");
    }
}
