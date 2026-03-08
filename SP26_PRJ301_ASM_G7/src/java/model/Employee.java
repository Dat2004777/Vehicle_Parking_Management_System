package model;

public class Employee {

    private int employeeId;
    private int accountId;
    private String firstName;
    private String lastName;
    private String phone;
    private int siteId;

    public Employee() {
    }

    public Employee(int accountId, String firstName, String lastName, String phone, int siteId) {
        this.accountId = accountId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.siteId = siteId;
    }

    public Employee(int employeeId, int accountId, String firstName, String lastName, String phone, int siteId) {
        this.employeeId = employeeId;
        this.accountId = accountId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.siteId = siteId;
    }

    public String getFullName() {
        return lastName + " " + firstName;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
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

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public String getName() {
        return lastName + " " + firstName;
    }

    public String getDisplayEmployeeId() {
        return "E" + employeeId;
    }
}
