package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import model.dto.CardSearchResultDTO;
import model.dto.TransactionHistoryDTO;
import model.dto.VehicleSearchResultDTO;

public class SearchDAO extends DBContext {

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yyyy");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // =======================================================================
    // 1. TÌM KIẾM XE ĐANG TRONG BÃI
    // =======================================================================
    public VehicleSearchResultDTO findActiveVehicleByPlate(String licensePlate) {
        String sql = "SELECT ps.license_plate, ps.card_id, ps.entry_time, vt.name AS vehicle_type, "
                   + "ps.session_type, pa.area_name "
                   + "FROM ParkingSessions ps "
                   + "JOIN VehicleTypes vt ON ps.vehicle_type_id = vt.vehicle_type_id "
                   + "JOIN ParkingCards pc ON ps.card_id = pc.card_id "
                   + "LEFT JOIN ParkingAreas pa ON pc.site_id = pa.site_id AND ps.vehicle_type_id = pa.vehicle_type_id "
                   + "WHERE ps.license_plate = ? AND ps.session_state = 'parked' AND ps.status = 'active'";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, licensePlate);
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
    public List<TransactionHistoryDTO> getVehicleTransactions(String licensePlate) {
        List<TransactionHistoryDTO> list = new ArrayList<>();
        // Dùng UNION ALL để gộp cả lượt VÀO và lượt RA thành 1 danh sách, sắp xếp mới nhất lên đầu
        String sql = "SELECT TOP 10 * FROM ( "
                   + "  SELECT entry_time AS txn_time, 'IN' AS action_type, license_plate FROM ParkingSessions "
                   + "  WHERE license_plate = ? AND status = 'active' AND entry_time IS NOT NULL "
                   + "  UNION ALL "
                   + "  SELECT exit_time AS txn_time, 'OUT' AS action_type, license_plate FROM ParkingSessions "
                   + "  WHERE license_plate = ? AND session_state = 'completed' AND status = 'active' AND exit_time IS NOT NULL "
                   + ") AS History "
                   + "ORDER BY txn_time DESC";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, licensePlate);
            st.setString(2, licensePlate);
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
    public CardSearchResultDTO findCardByRfid(String rfid) {
        String sql = "SELECT pc.card_id, pc.card_state, pc.status AS card_sys_status, "
                   + "s.subscription_id, s.license_plate AS registered_plate, s.end_date, "
                   + "c.first_name, c.last_name "
                   + "FROM ParkingCards pc "
                   + "LEFT JOIN Subscriptions s ON pc.card_id = s.card_id AND s.sub_state = 'active' AND s.status = 'active' "
                   + "LEFT JOIN Customers c ON s.customer_id = c.customer_id "
                   + "WHERE pc.card_id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, rfid);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                CardSearchResultDTO dto = new CardSearchResultDTO();
                dto.setRfid(rs.getString("card_id"));
                
                // Logic Trạng thái thẻ
                String sysStatus = rs.getString("card_sys_status");
                if ("inactive".equalsIgnoreCase(sysStatus)) {
                    dto.setStatus("LOCKED");
                } else {
                    dto.setStatus("ACTIVE");
                }

                // Logic Phân loại thẻ & Lấy thông tin chủ xe
                if (rs.getObject("subscription_id") != null) {
                    dto.setCardType("Vé tháng");
                    dto.setRegisteredPlate(rs.getString("registered_plate"));
                    dto.setOwnerName(rs.getString("last_name") + " " + rs.getString("first_name"));
                    if (rs.getTimestamp("end_date") != null) {
                        dto.setExpiryDate(rs.getTimestamp("end_date").toLocalDateTime().format(dateFormatter));
                    }
                } else {
                    dto.setCardType("Vé lượt");
                    // Các trường ownerName, plate, expiryDate để null
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
    public List<TransactionHistoryDTO> getCardTransactions(String rfid) {
        List<TransactionHistoryDTO> list = new ArrayList<>();
        String sql = "SELECT TOP 10 * FROM ( "
                   + "  SELECT entry_time AS txn_time, 'IN' AS action_type, license_plate FROM ParkingSessions "
                   + "  WHERE card_id = ? AND status = 'active' AND entry_time IS NOT NULL "
                   + "  UNION ALL "
                   + "  SELECT exit_time AS txn_time, 'OUT' AS action_type, license_plate FROM ParkingSessions "
                   + "  WHERE card_id = ? AND session_state = 'completed' AND status = 'active' AND exit_time IS NOT NULL "
                   + ") AS History "
                   + "ORDER BY txn_time DESC";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, rfid);
            st.setString(2, rfid);
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