/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.util.List;
import model.PriceConfig;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;
/**
 *
 * @author dat20
 */
public class PriceConfigsDAO extends DBContext {

    public void insertPriceConfigs(int siteId, List<PriceConfig> prices) {
        String sql = """
                     INSERT INTO PriceConfigs (site_id, vehicle_type_id, type, base_price) 
                     VALUES (?, ?, ?, ?)
                     """;

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            for (PriceConfig price : prices) {
                ps.setInt(1, siteId);
                ps.setInt(2, price.getVehicleTypeId());
                ps.setString(3, price.getType());
                ps.setLong(1, price.getBasePrice());
            }
            ps.executeQuery();
        } catch (Exception e) {
            System.out.println("Error PriceConfigsDAO.insertPriceConfigs: " + e.getMessage());
        }
    }
    
    public long getBasePrice(int siteId, int vehicleTypeId, String type) {
        String sql = "SELECT base_price FROM PriceConfigs " +
                     "WHERE site_id = ? AND vehicle_type_id = ? " +
                     "AND type = ? AND status = 'active'";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, siteId);
            ps.setInt(2, vehicleTypeId);
            ps.setString(3, type);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getLong("base_price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}