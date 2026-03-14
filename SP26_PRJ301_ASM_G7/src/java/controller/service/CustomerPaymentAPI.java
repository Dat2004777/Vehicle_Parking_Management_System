/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.service;

import dal.BookingDAO;
import dal.CardDAO;
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
import model.Account;
import model.Customer;
import model.ParkingCard;
import model.PaymentTransaction;
import model.Subscription;
import model.dto.BookingPreviewDTO;
import utils.ValidationUtils;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "BookingPreviewAPI", urlPatterns = {"/api/payment"})
public class CustomerPaymentAPI extends HttpServlet {

    private final CardDAO cardDAO = new CardDAO();
    private final PaymentTransactionDAO paymentDAO = new PaymentTransactionDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();

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
            out.println("<title>Servlet BookingPreviewAPI</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet BookingPreviewAPI at " + request.getContextPath() + "</h1>");
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
        request.getRequestDispatcher("/404-error.jsp").forward(request, response);
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
        Account account = (Account) session.getAttribute("account");

        if (account == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String type = request.getParameter("type");

        if (type == null || type.isBlank()) {
            response.sendRedirect(request.getContextPath());
            return;
        }

        switch (type) {
            case "booking":
                bookingPayment(request, response);
                break;
            case "buying":
                buyingPayment(request, response);
        }

    }

    private void bookingPayment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();

        String siteId = request.getParameter("siteId");
        Customer customer = (Customer) session.getAttribute("customer");
        BookingPreviewDTO bookingPreview = (BookingPreviewDTO) session.getAttribute("bookingPreview");

        if (siteId == null || customer == null || bookingPreview == null) {
            out.print("{\"success\": false, \"message\": \"Phiên làm việc hết hạn hoặc thiếu dữ liệu!\"}");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            //  Lấy thẻ trống
            ParkingCard card = cardDAO.getAvailableCardAtSite(Integer.parseInt(siteId));
            if (card == null) {
                out.print("{\"success\": false, \"message\": \"Đã hết thẻ trống tại bãi này!\"}");
                return;
            }

            BookingDAO bookingDAO = new BookingDAO();

            //check tiền trước khi thanh toán
            long walletAmount = customer.getWalletAmount();
            long totalPrice = bookingPreview.getTotalPrice();

            if (walletAmount < totalPrice) {
                out.print("{\"success\": false, \"message\": \"Ví không đủ tiền!\"}");
                return;
            }

            //1.Set trạng thái ban đầu để lỡ giao dịch đặt vé fail
            // Tạo Booking
            int bookingId = bookingDAO.insertBooking(
                    customer.getCustomerId(),
                    card.getCardId(),
                    bookingPreview.getVehicleTypeId(),
                    "cancelled",
                    bookingPreview.getTimeIn(),
                    bookingPreview.getTimeOut(),
                    bookingPreview.getTotalPrice()
            );

            //Tạo payment với trạng thái fail trước
            PaymentTransaction txn = new PaymentTransaction();
            txn.setTransactionType(PaymentTransaction.TransactionType.BOOKING);
            txn.setTargetId(bookingId);
            txn.setTotalAmount(bookingPreview.getTotalPrice());
            txn.setPaymentStatus("failed");
            boolean isTxnSaved = paymentDAO.add(txn);

//            //  Lấy thẻ trống
//            ParkingCard card = cardDAO.getAvailableCardAtSite(Integer.parseInt(siteId));
//            if (card == null) {
//                out.print("{\"success\": false, \"message\": \"Đã hết thẻ trống tại bãi này!\"}");
//                return;
//            }
            if (bookingId == -1) {
                out.print("{\"success\": false, \"message\": \"Lỗi tạo đơn đặt chỗ!\"}");
                return;
            }

            if (isTxnSaved) {

                // trừ tiền
                long newWallet = walletAmount - totalPrice;

                boolean walletUpdated = customerDAO.updateWalletAmount(
                        customer.getCustomerId(),
                        newWallet
                );

                //update booking_state
                bookingDAO.updateBookingState(bookingId, "accepted");

                // update card
                cardDAO.updateCard(Integer.parseInt(siteId), card.getCardId(), "assigned");

                //update payment transaction
                paymentDAO.updatePaymentStatus(txn, "completed");

                // update session
                customer.setWalletAmount(newWallet);
                session.setAttribute("customer", customer);

                session.removeAttribute("bookingPreview");

                out.print("{\"success\": true, \"message\": \"Đặt chỗ thành công! Mã đơn: #" + bookingId + "\"}");

            } else {
                out.print("{\"success\": false, \"message\": \"Giao dịch thất bại!\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\": false, \"message\": \"Có lỗi xảy ra: " + e.getMessage() + "\"}");
        }
    }

    private void buyingPayment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("customer");
        String siteId = request.getParameter("siteId");
        String vehicleId = request.getParameter("vehicleId");
        String planType = request.getParameter("planType");
        String licensePlate = request.getParameter("licensePlate");

        if (customer == null) {
            out.print("{\"success\": false, \"message\": \"Phiên làm việc hết hạn hoặc thiếu dữ liệu!\"}");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (vehicleId == null || vehicleId.isBlank() || planType == null || planType.isBlank() || licensePlate == null || licensePlate.isBlank()) {
            out.print("{\"success\": false, \"message\": \"Thiếu dữ liệu!\"}");
            return;
        }

        String normalizedPlate = ValidationUtils.cleanLicensePlate(licensePlate);
        boolean valid = false;
        try {
            if (Integer.parseInt(vehicleId) == 1) {
                valid = ValidationUtils.isValidCarPlate(normalizedPlate);
            } else if (Integer.parseInt(vehicleId) == 2) {
                valid = ValidationUtils.isValidMotorbikePlate(normalizedPlate);
            }

            if (!valid) {
                out.print("{\"success\": false, \"message\": \"Biển số xe không đúng định dạng!\"}");
                return;
            }

            PriceConfigDAO priceDAO = new PriceConfigDAO();
            SubscriptionDAO subscriptionDAO = new SubscriptionDAO();
            long basePrice = priceDAO.getPriceByVehicleAndSite("monthly", Integer.parseInt(siteId), Integer.parseInt(vehicleId));

            long totalPrice = 0;
            LocalDateTime startDate = LocalDateTime.now();
            LocalDateTime endDate = null;

            if (planType.equals("month")) {
                totalPrice = basePrice;
                endDate = startDate.withDayOfMonth(startDate.toLocalDate().lengthOfMonth());
            } else if (planType.equals("quarter")) {
                totalPrice = basePrice * 3 * 70 / 100;
                LocalDateTime temp = startDate.plusMonths(3);
                endDate = temp.withDayOfMonth(temp.toLocalDate().lengthOfMonth());
            } else if (planType.equals("year")) {
                totalPrice = basePrice * 12 * 83 / 100;
                LocalDateTime temp = startDate.plusMonths(12);
                endDate = temp.withDayOfMonth(temp.toLocalDate().lengthOfMonth());
            }

            //Kiểm tra thẻ xem còn hiệu lực ko(nếu có thì ko được mua)
            boolean checkSubscription = subscriptionDAO.checkSubscription(Integer.parseInt(siteId), licensePlate);
            if (checkSubscription) {
                out.print("{\"success\": false, \"message\": \"Thẻ của xe này vẫn còn hiêu lực. Vui lòng chờ hết hạn!\"}");
                return;
            }
            //Laays the trong
            ParkingCard card = cardDAO.getAvailableCardAtSite(Integer.parseInt(siteId));
            if (card == null) {
                out.print("{\"success\": false, \"message\": \"Đã hết thẻ trống tại bãi này!\"}");
                return;
            }
            //check tiền trước khi thanh toán
            long walletAmount = customer.getWalletAmount();

            if (walletAmount < totalPrice) {
                out.print("{\"success\": false, \"message\": \"Ví không đủ tiền!\"}");
                return;
            }

            //Tạo trạng thái mua vé trước
            //Subscription
            Subscription subscription = new Subscription();
            subscription.setCustomerId(customer.getCustomerId());
            subscription.setCardId(card.getCardId());
            subscription.setLicensePlate(licensePlate);
            subscription.setVehicleTypeId(Integer.parseInt(vehicleId));
            subscription.setStartDate(startDate);
            subscription.setEndDate(endDate);
            subscription.setSubState("active");
            subscription.setAppliedPrice(totalPrice);

            int subscriptionId = subscriptionDAO.insertAndReturnId(subscription);
            if (subscriptionId == -1) {
                out.print("{\"success\": false, \"message\": \"Lỗi tạo đơn múa vé!\"}");
                return;
            }

            //PaymentTransaction
            PaymentTransaction txn = new PaymentTransaction();
            txn.setTransactionType(PaymentTransaction.TransactionType.SUBSCRIPTION);
            txn.setTargetId(subscriptionId);
            txn.setTotalAmount(totalPrice);
            txn.setPaymentStatus("failed");
            boolean isTxnSaved = paymentDAO.add(txn);

            if (isTxnSaved) {
                // trừ tiền
                long newWallet = walletAmount - totalPrice;

                boolean walletUpdated = customerDAO.updateWalletAmount(
                        customer.getCustomerId(),
                        newWallet
                );

                // update card
                cardDAO.updateCard(Integer.parseInt(siteId), card.getCardId(), "assigned");

                //update payment transaction
                paymentDAO.updatePaymentStatus(txn, "completed");

                // update session
                customer.setWalletAmount(newWallet);
                session.setAttribute("customer", customer);

                out.print("{\"success\": true, \"message\": \"Mua vé thành công! Mã đơn: #" + subscriptionId + "\"}");
            } else {
                subscriptionDAO.updateSubscription(subscriptionId);
                out.print("{\"success\": false, \"message\": \"Giao dịch thất bại!\"}");
            }

        } catch (NumberFormatException e) {
            System.out.println("Lỗi parse");
            out.print("{\"success\": false, \"message\": \"Có lỗi xảy ra: " + e.getMessage() + "\"}");
        } catch (Exception e) {
            System.out.println("Lỗi lấy dữ liệu");
            out.print("{\"success\": false, \"message\": \"Có lỗi xảy ra: " + e.getMessage() + "\"}");
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
