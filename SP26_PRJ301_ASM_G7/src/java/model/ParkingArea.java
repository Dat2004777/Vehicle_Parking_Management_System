package model;

/**
 *
 * @author Admin
 */
public class ParkingArea {

    private int areaId;
    private int siteId;
    private String areaName;
    private int vehicleTypeId;
    private int totalSlots;
    private String vehicleTypeName;

    public ParkingArea() {
    }

    public ParkingArea(int areaId, int siteId, String areaName, int vehicleTypeId, int totalSlots,
            String vehicleTypeName) {
        this.areaId = areaId;
        this.siteId = siteId;
        this.areaName = areaName;
        this.vehicleTypeId = vehicleTypeId;
        this.totalSlots = totalSlots;
        this.vehicleTypeName = vehicleTypeName;
    }

    public ParkingArea(int siteId, int vehicleTypeId, int totalSlots) {
        this.siteId = siteId;
        this.vehicleTypeId = vehicleTypeId;
        this.totalSlots = totalSlots;
    }

    public ParkingArea(int areaId, int siteId, String areaName, int vehicleTypeId, int totalSlots) {
        this.areaId = areaId;
        this.siteId = siteId;
        this.areaName = areaName;
        this.vehicleTypeId = vehicleTypeId;
        this.totalSlots = totalSlots;
    }

    public ParkingArea(int siteId, String areaName, int vehicleTypeId, int totalSlots, String vehicleTypeName) {
        this.siteId = siteId;
        this.areaName = areaName;
        this.vehicleTypeId = vehicleTypeId;
        this.totalSlots = totalSlots;
        this.vehicleTypeName = vehicleTypeName;
    }

    public ParkingArea(int siteId, String areaName, int vehicleTypeId, int totalSlots) {
        this.siteId = siteId;
        this.areaName = areaName;
        this.vehicleTypeId = vehicleTypeId;
        this.totalSlots = totalSlots;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public int getVehicleTypeId() {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(int vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }

    public int getTotalSlots() {
        return totalSlots;
    }

    public void setTotalSlots(int totalSlots) {
        this.totalSlots = totalSlots;
    }

    public String getVehicleTypeName() {
        return vehicleTypeName;
    }

}
