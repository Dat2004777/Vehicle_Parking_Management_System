/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dto;

import java.time.LocalDate;

/**
 *
 * @author Admin
 */
public class SubscriptionDataDTO {

    private String actionType;
    private String phone;
    private String fullName;
    private String plate;
    private String cardId;
    private int vehicleTypeId;
    private int configId;
    private LocalDate startDate;
    private Integer oldSubId;

    public SubscriptionDataDTO() {
    }

    public SubscriptionDataDTO(String actionType, String phone, String fullName, String plate, String cardId, int vehicleTypeId, int configId, LocalDate startDate, Integer oldSubId) {
        this.actionType = actionType;
        this.phone = phone;
        this.fullName = fullName;
        this.plate = plate;
        this.cardId = cardId;
        this.vehicleTypeId = vehicleTypeId;
        this.configId = configId;
        this.startDate = startDate;
        this.oldSubId = oldSubId;
    }
    
    
    
    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public int getVehicleTypeId() {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(int vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }

    public int getConfigId() {
        return configId;
    }

    public void setConfigId(int configId) {
        this.configId = configId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Integer getOldSubId() {
        return oldSubId;
    }

    public void setOldSubId(Integer oldSubId) {
        this.oldSubId = oldSubId;
    }
    
    
    
}
