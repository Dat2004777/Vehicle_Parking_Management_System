package dal;

import model.ParkingArea;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AreaDAO extends DBContext {

    // 1. LẤY TẤT CẢ KHU VỰC CỦA 1 BÃI XE (Quan trọng nhất cho trang Detail)
    public List<ParkingArea> getAreasBySite(int siteId) {
        List<ParkingArea> list = new ArrayList<>();
        String sql = "SELECT * FROM ParkingAreas WHERE site_id = ?";

        try (
                PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, siteId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapRowToArea(rs, 0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. LẤY CHI TIẾT 1 KHU VỰC (Để ném vào form Update)
    public ParkingArea getAreaById(int areaId) {
        String sql = "SELECT * FROM ParkingAreas WHERE area_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, areaId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRowToArea(rs, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 3. THÊM KHU VỰC MỚI
    public void insertAreas(List<ParkingArea> areas) {
        String sql = """
                     INSERT INTO ParkingAreas (site_id, vehicle_type_id, totalSlots) VALUES (?, ?, ?)
                     """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            for (ParkingArea area : areas) {
                ps.setInt(1, area.getSiteId());
                ps.setInt(2, area.getVehicleTypeId());
                ps.setInt(3, area.getTotalSlots());
                ps.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println("Error AreaDAO.insertAreas: " + e.getMessage());
        }
    }

    // 4. CẬP NHẬT KHU VỰC
    public boolean updateArea(ParkingArea area) {
        String sql = "UPDATE ParkingAreas SET area_name = ?, vehicle_type_id = ?, total_slots = ? WHERE area_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, area.getAreaName());
            ps.setInt(2, area.getVehicleTypeId());
            ps.setInt(3, area.getTotalSlots());
            ps.setInt(4, area.getAreaId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 5. XÓA KHU VỰC
    public void deleteAreaBySiteId(int siteId) {
        String sql = "DELETE FROM ParkingAreas WHERE site_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, siteId);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error AreaDAO.deleteAreaBySiteId: " + e.getMessage());
        }
    }

    // 6. XÓA TẤT CẢ KHU VỰC CỦA 1 SITE (Dùng khi xóa Site cha)
    public boolean deleteAreasBySite(int siteId) {
        String sql = "DELETE FROM ParkingAreas WHERE site_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, siteId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private ParkingArea mapRowToArea(ResultSet rs, int tmp) throws SQLException {
        int id = rs.getInt("area_id");
        int siteId = rs.getInt("site_id");
        String name = rs.getString("area_name");
        int vehicleTypeId = rs.getInt("vehicle_type_id");
        int totalSlots = rs.getInt("total_slots");

        return new ParkingArea(siteId, siteId, name, vehicleTypeId, totalSlots);
    }
}
