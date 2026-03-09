/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.ParkingCard;
import utils.RandomCardId;

/**
 *
 * @author ADMIN
 */
public class CardDAO extends DBContext {
    // 1. Lấy tất cả thẻ (Get All)

    public List<ParkingCard> getAll() {
        List<ParkingCard> list = new ArrayList<>();
        String sql = "SELECT * FROM ParkingCards";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRowToCard(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Tìm thẻ theo ID
    public ParkingCard getById(String id) {
        String sql = "SELECT * FROM ParkingCards WHERE card_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRowToCard(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 3. Thêm thẻ mới (Nhập kho thẻ)
    public void add(ParkingCard card) {
        String sql = "INSERT INTO ParkingCards (card_id, site_id, status) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, card.getCardId());
            ps.setInt(2, card.getSiteId()); // Đã sửa thành setInt
            ps.setString(3, card.getState().name().toLowerCase());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Add Card: " + e.getMessage());
        }
    }

    // 4. Cập nhật thông tin thẻ
    public void update(ParkingCard card) {
        String sql = "UPDATE ParkingCards SET site_id = ?, status = ? WHERE card_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, card.getSiteId()); // Đã sửa thành setInt
            ps.setString(2, card.getState().name().toLowerCase());
            ps.setString(3, card.getCardId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 5. Xóa thẻ (Hủy thẻ hỏng)
    public void delete(String id) {
        String sql = "DELETE FROM ParkingCards WHERE card_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- CÁC METHOD NGHIỆP VỤ QUAN TRỌNG ---
    // 6. Lấy danh sách thẻ của một bãi xe cụ thể
    public List<ParkingCard> getBySite(int siteId) { // Đã sửa tham số thành int
        List<ParkingCard> list = new ArrayList<>();
        String sql = "SELECT * FROM ParkingCards WHERE site_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, siteId); // Đã sửa thành setInt
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRowToCard(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 7. [QUAN TRỌNG] Lấy 1 thẻ trống bất kỳ tại bãi xe để cấp cho khách vào
    public ParkingCard getAvailableCardAtSite(int siteId) { // Tham số đã là int từ trước
        // SQL Server dùng TOP 1, MySQL dùng LIMIT 1
        String sql = "SELECT TOP 1 * FROM ParkingCards WHERE site_id = ? AND card_state = 'available' AND status = 'active'"; // Đã
                                                                                                                              // sửa
                                                                                                                              // lại
                                                                                                                              // card_state
                                                                                                                              // thành
                                                                                                                              // status
                                                                                                                              // cho
                                                                                                                              // khớp
                                                                                                                              // với
                                                                                                                              // Enum
                                                                                                                              // của
                                                                                                                              // bạn
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, siteId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRowToCard(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Hết thẻ
    }

    // 8. Đếm số lượng thẻ trống tại bãi (Để hiển thị Dashboard: "Còn 50 slot")
    public int countAvailableCards(int siteId) { // Đã sửa tham số thành int
        String sql = "SELECT COUNT(*) FROM ParkingCards WHERE site_id = ? AND status = 'AVAILABLE'";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, siteId); // Đã sửa thành setInt
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 9. Cập nhật nhanh trạng thái thẻ (Dùng khi Check-in/Check-out)
    public void updateState(String cardId, ParkingCard.State newState) {
        String sql = "UPDATE ParkingCards SET card_state = ? WHERE card_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, newState.name().toLowerCase());
            ps.setString(2, cardId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update Card State
    public boolean updateCard(int siteId, String cardId, String state) {

        String sql = """
                UPDATE ParkingCards
                SET card_state = ?
                WHERE site_id = ?
                AND card_id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, state);
            ps.setInt(2, siteId);
            ps.setString(3, cardId);

            int affectedRows = ps.executeUpdate();

            return affectedRows > 0;

        } catch (Exception e) {
            System.out.println("Lỗi update Card");
            e.printStackTrace();
            return false;
        }
    }

    // --- Helper Mapping ---
    private ParkingCard mapRowToCard(ResultSet rs) throws SQLException {
        String statusStr = rs.getString("card_state");
        ParkingCard.State status = ParkingCard.State.AVAILABLE; // Default
        try {
            if (statusStr != null) {
                status = ParkingCard.State.valueOf(statusStr.toUpperCase());
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return new ParkingCard(
                rs.getString("card_id"),
                rs.getInt("site_id"), // Đã sửa thành getInt
                status);

    }

    public void addMultipleCards(int siteId, int quantity) {
        String sql = """
                INSERT INTO ParkingCards(card_id, site_id) VALUES (?, ?)
                """;

        try {
            PreparedStatement ps = connection.prepareStatement(sql);

            for (int i = 0; i < quantity; i++) {
                String cardId = RandomCardId.generateCardId(siteId);
                ps.setString(1, cardId);
                ps.setInt(2, siteId);
                ps.executeUpdate();
            }

        } catch (Exception e) {
            System.out.println("Error CardDAO.addMultipleCards: " + e.getMessage());
        }

    }

    public void softDeleteAllCardBySiteId(int siteId) {
        String sql = """
                UPDATE ParkingCards SET status = 'inactive'
                WHERE site_id = ?
                """;

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, siteId);
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("Error CardDAO.softDeleteAllCardBySiteId: " + e.getMessage());
        }
    }
}
