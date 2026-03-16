package model.dto;

import java.util.Date;

public class AdminTransactionDTO {

    private String transactionCode;
    private String customerName;
    private String licensePlate;
    private String vehicleType; // Chứa giá trị 'car' hoặc 'motorbike'
    private Date startDate;
    private Date endDate;
    private long totalAmount;   // Dùng kiểu long để khớp với BIGINT trong CSDL
    private String status;      // Chứa giá trị 'accepted', 'completed', 'failed'

    // 1. Constructor rỗng (Bắt buộc phải có khi làm việc với DTO/Model)
    public AdminTransactionDTO() {
    }

    // 2. Constructor đầy đủ tham số để gọi nhanh trong DAO
    public AdminTransactionDTO(String transactionCode, String customerName, String licensePlate,
            String vehicleType, Date startDate, Date endDate,
            long totalAmount, String status) {
        this.transactionCode = transactionCode;
        this.customerName = customerName;
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    // 3. Getters và Setters
    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // 4. Hàm phụ trợ (Helper method): Tự động lấy 2 chữ cái đầu của Tên để hiển thị Avatar trên JSP
    public String getAvatarInitials() {
        if (customerName == null || customerName.trim().isEmpty()) {
            return "KH"; // Khách hàng
        }

        String[] words = customerName.trim().split("\\s+");
        if (words.length >= 2) {
            // Lấy chữ cái đầu của họ và chữ cái đầu của tên
            return (words[0].substring(0, 1) + words[words.length - 1].substring(0, 1)).toUpperCase();
        }

        // Nếu tên chỉ có 1 từ
        return customerName.substring(0, 1).toUpperCase();
    }
}
