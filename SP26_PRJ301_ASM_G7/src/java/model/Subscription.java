/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDateTime;

/**
 *
 * @author ADMIN
 */
public class Subscription {

    private int subscriptionId;
    private int customerId;
    private String cardId;
    private String licensePlate;
    private int vehicleTypeId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String subState;     // 'active', 'expired' (Trạng thái hạn dùng)
    private long appliedPrice;   // Giá tiền tại thời điểm mua

    public Subscription() {
    }

    public Subscription(int subscriptionId, int customerId, String cardId, String licensePlate, int vehicleTypeId, LocalDateTime startDate, LocalDateTime endDate, String subState, long appliedPrice) {
        this.subscriptionId = subscriptionId;
        this.customerId = customerId;
        this.cardId = cardId;
        this.licensePlate = licensePlate;
        this.vehicleTypeId = vehicleTypeId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.subState = subState;
        this.appliedPrice = appliedPrice;
    }

    public int getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(int subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public int getVehicleTypeId() {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(int vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getSubState() {
        return subState;
    }

    public void setSubState(String subState) {
        this.subState = subState;
    }

    public long getAppliedPrice() {
        return appliedPrice;
    }

    public void setAppliedPrice(long appliedPrice) {
        this.appliedPrice = appliedPrice;
    }
}
