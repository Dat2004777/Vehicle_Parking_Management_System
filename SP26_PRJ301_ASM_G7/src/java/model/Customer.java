/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ADMIN
 */
public class Customer {
    private int customerId; //PK
    private String firstname;
    private String lastname;
    private String phone;
    private String email;
    private long walletAmount;
    private int accountId; //FK tới Account

    public Customer() {
    }

    
    
    public Customer(int customerId, String firstname, String lastname, String phone, String email, long walletAmount, int accountId) {
        this.customerId = customerId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.email = email;
        this.walletAmount= walletAmount;
        this.accountId = accountId;
    }
    
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getWalletAmount() {
        return walletAmount;
    }

    public void setWalletAmount(long walletAmount) {
        this.walletAmount = walletAmount;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
    
    
    

}
