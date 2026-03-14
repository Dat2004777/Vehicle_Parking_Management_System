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
public class Booking {
    private int bookingId;
    private int customerId;
    private String cardId;
    private int vehicleTypeId;
    private LocalDateTime timeIn;
    private LocalDateTime timeOut;
    private long bookingAmount;

    public Booking() {
    }

    public Booking(int bookingId, int customerId, String cardId, int vehicleTypeId, LocalDateTime timeIn, LocalDateTime timeOut, long bookingAmount) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.cardId = cardId;
        this.vehicleTypeId = vehicleTypeId;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.bookingAmount = bookingAmount;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public int getVehicleTypeId() {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(int vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }

    public LocalDateTime getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(LocalDateTime timeIn) {
        this.timeIn = timeIn;
    }

    public LocalDateTime getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(LocalDateTime timeOut) {
        this.timeOut = timeOut;
    }

    public long getBookingAmount() {
        return bookingAmount;
    }

    public void setBookingAmount(long bookingAmount) {
        this.bookingAmount = bookingAmount;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    
    
}
