package model.dto.search;

public class CardSearchResultDTO {

    private String rfid;             // Mã thẻ RFID
    private String status;           // Trạng thái thẻ (ACTIVE, LOST, INACTIVE...)
    private String cardType;         // Loại vé ("Vé tháng" hoặc "Vé lượt")

    // --- Các thông tin dưới đây có thể null nếu là "Vé lượt" ---
    private String ownerName;        // Tên chủ sở hữu
    private String registeredPlate;  // Biển số đã đăng ký
    private String expiryDate;       // Ngày hết hạn (đã format thành chuỗi)

    private String bookingStart;
    private String bookingEnd;

    private boolean currentlyParked;   // True nếu thẻ đang được dùng cho xe trong bãi
    private String currentParkedPlate;   // Biển số xe đang dùng thẻ này (nếu đang đỗ)
    private String currentEntryTime;

    public CardSearchResultDTO() {
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

    public boolean isCurrentlyParked() {
        return currentlyParked;
    }

    public void setCurrentlyParked(boolean currentlyParked) {
        this.currentlyParked = currentlyParked;
    }

    public String getCurrentParkedPlate() {
        return currentParkedPlate;
    }

    public void setCurrentParkedPlate(String currentParkedPlate) {
        this.currentParkedPlate = currentParkedPlate;
    }

    public String getCurrentEntryTime() {
        return currentEntryTime;
    }

    public void setCurrentEntryTime(String currentEntryTime) {
        this.currentEntryTime = currentEntryTime;
    }

    public String getBookingStart() {
        return bookingStart;
    }

    public void setBookingStart(String bookingStart) {
        this.bookingStart = bookingStart;
    }

    public String getBookingEnd() {
        return bookingEnd;
    }

    public void setBookingEnd(String bookingEnd) {
        this.bookingEnd = bookingEnd;
    }
    
    
}
