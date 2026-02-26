/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.dashboard;

import dal.ParkingSessionDAO;
import dal.PaymentTransactionDAO;
import dal.SiteDAO;
import dal.SubscriptionDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.dto.DashboardDTO;
import model.dto.SiteDensityDTO;
import utils.UrlConstants;
import utils.ValidationUtils;

/**
 *
 * @author dat20
 */
@WebServlet(name = "AdminDashboardController", urlPatterns = {UrlConstants.URL_ADMIN + "/dashboard", UrlConstants.URL_ADMIN})
public class AdminDashboardController extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
        ValidationUtils validationUtils = new ValidationUtils();

        PaymentTransactionDAO paymentTransactionDAO = new PaymentTransactionDAO();
        SiteDAO siteDAO = new SiteDAO();
        SubscriptionDAO subscriptionDAO = new SubscriptionDAO();
        ParkingSessionDAO parkingSessionDAO = new ParkingSessionDAO();

        String siteIdStr = request.getParameter("siteId");

        if (siteIdStr == null || siteIdStr.equalsIgnoreCase("0")) {
            DashboardDTO dashboardDTO = new DashboardDTO(
                    paymentTransactionDAO.getTotalAmountInCurrentMonth(),
                    parkingSessionDAO.getCurrentParkedVehiclesInCurrentMonth(),
                    subscriptionDAO.getTotalSubscriptionByCurrentMonth(),
                    siteDAO.getAllActiveSites());
            List<Long> chartData = paymentTransactionDAO.getWeeklyRevenue();
            List<SiteDensityDTO> siteDensityDTOs = siteDAO.getSiteDensities();
            request.setAttribute("siteDensityDTOs", siteDensityDTOs);
            request.setAttribute("chartData", chartData);
            request.setAttribute("dashboardDTO", dashboardDTO);
        } else {
            try {
                int siteId = validationUtils.requireValidInt(siteIdStr, "Error from parseInt siteId in AdminDashboardController");
                DashboardDTO dashboardDTO = new DashboardDTO(
                        paymentTransactionDAO.getTotalAmountInCurrentMonthById(siteId),
                        parkingSessionDAO.getCurrentParkedVehiclesInCurrentMonthById(siteId),
                        subscriptionDAO.getTotalSubscriptionInCurrentMonthById(siteId),
                        siteDAO.getAllActiveSites());
                List<Long> chartData = paymentTransactionDAO.getWeeklyRevenueBySiteId(siteId);
                List<SiteDensityDTO> siteDensityDTOs = siteDAO.getSiteDensitiesById(siteId);
                request.setAttribute("siteDensityDTOs", siteDensityDTOs);
                request.setAttribute("chartData", chartData);
                request.setAttribute("dashboardDTO", dashboardDTO);
                request.setAttribute("siteId", siteId);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        request.getRequestDispatcher("/WEB-INF/views/dashboard/dashboard.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
