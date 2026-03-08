/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.time.LocalDateTime;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
/**
 *
 * @author ADMIN
 */
public class BookingDAO extends DBContext {

    public int insertBooking(int customerId, String cardId, int vehicleId, String bookingState, LocalDateTime startTime, LocalDateTime endTime, long bookingAmount) {
        String sql
                = """
               INSERT INTO Bookings(customer_id, card_id, vehicle_type_id, start_time, end_time, booking_state, booking_amount)
               VALUES(?, ?, ?, ?, ?, ?, ?)
               """;

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, customerId);
            ps.setString(2, cardId);
            ps.setInt(3, vehicleId);
            ps.setObject(4, startTime);
            ps.setObject(5, endTime);
            ps.setString(6,bookingState);
            ps.setLong(7, bookingAmount);

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new Exception("Lỗi insert vào bảng, không thêm dòng nào");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                throw new Exception("Lỗi insert booking và lấy ra ID");
            }
        } catch (Exception e) {
            System.out.println("Lỗi insert into Booking Table");
            e.printStackTrace();
            return -1;
        }
    }
    
    public void updateBookingState(int bookingId, String state){
        String sql =
                """
                UPDATE Bookings
                SET  booking_state = ?
                WHERE booking_id = ?
                """;
        
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1,state);
            ps.setInt(2,bookingId);
            
            ps.executeUpdate();
        }catch(Exception e){
            System.out.println("Lỗi update lại trạng thái của booking");
            e.printStackTrace();
        }
    }
    
}
