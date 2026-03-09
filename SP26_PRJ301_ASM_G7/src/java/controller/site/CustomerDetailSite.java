/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.site;

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
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import model.ParkingSite;
import model.dto.BookingPreviewDTO;
import model.dto.VehicleBasePriceDTO;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "CustomerSDetailSite", urlPatterns = {"/sites/site-detail"})
public class CustomerDetailSite extends HttpServlet {

    private final SiteDAO siteDAO = new SiteDAO();
    private final PriceConfigDAO priceConfigDAO = new PriceConfigDAO();

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
            out.println("<title>Servlet CustomerSDetailSite</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CustomerSDetailSite at " + request.getContextPath() + "</h1>");
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

        String action = request.getParameter("action");
        if (action == null || action.isBlank()) {
            response.sendRedirect(request.getContextPath());
            return;
        }
        String siteId = request.getParameter("siteId");
        if (siteId == null || siteId.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/sites?action=" + action);
            return;
        }

        switch (action) {
            case "booking":
                redirectToBookingPage(request, response, siteId);
                break;
            case "buying":
                redirectToBuyingPage(request, response, siteId);
                break;
        }

    }

    private void redirectToBookingPage(HttpServletRequest request, HttpServletResponse response, String siteId)
            throws ServletException, IOException {
        List<VehicleBasePriceDTO> vehicles = new ArrayList<>();
        ParkingSite site = new ParkingSite(id, name, address, region, status, managerId, totalSlots);

        try {
            site = siteDAO.getById(Integer.parseInt(siteId));
            vehicles = priceConfigDAO.getBasePriceHour(Integer.parseInt(siteId));
        } catch (NumberFormatException e) {
            System.out.println(e.toString());
        }
        request.setAttribute("site", site);
        request.setAttribute("vehicles", vehicles);
        request.getRequestDispatcher("/WEB-INF/views/public/site-detail.jsp").forward(request, response);
    }

    private void redirectToBuyingPage(HttpServletRequest request, HttpServletResponse response, String siteId)
            throws ServletException, IOException {
        List<VehicleBasePriceDTO> vehicles = new ArrayList<>();
        ParkingSite site = new ParkingSite(id, name, address, region, status, managerId, totalSlots);
        try {
            site = siteDAO.getById(Integer.parseInt(siteId));
            vehicles = priceConfigDAO.getBasePriceMonth(Integer.parseInt(siteId));
        } catch (NumberFormatException e) {
            System.out.println(e.toString());
        }
        request.setAttribute("site", site);
        request.setAttribute("vehicles", vehicles);
        request.getRequestDispatcher("/WEB-INF/views/public/buying-transaction.jsp").forward(request, response);
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

        HttpSession session = request.getSession();

        //Lấy id của site
        String siteId = request.getParameter("siteId");

        //Time đến
        String dateIn = request.getParameter("dateIn");
        String timeIn = request.getParameter("timeIn");

        //Time ra
        String dateOut = request.getParameter("dateOut");
        String timeOut = request.getParameter("timeOut");

        if (dateIn == null || dateIn.isBlank() || timeIn == null || timeIn.isBlank()
                || dateOut == null || dateOut.isBlank() || timeOut == null || timeOut.isBlank()) {
            request.setAttribute("dateError", "Không được để trống ngày");
            request.getRequestDispatcher("/WEB-INFF/views/public/site-detail.jsp").forward(request, response);
            return;
        }

        //lay lai danh sach
        List<VehicleBasePriceDTO> vehicles = new ArrayList<>();
        ParkingSite site = new ParkingSite(id, name, address, region, status, managerId, totalSlots);

        try {
            site = siteDAO.getById(Integer.parseInt(siteId));
            vehicles = priceConfigDAO.getBasePriceHour(Integer.parseInt(siteId));
        } catch (NumberFormatException e) {
            System.out.println(e.toString());
        }
        request.setAttribute("site", site);
        request.setAttribute("vehicles", vehicles);

        boolean hasError = false;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        LocalDateTime inDateTime = LocalDateTime.parse(dateIn + " " + timeIn, formatter);
        LocalDateTime outDateTime = LocalDateTime.parse(dateOut + " " + timeOut, formatter);

        if (inDateTime.isAfter(outDateTime) || inDateTime.isEqual(outDateTime)) {
            request.setAttribute("dateError", "Thời gian ra phải sau thời gian vào!");
            hasError = true;
        }

        int hours = (int) Duration.between(inDateTime, outDateTime).toHours();
        if (hours <= 0) {
            request.setAttribute("dateError", "Thời gian không đủ để đặt chỗ!");
            hasError = true;
        }
        if (hasError) {
            request.getRequestDispatcher("/WEB-INF/views/public/site-detail.jsp").forward(request, response);
            return;
        } else {
            String vehicleId = request.getParameter("typeVehicle");
    
            PriceConfigDAO priceConfigDAO = new PriceConfigDAO();
            try {
                long basePrice = priceConfigDAO.getPriceByVehicleAndSite("hourly", Integer.parseInt(siteId), Integer.parseInt(vehicleId));
                long totalPrice = basePrice * hours;

                BookingPreviewDTO bookingPreview = new BookingPreviewDTO(inDateTime, outDateTime, Integer.parseInt(vehicleId), hours, basePrice, totalPrice);
                session.setAttribute("bookingPreview", bookingPreview);
//                session.setAttribute("inDateTime", inDateTime);
//                session.setAttribute("outDateTime", outDateTime);
//                session.setAttribute("hours", hours);
//                session.setAttribute("price", basePrice);
//                session.setAttribute("totalPrice", totalPrice);
                response.sendRedirect(request.getContextPath() + "/payment?action=booking&siteId=" + siteId);
            } catch (NumberFormatException e) {
                System.out.println(e.toString());
                request.setAttribute("dateError", "Lỗi xác nhận thông tin, mời bạn nhập lại!");
                request.getRequestDispatcher("/WEB-INF/views/public/site-detail.jsp").forward(request, response);
                return;
            }
        }
//            String hours = request.getParameter("hour");
//            String basePrice = request.getParameter("price");
//            String totalPrice = request.getParameter("totalPrice");
//            session.setAttribute("inDateTime", inDateTime);
//            session.setAttribute("outDateTime",outDateTime);
//            session.setAttribute("hours", hours);
//            session.setAttribute("price", basePrice);
//            session.setAttribute("totalPrice", totalPrice);
//            response.sendRedirect(request.getContextPath() + "/payment?action=booking&siteId=" + siteId);

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
