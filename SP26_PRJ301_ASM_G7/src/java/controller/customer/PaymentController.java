/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.customer;

import dal.PriceConfigDAO;
import dal.SiteDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import model.Account;
import model.ParkingSite;
import model.dto.BookingPreviewDTO;
import model.dto.VehicleBasePriceDTO;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "PaymentController", urlPatterns = {"/payment"})
public class PaymentController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet PaymentController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PaymentController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

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

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        if (account == null || account.getRole() != Account.RoleEnum.CUSTOMER){ 
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        

        String action = request.getParameter("action");
        String siteId = request.getParameter("siteId");

        if (action == null || siteId == null || action.isBlank() || siteId.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/sites?action=booking");
            return;
        }

        SiteDAO siteDAO = new SiteDAO();
        PriceConfigDAO priceConfigDAO = new PriceConfigDAO();

        ParkingSite site = new ParkingSite();
        List<VehicleBasePriceDTO> vehicles = new ArrayList<>();
        try {
            site = siteDAO.getById(Integer.parseInt(siteId));
            vehicles = priceConfigDAO.getBasePriceHour(Integer.parseInt(siteId));
        } catch (NumberFormatException e) {
            System.out.println(e.toString());
        }

        BookingPreviewDTO bookingPreview = (BookingPreviewDTO) session.getAttribute("bookingPreview");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        LocalDateTime formattedIn = bookingPreview.getTimeIn();
        LocalDateTime formattedOut = bookingPreview.getTimeOut();

        String inDateTime = formattedIn.format(formatter);
        String outDateTime = formattedOut.format(formatter);

        int hours = bookingPreview.getHours();
        long price = bookingPreview.getBasePrice();
        long totalPrice = bookingPreview.getTotalPrice();

        request.setAttribute("site", site);
        request.setAttribute("vehicles", vehicles);
        request.setAttribute("inDateTime", inDateTime);
        request.setAttribute("outDateTime", outDateTime);
        request.setAttribute("hours", hours);
        request.setAttribute("price", price);
        request.setAttribute("totalPrice", totalPrice);

//        session.removeAttribute("inDateTime");
//        session.removeAttribute("outDateTime");
//        session.removeAttribute("hours");
//        session.removeAttribute("price");
//        session.removeAttribute("totalPrice");
        request.getRequestDispatcher("/WEB-INF/views/public/booking-transaction.jsp").forward(request, response);
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
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
