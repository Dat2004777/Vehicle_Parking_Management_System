/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.dto.AdminListEmployeeDTO;

/**
 *
 * @author dat20
 */
public class ManagerDAO extends DBContext {

    // 1. Đếm số xe ĐANG ĐỖ trong bãi (session_state = 'parked')
    public int countCurrentlyParked(int siteId) {
        int count = 0;
        String sql = "SELECT COUNT(ps.session_id) AS total "
                + "FROM ParkingSessions ps "
                + "JOIN ParkingCards pc ON ps.card_id = pc.card_id "
                + "WHERE pc.site_id = ? AND ps.session_state = 'parked' AND ps.status = 'active'";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, siteId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt("total");
            }
        } catch (Exception e) {
            System.out.println("Error ManagerDAO.countCurrentlyParked: " + e.getMessage());
        }
        return count;
    }

    // 2. Tính tổng DOANH THU trong ngày hôm nay của bãi xe
    public long getTodayRevenue(int siteId) {
        long revenue = 0;
        String sql = "SELECT COALESCE(SUM(pt.total_amount), 0) AS revenue "
                + "FROM PaymentTransactions pt "
                + "LEFT JOIN ParkingSessions ps ON pt.session_id = ps.session_id "
                + "LEFT JOIN Subscriptions s ON pt.subscription_id = s.subscription_id "
                + "LEFT JOIN ParkingCards pc ON (pc.card_id = ps.card_id OR pc.card_id = s.card_id) "
                + "WHERE pc.site_id = ? "
                + "  AND CAST(pt.payment_date AS DATE) = CAST(GETDATE() AS DATE) "
                + "  AND pt.payment_status = 'completed' AND pt.status = 'active'";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, siteId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                revenue = rs.getLong("revenue");
            }
        } catch (Exception e) {
            System.out.println("Error ManagerDAO.getTodayRevenue: " + e.getMessage());
        }
        return revenue;
    }

    // 3. Đếm số VÉ THÁNG đang có hiệu lực tại bãi xe
    public int countActiveSubscriptions(int siteId) {
        int count = 0;
        String sql = "SELECT COUNT(s.subscription_id) AS total "
                + "FROM Subscriptions s "
                + "JOIN ParkingCards pc ON s.card_id = pc.card_id "
                + "WHERE pc.site_id = ? AND s.sub_state = 'active' AND s.status = 'active'";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, siteId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt("total");
            }
        } catch (Exception e) {
            System.out.println("Error ManagerDAO.countActiveSubscriptions: " + e.getMessage());
        }
        return count;
    }

    // 4. Đếm số lượng ĐẶT TRƯỚC (Bookings) trong ngày hôm nay
    public int countTodayBookings(int siteId) {
        int count = 0;
        String sql = "SELECT COUNT(b.booking_id) AS total "
                + "FROM Bookings b "
                + "JOIN ParkingCards pc ON b.card_id = pc.card_id "
                + "WHERE pc.site_id = ? AND CAST(b.start_time AS DATE) = CAST(GETDATE() AS DATE) AND b.status = 'active'";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, siteId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt("total");
            }
        } catch (Exception e) {
            System.out.println("Error ManagerDAO.countTodayBookings: " + e.getMessage());
        }
        return count;
    }

}
