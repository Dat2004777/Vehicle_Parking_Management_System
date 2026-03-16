/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.site.adminSite;

import dal.SiteDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Employee;
import model.ParkingSite;
import model.dto.SiteDensityDTO;
import model.dto.SiteStateDTO;
import utils.UrlConstants;

/**
 *
 * @author dat20
 */
@WebServlet(name = "AdminSiteController", urlPatterns = {UrlConstants.URL_ADMIN + "/site"})
public class AdminSiteController extends HttpServlet {

    private SiteDAO siteDAO = new SiteDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        Employee emp = (Employee) session.getAttribute("admin");

        if (emp == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String siteSearchQuery = request.getParameter("siteSearchQuery");

        List<ParkingSite> parkingSites;
        if (siteSearchQuery != null && !siteSearchQuery.trim().isEmpty()) {
            parkingSites = siteDAO.siteSearchQuery(siteSearchQuery);
        } else {
            parkingSites = siteDAO.getAllSites();
        }

        List<ParkingSite> operatingSites = new ArrayList<>();
        List<ParkingSite> closedSites = new ArrayList<>();
        List<ParkingSite> maintenanceSites = new ArrayList<>();

        for (ParkingSite parkingSite : parkingSites) {
            switch (parkingSite.getSiteState()) {
                case OPERATING:
                    operatingSites.add(parkingSite);
                    break;
                case MAINTENANCE:
                    maintenanceSites.add(parkingSite);
                    break;
                case CLOSED:
                    closedSites.add(parkingSite);
                    break;
            }
        }

        int totalSites = parkingSites.size();
        int operatingSiteCount = operatingSites.size();
        int closedSiteCount = closedSites.size();
        int maintenanceSiteCount = maintenanceSites.size();

        SiteStateDTO siteStateDTO = new SiteStateDTO(
                totalSites,
                operatingSiteCount,
                closedSiteCount,
                maintenanceSiteCount);

        List<SiteDensityDTO> siteDensityDTOs;
        if (siteSearchQuery != null && !siteSearchQuery.trim().isEmpty()) {
            siteDensityDTOs = siteDAO.getSiteDensities(siteSearchQuery);
        } else {
            siteDensityDTOs = siteDAO.getSiteDensities();
        }

        Map<Integer, SiteDensityDTO> densityMap = new HashMap<>();
        for (SiteDensityDTO siteDensityDTO : siteDensityDTOs) {
            densityMap.put(siteDensityDTO.getSiteId(), siteDensityDTO);
        }

        request.setAttribute("densityMap", densityMap);
        request.setAttribute("siteStateDTO", siteStateDTO);
        request.setAttribute("parkingSites", parkingSites);

        request.getRequestDispatcher("/WEB-INF/views/site/admin/list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

}
