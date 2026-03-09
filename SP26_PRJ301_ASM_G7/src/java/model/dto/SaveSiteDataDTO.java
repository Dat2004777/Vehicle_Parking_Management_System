/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dto;

import java.util.List;

/**
 *
 * @author dat20
 */
public class SaveSiteDataDTO {

    private String siteName;
    private String siteAddress;
    private String siteRegion;
    private String siteManagerId; // Để String để khớp với Parameter nhận từ Form
    private String siteState;
    private List<VehicleConfigStrDTO> vehicleConfigs;

    public SaveSiteDataDTO() {
    }

    public SaveSiteDataDTO(String siteName, String siteAddress, String siteRegion,
            String siteManagerId, String siteState, List<VehicleConfigStrDTO> vehicleConfigs) {
        this.siteName = siteName;
        this.siteAddress = siteAddress;
        this.siteRegion = siteRegion;
        this.siteManagerId = siteManagerId;
        this.siteState = siteState;
        this.vehicleConfigs = vehicleConfigs;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteAddress() {
        return siteAddress;
    }

    public void setSiteAddress(String siteAddress) {
        this.siteAddress = siteAddress;
    }

    public String getSiteRegion() {
        return siteRegion;
    }

    public void setSiteRegion(String siteRegion) {
        this.siteRegion = siteRegion;
    }

    public String getSiteManagerId() {
        return siteManagerId;
    }

    public void setSiteManagerId(String siteManagerId) {
        this.siteManagerId = siteManagerId;
    }

    public String getSiteState() {
        return siteState;
    }

    public void setSiteState(String siteState) {
        this.siteState = siteState;
    }

    public List<VehicleConfigStrDTO> getVehicleConfigs() {
        return vehicleConfigs;
    }

    public void setVehicleConfigs(List<VehicleConfigStrDTO> vehicleConfigs) {
        this.vehicleConfigs = vehicleConfigs;
    }

}
