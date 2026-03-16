/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dto;

import model.Booking;

/**
 *
 * @author ADMIN
 */
public class HistoryBookingDTO {
    private Booking booking;
    private String siteName;
    private String state;

    public HistoryBookingDTO() {
    }

    public HistoryBookingDTO(Booking booking, String siteName, String state) {
        this.booking = booking;
        this.siteName = siteName;
        this.state = state;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    
    
}
