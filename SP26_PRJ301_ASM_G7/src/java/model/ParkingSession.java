package model;

import java.time.LocalDateTime;

public class ParkingSession {
    // Đã đồng bộ tên biến theo chuẩn camelCase dựa trên cột của DB
    private int sessionId;          // Cột: session_id
    private String cardId;          // Cột: card_id
    private Integer vehicleTypeId;  // Cột: vehicle_type_id (Dùng Integer thay vì int để cho phép null)
    private String licensePlate;    // Cột: license_plate
    private LocalDateTime entryTime;// Cột: entry_time (Đã đổi sang LocalDateTime)
    private LocalDateTime exitTime; // Cột: exit_time (Đã đổi sang LocalDateTime)
    private String sessionType;     // Cột: session_type
    private Long feeAmount;       // Cột: fee_amount (Dùng Double cho tiền tệ, hoặc BigDecimal nếu cần siêu chính xác)
    private String sessionState;    // Cột: session_state
    private String status;          // Cột: status

    // 1. Constructor rỗng
    public ParkingSession() {
    }

    // 2. Constructor đầy đủ
    public ParkingSession(int sessionId, String cardId, Integer vehicleTypeId, String licensePlate, 
                          LocalDateTime entryTime, LocalDateTime exitTime, String sessionType, 
                          Long feeAmount, String sessionState, String status) {
        this.sessionId = sessionId;
        this.cardId = cardId;
        this.vehicleTypeId = vehicleTypeId;
        this.licensePlate = licensePlate;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.sessionType = sessionType;
        this.feeAmount = feeAmount;
        this.sessionState = sessionState;
        this.status = status;
    }

    // 3. Getters và Setters
    public int getSessionId() { return sessionId; }
    public void setSessionId(int sessionId) { this.sessionId = sessionId; }

    public String getCardId() { return cardId; }
    public void setCardId(String cardId) { this.cardId = cardId; }

    public Integer getVehicleTypeId() { return vehicleTypeId; }
    public void setVehicleTypeId(Integer vehicleTypeId) { this.vehicleTypeId = vehicleTypeId; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public LocalDateTime getEntryTime() { return entryTime; }
    public void setEntryTime(LocalDateTime entryTime) { this.entryTime = entryTime; }

    public LocalDateTime getExitTime() { return exitTime; }
    public void setExitTime(LocalDateTime exitTime) { this.exitTime = exitTime; }

    public String getSessionType() { return sessionType; }
    public void setSessionType(String sessionType) { this.sessionType = sessionType; }

    public Long getFeeAmount() { return feeAmount; }
    public void setFeeAmount(Long feeAmount) { this.feeAmount = feeAmount; }

    public String getSessionState() { return sessionState; }
    public void setSessionState(String sessionState) { this.sessionState = sessionState; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}