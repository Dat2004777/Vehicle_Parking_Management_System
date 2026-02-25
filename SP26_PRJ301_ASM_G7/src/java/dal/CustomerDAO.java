/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
}
