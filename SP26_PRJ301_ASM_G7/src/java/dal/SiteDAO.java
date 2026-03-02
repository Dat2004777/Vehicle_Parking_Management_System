package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.ParkingSite;
import model.dto.SiteDensityDTO;

/**
 *
 * @author ADMIN
 */
public class SiteDAO extends DBContext {

    public void add(ParkingSite site) {
        String sql = "INSERT INTO ParkingSites (site_name, address, region,manager_id, status) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, site.getSiteName());
            ps.setString(2, site.getAddress());

            ps.setString(3, site.getRegion().name());

            ps.setInt(4, site.getManagerId());
            ps.setString(5, site.getSiteState().name());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error add: " + e.getMessage());
        }
    }

    public void update(ParkingSite newSiteData) {
        String sql
                = """
                UPDATE ParkingSites
                SET site_name = ?, address = ?, region = ?, status = ?, manager_id = ? 
                WHERE site_id = ?
                """;
        try {
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, newSiteData.getSiteName());
            ps.setString(2, newSiteData.getAddress());

            ps.setString(3, newSiteData.getRegion().name());
            ps.setString(4, newSiteData.getSiteState().name());
            ps.setInt(5, newSiteData.getManagerId());

            ps.setInt(6, newSiteData.getSiteId());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error update: " + e.getMessage());
        }
    }

    // 5. Xóa (Delete)
    public void delete(int id) {
        String sql = "DELETE FROM ParkingSites WHERE site_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error delete: " + e.getMessage());
        }
    }

    public List<ParkingSite> getAll() {
        List<ParkingSite> list = new ArrayList<>();
        String sql
                = """
                SELECT s.site_id, s.site_name,s.address, s.region, s.manager_id, s.status,SUM(a.totalSlots) AS total_slots
                    FROM ParkingSites s
                    JOIN ParkingAreas a ON s.site_id = a.site_id 
                    GROUP BY 
                        s.site_id,
                        s.site_name,
                        s.address,
                        s.region,
                        s.manager_id,
                        s.status;
                """;

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ParkingSite site = mapRowToSite(rs);
                list.add(site);
            }
        } catch (SQLException e) {
            System.out.println("Error getAll: " + e.getMessage());
        }
        return list;
    }

    public ParkingSite getById(int id) {
        String sql
                = """
                SELECT s.site_id, s.site_name,s.address, s.region, s.manager_id, s.status,SUM(a.totalSlots) AS total_slots
                    FROM ParkingSites s
                    JOIN ParkingAreas a ON s.site_id = a.site_id 
                    WHERE s.site_id = ?
                    GROUP BY 
                        s.site_id,
                        s.site_name,
                        s.address,
                        s.region,
                        s.manager_id,
                        s.status;
                """;
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRowToSite(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error getById: " + e.getMessage());
        }
        return null;
    }

    public List<ParkingSite> searchByName(String keyword) {
        List<ParkingSite> list = new ArrayList<>();

        String sql
                = """
                SELECT s.site_id, s.site_name,s.address, s.region, s.manager_id, s.status,SUM(a.totalSlots) AS total_slots
                    FROM ParkingSites s
                    JOIN ParkingAreas a ON s.site_id = a.site_id 
                    WHERE s.site_name LIKE ?
                    GROUP BY 
                        s.site_id,
                        s.site_name,
                        s.address,
                        s.region,
                        s.manager_id,
                        s.status;
                """;

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRowToSite(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error searchByName: " + e.getMessage());
        }
        return list;
    }

    public List<ParkingSite> getSpecificSites(String siteRegion, String querySite) {
        String sql
                = """
                SELECT s.site_id, s.site_name,s.address, s.region, s.manager_id, s.status,SUM(a.totalSlots) AS total_slots
                    FROM ParkingSites s
                    JOIN ParkingAreas a ON s.site_id = a.site_id 
                    WHERE (? = '' OR s.region = ?) AND (
                        ? = '' 
                        OR s.site_name COLLATE Latin1_General_CI_AI LIKE ?
                        OR s.address COLLATE Latin1_General_CI_AI LIKE ?
                    )
                    GROUP BY 
                        s.site_id,
                        s.site_name,
                        s.address,
                        s.region,
                        s.manager_id,
                        s.status;
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, siteRegion);
            ps.setString(2, siteRegion);

            ps.setString(3, querySite);
            ps.setString(4, "%" + querySite + "%");
            ps.setString(5, "%" + querySite + "%");

            ResultSet rs = ps.executeQuery();
            List<ParkingSite> listSites = new ArrayList<>();

            while (rs.next()) {
                listSites.add(mapRowToSite(rs));
            }
            return listSites;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi lấy dữ liệu sites cụ thể");
            return null;
        }
    }

    private ParkingSite mapRowToSite(ResultSet rs) throws SQLException {
        int id = rs.getInt("site_id");
        String name = rs.getString("site_name");
        String address = rs.getString("address");
        String regionStr = rs.getString("region");
        String statusStr = rs.getString("status");
        int managerId = rs.getInt("manager_id");
        int totalSlots = rs.getInt("total_slots");
        ParkingSite.Region region = ParkingSite.Region.NORTH; // Default
        try {
            if (regionStr != null) {
                region = ParkingSite.Region.valueOf(regionStr.toUpperCase());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi convert Region: " + regionStr);
        }

        ParkingSite.State status = ParkingSite.State.CLOSED;
        try {
            if (statusStr != null) {
                status = ParkingSite.State.valueOf(statusStr.toUpperCase());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi convert Status: " + statusStr);
        }

        return new ParkingSite(id, name, address, region, status, managerId, totalSlots);
    }

    public List<ParkingSite> getAllSites() {
        List<ParkingSite> list = new ArrayList<>();
        String sql = """
                SELECT ps.site_id, ps.site_name, ps.address, ps.region, ps.operating_state, ps.manager_id
                FROM ParkingSites ps
                WHERE ps.status = 'active'
                """;

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ParkingSite site = new ParkingSite();
                site.setSiteId(rs.getInt("site_id"));
                site.setSiteName(rs.getString("site_name"));
                site.setAddress(rs.getString("address"));
                String regionStr = rs.getString("region");
                site.setRegion(ParkingSite.Region.valueOf(regionStr.toUpperCase().trim()));
                String siteStateStr = rs.getString("operating_state");
                site.setSiteState(ParkingSite.State.valueOf(siteStateStr.toUpperCase().trim()));
                site.setManagerId(rs.getInt("manager_id"));

                list.add(site);
            }
        } catch (Exception e) {
            System.out.println("Error siteDAO.getAllSites: " + e.getMessage());
        }

        return list;
    }

    public List<SiteDensityDTO> getSiteDensities() {
        List<SiteDensityDTO> list = new ArrayList<>();

        // Câu Query gom nhóm dữ liệu theo từng Site
        String sql = """
            SELECT 
                s.site_id, 
                s.site_name,
                ISNULL(p.current_parked, 0) AS current_parked,
                ISNULL(a.max_capacity, 0) AS max_capacity
            FROM ParkingSites s
            
            -- 1. Tính tổng số Slot của toàn bộ Area thuộc Site
            LEFT JOIN (
                SELECT site_id, SUM(totalSlots) as max_capacity
                FROM ParkingAreas
                WHERE status = 'active'
                GROUP BY site_id
            ) a ON s.site_id = a.site_id
            
            -- 2. Đếm số lượng xe đang nằm trong bãi (session_state = 'parked')
            LEFT JOIN (
                SELECT pc.site_id, COUNT(ps.session_id) as current_parked
                FROM ParkingSessions ps
                JOIN ParkingCards pc ON ps.card_id = pc.card_id
                WHERE ps.session_state = 'parked' AND ps.status = 'active'
                GROUP BY pc.site_id
            ) p ON s.site_id = p.site_id
            
            WHERE s.status = 'active' 
            """;

        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                SiteDensityDTO dto = new SiteDensityDTO();
                dto.setSiteId(rs.getInt("site_id"));
                dto.setSiteName(rs.getString("site_name"));
                dto.setCurrentParked(rs.getInt("current_parked"));
                dto.setMaxCapacity(rs.getInt("max_capacity"));

                list.add(dto);
            }
        } catch (Exception e) {
            System.out.println("Error siteDAO.getSiteDensities: " + e.getMessage());
        }

        return list;
    }

    public List<SiteDensityDTO> getSiteDensitiesById(int siteId) {
        List<SiteDensityDTO> list = new ArrayList<>();
        String sql = """
            SELECT 
                s.site_id, 
                s.site_name,
                ISNULL(p.current_parked, 0) AS current_parked,
                ISNULL(a.max_capacity, 0) AS max_capacity,
                ISNULL(p.car_current_parked, 0) AS car_current_parked,
                ISNULL(a.car_max_capacity, 0) AS car_max_capacity,
                ISNULL(p.moto_current_parked, 0) AS moto_current_parked,
                ISNULL(a.moto_max_capacity, 0) AS moto_max_capacity
            FROM ParkingSites s
            
            -- 1. Tính tổng số Slot, bóc tách riêng cho xe loại 1 (Car) và 2 (Motorbike)
            LEFT JOIN (
                SELECT 
                    site_id, 
                    SUM(totalSlots) as max_capacity,
                    SUM(CASE WHEN vehicle_type_id = 1 THEN totalSlots ELSE 0 END) as car_max_capacity,
                    SUM(CASE WHEN vehicle_type_id = 2 THEN totalSlots ELSE 0 END) as moto_max_capacity
                FROM ParkingAreas
                WHERE status = 'active'
                GROUP BY site_id
            ) a ON s.site_id = a.site_id
            
            -- 2. Đếm số xe đang đỗ, bóc tách riêng theo vehicle_type_id
            LEFT JOIN (
                SELECT 
                    pc.site_id, 
                    COUNT(ps.session_id) as current_parked,
                    SUM(CASE WHEN ps.vehicle_type_id = 1 THEN 1 ELSE 0 END) as car_current_parked,
                    SUM(CASE WHEN ps.vehicle_type_id = 2 THEN 1 ELSE 0 END) as moto_current_parked
                FROM ParkingSessions ps
                JOIN ParkingCards pc ON ps.card_id = pc.card_id
                WHERE ps.session_state = 'parked' AND ps.status = 'active'
                GROUP BY pc.site_id
            ) p ON s.site_id = p.site_id
            
            WHERE s.status = 'active' AND s.site_id = ?
            """;

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, siteId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SiteDensityDTO dto = new SiteDensityDTO();
                dto.setSiteId(rs.getInt("site_id"));
                dto.setSiteName(rs.getString("site_name"));
                dto.setCurrentParked(rs.getInt("current_parked"));
                dto.setMaxCapacity(rs.getInt("max_capacity"));

                // Map dữ liệu Ô tô
                dto.setCarCurrentParked(rs.getInt("car_current_parked"));
                dto.setCarMaxCapacity(rs.getInt("car_max_capacity"));

                // Map dữ liệu Xe máy
                dto.setMotoCurrentParked(rs.getInt("moto_current_parked"));
                dto.setMotoMaxCapacity(rs.getInt("moto_max_capacity"));

                list.add(dto);
            }
        } catch (Exception e) {
            System.out.println("Error siteDAO.getSiteDensitiesById: " + e.getMessage());
        }
        return list;
    }
}
