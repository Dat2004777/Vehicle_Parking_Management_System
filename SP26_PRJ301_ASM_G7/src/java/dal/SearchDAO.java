package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import model.dto.search.CardSearchResultDTO;
import model.dto.TransactionHistoryDTO;
import model.dto.search.VehicleSearchResultDTO;

public class SearchDAO extends DBContext {

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yyyy");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // =======================================================================
    // 1. TÌM KIẾM XE ĐANG TRONG BÃI
    // =======================================================================
    public VehicleSearchResultDTO findActiveVehicleByPlate(String licensePlate, int siteId) {
        String sql = "SELECT ps.license_plate, ps.card_id, ps.entry_time, vt.name AS vehicle_type, "
                + "ps.session_type, pa.area_name "
                + "FROM ParkingSessions ps "
                + "JOIN VehicleTypes vt ON ps.vehicle_type_id = vt.vehicle_type_id "
                + "JOIN ParkingCards pc ON ps.card_id = pc.card_id "
                + "LEFT JOIN ParkingAreas pa ON pc.site_id = pa.site_id AND ps.vehicle_type_id = pa.vehicle_type_id "
                + "WHERE ps.license_plate = ? AND pc.site_id = ? AND ps.session_state = 'parked' AND ps.status = 'active'";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, licensePlate);
            st.setInt(2, siteId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                VehicleSearchResultDTO dto = new VehicleSearchResultDTO();
                dto.setLicensePlate(rs.getString("license_plate"));
                dto.setRfid(rs.getString("card_id"));

                // Format thời gian
                if (rs.getTimestamp("entry_time") != null) {
                    dto.setEntryTime(rs.getTimestamp("entry_time").toLocalDateTime().format(timeFormatter));
                }

                dto.setAreaName(rs.getString("area_name") != null ? rs.getString("area_name") : "Chưa xác định");

                // Chuẩn hóa loại xe để hiển thị đẹp hơn
                String vType = rs.getString("vehicle_type");
                dto.setVehicleType("car".equalsIgnoreCase(vType) ? "Ô tô" : "Xe máy");

                // Xác định vé tháng/lượt
                dto.setIsMonthlyTicket("noncasual".equalsIgnoreCase(rs.getString("session_type")));
                return dto;
            }
        } catch (SQLException e) {
            System.out.println("Lỗi findActiveVehicleByPlate: " + e.getMessage());
        }
        return null;
    }

    // =======================================================================
    // 2. LẤY LỊCH SỬ GIAO DỊCH CỦA XE (TOP 10)
    // =======================================================================
    public List<TransactionHistoryDTO> getVehicleTransactions(String licensePlate, int siteId) {
        List<TransactionHistoryDTO> list = new ArrayList<>();
        // Dùng UNION ALL để gộp cả lượt VÀO và lượt RA thành 1 danh sách, sắp xếp mới nhất lên đầu
        String sql = """
                     SELECT TOP 10 * FROM ( 
                                     SELECT entry_time AS txn_time, 'IN' AS action_type, license_plate, card_id FROM ParkingSessions 
                                      WHERE license_plate = ? AND status = 'active' AND entry_time IS NOT NULL 
                                      UNION ALL 
                                       SELECT exit_time AS txn_time, 'OUT' AS action_type, license_plate, card_id FROM ParkingSessions 
                                     WHERE license_plate = ? AND session_state = 'completed' AND status = 'active' AND exit_time IS NOT NULL 
                                     ) AS History
                                     JOIN ParkingCards pc ON History.card_id = pc.card_id
                                     WHERE site_id = ?
                                     ORDER BY txn_time DESC
                     """;
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, licensePlate);
            st.setString(2, licensePlate);
            st.setInt(3, siteId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                TransactionHistoryDTO dto = new TransactionHistoryDTO();
                dto.setTime(rs.getTimestamp("txn_time").toLocalDateTime().format(timeFormatter));
                dto.setAction(rs.getString("action_type"));
                dto.setLicensePlate(rs.getString("license_plate"));
                dto.setStaffName("Hệ thống"); // Mock do DB chưa lưu employee_id
                list.add(dto);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi getVehicleTransactions: " + e.getMessage());
        }
        return list;
    }

    // =======================================================================
    // 3. TÌM KIẾM THÔNG TIN THẺ
    // =======================================================================
    public CardSearchResultDTO findCardById(String id, int siteId) {
        String sql = """
                                    SELECT 
                                        pc.card_id, pc.card_state, pc.status AS card_sys_status, 
                                        s.subscription_id, s.license_plate AS registered_plate, s.end_date,
                                        c1.first_name AS owner_first_name, c1.last_name AS owner_last_name, 
                                        ps.session_state, ps.entry_time, ps.license_plate AS parked_plate,
                                        b.booking_id, c2.first_name AS booker_first_name, c2.last_name AS booker_last_name, b.start_time AS booking_start, b.end_time AS booking_end
                                    FROM ParkingCards pc 
                                    LEFT JOIN Subscriptions s ON pc.card_id = s.card_id AND s.sub_state = 'active' AND s.status = 'active'
                                    LEFT JOIN Customers c1 ON s.customer_id = c1.customer_id
                                    LEFT JOIN ParkingSessions ps ON pc.card_id = ps.card_id AND ps.session_state = 'parked'
                                    LEFT JOIN Bookings b ON b.card_id = pc.card_id AND b.booking_state = 'accepted'
                                    LEFT JOIN Customers c2 ON b.customer_id = c2.customer_id
                                    WHERE pc.card_id = ? AND pc.site_id = ?
                    """;

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, id);
            st.setInt(2, siteId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                CardSearchResultDTO dto = new CardSearchResultDTO();
                dto.setRfid(rs.getString("card_id"));

                // 1. Logic Trạng thái hệ thống của thẻ
                String sysStatus = rs.getString("card_sys_status");
                dto.setStatus("inactive".equalsIgnoreCase(sysStatus) ? "LOCKED" : "ACTIVE");

                // 2. Logic Phân loại thẻ & Lấy thông tin chủ xe
                if (rs.getObject("subscription_id") != null) {
                    dto.setCardType("Vé tháng");
                    dto.setRegisteredPlate(rs.getString("registered_plate"));

                    // Xử lý nối tên an toàn (tránh hiện "null null")
                    String fName = rs.getString("owner_first_name");
                    String lName = rs.getString("owner_last_name");
                    dto.setOwnerName(((lName != null ? lName : "") + " " + (fName != null ? fName : "")).trim());

                    if (rs.getTimestamp("end_date") != null) {
                        dto.setExpiryDate(rs.getTimestamp("end_date").toLocalDateTime().format(dateFormatter));
                    }
                } else if (rs.getObject("booking_id") != null) {
                    dto.setCardType("Vé đặt trước");
                    String fName = rs.getString("booker_first_name");
                    String lName = rs.getString("booker_last_name");
                    dto.setOwnerName(((lName != null ? lName : "") + " " + (fName != null ? fName : "")).trim());
                    if (rs.getTimestamp("booking_start") != null) {
                        dto.setBookingStart(rs.getTimestamp("booking_start").toLocalDateTime().format(timeFormatter)); // hoặc dateFormatter tùy bạn
                    }
                    if (rs.getTimestamp("booking_end") != null) {
                        dto.setBookingEnd(rs.getTimestamp("booking_end").toLocalDateTime().format(timeFormatter));
                    }
                } else {
                    dto.setCardType("Vé lượt");
                }

                // 3. Logic: Thẻ này có đang nằm trong bãi không? (CỰC KỲ QUAN TRỌNG)
                if ("parked".equalsIgnoreCase(rs.getString("session_state"))) {
                    dto.setCurrentlyParked(true);
                    dto.setCurrentParkedPlate(rs.getString("parked_plate"));

                    if (rs.getTimestamp("entry_time") != null) {
                        // Nhớ dùng đúng formatter thời gian của bạn (vd: timeFormatter)
                        dto.setCurrentEntryTime(rs.getTimestamp("entry_time").toLocalDateTime().format(timeFormatter));
                    }
                } else {
                    dto.setCurrentlyParked(false);
                }

                return dto;
            }
        } catch (SQLException e) {
            System.out.println("Lỗi findCardByRfid: " + e.getMessage());
        }
        return null;
    }

    // =======================================================================
    // 4. LẤY LỊCH SỬ GIAO DỊCH CỦA THẺ (TOP 10)
    // =======================================================================
    public List<TransactionHistoryDTO> getCardTransactions(String id, int siteId) {
        List<TransactionHistoryDTO> list = new ArrayList<>();
        String sql = """
                     SELECT TOP 10 * FROM (
                        SELECT entry_time AS txn_time, 'IN' AS action_type, license_plate, card_id FROM ParkingSessions
                        WHERE card_id = ? AND status = 'active' AND entry_time IS NOT NULL
                        UNION ALL
                        SELECT exit_time AS txn_time, 'OUT' AS action_type, license_plate, card_id FROM ParkingSessions
                        WHERE card_id = ? AND session_state = 'completed' AND status = 'active' AND exit_time IS NOT NULL
                     ) AS History
                     JOIN ParkingCards pc ON History.card_id = pc.card_id
                     WHERE site_id = ?
                     ORDER BY txn_time DESC
                     """;
        
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, id);
            st.setString(2, id);
            st.setInt(3, siteId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                TransactionHistoryDTO dto = new TransactionHistoryDTO();
                dto.setTime(rs.getTimestamp("txn_time").toLocalDateTime().format(timeFormatter));
                dto.setAction(rs.getString("action_type"));
                dto.setLicensePlate(rs.getString("license_plate") != null ? rs.getString("license_plate") : "Không biển số");
                dto.setStaffName("Hệ thống");
                list.add(dto);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi getCardTransactions: " + e.getMessage());
        }
        return list;
    }
}
