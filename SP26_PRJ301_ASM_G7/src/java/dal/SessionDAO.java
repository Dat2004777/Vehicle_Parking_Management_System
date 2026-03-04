/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author dat20
 */
public class SessionDAO extends DBContext {

    public int getCurrentParkedVehiclesInCurrentMonth() {

        String sql = """
                    SELECT COUNT(ps.session_id) as total_parkingSessions
                    FROM ParkingSessions ps
                    WHERE MONTH(ps.entry_time) = MONTH(GETDATE()) 
                     	AND YEAR(ps.entry_time) = YEAR(GETDATE())
                     	AND ps.session_state = 'parked'
                     """;

        int CurrentParkedVehicles = 0;

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                CurrentParkedVehicles = rs.getInt("total_parkingSessions");
            }
        } catch (Exception e) {
            System.out.println("Error SessionDAO.getCurrentParkedVehiclesInCurrentMonth: " + e.getMessage());
        }

        return CurrentParkedVehicles;
    }

    public int getCurrentParkedVehiclesInCurrentMonthById(int siteId) {

        String sql = """
                    SELECT COUNT(ps.session_id) as total_parkingSessions
                    FROM ParkingSessions ps
                    JOIN ParkingCards pc ON ps.card_id = pc.card_id
                    WHERE MONTH(ps.entry_time) = MONTH(GETDATE()) 
                     	AND YEAR(ps.entry_time) = YEAR(GETDATE())
                     	AND ps.session_state = 'parked'
                        AND pc.site_id = ?
                     """;

        int CurrentParkedVehicles = 0;

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, siteId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                CurrentParkedVehicles = rs.getInt("total_parkingSessions");
            }
        } catch (Exception e) {
            System.out.println("Error SessionDAO.getCurrentParkedVehiclesInCurrentMonthById: " + e.getMessage());
        }

        return CurrentParkedVehicles;
    }
}
