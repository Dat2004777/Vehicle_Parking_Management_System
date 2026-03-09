package model.dto;

import java.util.List;
import model.Employee;
import model.Vehicle;
import model.ParkingSite;

public class SiteFormDataDTO {

    // Các mảng Enum
    private ParkingSite.Region[] regions;
    private ParkingSite.State[] states;

    // Các danh sách từ Database
    private List<Vehicle> vehicles;
    private List<Employee> availableManagers; // Đổi tên cho rõ nghĩa là nhân viên rảnh

    // Constructors
    public SiteFormDataDTO() {
    }

    public SiteFormDataDTO(ParkingSite.Region[] regions, ParkingSite.State[] states,
            List<Vehicle> vehicles, List<Employee> availableManagers) {
        this.regions = regions;
        this.states = states;
        this.vehicles = vehicles;
        this.availableManagers = availableManagers;
    }

    // Getters and Setters
    public ParkingSite.Region[] getRegions() {
        return regions;
    }

    public void setRegions(ParkingSite.Region[] regions) {
        this.regions = regions;
    }

    public ParkingSite.State[] getStates() {
        return states;
    }

    public void setStates(ParkingSite.State[] states) {
        this.states = states;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public List<Employee> getAvailableManagers() {
        return availableManagers;
    }

    public void setAvailableManagers(List<Employee> availableManagers) {
        this.availableManagers = availableManagers;
    }
}
