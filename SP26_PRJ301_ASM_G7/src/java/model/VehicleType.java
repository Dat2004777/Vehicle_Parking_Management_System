/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Admin
 */
public class VehicleType {

    private int vehicleTypeId;
    private String vehicleTypeName;

    public VehicleType() {
    }
    
    
    public VehicleType(int vehicleTypeId, String vehicleTypeName) {
        this.vehicleTypeId = vehicleTypeId;
        this.vehicleTypeName = vehicleTypeName;
    }

    public int getVehicleTypeId() {
        return vehicleTypeId;
    }

    public String getVehicleTypeName() {
        return vehicleTypeName;
    }

    public void setVehicleTypeId(int vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }

    public void setVehicleTypeName(String vehicleTypeName) {
        this.vehicleTypeName = vehicleTypeName;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VehicleType that = (VehicleType) o;
        // Giả sử thuộc tính ID của bạn tên là vehicleTypeId
        return vehicleTypeId == that.vehicleTypeId;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(vehicleTypeId);
    }
}
