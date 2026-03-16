/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dto;

import model.VehicleType;

/**
 *
 * @author ADMIN
 */
public class VehicleBasePriceDTO {
    private VehicleType vehicle;
    private long basePrice;

    public VehicleBasePriceDTO(VehicleType vehicle, long basePrice) {
        this.vehicle = vehicle;
        this.basePrice = basePrice;
    }

    public VehicleType getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleType vehicle) {
        this.vehicle = vehicle;
    }

    public long getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(long basePrice) {
        this.basePrice = basePrice;
    }
    
    
}
