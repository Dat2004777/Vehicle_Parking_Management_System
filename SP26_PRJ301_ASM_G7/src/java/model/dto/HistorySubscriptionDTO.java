/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dto;

import model.Subscription;


/**
 *
 * @author ADMIN
 */
public class HistorySubscriptionDTO {
    private Subscription subscription;
    private String siteName;
    private int dayRemain;

    public HistorySubscriptionDTO() {
    }

    public HistorySubscriptionDTO(Subscription subscription, String siteName, int dayRemain) {
        this.subscription = subscription;
        this.siteName = siteName;
        this.dayRemain = dayRemain;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public int getDayRemain() {
        return dayRemain;
    }

    public void setDayRemain(int dayRemain) {
        this.dayRemain = dayRemain;
    }
    
    
    
}
