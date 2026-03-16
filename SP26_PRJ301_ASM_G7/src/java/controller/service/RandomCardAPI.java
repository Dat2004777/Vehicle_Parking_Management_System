package controller.service;

import dal.CardDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import model.Employee;
import model.ParkingCard;

@WebServlet(name = "RandomCardApiController", urlPatterns = {"/api/parking/random-card"})
public class RandomCardAPI extends HttpServlet {

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        Employee emp = (Employee) request.getSession().getAttribute("staff");
        if (emp == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        CardDAO cardDAO = new CardDAO();
        ParkingCard card = cardDAO.getAvailableCardAtSite(((Employee)request.getSession().getAttribute("staff")).getSiteId());
        
        if (card != null) {
            // Trả về JSON chứa mã thẻ
            String cardId = card.getCardId();
            response.getWriter().write("{\"success\": true, \"cardId\": \"" + cardId + "\"}");
        } else {
            response.getWriter().write("{\"success\": false, \"message\": \"Đã hết thẻ trống tại bãi này!\"}");
        }
    }
}