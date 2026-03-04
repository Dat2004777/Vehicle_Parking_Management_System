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
import utils.UrlConstants;
import utils.ValidationUtils;

/**
 *
 * @author dat20
 */
@WebServlet(name = "DeleteSiteController", urlPatterns = {UrlConstants.URL_ADMIN + "/site/detail/delete"})
public class DeleteSiteController extends HttpServlet {

    private SiteDAO siteDAO = new SiteDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession httpSession = request.getSession();
        String siteIdStr = request.getParameter("siteId");
        
        int valideSiteId = ValidationUtils.requireValidInt(siteIdStr, "SiteId không được trống!");

        if (siteDAO.deleteSiteBySiteIdAndChangeEmp(valideSiteId)) {
            response.sendRedirect(httpSession.getAttribute("ctx") + "/site?msg=deleteSuccess");
        } else {
            response.sendRedirect(httpSession.getAttribute("ctx") + "/site?msg=deleteError");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
