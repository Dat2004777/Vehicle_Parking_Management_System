package controller.manager;

import dal.ManagerDAO;
import dal.SiteDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Employee;
import model.ParkingSite;
import utils.UrlConstants;

/**
 * Controller xử lý trang chủ (Dashboard) dành riêng cho Manager
 */
@WebServlet(name = "ManagerDashboardController", urlPatterns = {UrlConstants.URL_MANAGER + "/dashboard", UrlConstants.URL_MANAGER})
public class ManagerDashboardController extends HttpServlet {

    private SiteDAO siteDAO = new SiteDAO();
    private ManagerDAO dashboardDAO = new ManagerDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        Integer userSiteId = (Integer) session.getAttribute("userSiteId");
        String username = (String) session.getAttribute("username");

        request.setAttribute("managerName", username != null ? username : "Quản lý");

        Employee emp = (Employee) session.getAttribute("manager");

        if (emp == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (userSiteId != null && userSiteId > 0) {
            // Lấy thông tin bãi xe
            ParkingSite mySite = siteDAO.getSiteById(userSiteId);
            request.setAttribute("mySite", mySite);

            // LẤY DỮ LIỆU THỐNG KÊ TỪ DAO
            int totalParked = dashboardDAO.countCurrentlyParked(userSiteId);
            long todayRevenue = dashboardDAO.getTodayRevenue(userSiteId);
            int activeSubs = dashboardDAO.countActiveSubscriptions(userSiteId);
            int todayBookings = dashboardDAO.countTodayBookings(userSiteId);
            java.util.Map<String, Long> revenueMap = dashboardDAO.getLast7DaysRevenue(userSiteId);
            StringBuilder labels = new StringBuilder("[");
            StringBuilder data = new StringBuilder("[");

            for (java.util.Map.Entry<String, Long> entry : revenueMap.entrySet()) {
                labels.append("'").append(entry.getKey()).append("',");
                data.append(entry.getValue()).append(",");
            }
            labels.append("]");
            data.append("]");

            // Xóa dấu phẩy thừa ở cuối chuỗi JSON (ví dụ: ['12/10',] -> ['12/10'])
            request.setAttribute("chartLabels", labels.toString().replace(",]", "]"));
            request.setAttribute("chartData", data.toString().replace(",]", "]"));

            // BẮN LÊN JSP
            request.setAttribute("totalParked", totalParked);
            request.setAttribute("todayRevenue", todayRevenue);
            request.setAttribute("activeSubs", activeSubs);
            request.setAttribute("todayBookings", todayBookings);

        } else {
            request.setAttribute("errorMessage", "Bạn chưa được phân công quản lý bãi xe nào. Vui lòng liên hệ Admin!");
        }

        request.getRequestDispatcher("/WEB-INF/views/manager/dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
