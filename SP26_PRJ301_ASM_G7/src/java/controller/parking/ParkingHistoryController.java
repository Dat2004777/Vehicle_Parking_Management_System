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
        
        String state = request.getParameter("state");
        Employee emp = (Employee) request.getSession().getAttribute("staff");
        
        if (emp == null) {
            response.sendRedirect("/login");
            return;
        }
        
        List<RecentActivityDTO> recentLogs = getRecentActivities(emp.getSiteId(), 20, state);
        request.setAttribute("recentLogs", recentLogs);
        request.getRequestDispatcher("/WEB-INF/views/parking/history.jsp").forward(request, response);
    }

    /**
     * Hàm lấy nhật ký từ Database và biến đổi thành DTO hiển thị (Chuẩn Clean
     * Architecture)
     */
    private List<RecentActivityDTO> getRecentActivities(int siteId, int limit, String state) {
        List<ParkingSession> rawLogs = sessionDAO.getRecentLogs(siteId, limit, state);
        List<RecentActivityDTO> dtoList = new ArrayList<>();

        // CẬP NHẬT: Thêm định dạng Ngày/Tháng/Năm (dd/MM/yyyy)
        // Dùng HH:mm (24h) thường chuyên nghiệp hơn cho bãi xe, nếu thích AM/PM thì dùng hh:mm a
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (ParkingSession session : rawLogs) {
            String formattedTime = "--/--/---- --:--";

            // Lấy thời gian tùy thuộc vào việc xe đang đỗ hay đã ra
            if ("parked".equalsIgnoreCase(session.getSessionState()) && session.getEntryTime() != null) {
                formattedTime = session.getEntryTime().format(timeFormatter);
            } else if ("completed".equalsIgnoreCase(session.getSessionState()) && session.getExitTime() != null) {
                formattedTime = session.getExitTime().format(timeFormatter);
            }

            // DTO giờ đây cực kỳ mỏng nhẹ, Controller không còn chứa code Giao diện (CSS) nữa
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
