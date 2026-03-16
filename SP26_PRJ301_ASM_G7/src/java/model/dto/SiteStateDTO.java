/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dto;

/**
 *
 * @author dat20
 */
public class SiteStateDTO {

    private int totalSites;
    private int operatingSites;
    private int closedSites;
    private int maintenanceSites;

    public SiteStateDTO() {
    }

    public SiteStateDTO(int totalSites, int operatingSites, int closedSites, int maintenanceSites) {
        this.totalSites = totalSites;
        this.operatingSites = operatingSites;
        this.closedSites = closedSites;
        this.maintenanceSites = maintenanceSites;
    }

    public int getTotalSites() {
        return totalSites;
    }

    public void setTotalSites(int totalSites) {
        this.totalSites = totalSites;
    }

    public int getOperatingSites() {
        return operatingSites;
    }

    public void setOperatingSites(int operatingSites) {
        this.operatingSites = operatingSites;
    }

    public int getClosedSites() {
        return closedSites;
    }

    public void setClosedSites(int closedSites) {
        this.closedSites = closedSites;
    }

    public int getMaintenanceSites() {
        return maintenanceSites;
    }

    public void setMaintenanceSites(int maintenanceSites) {
        this.maintenanceSites = maintenanceSites;
    }

}
