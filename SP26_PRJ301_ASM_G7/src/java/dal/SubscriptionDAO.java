/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;
import model.Subscription;

/**
 *
 * @author ADMIN
 */
public class SubscriptionDAO extends DBContext {
    // =========================================================================
    // 1. KIỂM TRA BIỂN SỐ ĐÃ CÓ VÉ CÒN HẠN CHƯA
    // =========================================================================
    public boolean hasActiveSubscriptionByPlate(String plate) {
        // Một vé được coi là "Đang hoạt động" nếu:
        // - Biển số khớp
        // - sub_state là 'active'
        // - status (xóa mềm) là 'active'
        // - Ngày hiện tại nằm trong khoảng start_date và end_date
        String sql = "SELECT COUNT(*) FROM Subscriptions "
                + "WHERE license_plate = ? AND sub_state = 'active' AND status = 'active' "
                + "AND GETDATE() BETWEEN start_date AND end_date";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, plate);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // =========================================================================
    // 2. KIỂM TRA MÃ THẺ ĐÃ ĐƯỢC GÁN CHO VÉ THÁNG NÀO CÒN HẠN CHƯA
    // =========================================================================
    public Subscription getActiveSubscriptionByCard(String cardId) {
        String sql = "SELECT * FROM Subscriptions "
                + "WHERE card_id = ? AND sub_state = 'active' AND status = 'active' "
                + "AND GETDATE() BETWEEN start_date AND end_date";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cardId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Subscription sub = new Subscription();
                    sub.setSubscriptionId(rs.getInt("subscription_id"));
                    sub.setCustomerId(rs.getInt("customer_id"));
                    sub.setCardId(rs.getString("card_id"));
                    sub.setLicensePlate(rs.getString("license_plate"));
                    sub.setVehicleTypeId(rs.getInt("vehicle_type_id"));

                    // Chuyển đổi từ SQL Timestamp sang Java LocalDateTime
                    Timestamp start = rs.getTimestamp("start_date");
                    if (start != null) {
                        sub.setStartDate(start.toLocalDateTime());
                    }

                    Timestamp end = rs.getTimestamp("end_date");
                    if (end != null) {
                        sub.setEndDate(end.toLocalDateTime());
                    }

                    sub.setSubState(rs.getString("sub_state"));
                    sub.setAppliedPrice(rs.getLong("applied_price"));

                    return sub;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // =========================================================================
    // 3. LẤY THÔNG TIN VÉ THEO ID (Dùng cho logic Gia hạn)
    // =========================================================================
    public Subscription getById(int subscriptionId) {
        String sql = "SELECT * FROM Subscriptions WHERE subscription_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, subscriptionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Subscription sub = new Subscription();
                    sub.setSubscriptionId(rs.getInt("subscription_id"));
                    sub.setCustomerId(rs.getInt("customer_id"));
                    sub.setCardId(rs.getString("card_id"));
                    sub.setLicensePlate(rs.getString("license_plate"));
                    sub.setVehicleTypeId(rs.getInt("vehicle_type_id"));

                    // Chuyển đổi từ SQL Timestamp sang Java LocalDateTime
                    Timestamp start = rs.getTimestamp("start_date");
                    if (start != null) {
                        sub.setStartDate(start.toLocalDateTime());
                    }

                    Timestamp end = rs.getTimestamp("end_date");
                    if (end != null) {
                        sub.setEndDate(end.toLocalDateTime());
                    }

                    sub.setSubState(rs.getString("sub_state"));
                    sub.setAppliedPrice(rs.getLong("applied_price"));

                    return sub;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Hàm lấy danh sách các biển số xe DUY NHẤT của 1 khách hàng
    public List<String> getDistinctPlatesByCustomerId(int customerId) {
        List<String> plates = new ArrayList<>();
        String sql = "SELECT DISTINCT license_plate FROM Subscriptions WHERE customer_id = ? AND status = 'active'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                plates.add(rs.getString("license_plate"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plates;
    }

    // Hàm lấy thông tin vé tháng mới nhất theo biển số
    public Subscription getLatestByPlate(String plate) {
        String sql = "SELECT TOP 1 * FROM Subscriptions "
                + "WHERE license_plate = ? AND sub_state = 'active' AND status = 'active' "
                + "ORDER BY end_date DESC";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, plate);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Subscription sub = new Subscription();
                sub.setSubscriptionId(rs.getInt("subscription_id"));
                sub.setVehicleTypeId(rs.getInt("vehicle_type_id"));
                sub.setEndDate(rs.getTimestamp("end_date").toLocalDateTime());
                sub.setSubState(rs.getString("sub_state")); // 'active' hoặc 'expired'
                return sub;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Thêm mới một bản ghi vé tháng (Subscription) vào Database
     *
     * @param sub Đối tượng Subscription chứa dữ liệu
     * @return true nếu insert thành công, false nếu thất bại
     */
    public boolean insertSubscription(Subscription sub) {
        String sql = "INSERT INTO [Subscriptions] "
                + "([customer_id], [card_id], [license_plate], [vehicle_type_id], "
                + "[start_date], [end_date], [sub_state], [applied_price]) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, sub.getCustomerId());
            ps.setString(2, sub.getCardId());
            ps.setString(3, sub.getLicensePlate());
            ps.setInt(4, sub.getVehicleTypeId());

            // Chuyển đổi LocalDateTime sang java.sql.Timestamp
            ps.setTimestamp(5, Timestamp.valueOf(sub.getStartDate()));
            ps.setTimestamp(6, Timestamp.valueOf(sub.getEndDate()));

            // Nếu object chưa set, mặc định vé mới tạo sẽ là 'ACTIVE'
            String subState = (sub.getSubState() != null) ? sub.getSubState() : "active";
            ps.setString(7, subState);

            // applied_price (Giá áp dụng)
            ps.setLong(8, sub.getAppliedPrice());

            // Thực thi câu lệnh
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            System.err.println("Lỗi khi insert Subscription: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Thêm mới vé tháng và trả về ID tự tăng (subscription_id)
     *
     * * @param sub Đối tượng Subscription chứa dữ liệu
     * 
     * @return ID của vé tháng vừa tạo, hoặc -1 nếu thất bại
     */
    public int insertAndReturnId(Subscription sub) {
        String sql = "INSERT INTO [Subscriptions] "
                + "([customer_id], [card_id], [license_plate], [vehicle_type_id], "
                + "[start_date], [end_date], [sub_state], [applied_price], [status]) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Sử dụng hằng số Statement.RETURN_GENERATED_KEYS để yêu cầu lấy ID về
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, sub.getCustomerId());
            ps.setString(2, sub.getCardId());
            ps.setString(3, sub.getLicensePlate());
            ps.setInt(4, sub.getVehicleTypeId());

            ps.setTimestamp(5, Timestamp.valueOf(sub.getStartDate()));
            ps.setTimestamp(6, Timestamp.valueOf(sub.getEndDate()));

            String subState = (sub.getSubState() != null) ? sub.getSubState() : "active";
            ps.setString(7, subState);

            ps.setLong(8, sub.getAppliedPrice());

            // Mặc định status dữ liệu là active (để dùng cho soft-delete nếu cần)
            ps.setString(9, "active");

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                // Lấy khay chứa các khóa (ID) vừa được sinh ra
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // Trả về ID đầu tiên (subscription_id)
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi tại SubscriptionDAO.insertAndReturnId: " + e.getMessage());
            e.printStackTrace();
        }

        return -1; // Trả về -1 nếu có lỗi xảy ra
    }

    public void updateSubscription(int subscriptionId) {
        String sql = """
                UPDATE Subscriptions
                SET status = 'inactive'
                WHERE subscription_id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, subscriptionId);

            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Loi update subscrition status");
            e.printStackTrace();
        }
    }

    public boolean checkSubscription(int siteId, String licensePlate) {
        String sql = """
                SELECT TOP 1 1
                FROM Subscriptions s
                JOIN ParkingCards c ON s.card_id = c.card_id
                WHERE c.site_id = ?
                AND s.license_plate = ?
                AND s.sub_state = 'active'
                AND s.status = 'active'
                AND GETDATE() BETWEEN s.start_date AND s.end_date
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, siteId);
            ps.setString(2, licensePlate);

            ResultSet rs = ps.executeQuery();

            return rs.next();
        } catch (Exception e) {
            System.out.println("Loi check Subscription");
            e.printStackTrace();
            return false;
        }

    }

    public int getTotalSubscriptionByCurrentMonth() {
        String sql = """
                SELECT COUNT(s.subscription_id) as total_subscription
                FROM Subscriptions s
                WHERE MONTH(s.start_date) = MONTH(GETDATE())
                	AND YEAR(s.start_date) = YEAR(GETDATE())
                """;

        int totalSubscription = 0;

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                totalSubscription = rs.getInt("total_subscription");
            }
        } catch (Exception e) {
            System.out.println("Error SubscriptionDAO.getTotalSubscriptionByCurrentMonth: " + e.getMessage());
        }

        return totalSubscription;
    }

    public int getTotalSubscriptionInCurrentMonthById(int siteId) {
        String sql = """
                SELECT COUNT(s.subscription_id) as total_subscription
                FROM Subscriptions s
                JOIN ParkingCards pc ON s.card_id = pc.card_id
                WHERE MONTH(s.start_date) = MONTH(GETDATE())
                      AND YEAR(s.start_date) = YEAR(GETDATE())
                   AND pc.site_id = ?
                 """;

        int totalSubscription = 0;

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, siteId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                totalSubscription = rs.getInt("total_subscription");
            }
        } catch (Exception e) {
            System.out.println("Error SubscriptionDAO.getTotalSubscriptionInCurrentMonthById: " + e.getMessage());
        }

        return totalSubscription;
    }
}
