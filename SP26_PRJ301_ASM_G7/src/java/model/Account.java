/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ADMIN
 */
public class Account {

    private int accountId;
    private String username;
    private String password;
    private RoleEnum role;
    private int employeeId;

    public Account() {
    }

    public Account(int accountId, String username, RoleEnum role) {
        this.accountId = accountId;
        this.username = username;
        this.role = role;
    }

    public Account(int accountId, String username, RoleEnum role, int employeeId) {
        this.accountId = accountId;
        this.username = username;
        this.role = role;
        this.employeeId = employeeId;
    }

    public int getAccount_id() {
        return accountId;
    }

    public void setAccount_id(int accountId) {
        this.accountId = accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public enum RoleEnum {
        ADMIN,
        STAFF,
        MANAGER,
        CUSTOMER;

        public static RoleEnum from(String value) {
            return value == null
                    ? null
                    : RoleEnum.valueOf(value.trim().toUpperCase());
        }

    }
}
