package model.dto;

public class TransactionHistoryDTO {
    private String time;         // Thời gian thực hiện giao dịch (VD: "08:15:30 - 25/10/2023")
    private String action;       // Loại hành động: "IN" (Vào) hoặc "OUT" (Ra)
    private String licensePlate; // Biển số xe đi kèm (Chủ yếu dùng cho tab tra cứu Thẻ)
    private String staffName;    // Tên nhân viên xác nhận giao dịch (Nếu chưa có dữ liệu employee_id, có thể để trống hoặc "Auto")

    public TransactionHistoryDTO() {
    }

    public TransactionHistoryDTO(String time, String action, String licensePlate, String staffName) {
        this.time = time;
        this.action = action;
        this.licensePlate = licensePlate;
        this.staffName = staffName;
    }

    // --- Getters and Setters ---

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
}