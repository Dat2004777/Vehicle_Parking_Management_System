package model.dto;

import model.VehicleType;

public class AreaDetailDTO {

    private int areaId;
    private String areaName;
    private VehicleType vehicleType; // Tên loại xe (Ô tô, Xe máy...)
    private int totalSlots;
    private int occupiedSlots;
    private int availableSlots;

    public AreaDetailDTO(int areaId, String areaName, VehicleType vehicleType, int totalSlots, int occupiedSlots) {
        this.areaId = areaId;
        this.areaName = areaName;
        this.vehicleType = vehicleType;
        this.totalSlots = totalSlots;
        this.occupiedSlots = occupiedSlots;
        this.availableSlots = Math.max(0, totalSlots - occupiedSlots);
    }

    // Các hàm Getter...
    public int getAreaId() {
        return areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public int getTotalSlots() {
        return totalSlots;
    }

    public int getOccupiedSlots() {
        return occupiedSlots;
    }

    public int getAvailableSlots() {
        return availableSlots;
    }
}
