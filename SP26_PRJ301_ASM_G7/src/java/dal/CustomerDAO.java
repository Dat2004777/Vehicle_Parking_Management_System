/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.Customer;

/**
 *
 * @author ADMIN
 */
public class CustomerDAO extends DBContext {

    //Tạo 1 Customer object vào trong database
    public void insertCustomer(String firstname, String lastname, String phone, String email, int accountID) {
        String sql
                = """
                INSERT INTO Customers(first_name,last_name,phone,email,account_id)
                VALUES(?,?,?,?,?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, firstname);
            ps.setString(2, lastname);
            ps.setString(3, phone);
            ps.setString(4, email);
            ps.setInt(5, accountID);

            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Lỗi thêm giá trị Customer");
        }
    }

    public Customer getCustomerProfile(int account_id) {
        String sql
                = """
                SELECT a.account_id, c.customer_id, c.first_name, c.last_name, c.phone, c.email, c.wallet_amount
                FROM Accounts a
                JOIN Customers c ON a.account_id = c.account_id
                WHERE a.account_id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, account_id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int customer_id = rs.getInt("customer_id");
                String firstname = rs.getString("first_name");
                String lastname = rs.getString("last_name");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                long wallet = rs.getLong("wallet_amount");
                Customer customer = new Customer(customer_id, firstname, lastname, phone, email, wallet, account_id);

                return customer;
            }
            return null;
        } catch (Exception e) {
            System.out.println("Lỗi lấy dữ liệu user");
            e.printStackTrace();
            return null;
        }
    }

    public boolean isEmailExist(String email) {
        String sql = "SELECT 1 FROM Customers WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next(); 

        } catch (Exception e) {
            System.out.println("Lỗi lấy user-email");
            e.printStackTrace();
            return false;
        }
    }
}
