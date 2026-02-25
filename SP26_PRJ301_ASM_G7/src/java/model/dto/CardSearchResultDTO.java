package model.dto;

public class CardSearchResultDTO {
    private String rfid;             // Mã thẻ RFID
    private String status;           // Trạng thái thẻ (ACTIVE, LOST, INACTIVE...)
    private String cardType;         // Loại vé ("Vé tháng" hoặc "Vé lượt")
    
    // --- Các thông tin dưới đây có thể null nếu là "Vé lượt" ---
    private String ownerName;        // Tên chủ sở hữu
    private String registeredPlate;  // Biển số đã đăng ký
    private String expiryDate;       // Ngày hết hạn (đã format thành chuỗi)

    public CardSearchResultDTO() {
    }

    public CardSearchResultDTO(String rfid, String status, String cardType, String ownerName, String registeredPlate, String expiryDate) {
        this.rfid = rfid;
        this.status = status;
        this.cardType = cardType;
        this.ownerName = ownerName;
        this.registeredPlate = registeredPlate;
        this.expiryDate = expiryDate;
    }

    // --- Getters and Setters ---

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getRegisteredPlate() {
        return registeredPlate;
    }

    public void setRegisteredPlate(String registeredPlate) {
        this.registeredPlate = registeredPlate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}