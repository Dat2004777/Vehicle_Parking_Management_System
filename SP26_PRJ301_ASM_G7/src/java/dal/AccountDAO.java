/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import model.Account;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 *
 * @author ADMIN
 */
public class AccountDAO extends DBContext {

    public Account checkAccount(String username, String password) {
        String sql
                = """
                SELECT account_id,username,password,role
                FROM Accounts
                WHERE username = ? and password = ? and status = 'active'
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int account_id = rs.getInt("account_id");
                String user = rs.getString("username");
                String role = rs.getString("role");

                Account acc = new Account(account_id, user, Account.RoleEnum.from(role));
                return acc;
            }
            return null;
        } catch (Exception e) {
            System.out.println("Fail to checkPass");
            return null;
        }
    }

    public boolean getUsername(String username) {
        String sql
                = """
                SELECT username
                FROM Accounts
                WHERE username = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println("Fail to find username");
            return false;
        }
    }

    public int insertAccount(String username, String password, String role) {
        String sql_1
                = """
                INSERT INTO ACCOUNTS(username,password,role)
                VALUES(?,?,?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(
                sql_1, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new Exception("Lỗi insert vào bảng, không thêm dòng nào");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                throw new Exception("Lỗi insert account và lấy ra ID");
            }
        } catch (Exception e) {
            System.out.println("Lỗi thêm giá trị");
            return -1;
        }
    }
    
    public boolean changePassword(int accountId, String newPass){
        String sql =
                """
                UPDATE Accounts
                SET passsword ?
                WHERE account_id = ?
                """;
        
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1,newPass);
            ps.setInt(2,accountId);
             
            int row = ps.executeUpdate();
            return row > 0;
        }catch(Exception e){
            System.out.println("Lỗi cập thay đổi password của Account");
            e.printStackTrace();
            return false;
        }
    }
}
