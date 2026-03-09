package dal;

import model.ParkingArea;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AreaDAO extends DBContext {

    // 1. LẤY TẤT CẢ KHU VỰC CỦA 1 BÃI XE KÈM LOẠI XE
    public List<ParkingArea> getAreasBySite(int siteId) {
        List<ParkingArea> list = new ArrayList<>();
        // Tối ưu: JOIN thẳng với bảng VehicleTypes để lấy tên loại xe
        String sql = "SELECT pa.*, vt.name AS vehicle_type_name "
                + "FROM [ParkingAreas] pa "
                + "LEFT JOIN [VehicleTypes] vt ON pa.vehicle_type_id = vt.vehicle_type_id "
                + "WHERE pa.site_id = ? AND pa.status = 'active'";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, siteId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToArea(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. LẤY CHI TIẾT 1 KHU VỰC KÈM LOẠI XE (Dùng cho form Update)
    public ParkingArea getAreaById(int areaId) {
        String sql = "SELECT pa.*, vt.name AS vehicle_type_name "
                + "FROM [ParkingAreas] pa "
                + "LEFT JOIN [VehicleTypes] vt ON pa.vehicle_type_id = vt.vehicle_type_id "
                + "WHERE pa.area_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, areaId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToArea(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

// 3. THÊM KHU VỰC MỚI
    public boolean insertArea(ParkingArea area) {
        String sql = "INSERT INTO [ParkingAreas] (site_id, area_name, vehicle_type_id, totalSlots) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, area.getSiteId());
            ps.setString(2, area.getAreaName());
            ps.setInt(3, area.getVehicleTypeId());
            ps.setInt(4, area.getTotalSlots());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error AreaDAO.insertAreas: " + e.getMessage());
        }
        return false;
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
        // Đã sửa lại lỗi sai tên cột từ total_slots thành totalSlots cho khớp với SQL
        String sql = "UPDATE [ParkingAreas] SET area_name = ?, vehicle_type_id = ?, totalSlots = ? WHERE area_id = ?";

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

    // 5. XÓA KHU VỰC (Soft Delete)
    public boolean deleteArea(int areaId) {
        String sql = "UPDATE [ParkingAreas] SET status = 'inactive' WHERE area_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, areaId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error AreaDAO.deleteAreaBySiteId: " + e.getMessage());
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

    // 6. XÓA TẤT CẢ KHU VỰC CỦA 1 SITE
    public boolean deleteAreasBySite(int siteId) {
        String sql = "UPDATE [ParkingAreas] SET status = 'inactive' WHERE site_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, siteId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ==========================================
    // HÀM MAPPER NỘI BỘ
    // ==========================================
    private ParkingArea mapRowToArea(ResultSet rs, int tmp) throws SQLException {
        int id = rs.getInt("area_id");
        int siteId = rs.getInt("site_id");
        String name = rs.getString("area_name");
        int vehicleTypeId = rs.getInt("vehicle_type_id");
        int totalSlots = rs.getInt("totalSlots");

        // Trích xuất thêm Tên loại xe từ câu lệnh JOIN
        String vehicleTypeName = rs.getString("vehicle_type_name");

        // Trả về Entity kèm theo tên loại xe
        return new ParkingArea(id, siteId, name, vehicleTypeId, totalSlots, vehicleTypeName);
    }

    private ParkingArea mapRowToArea(ResultSet rs) throws SQLException {
        int id = rs.getInt("area_id");
        int siteId = rs.getInt("site_id");
        String name = rs.getString("area_name");
        int vehicleTypeId = rs.getInt("vehicle_type_id");
        int totalSlots = rs.getInt("total_slots");

        return new ParkingArea(siteId, siteId, name, vehicleTypeId, totalSlots);
    }
}
