/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.parking;

import dal.SessionDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import model.Employee;
import model.ParkingSession;
import model.dto.RecentActivityDTO;
import utils.UrlConstants;

/**
 *
 * @author Admin
 */
@WebServlet(name = "HistoryController", urlPatterns = {UrlConstants.URL_STAFF + "/parking/history"})
public class ParkingHistoryController extends HttpServlet {

    private SessionDAO sessionDAO = new SessionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Employee emp = (Employee) request.getSession().getAttribute("staff");
        if (emp == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String state = request.getParameter("state");

        // --- BẮT ĐẦU LOGIC PHÂN TRANG ---
        int pageSize = 2; // Số dòng trên 1 trang (bạn có thể đổi thành 15 hoặc 20)
        int page = 1;      // Mặc định là trang 1

        String pageStr = request.getParameter("page");
        if (pageStr != null && !pageStr.isEmpty()) {
            try {
                page = Integer.parseInt(pageStr);
                if (page < 1) {
                    page = 1; // Chống người dùng nhập ?page=-5
                }
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        // 1. Tính tổng số trang
        int totalRecords = sessionDAO.getTotalLogsCount(emp.getSiteId(), state);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        // Đảm bảo page không vượt quá totalPages nếu người dùng nhập linh tinh
        if (page > totalPages && totalPages > 0) {
            page = totalPages;
        }

        // 2. Lấy danh sách DTO theo trang
        List<RecentActivityDTO> recentLogs = getRecentActivitiesByPage(emp.getSiteId(), page, pageSize, state);
        // --- KẾT THÚC LOGIC PHÂN TRANG ---

        // Đẩy dữ liệu sang JSP
        request.setAttribute("recentLogs", recentLogs);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("state", state); // Truyền ngược state lại JSP để giữ filter khi chuyển trang

        request.getRequestDispatcher("/WEB-INF/views/parking/history.jsp").forward(request, response);
    }

    // Cập nhật lại hàm này để gọi hàm getLogsByPage mới trong DAO
    private List<RecentActivityDTO> getRecentActivitiesByPage(int siteId, int page, int pageSize, String state) {
        List<ParkingSession> rawLogs = sessionDAO.getLogsByPage(siteId, page, pageSize, state);
        List<RecentActivityDTO> dtoList = new ArrayList<>();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (ParkingSession session : rawLogs) {
            String formattedTime = "--/--/---- --:--";
            if ("parked".equalsIgnoreCase(session.getSessionState()) && session.getEntryTime() != null) {
                formattedTime = session.getEntryTime().format(timeFormatter);
            } else if ("completed".equalsIgnoreCase(session.getSessionState()) && session.getExitTime() != null) {
                formattedTime = session.getExitTime().format(timeFormatter);
            }

            dtoList.add(new RecentActivityDTO(
                    session.getLicensePlate(),
                    formattedTime,
                    session.getSessionState(),
                    session.getCardId()
            ));
        }
        return dtoList;
    }

}
