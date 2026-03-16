/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dto;

import java.util.Date;

/**
 *
 * @author dat20
 */
public class AdminTransactionHistoryDTO {

    private String transactionCode;
    private String customerName;
    private String serviceType; // Ví dụ: "Vé tháng (Ô tô)"
    private long amount;
    private String paymentMethod; // Mặc định giả lập "Tiền mặt" hoặc "Momo"
    private Date paymentTime;
    private String status;

    public AdminTransactionHistoryDTO() {
    }

    public AdminTransactionHistoryDTO(String transactionCode, String customerName, String serviceType,
            long amount, String paymentMethod, Date paymentTime, String status) {
        this.transactionCode = transactionCode;
        this.customerName = customerName;
        this.serviceType = serviceType;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentTime = paymentTime;
        this.status = status;
    }

    // --- Getters & Setters ---
    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Helper: Lấy 2 chữ cái đầu làm Avatar
    public String getAvatarInitials() {
        if (customerName == null || customerName.trim().isEmpty() || customerName.equals("Khách vãng lai")) {
            return "VL";
        }
        String[] words = customerName.trim().split("\\s+");
        if (words.length >= 2) {
            return (words[0].substring(0, 1) + words[words.length - 1].substring(0, 1)).toUpperCase();
        }
        return customerName.substring(0, 1).toUpperCase();
    }
}
