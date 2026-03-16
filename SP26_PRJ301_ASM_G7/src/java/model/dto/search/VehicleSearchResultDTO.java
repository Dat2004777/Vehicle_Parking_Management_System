package model.dto.search;

public class VehicleSearchResultDTO {
    private String licensePlate;     // Biển số xe
    private String rfid;             // Mã thẻ RFID đang giữ
    private String entryTime;        // Thời gian vào bãi (đã format thành chuỗi)
    private String areaName;         // Tên khu vực đỗ (VD: "Tầng B1 - Khu A")
    private String vehicleType;      // Tên loại xe (VD: "car", "motorbike")
    private boolean isMonthlyTicket; // true: Vé tháng, false: Vé lượt

    public VehicleSearchResultDTO() {
    }

    public VehicleSearchResultDTO(String licensePlate, String rfid, String entryTime, String areaName, String vehicleType, boolean isMonthlyTicket) {
        this.licensePlate = licensePlate;
        this.rfid = rfid;
        this.entryTime = entryTime;
        this.areaName = areaName;
        this.vehicleType = vehicleType;
        this.isMonthlyTicket = isMonthlyTicket;
    }

    // --- Getters and Setters ---

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public boolean isIsMonthlyTicket() {
        return isMonthlyTicket;
    }

    public void setIsMonthlyTicket(boolean isMonthlyTicket) {
        this.isMonthlyTicket = isMonthlyTicket;
    }
}