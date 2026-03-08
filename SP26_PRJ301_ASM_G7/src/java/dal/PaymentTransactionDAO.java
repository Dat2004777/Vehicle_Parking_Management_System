/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import model.PaymentTransaction;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Admin
 */
public class PaymentTransactionDAO extends DBContext {

    public boolean add(PaymentTransaction txn) {
        String idColumn = "";

        // Switch trực tiếp trên Enum (Không sợ gõ sai String nữa)
        switch (txn.getTransactionType()) {
            case BOOKING:
                idColumn = "booking_id";
                break;
            case SUBSCRIPTION:
                idColumn = "subscription_id";
                break;
            case SESSION:
                idColumn = "session_id";
                break;
            default:
                return false; // Code an toàn: Nếu lọt qua đây thì tự động hủy
        }

        // Tạo câu SQL động
        String sql = "INSERT INTO PaymentTransactions (" + idColumn + ", total_amount, payment_date, payment_status) "
                + "VALUES (?, ?, GETDATE(), ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, txn.getTargetId());
            ps.setLong(2, txn.getTotalAmount());
            ps.setString(3, txn.getPaymentStatus().toLowerCase());
            
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Lỗi PaymentTransactionDAO.add: " + e.getMessage());
            return false;
        }
    }
    
    public void updatePaymentStatus(PaymentTransaction txn, String state){
        String target = "";
        
        switch(txn.getTransactionType()){
            case BOOKING:
                target = "booking_id";
                break;
            case SUBSCRIPTION:
                target = "subscription_id";
                break;
        }
        String sql = 
                "UPDATE PaymentTransactions SET payment_status = ? WHERE " + target + " = ?";
        
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1,state);
            ps.setInt(2,txn.getTargetId());
            
            ps.executeUpdate();
        }catch(Exception e){
            System.out.println("Lỗi update bảng Payment");
            e.printStackTrace();
        }
    }
}
