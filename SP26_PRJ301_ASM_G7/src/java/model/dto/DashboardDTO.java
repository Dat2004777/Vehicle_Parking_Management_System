/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dto;

import java.util.List;
import model.ParkingSite;

/**
 *
 * @author dat20
 */
public class DashboardDTO {

    private long totalAmount;
    private int currentParkedVehicles;
    private int totalSubscription;
    private List<ParkingSite> allActiveSites;

    public DashboardDTO() {
    }

    public DashboardDTO(long totalAmount, int currentParkedVehicles, int totalSubscription, List<ParkingSite> allActiveSites) {
        this.totalAmount = totalAmount;
        this.currentParkedVehicles = currentParkedVehicles;
        this.totalSubscription = totalSubscription;
        this.allActiveSites = allActiveSites;
    }

    public long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getCurrentParkedVehicles() {
        return currentParkedVehicles;
    }

    public void setCurrentParkedVehicles(int currentParkedVehicles) {
        this.currentParkedVehicles = currentParkedVehicles;
    }

    public int getTotalSubscription() {
        return totalSubscription;
    }

    public void setTotalSubscription(int totalSubscription) {
        this.totalSubscription = totalSubscription;
    }

    public List<ParkingSite> getAllActiveSites() {
        return allActiveSites;
    }

    public void setAllActiveSites(List<ParkingSite> allActiveSites) {
        this.allActiveSites = allActiveSites;
    }

}
