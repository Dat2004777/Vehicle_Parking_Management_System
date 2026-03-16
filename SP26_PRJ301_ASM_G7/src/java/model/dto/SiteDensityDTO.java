/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dto;

/**
 *
 * @author dat20
 */
public class SiteDensityDTO {

    private int siteId;
    private String siteName;
    private int currentParked;
    private int maxCapacity;

    // THÊM MỚI: Dữ liệu cho Ô tô
    private int carCurrentParked;
    private int carMaxCapacity;

    // THÊM MỚI: Dữ liệu cho Xe máy
    private int motoCurrentParked;
    private int motoMaxCapacity;

    // Constructors
    public SiteDensityDTO() {
    }

    public SiteDensityDTO(int siteId, String siteName, int currentParked, int maxCapacity) {
        this.siteId = siteId;
        this.siteName = siteName;
        this.currentParked = currentParked;
        this.maxCapacity = maxCapacity;
    }

    // Getters and Setters
    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public int getCurrentParked() {
        return currentParked;
    }

    public void setCurrentParked(int currentParked) {
        this.currentParked = currentParked;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    // Hàm phụ trợ tính % để vẽ thanh Progress Bar
    public double getDensityPercentage() {
        if (maxCapacity == 0) {
            return 0;
        }
        return (double) ((currentParked * 100.0) / maxCapacity);
    }

    // Thêm Getter/Setter cho các biến mới
    public int getCarCurrentParked() {
        return carCurrentParked;
    }

    public void setCarCurrentParked(int carCurrentParked) {
        this.carCurrentParked = carCurrentParked;
    }

    public int getCarMaxCapacity() {
        return carMaxCapacity;
    }

    public void setCarMaxCapacity(int carMaxCapacity) {
        this.carMaxCapacity = carMaxCapacity;
    }

    public int getMotoCurrentParked() {
        return motoCurrentParked;
    }

    public void setMotoCurrentParked(int motoCurrentParked) {
        this.motoCurrentParked = motoCurrentParked;
    }

    public int getMotoMaxCapacity() {
        return motoMaxCapacity;
    }

    public void setMotoMaxCapacity(int motoMaxCapacity) {
        this.motoMaxCapacity = motoMaxCapacity;
    }

    // Thêm hàm tính % cho Ô tô
    public double getCarDensityPercentage() {
        if (carMaxCapacity == 0) {
            return 0;
        }
        return (double) ((carCurrentParked * 100.0) / carMaxCapacity);
    }

    // Thêm hàm tính % cho Xe máy
    public double getMotoDensityPercentage() {
        if (motoMaxCapacity == 0) {
            return 0;
        }
        return (double) ((motoCurrentParked * 100.0) / motoMaxCapacity);
    }
    
    public int getAvailableCapacity() {
        return getMaxCapacity() - getCurrentParked();
    }
}
