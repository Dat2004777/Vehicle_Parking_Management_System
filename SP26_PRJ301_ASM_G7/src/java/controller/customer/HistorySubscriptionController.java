/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.customer;

import dal.CustomerDAO;
import dal.PaymentTransactionDAO;
import dal.PriceConfigDAO;
import dal.SubscriptionDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import model.Account;
import model.Customer;
import model.PaymentTransaction;
import model.Subscription;
import model.dto.HistorySubscriptionDTO;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "HistorySubscriptionController", urlPatterns = {"/customer-info/history-subscriptions"})
public class HistorySubscriptionController extends HttpServlet {

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
            out.println("<title>Servlet HistorySubscriptionController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet HistorySubscriptionController at " + request.getContextPath() + "</h1>");
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
        Customer customer = (Customer) session.getAttribute("customer");
        if (account == null || customer == null || account.getRole() != Account.RoleEnum.CUSTOMER) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        SubscriptionDAO subscriptionDAO = new SubscriptionDAO();
        List<HistorySubscriptionDTO> historySubscriptions = subscriptionDAO.getAllSubscription(customer.getCustomerId());
        for (HistorySubscriptionDTO s : historySubscriptions) {
            System.out.println(s.getSubscription().getSubState());
        }
        request.setAttribute("subscriptions", historySubscriptions);
        request.getRequestDispatcher("/WEB-INF/views/customer/history-subscription.jsp").forward(request, response);
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
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("customer");

        String subscriptionId = request.getParameter("subscriptionId");
        String planType = request.getParameter("planType");
        String siteId = request.getParameter("siteId");
        if (subscriptionId == null || subscriptionId.isBlank() || planType == null || planType.isBlank() || siteId == null || siteId.isBlank()) {
            out.print("{\"success\": false, \"message\": \"Lỗi gia hạn thẻ!\"}");
            return;
        }

        SubscriptionDAO subscriptionDAO = new SubscriptionDAO();
        PriceConfigDAO priceDAO = new PriceConfigDAO();
        CustomerDAO customerDAO = new CustomerDAO();
        PaymentTransactionDAO paymentDAO = new PaymentTransactionDAO();
        try {
            Subscription subscription = subscriptionDAO.getById(Integer.parseInt(subscriptionId));
            if (subscription == null) {
                out.print("{\"success\": false, \"message\": \"Lỗi láy giá trị của thẻ!\"}");
                return;
            }

            if (subscription.getEndDate().isBefore(LocalDateTime.now())) {
                out.print("{\"success\":false,\"message\":\"Vé đã hết hạn. Vui lòng mua vé mới!\"}");
                return;
            }

            if (subscription.getCustomerId() != customer.getCustomerId()) {
                out.print("{\"success\":false,\"message\":\"Không hợp lệ Customer\"}");
                return;
            }

            long totalPrice = 0;
            LocalDateTime newEndDate = subscription.getEndDate();

            long basePrice = priceDAO.getPriceByVehicleAndSite("monthly", Integer.parseInt(siteId), subscription.getVehicleTypeId());

            if (planType.equals("month")) {
                totalPrice = basePrice;
                newEndDate = newEndDate.plusMonths(1);
            }

            if (planType.equals("quarter")) {
                totalPrice = basePrice * 3 * 90 / 100;
                newEndDate = newEndDate.plusMonths(3);
            }

            if (planType.equals("year")) {
                totalPrice = basePrice * 12 * 83 / 100;
                newEndDate = newEndDate.plusMonths(12);
            }

            long wallet = customer.getWalletAmount();
            if (wallet < totalPrice) {
                out.print("{\"success\":false,\"message\":\"Ví không đủ tiền\"}");
                return;
            }

            // update end_date
            subscriptionDAO.updateEndDate(subscription.getSubscriptionId(), newEndDate);

            // payment transaction
            PaymentTransaction txn = new PaymentTransaction();
            txn.setTransactionType(PaymentTransaction.TransactionType.SUBSCRIPTION);
            txn.setTargetId(Integer.parseInt(subscriptionId));
            txn.setTotalAmount(totalPrice);
            txn.setPaymentStatus("failed");

            boolean isTxnSaved = paymentDAO.add(txn);
            if (isTxnSaved) {
                // trừ ví
                long newWallet = wallet - totalPrice;
                boolean walletUpdated = customerDAO.updateWalletAmount(customer.getCustomerId(), newWallet);
                //update payment transaction
                paymentDAO.updatePaymentStatus(txn, "completed");
                customer.setWalletAmount(newWallet);
                session.setAttribute("customer", customer);
            }

            out.print("{\"success\":true,\"message\":\"Gia hạn thành công\"}");
        } catch (NumberFormatException e) {
            System.out.println("Lỗi parse subscriptionId");
            out.print("{\"success\": false, \"message\": \"Lỗi hệ thống!\"}");
        } catch (Exception e) {
            System.out.println("Lỗi gia hạn vé");
            out.print("{\"success\": false, \"message\": \"Lỗi hệ thống!\"}");
        }
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
