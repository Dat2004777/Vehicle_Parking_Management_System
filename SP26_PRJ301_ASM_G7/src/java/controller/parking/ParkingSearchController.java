/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.parking;

import dal.SearchDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Employee;
import model.dto.search.CardSearchResultDTO;
import model.dto.TransactionHistoryDTO;
import model.dto.search.VehicleSearchResultDTO;
import utils.UrlConstants;

@WebServlet(name = "StaffSearchController", urlPatterns = {UrlConstants.URL_STAFF + "/search"})
public class ParkingSearchController extends HttpServlet {

    private final SearchDAO searchDAO = new SearchDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String type = request.getParameter("type");
        String query = request.getParameter("query");
        Employee emp = (Employee) request.getSession().getAttribute("staff");
        if (emp == null) {
            response.sendRedirect("/login");
            return;
        }
        
        int siteId = emp.getSiteId();

        if (type != null && query != null && !query.trim().isEmpty()) {
            query = query.trim();

            if ("vehicle".equals(type)) {
                // Tiền xử lý: Viết hoa biển số xe để tìm kiếm chính xác
                query = query.toUpperCase();

                // Gọi DAO lấy dữ liệu xe
                VehicleSearchResultDTO vehicleResult = searchDAO.findActiveVehicleByPlate(query, siteId);

                if (vehicleResult != null) {
                    // Nếu tìm thấy xe, mới gọi tiếp để lấy lịch sử
                    List<TransactionHistoryDTO> txns = searchDAO.getVehicleTransactions(query, siteId);
                    request.setAttribute("vehicleResult", vehicleResult);
                    request.setAttribute("vehicleTransactions", txns);
                }
            } else if ("card".equals(type)) {
                // Gọi DAO lấy dữ liệu thẻ
                CardSearchResultDTO cardResult = searchDAO.findCardById(query, siteId);

                if (cardResult != null) {
                    // Nếu tìm thấy thẻ, mới gọi tiếp để lấy lịch sử
                    List<TransactionHistoryDTO> txns = searchDAO.getCardTransactions(query, siteId);
                    request.setAttribute("cardResult", cardResult);
                    request.setAttribute("cardTransactions", txns);
                }
            }
        }

        // 2. Giữ lại giá trị để JSP không bị mất khi load lại
        request.setAttribute("activeTab", type != null ? type : "vehicle");
        // Param query sẽ tự động được JSP lấy qua ${param.query}

        // 3. Forward dữ liệu sang View
        request.getRequestDispatcher("/WEB-INF/views/parking/search.jsp").forward(request, response);
    }

}
