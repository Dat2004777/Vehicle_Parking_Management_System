package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.ParkingSite;
import model.VehicleType;
import model.dto.SiteDensityDTO;

/**
 *
 * @author ADMIN
 */
public class SiteDAO extends DBContext {

    public int addSiteAndGetId(ParkingSite site) {
        String sql = """
                     INSERT INTO ParkingSites (site_name, address, region, manager_id, operating_state) 
                     VALUES (?, ?, ?, ?, ?);
                     """;
        try {
            PreparedStatement ps = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, site.getSiteName());
            ps.setString(2, site.getAddress());
            ps.setString(3, site.getRegion().name().toLowerCase());

            // XỬ LÝ LƯU NULL NẾU managerId == 0
            if (site.getManagerId() == 0) {
                ps.setNull(4, java.sql.Types.INTEGER);
            } else {
                ps.setInt(4, site.getManagerId());
            }

            ps.setString(5, site.getSiteState().name().toLowerCase());

            ps.executeUpdate();

            // Lấy ID vừa tự động tăng để dùng cho việc lưu ParkingAreas
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error SiteDAO.addSite: " + e.getMessage());
        }
        return -1;
    }

    public void updateParkingSite(ParkingSite newSiteData) {
        String sql
                = """
                UPDATE ParkingSites
                SET site_name = ?, address = ?, region = ?, operating_state = ?, manager_id = ? 
                WHERE site_id = ?
                """;
        try {
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, newSiteData.getSiteName());
            ps.setString(2, newSiteData.getAddress());

            ps.setString(3, newSiteData.getRegion().name().toLowerCase());
            ps.setString(4, newSiteData.getSiteState().name().toLowerCase());

            // KIỂM TRA MANAGER ID
            if (newSiteData.getManagerId() == 0) {
                // Nếu là 0, set giá trị NULL cho cột manager_id (kiểu INT)
                ps.setNull(5, java.sql.Types.INTEGER);
            } else {
                ps.setInt(5, newSiteData.getManagerId());
            }

            ps.setInt(6, newSiteData.getSiteId());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error SiteDAO.updateParkingSite: " + e.getMessage());
        }
    }

    // 5. Xóa (Delete)
    public void deleteSiteBySiteIdAndChangeEmp(int siteId) {
        try {

            // Bước 1: Giải phóng nhân viên (Set site_id = NULL cho các nhân viên thuộc site này)
            String sqlUpdateEmployees = "UPDATE Employees SET site_id = NULL WHERE site_id = ?";
            try (PreparedStatement psEmp = connection.prepareStatement(sqlUpdateEmployees)) {
                psEmp.setInt(1, siteId);
                psEmp.executeUpdate();
            }

            // Bước 2: Xóa mềm bãi xe (hoặc xóa cứng tùy bạn)
            String sqlDeleteSite = "UPDATE ParkingSites SET status = 'inactive' WHERE site_id = ?";
            try (PreparedStatement psSite = connection.prepareStatement(sqlDeleteSite)) {
                psSite.setInt(1, siteId);
                psSite.executeUpdate();
            }

        } catch (SQLException e) {
            System.out.println("Error SiteDAO.deleteSiteBySiteIdAndChangeEmp : " + e.getMessage());
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

    public ParkingSite getById(int siteId) {
        String sql
                = """
                SELECT s.site_id, s.site_name,s.address, s.region, s.manager_id, s.operating_state,SUM(a.totalSlots) AS total_slots
                    FROM ParkingSites s
                    JOIN ParkingAreas a ON s.site_id = a.site_id 
                    WHERE s.site_id = ?
                    GROUP BY 
                        s.site_id,
                        s.site_name,
                        s.address,
                        s.region,
                        s.manager_id,
                        s.operating_state;
                """;
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, siteId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRowToSite2(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error SiteDAO.getSiteById: " + e.getMessage());
        }
        return null;
    }

    public ParkingSite getSiteById(int siteId) {
        String sql
                = """
                SELECT s.site_id, s.site_name,s.address, s.region, s.manager_id, s.operating_state
                FROM ParkingSites s
                WHERE s.site_id = ? AND s.status = 'active'
                """;
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, siteId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRowToSite2(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error SiteDAO.getSiteById: " + e.getMessage());
        }
        return null;
    }

    public List<ParkingSite> siteSearchQuery(String siteSearchQuery) {
        List<ParkingSite> list = new ArrayList<>();

        String sql = """
                        SELECT * FROM ParkingSites ps
                        WHERE (ps.site_name like ? OR ps.address like ?) AND ps.status = 'active'
                    """;

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, "%" + siteSearchQuery + "%");
            ps.setString(2, "%" + siteSearchQuery + "%");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ParkingSite site = new ParkingSite(id, name, address, region, status, managerId, totalSlots);
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
        } catch (SQLException e) {
            System.out.println("Error SiteDAO.siteSearchQuery: " + e.getMessage());
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

    public List<ParkingSite> getAllSitesWithAvailableSlots() {
        List<ParkingSite> list = new ArrayList<>();

        String sql = """
                    SELECT 
                        ps.site_id, 
                        ps.site_name, 
                        ps.address, 
                        ps.region,
                        ps.operating_state,
                        (
                            SELECT COUNT(pc.card_id)
                            FROM ParkingCards pc
                            WHERE pc.site_id = ps.site_id
                            AND pc.card_state = 'available'
                            AND pc.status = 'active'
                        ) AS availableSlots
                        FROM ParkingSites ps
                        WHERE ps.status = 'active'
                    """;

        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ParkingSite site = new ParkingSite(id, name, address, region, status, managerId, totalSlots);
                site.setSiteId(rs.getInt("site_id"));
                site.setSiteName(rs.getString("site_name"));
                site.setAddress(rs.getString("address"));
                site.setRegion(ParkingSite.Region.valueOf(rs.getString("region").toUpperCase()));
                site.setSiteStatus(ParkingSite.State.valueOf(rs.getString("operating_state").toUpperCase()));
                site.setTotalSlots(rs.getInt("availableSlots"));

                list.add(site);
            }

        } catch (Exception e) {
            System.out.println("Lỗi lấy Sites với available slots");
            e.printStackTrace();
        }

        return list;
    }

    public List<ParkingSite> filterSites(String address, String region, String status, String vehicleTypeId) {
        String sql
                = """
                SELECT 
                    ps.site_id, 
                    ps.site_name, 
                    ps.address, 
                    ps.region, 
                    ps.operating_state,
                    (
                        SELECT COUNT(pc.card_id)
                        FROM ParkingCards pc
                        WHERE pc.site_id = ps.site_id 
                        AND pc.card_state = 'available' 
                        AND pc.status = 'active'
                    ) AS availableSlots
                FROM ParkingSites ps
                WHERE 
                    ps.status = 'active'
                    AND (? = '' OR ps.region = ?)
                    AND (
                        ? = '' 
                        OR ps.site_name COLLATE Latin1_General_CI_AI LIKE ?
                        OR ps.address COLLATE Latin1_General_CI_AI LIKE ?
                    )
                    AND (? = '' OR ps.operating_state = ?)
                    AND (
                        ? = ''
                        OR EXISTS (
                            SELECT 1
                            FROM ParkingAreas pa
                            WHERE pa.site_id = ps.site_id
                            AND pa.status = 'active'
                            AND pa.vehicle_type_id = ?
                        )
                    )
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, region);
            ps.setString(2, region);

            ps.setString(3, address);
            ps.setString(4, "%" + address + "%");
            ps.setString(5, "%" + address + "%");

            ps.setString(6, status);
            ps.setString(7, status);

            ps.setString(8, vehicleTypeId);
            ps.setString(9, vehicleTypeId);

            List<ParkingSite> list = new ArrayList<>();

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ParkingSite site = new ParkingSite(id, name, address, region, status, managerId, totalSlots, 0);
                site.setSiteId(rs.getInt("site_id"));
                site.setSiteName(rs.getString("site_name"));
                site.setAddress(rs.getString("address"));
                site.setRegion(ParkingSite.Region.valueOf(rs.getString("region").toUpperCase()));
                site.setSiteStatus(ParkingSite.State.valueOf(rs.getString("operating_state").toUpperCase()));
                site.setTotalSlots(rs.getInt("availableSlots"));

                list.add(site);
            }

            return list;
        } catch (Exception e) {
            System.out.println("Lỗi lấy ra chi tiết site đã filter");
            return null;
        }
    }

    public List<String> getAllRegions() {
        String sql
                = """
                SELECT DISTINCT region
                FROM ParkingSites
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            List<String> list = new ArrayList<>();
            while (rs.next()) {
                list.add(rs.getString("region"));
            }
            return list;
        } catch (Exception e) {
            System.out.println("Lỗi lấy ra các region");
            e.printStackTrace();
            return null;
        }
    }

    public List<VehicleType> getVehicles() {
        String sql
                = """
                SELECT vehicle_type_id, name
                FROM VehicleTypes
                WHERE status = 'active'
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            List<VehicleType> list = new ArrayList<>();
            while (rs.next()) {
                VehicleType.VehicleName vehicleEnum
                        = VehicleType.VehicleName.valueOf(rs.getString("name").toUpperCase());

                list.add(new VehicleType(rs.getInt("vehicle_type_id"), vehicleEnum));
            }

            return list;
        } catch (Exception e) {
            System.out.println("Lỗi lấy ra vehicles");
            e.printStackTrace();
            return null;
        }
    }

    private ParkingSite mapRowToSite(ResultSet rs) throws SQLException {
        int id = rs.getInt("site_id");
        String name = rs.getString("site_name");
        String address = rs.getString("address");
        String regionStr = rs.getString("region");
        String stateStr = rs.getString("operating_state");
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
            if (stateStr != null) {
                status = ParkingSite.State.valueOf(stateStr.toUpperCase());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi convert Status: " + stateStr);
        }

        return new ParkingSite(id, name, address, region, status, managerId, totalSlots);
    }

    private ParkingSite mapRowToSite2(ResultSet rs) throws SQLException {
        int id = rs.getInt("site_id");
        String name = rs.getString("site_name");
        String address = rs.getString("address");
        String regionStr = rs.getString("region");
        String stateStr = rs.getString("operating_state");
        int managerId = rs.getInt("manager_id");
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
            if (stateStr != null) {
                status = ParkingSite.State.valueOf(stateStr.toUpperCase());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi convert Status: " + stateStr);
        }

        return new ParkingSite(id, name, address, region, status, managerId);
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
                ParkingSite site = new ParkingSite(id, name, address, region, status, managerId, totalSlots);
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

    public List<SiteDensityDTO> getSiteDensities(String siteSearchQuery) {
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
            
            WHERE s.status = 'active' AND (s.site_name LIKE ? OR s.address LIKE ?)
            """;

        try (PreparedStatement ps = connection.prepareStatement(sql);) {
            ps.setString(1, "%" + siteSearchQuery + "%");
            ps.setString(2, "%" + siteSearchQuery + "%");
            ResultSet rs = ps.executeQuery();
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

    public void setNullManagerIdWhenChangeSiteEmployee(int employeeId) {
        String sql = """
               Update ParkingSites set manager_id = NULL WHERE manager_id = ?
               """;

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, employeeId);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error siteDAO.setNullManagerIdWhenChangeSiteEmployee: " + e.getMessage());
        }
    }
}
