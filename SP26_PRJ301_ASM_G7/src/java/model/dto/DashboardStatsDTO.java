package model.dto;

public class DashboardStatsDTO {
    private String siteName;
    private String empName;
    private int totalCapacity;
    private int occupiedSpaces;
    private int availableSpaces;

    public DashboardStatsDTO(String siteName, String empName, int totalCapacity, int occupiedSpaces, int availableSpaces) {
        this.siteName = siteName;
        this.empName = empName;
        this.totalCapacity = totalCapacity;
        this.occupiedSpaces = occupiedSpaces;
        this.availableSpaces = availableSpaces;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public int getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(int totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public int getOccupiedSpaces() {
        return occupiedSpaces;
    }

    public void setOccupiedSpaces(int occupiedSpaces) {
        this.occupiedSpaces = occupiedSpaces;
    }

    public int getAvailableSpaces() {
        return availableSpaces;
    }

    public void setAvailableSpaces(int availableSpaces) {
        this.availableSpaces = availableSpaces;
    }

    
    
}