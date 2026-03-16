/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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

    public List<ParkingSession> getRecentLogs(int siteId, int limit, String state) {
        List<ParkingSession> list = new ArrayList<>();

        // 1. Dùng StringBuilder để xây dựng câu SQL động
        StringBuilder sql = new StringBuilder("""
                SELECT TOP (?) s.session_id, s.card_id, c.site_id, s.license_plate,
                               s.entry_time, s.exit_time, s.session_state, s.status
                FROM ParkingSessions s
                JOIN ParkingCards c ON s.card_id = c.card_id
                WHERE c.site_id = ? AND s.status = 'active'
                """);

        // 2. Kiểm tra state: Nếu khác null và không rỗng thì nối thêm điều kiện WHERE
        boolean hasStateFilter = state != null && !state.trim().isEmpty();
        if (hasStateFilter) {
            sql.append(" AND s.session_state = ? ");
        }

        // Nối thêm ORDER BY ở cuối cùng
        sql.append(" ORDER BY s.entry_time DESC ");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {

            // 3. Set tham số cứng ban đầu
            ps.setInt(1, limit);
            ps.setInt(2, siteId);

            // 4. Nếu có lọc state thì set tham số thứ 3
            if (hasStateFilter) {
                ps.setString(3, state.trim());
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ParkingSession session = new ParkingSession();

                    session.setSessionId(rs.getInt("session_id"));
                    session.setCardId(rs.getString("card_id"));
                    session.setLicensePlate(rs.getString("license_plate"));

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

        // Chỉ dùng try-with-resources cho PreparedStatement để không làm đóng mất
        // connection tổng
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
                + "WHERE card_id = ?  AND status = 'active' AND session_state = 'parked'"
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

    public ParkingSession getActiveSession(String cardId, String licensePlate) {
        String sql = "SELECT TOP 1 * FROM ParkingSessions "
                + "WHERE card_id = ? AND license_plate = ? AND status = 'active' AND session_state = 'parked'"
                + "ORDER BY entry_time DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, cardId);
            ps.setString(2, licensePlate);

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

    public LocalDateTime getExpectedTimeOut(String cardId) {
        String sql = """
                SELECT
                    CASE
                        WHEN s.session_type = 'noncasual' THEN
                            COALESCE(
                                (SELECT TOP 1 sub.end_date FROM Subscriptions sub
                                 WHERE sub.card_id = s.card_id AND sub.sub_state = 'active'
                                 ORDER BY sub.end_date DESC),
                                (SELECT TOP 1 b.end_time FROM Bookings b
                                 WHERE b.card_id = s.card_id AND b.booking_state = 'completed')
                            )
                        ELSE NULL
                    END AS expected_time_out
                FROM ParkingSessions s
                WHERE s.card_id = ? AND s.session_state = 'parked';
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cardId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Lưu ý: Tên cột ở đây phải khớp với ALIAS 'expected_time_out' trong SQL
                Timestamp ts = rs.getTimestamp("expected_time_out");
                if (ts != null) {
                    return ts.toLocalDateTime();
                }
            }
        } catch (SQLException e) {
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
