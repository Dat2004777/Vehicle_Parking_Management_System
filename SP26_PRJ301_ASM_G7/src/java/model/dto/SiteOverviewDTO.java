package model.dto;

import java.util.List;
import java.util.Map;
import model.ParkingSite;
import model.VehicleType;

public class SiteOverviewDTO {

    private String siteName;
    private String address;
    private ParkingSite.Status operatingState;
    private int totalCapacity;
    private int totalOccupied;
    private int totalAvailable;
    private List<AreaDetailDTO> areas;
    private Map<VehicleType, Integer> slotPerVehicle;

    public SiteOverviewDTO(String siteName, String address, ParkingSite.Status operatingState, List<AreaDetailDTO> areas) {
        this.siteName = siteName;
        this.address = address;
        this.operatingState = operatingState;
        this.areas = areas;

        // Tự động tính toán tổng các số liệu từ danh sách areas
        this.totalCapacity = areas.stream().mapToInt(AreaDetailDTO::getTotalSlots).sum();
        this.totalOccupied = areas.stream().mapToInt(AreaDetailDTO::getOccupiedSlots).sum();
        this.totalAvailable = Math.max(0, totalCapacity - totalOccupied);

    }
    

    // Cấp các hàm Getter để JSP gọi (ví dụ: ${overview.siteName})
    public String getSiteName() {
        return siteName;
    }

    public String getAddress() {
        return address;
    }

    public ParkingSite.Status getOperatingState() {
        return operatingState;
    }

    public int getTotalCapacity() {
        return totalCapacity;
    }

    public int getTotalOccupied() {
        return totalOccupied;
    }

    public int getTotalAvailable() {
        return totalAvailable;
    }

    public List<AreaDetailDTO> getAreas() {
        return areas;
    }

    public Map<VehicleType, Integer> getSlotPerVehicle() {
        return slotPerVehicle;
    }

    public void setSlotPerVehicle(Map<VehicleType, Integer> slotPerVehicle) {
        this.slotPerVehicle = slotPerVehicle;
    }

}
