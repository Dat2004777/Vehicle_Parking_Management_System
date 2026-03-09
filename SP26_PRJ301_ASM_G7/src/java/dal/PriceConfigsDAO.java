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
import java.util.ArrayList;

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

    public void insertPriceConfigs(List<PriceConfig> prices) {
        String sql = """
                INSERT INTO PriceConfigs (site_id, vehicle_type_id, type, base_price)
                VALUES (?, ?, ?, ?)
                """;

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            for (PriceConfig price : prices) {
                ps.setInt(1, price.getSiteId());
                ps.setInt(2, price.getVehicleTypeId());
                ps.setString(3, price.getType());
                ps.setLong(4, price.getBasePrice());
                ps.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println("Error PriceConfigsDAO.insertPriceConfigs: " + e.getMessage());
        }
    }

    public long getBasePrice(int siteId, int vehicleTypeId, String type) {
        String sql = "SELECT base_price FROM PriceConfigs "
                + "WHERE site_id = ? AND vehicle_type_id = ? "
                + "AND type = ? AND status = 'active'";
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

    public List<PriceConfig> getAllSubscriptionPricesBySite(int siteId) {
        List<PriceConfig> list = new ArrayList<>();

        // Đảm bảo tên bảng (PriceConfigs) khớp với DB của bạn
        // Lọc bỏ 'hourly' để chỉ lấy các gói vé tháng (monthly, quarterly,...)
        // Chỉ lấy các cấu hình đang 'ACTIVE'
        String sql = "SELECT [config_id], [site_id], [vehicle_type_id], [type], [base_price] "
                + "FROM [PriceConfigs] "
                + "WHERE [site_id] = ? AND [type] != 'hourly' AND [status] = 'active'";

        try (
                PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, siteId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PriceConfig pc = new PriceConfig();

                    // Map data từ ResultSet vào Object
                    pc.setConfigId(rs.getInt("config_id"));
                    pc.setSiteId(rs.getInt("site_id"));
                    pc.setVehicleTypeId(rs.getInt("vehicle_type_id"));

                    // Cột 'type' trong DB chính là 'subType' (monthly, quarterly...) mà ta dùng ở
                    // Controller
                    pc.setType(rs.getString("type"));

                    // Cột 'base_price' trong DB map vào biến 'price' trong Object
                    pc.setBasePrice(rs.getLong("base_price"));

                    list.add(pc);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Ghi log lỗi nếu cần thiết
        }

        return list;
    }

    /**
     * Lấy chi tiết một cấu hình giá dựa vào ID
     *
     * @param configId ID của gói cước
     * @return Đối tượng PriceConfig, hoặc null nếu không tìm thấy
     */
    public PriceConfig getConfigById(int configId) {
        String sql = "SELECT [config_id], [site_id], [vehicle_type_id], [type], [base_price]"
                + "FROM [PriceConfigs] WHERE [config_id] = ?";

        try (// Gọi hàm kết nối của bạn
                PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, configId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PriceConfig config = new PriceConfig();
                    config.setConfigId(rs.getInt("config_id"));
                    config.setSiteId(rs.getInt("site_id"));
                    config.setVehicleTypeId(rs.getInt("vehicle_type_id"));
                    config.setType(rs.getString("type"));
                    config.setBasePrice(rs.getLong("base_price"));

                    return config;
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi tại getConfigById: " + e.getMessage());
            e.printStackTrace();
        }

        return null; // Không tìm thấy hoặc có lỗi
    }

    public void deletePriceConfigsBySiteId(int siteId) {
        String sql = "DELETE FROM PriceConfigs WHERE site_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, siteId);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error PriceConfigsDAO.deletePriceConfigsBySiteId: " + e.getMessage());
        }
    }
}
