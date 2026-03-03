/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author dat20
 */
public class PriceConfig {

    private int siteId;
    private int vehicleTypeId;
    private String type;
    private long basePrice;

    public PriceConfig() {
    }

    public PriceConfig(int siteId, int vehicleTypeId, String type, long basePrice) {
        this.siteId = siteId;
        this.vehicleTypeId = vehicleTypeId;
        this.type = type;
        this.basePrice = basePrice;
    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public int getVehicleTypeId() {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(int vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(long basePrice) {
        this.basePrice = basePrice;
    }

}