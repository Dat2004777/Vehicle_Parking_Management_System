package controller.subscription;

///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
// */
//package subscription;
//
//import dal.SubscriptionDAO;
//import java.io.IOException;
//import java.io.PrintWriter;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.time.DayOfWeek;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.time.temporal.TemporalAdjusters;
//import java.util.ArrayList;
//import java.util.List;
//import model.subscription.SubscriptionDTO;
//
///**
// *
// * @author dat20
// */
//public class ListSubscription extends HttpServlet {
//
//    /**
//     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
//     * methods.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        response.setContentType("text/html;charset=UTF-8");
//        try (PrintWriter out = response.getWriter()) {
//            /* TODO output your page here. You may use following sample code. */
//            out.println("<!DOCTYPE html>");
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Servlet SubscriptionServlet</title>");
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet SubscriptionServlet at " + request.getContextPath() + "</h1>");
//            out.println("</body>");
//            out.println("</html>");
//        }
//    }
//
//    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
//    /**
//     * Handles the HTTP <code>GET</code> method.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        SubscriptionDAO subscriptionDAO = new SubscriptionDAO();
//        String timeFilter = request.getParameter("time-filter");
//        String searchName = request.getParameter("searchName");
//
//        List<SubscriptionDTO> listSubscriptionHistoryDTOs = new ArrayList<>();
//
//        if (searchName == null) {
//            searchName = "";
//        }
//
//        if (timeFilter == null) {
//            timeFilter = "all";
//        }
//
//        LocalDateTime dateFrom;
//        LocalDateTime dateTo;
//
//        LocalDate today = LocalDate.now();
//
//        dateTo = today.atTime(LocalTime.MAX);
//
//        switch (timeFilter) {
//            case "week":
//                dateFrom = today.with(DayOfWeek.MONDAY).atStartOfDay();
//                listSubscriptionHistoryDTOs = subscriptionDAO.getSubscriptionHistory(dateFrom, dateTo, searchName);
//                break;
//            case "day":
//                dateFrom = today.atStartOfDay();
//                listSubscriptionHistoryDTOs = subscriptionDAO.getSubscriptionHistory(dateFrom, dateTo, searchName);
//                break;
//            case "month":
//                dateFrom = today.with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
//                listSubscriptionHistoryDTOs = subscriptionDAO.getSubscriptionHistory(dateFrom, dateTo, searchName);
//                break;
//            case "year":
//                dateFrom = today.with(TemporalAdjusters.firstDayOfYear()).atStartOfDay();
//                listSubscriptionHistoryDTOs = subscriptionDAO.getSubscriptionHistory(dateFrom, dateTo, searchName);
//                break;
//            case "all":
//            default:
//                listSubscriptionHistoryDTOs = subscriptionDAO.getSubscriptionHistory(searchName);
//                break;
//        }
//
//        request.setAttribute("listSubscriptionHistory", listSubscriptionHistoryDTOs);
//        request.setAttribute("currentTimeFilter", timeFilter);
//
//        request.getRequestDispatcher("/views/admin/subscription/list-subscription.jsp").forward(request, response);
//    }
//
//    /**
//     * Handles the HTTP <code>POST</code> method.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        processRequest(request, response);
//    }
//
//    /**
//     * Returns a short description of the servlet.
//     *
//     * @return a String containing servlet description
//     */
//    @Override
//    public String getServletInfo() {
//        return "Short description";
//    }// </editor-fold>
//
//}
