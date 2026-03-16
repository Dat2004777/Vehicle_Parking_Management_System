/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dto;

/**
 *
 * @author dat20
 */
public class VehicleConfigDTO {

    private int vehicleTypeId;
    private VehicleName vehicleName;
    private int capacity;
    private long hourlyPrice;
    private long monthlyPrice;

    public VehicleConfigDTO() {
    }

    public VehicleConfigDTO(int vehicleTypeId, VehicleName vehicleName, int capacity, long hourlyPrice, long monthlyPrice) {
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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public long getHourlyPrice() {
        return hourlyPrice;
    }

    public void setHourlyPrice(long hourlyPrice) {
        this.hourlyPrice = hourlyPrice;
    }

    public long getMonthlyPrice() {
        return monthlyPrice;
    }

    public void setMonthlyPrice(long monthlyPrice) {
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
