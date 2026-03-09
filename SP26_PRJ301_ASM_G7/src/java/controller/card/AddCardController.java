/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.card;

import dal.CardDAO;
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
@WebServlet(name = "AddCardController", urlPatterns = {UrlConstants.URL_ADMIN + "/site/add-card"})
public class AddCardController extends HttpServlet {

    CardDAO cardDAO = new CardDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession httpSession = request.getSession();

        String siteIdStr = request.getParameter("siteId");
        String cardQuantityStr = request.getParameter("cardQuantity");

        try {
            int validSiteId = ValidationUtils.requireValidInt(siteIdStr, "SiteId không hợp lệ");
            int cardQuantity = ValidationUtils.requireValidInt(cardQuantityStr, "Số lượng thẻ không hợp lệ");

            //add card
            cardDAO.addMultipleCards(validSiteId, cardQuantity);

            httpSession.setAttribute("successMessage", "Thêm " + cardQuantity + " thẻ thành công");
            response.sendRedirect(httpSession.getAttribute("ctx") + "/site");
        } catch (Exception e) {
            httpSession.setAttribute("errorMessage", "Lỗi khi thêm thẻ ");
            response.sendRedirect(httpSession.getAttribute("ctx") + "/site/detail?siteId=" + siteIdStr);
        }

    }

}
