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
    private int siteId;
    private int dayRemain;
    private long basePrice;

    public HistorySubscriptionDTO() {
    }

    public HistorySubscriptionDTO(Subscription subscription, String siteName, int siteId, int dayRemain, long basePrice) {
        this.subscription = subscription;
        this.siteName = siteName;
        this.siteId = siteId;
        this.dayRemain = dayRemain;
        this.basePrice = basePrice;
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

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public long getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(long basePrice) {
        this.basePrice = basePrice;
    }
    
}
