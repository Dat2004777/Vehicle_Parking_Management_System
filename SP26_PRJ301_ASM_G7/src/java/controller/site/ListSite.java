package controller.site;

import dal.SiteDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.ParkingSite;
import utils.UrlConstants;

/**
 *
 * @author Admin
 */

//@WebServlet(name = "ListSite", urlPatterns = {UrlConstants.URL_ADMIN + "/site/list"})
public class ListSite extends HttpServlet {

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

        SiteDAO siteDAO = new SiteDAO();
        
        HttpSession session = request.getSession();
        
        session.setAttribute("rolePrefix", UrlConstants.URL_ADMIN);
        session.setAttribute("ctx", request.getContextPath() + UrlConstants.URL_ADMIN);
        
        List<ParkingSite> siteList = siteDAO.getAll();
        
        request.setAttribute("siteList", siteList);
        
        request.getRequestDispatcher("/WEB-INF/views/site/list.jsp").forward(request, response);
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
        doGet(request, response);
    }

}
