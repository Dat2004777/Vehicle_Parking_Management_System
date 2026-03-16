/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dto;

/**
 *
 * @author dat20
 */
public class VehicleConfigStrDTO {

    private int vehicleTypeId;
    private VehicleName vehicleName;
    private String capacity;
    private String hourlyPrice;
    private String monthlyPrice;

    public VehicleConfigStrDTO() {
    }

    public VehicleConfigStrDTO(int vehicleTypeId, VehicleName vehicleName, String capacity, String hourlyPrice, String monthlyPrice) {
        this.vehicleTypeId = vehicleTypeId;
        this.vehicleName = vehicleName;
        this.capacity = capacity;
        this.hourlyPrice = hourlyPrice;
        this.monthlyPrice = monthlyPrice;
    }

    public int getVehicleTypeId() {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(int vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }

    public VehicleName getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(VehicleName vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getHourlyPrice() {
        return hourlyPrice;
    }

    public void setHourlyPrice(String hourlyPrice) {
        this.hourlyPrice = hourlyPrice;
    }

    public String getMonthlyPrice() {
        return monthlyPrice;
    }

    public void setMonthlyPrice(String monthlyPrice) {
        this.monthlyPrice = monthlyPrice;
    }

    public enum VehicleName {
        CAR("Ôtô"),
        MOTORBIKE("Xe máy");

        private final String label;

        private VehicleName(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }
}
