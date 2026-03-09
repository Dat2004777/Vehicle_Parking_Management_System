/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dto;

/**
 *
 * @author dat20
 */
public class AdminListEmployeeDTO {

    private int employeeId;
    private String firstName;
    private String lastName;
    private String phone;
    private String siteName;
    private Role role;
            
    public AdminListEmployeeDTO() {
    }

    public AdminListEmployeeDTO(int employeeId, String firstName, String lastName, String phone, String siteName) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.siteName = siteName;
    }

    public AdminListEmployeeDTO(int employeeId, String firstName, String lastName, String phone, String siteName, Role role) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.siteName = siteName;
        this.role = role;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSiteName() {
        if (siteName == null) {
            return "Chưa có bãi xe";
        } else {
            return siteName;
        }
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getName() {
        return lastName + " " + firstName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getDisplayEmployeeId() {
        return "E" + employeeId;
    }

    public enum Role {
        ADMIN("Quản trị viên"),
        STAFF("Nhân viên trực bãi"),
        MANAGER("Quản lý");

        private final String label;

        private Role(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    public String getAvatarInitials() {
        String initialLast = (this.lastName != null && !this.lastName.trim().isEmpty())
                ? this.lastName.trim().substring(0, 1).toUpperCase() : "";

        String initialFirst = (this.firstName != null && !this.firstName.trim().isEmpty())
                ? this.firstName.trim().substring(0, 1).toUpperCase() : "";

        // Ghép chữ cái đầu của Họ và Tên (Ví dụ: Nguyễn An -> NA)
        return initialLast + initialFirst;
    }
}
