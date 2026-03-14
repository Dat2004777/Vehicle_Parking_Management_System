/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.time.LocalDateTime;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Booking;
import model.dto.HistoryBookingDTO;
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
    
    public List<HistoryBookingDTO> getAllHistoryBooking(int customerId){
        String sql = 
                """
                SELECT 
                    b.booking_id,
                    b.card_id,
                    b.vehicle_type_id,
                    b.customer_id,
                    b.start_time,
                    b.end_time,
                    b.booking_amount,
                
                    ps.site_name,
                
                    CASE 
                        WHEN CAST(b.start_time AS DATE) = CAST(GETDATE() AS DATE)
                            THEN 'completed'
                        WHEN b.start_time > GETDATE()
                            THEN 'upcoming'
                        ELSE 'completed'
                    END AS booking_status
                
                FROM Bookings b
                LEFT JOIN ParkingCards pc 
                    ON b.card_id = pc.card_id
                LEFT JOIN ParkingSites ps 
                    ON pc.site_id = ps.site_id
                
                WHERE b.customer_id = ? AND b.booking_state = 'accepted'
                ORDER BY b.start_time DESC
                """;
        
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            
            ps.setInt(1,customerId);
            ResultSet rs = ps.executeQuery();
            List<HistoryBookingDTO> list = new ArrayList<>();
            
            while(rs.next()){
                HistoryBookingDTO history = new HistoryBookingDTO();
                Booking booking = new Booking();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setCardId(rs.getString("card_id"));
                booking.setVehicleTypeId(rs.getInt("vehicle_type_id"));
                booking.setCustomerId(rs.getInt("customer_id"));
                booking.setTimeIn(rs.getTimestamp("start_time").toLocalDateTime());
                booking.setTimeOut(rs.getTimestamp("end_time").toLocalDateTime());
                booking.setBookingAmount(rs.getLong("booking_amount"));
                String siteName = rs.getString("site_name");
                String state = rs.getString("booking_status");
                
                history.setBooking(booking);
                history.setSiteName(siteName);
                history.setState(state);
                
                list.add(history);
            }
            
            return list;
        }catch(Exception e){
            System.out.println("Lỗi lấy ra danh sách history bookings");
            e.printStackTrace();
            return null;
        }
    }
}
