/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.site;

import dal.SiteDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import model.ParkingSite;
import model.VehicleType;

/**
 *
 * @author ADMIN
 */
@WebServlet(name="CustomerListSite", urlPatterns={"/sites"})
public class CustomerListSite extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
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
            out.println("<title>Servlet CustomerListSite</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CustomerListSite at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        SiteDAO siteDAO = new SiteDAO();
        List<ParkingSite> sites = new ArrayList<>();
        List<String> regions = siteDAO.getAllRegions();
        List<VehicleType> vehicles = siteDAO.getVehicles();
        String address = request.getParameter("siteAddress");
        String region = request.getParameter("region");
        String status = request.getParameter("status");
        String vehicleType = request.getParameter("vehicleType");
        
        if(address == null || address.isBlank()){
            address = "";
        }
        
        if(region == null || region.isBlank()){
            region = "";
        }
        
        if(status == null || status.isBlank()){
            status = "";
        }
        
        if(vehicleType == null || vehicleType.isBlank()){
            vehicleType = "";
        }
        
        if(address.isEmpty() && region.isEmpty() && status.isEmpty() && vehicleType.isEmpty()){
            sites = siteDAO.getAllSitesWithAvailableSlots();
        }else{
            sites = siteDAO.filterSites(address,region,status,vehicleType);
        }
        request.setAttribute("regions", regions);
        request.setAttribute("vehicles", vehicles);
        request.setAttribute("sites", sites);
        request.getRequestDispatcher("/WEB-INF/views/public/sites.jsp").forward(request, response);
        
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
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
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
