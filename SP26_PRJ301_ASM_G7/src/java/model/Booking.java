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
    private int cardId;
    private int vehicleTypeId;
    private LocalDateTime timeIn;
    private LocalDateTime timeOut;
    private int booking_amount;

    public Booking() {
    }

    public Booking(int bookingId, int customerId, int cardId, int vehicleTypeId, LocalDateTime timeIn, LocalDateTime timeOut, int booking_amount) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.cardId = cardId;
        this.vehicleTypeId = vehicleTypeId;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.booking_amount = booking_amount;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
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

    public int getBooking_amount() {
        return booking_amount;
    }

    public void setBooking_amount(int booking_amount) {
        this.booking_amount = booking_amount;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    
    
}
