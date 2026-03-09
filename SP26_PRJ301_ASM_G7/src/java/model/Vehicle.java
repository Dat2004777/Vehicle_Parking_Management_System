/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author dat20
 */
public class Vehicle {

    private int vehicleId;
    private VehicleName vehicleName;

    public Vehicle() {
    }

    public Vehicle(int vehicleId, VehicleName vehicleName) {
        this.vehicleId = vehicleId;
        this.vehicleName = vehicleName;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public VehicleName getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(VehicleName vehicleName) {
        this.vehicleName = vehicleName;
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
