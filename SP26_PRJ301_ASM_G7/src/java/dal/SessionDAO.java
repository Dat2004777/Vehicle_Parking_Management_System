package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.ParkingSession;

public class SessionDAO extends DBContext {

    // 1. Hàm đếm số xe ĐANG CÓ TRONG BÃI (Dùng cho 3 thẻ thống kê to ở trên)
    public int countActiveSessions(int siteId) {
        String sql = """
                     SELECT COUNT(*)
                     FROM ParkingSessions s
                     JOIN ParkingCards c ON s.card_id = c.card_id
                     WHERE c.site_id = ? AND s.status = 'active' AND s.session_state = 'parked'
                     """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, siteId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1); // Trả về con số kết quả của COUNT(*)
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0; // Trả về 0 nếu lỗi
    }

    // Thêm vào SessionDAO.java
    public int countActiveSessionsByArea(int areaId) {
        int count = 0;
        String sql = """
                     SELECT COUNT(*)
                     FROM ParkingSessions s
                     JOIN ParkingCards c ON s.card_id = c.card_id
                     JOIN ParkingAreas a ON a.site_id = c.site_id
                     WHERE a.area_id = ? AND s.status = 'active' AND s.session_state = 'parked' AND a.vehicle_type_id = s.vehicle_type_id
                     """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, areaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    // 2. Hàm lấy danh sách N lượt ra/vào mới nhất (Dùng cho thanh Offcanvas bên phải)
    public List<ParkingSession> getRecentLogs(int siteId, int limit) {
        List<ParkingSession> list = new ArrayList<>();

        // CẬP NHẬT 1: Thêm ngoặc đơn cho TOP (?), thêm cột s.status và thêm ORDER BY
        String sql = """
                     SELECT TOP (?) s.session_id, s.card_id, c.site_id, s.license_plate, 
                                  s.entry_time, s.exit_time, s.session_state, s.status
                     FROM ParkingSessions s
                     JOIN ParkingCards c ON s.card_id = c.card_id
                     WHERE c.site_id = ? AND s.status = 'active'
                     ORDER BY s.entry_time DESC
                     """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            // CẬP NHẬT 2: Set tham số đúng thứ tự dấu ? trong câu SQL
            ps.setInt(1, limit);
            ps.setInt(2, siteId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // CẬP NHẬT 3: Dùng Constructor rỗng và Setter vì câu SELECT không gọi hết các cột
                    ParkingSession session = new ParkingSession();

                    session.setSessionId(rs.getInt("session_id"));
                    session.setCardId(rs.getString("card_id"));
                    session.setLicensePlate(rs.getString("license_plate"));

                    // Lấy LocalDateTime chuẩn JDBC 4.2+ (Trực tiếp, không cần convert qua Timestamp)
                    session.setEntryTime(rs.getObject("entry_time", LocalDateTime.class));
                    session.setExitTime(rs.getObject("exit_time", LocalDateTime.class));

                    session.setSessionState(rs.getString("session_state"));
                    session.setStatus(rs.getString("status"));

                    list.add(session);
                }
            }
        } catch (Exception e) {
            System.out.println("Lỗi getRecentLogs trong SessionDAO: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // ==========================================================
    // 1. CÁC HÀM XỬ LÝ NGHIỆP VỤ CHECK-IN / CHECK-OUT
    // ==========================================================
    /**
     * HÀM 1: Lưu xe vào bãi (CHECK-IN)
     */
    public boolean checkIn(ParkingSession session) {
        String sql = "INSERT INTO ParkingSessions "
                + "(card_id, vehicle_type_id, license_plate, session_type, session_state) "
                + "VALUES (?, ?, ?, ?, ?)";

        // Chỉ dùng try-with-resources cho PreparedStatement để không làm đóng mất connection tổng
        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, session.getCardId());
            ps.setObject(2, session.getVehicleTypeId());
            ps.setString(3, session.getLicensePlate());
            ps.setString(4, session.getSessionType());
            ps.setString(5, session.getSessionState());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Lỗi Check-in: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * HÀM 2: Cập nhật giờ ra và tính tiền (CHECK-OUT)
     */
    public boolean checkOut(ParkingSession session) {
        String sql = "UPDATE ParkingSessions "
                + "SET exit_time = ?, fee_amount = ?, session_state = ?, status = ? "
                + "WHERE session_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setObject(1, session.getExitTime());
            ps.setObject(2, session.getFeeAmount());
            ps.setString(3, session.getSessionState());
            ps.setString(4, session.getStatus());
            ps.setInt(5, session.getSessionId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Lỗi Check-out: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // ==========================================================
    // 2. CÁC HÀM TRUY VẤN (TÌM KIẾM, LẤY DANH SÁCH)
    // ==========================================================
    public boolean isVehicleInLot(String licensePlate, int siteId) {
        // Dùng hàm COUNT, chỉ trả về 1 con số duy nhất là 0 hoặc >0. Siêu nhẹ!
        String sql = """
                 SELECT COUNT(1) FROM ParkingSessions s
                 JOIN ParkingCards c ON s.card_id = c.card_id
                 WHERE c.site_id = ? 
                   AND s.status = 'active'
                   AND s.session_state = 'parked' 
                   AND s.license_plate = ?
                 """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, siteId);
            ps.setString(2, licensePlate); // Truyền trực tiếp biển số xuống DB

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Nếu Count > 0 nghĩa là xe đã tồn tại
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi kiểm tra trùng biển số: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * HÀM 3: Tìm xe đang đỗ trong bãi dựa vào Mã thẻ HOẶC Biển số
     */
    public ParkingSession getActiveSession(String cardId) {
        String sql = "SELECT TOP 1 * FROM ParkingSessions "
                + "WHERE card_id = ?  AND status = 'active'"
                + "ORDER BY entry_time DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, cardId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToSession(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi Tìm xe Active: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * HÀM 4: Lấy toàn bộ lịch sử gửi xe (Mới nhất lên đầu)
     */
    public List<ParkingSession> getAll() {
        List<ParkingSession> list = new ArrayList<>();
        String sql = "SELECT * FROM ParkingSessions ORDER BY entry_time DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRowToSession(rs));
            }
        } catch (SQLException e) {
            System.out.println("Lỗi getAll SessionDAO: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    /**
     * HÀM 5: Đếm số lượng xe đang trong bãi (Dành cho Dashboard)
     */
    public int countActiveSessions() {
        String sql = "SELECT COUNT(*) FROM ParkingSessions WHERE session_state = 'ACTIVE'";

        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi countActiveSessions: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    // ==========================================================
    // 3. HELPER METHOD
    // ==========================================================
    /**
     * Ánh xạ từ ResultSet sang Object ParkingSession
     */
    private ParkingSession mapRowToSession(ResultSet rs) throws SQLException {
        ParkingSession session = new ParkingSession();

        session.setSessionId(rs.getInt("session_id"));
        session.setCardId(rs.getString("card_id"));
        session.setVehicleTypeId((Integer) rs.getObject("vehicle_type_id"));
        session.setLicensePlate(rs.getString("license_plate"));

        session.setEntryTime(rs.getObject("entry_time", LocalDateTime.class));
        session.setExitTime(rs.getObject("exit_time", LocalDateTime.class));

        session.setSessionType(rs.getString("session_type"));

        Object feeObj = rs.getObject("fee_amount");
        if (feeObj != null) {
            session.setFeeAmount(((Number) feeObj).longValue());
        }

        session.setSessionState(rs.getString("session_state"));
        session.setStatus(rs.getString("status"));

        return session;
    }
}
