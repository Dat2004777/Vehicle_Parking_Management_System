/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import model.PaymentTransaction;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Admin
 */
public class PaymentTransactionDAO extends DBContext {

    public boolean add(PaymentTransaction txn) {
        String idColumn = "";

        // Switch trực tiếp trên Enum (Không sợ gõ sai String nữa)
        switch (txn.getTransactionType()) {
            case BOOKING:
                idColumn = "booking_id";
                break;
            case SUBSCRIPTION:
                idColumn = "subscription_id";
                break;
            case SESSION:
                idColumn = "session_id";
                break;
            default:
                return false; // Code an toàn: Nếu lọt qua đây thì tự động hủy
        }

        // Tạo câu SQL động
        String sql = "INSERT INTO PaymentTransactions (" + idColumn + ", total_amount, payment_date, payment_status) "
                + "VALUES (?, ?, GETDATE(), ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, txn.getTargetId());
            ps.setLong(2, txn.getTotalAmount());
            ps.setString(3, txn.getPaymentStatus().toLowerCase());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Lỗi PaymentTransactionDAO.add: " + e.getMessage());
            return false;
        }
    }

    public void updatePaymentStatus(PaymentTransaction txn, String state) {
        String target = "";

        switch (txn.getTransactionType()) {
            case BOOKING:
                target = "booking_id";
                break;
            case SUBSCRIPTION:
                target = "subscription_id";
                break;
        }
        String sql = "UPDATE PaymentTransactions SET payment_status = ? WHERE " + target + " = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, state);
            ps.setInt(2, txn.getTargetId());

            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Lỗi update bảng Payment");
            e.printStackTrace();
        }

    }

    public long getTotalAmount() {
        String sql = """
                SELECT SUM(p.total_amount) as total_amount
                FROM PaymentTransactions p
                """;

        long totalAmount = 0;

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                totalAmount = rs.getLong("total_amount");
            }

        } catch (Exception e) {
            System.out.println("Error PaymentTransactionDAO.getTotalAmount: " + e.getMessage());
        }

        return totalAmount;
    }

    public long getTotalAmountInCurrentMonth() {
        String sql = """
                SELECT SUM(p.total_amount) as total_amount
                FROM PaymentTransactions p
                WHERE MONTH(p.payment_date) = MONTH(GETDATE())
                   AND YEAR(p.payment_date) = YEAR(GETDATE())
                """;

        long totalAmount = 0;

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                totalAmount = rs.getLong("total_amount");
            }

        } catch (Exception e) {
            System.out.println("Error PaymentTransactionDAO.getTotalAmountInCurrentMonth: " + e.getMessage());
        }

        return totalAmount;
    }

    public long getTotalAmountInCurrentMonthById(int siteId) {
        String sql = """
                SELECT SUM(pt.total_amount) as total_amount
                FROM PaymentTransactions pt
                LEFT JOIN Bookings b ON pt.booking_id = b.booking_id
                LEFT JOIN Subscriptions ms ON pt.subscription_id = ms.subscription_id
                LEFT JOIN ParkingSessions ps ON pt.session_id = ps.session_id
                LEFT JOIN ParkingCards pc ON pc.card_id = COALESCE(b.card_id, ms.card_id, ps.card_id)
                WHERE MONTH(pt.payment_date) = MONTH(GETDATE())
                    AND YEAR(pt.payment_date) = YEAR(GETDATE())
                    AND pc.site_id = ?
                """;

        long totalAmount = 0;

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, siteId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                totalAmount = rs.getLong("total_amount");
            }

        } catch (Exception e) {
            System.out.println("Error PaymentTransactionDAO.getTotalAmountInCurrentMonthById: " + e.getMessage());
        }

        return totalAmount;
    }

    public List<Long> getWeeklyRevenue() {
        // Khởi tạo danh sách 7 phần tử với giá trị 0
        Long[] weeklyData = { 0L, 0L, 0L, 0L, 0L, 0L, 0L };
        String sql = """
                SELECT
                    -- Lấy ID của ngày trong tuần (2 = Thứ 2, ..., 7 = Thứ 7, 1 = Chủ nhật)
                    DATEPART(WEEKDAY, payment_date) as DayID,
                    SUM(total_amount) as DailyRevenue
                FROM PaymentTransactions
                WHERE payment_date >= DATEADD(DAY, 2 - DATEPART(WEEKDAY, GETDATE()), CAST(GETDATE() AS DATE)) -- Bắt đầu từ Thứ 2 tuần này
                  AND payment_date <  DATEADD(DAY, 9 - DATEPART(WEEKDAY, GETDATE()), CAST(GETDATE() AS DATE)) -- Kết thúc hết Chủ nhật tuần này
                GROUP BY DATEPART(WEEKDAY, payment_date)
                ORDER BY (DATEPART(WEEKDAY, payment_date) + 5) % 7; -- Sắp xếp để Thứ 2 luôn đứng đầu
                 """;

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int dayId = rs.getInt("DayID"); // 1=CN, 2=T2, 3=T3...
                long amount = rs.getLong("DailyRevenue");

                // Chuyển DayID của SQL sang index của mảng (T2 là index 0)
                int index = (dayId == 1) ? 6 : (dayId - 2);
                weeklyData[index] = amount;
            }
        } catch (Exception e) {
            System.out.println("Error PaymentTransactionDAO.getWeeklyRevenue: " + e.getMessage());
        }
        return Arrays.asList(weeklyData);
    }

    public List<Long> getWeeklyRevenueBySiteId(int siteId) {
        // Khởi tạo danh sách 7 phần tử với giá trị 0 (Từ T2 đến CN)
        Long[] weeklyData = { 0L, 0L, 0L, 0L, 0L, 0L, 0L };

        String sql = """
                SELECT
                    DATEPART(WEEKDAY, pt.payment_date) as DayID,
                    SUM(pt.total_amount) as DailyRevenue
                FROM PaymentTransactions pt

                -- 1. Tìm site_id nếu đây là giao dịch Vé Lượt (Session)
                LEFT JOIN ParkingSessions sess ON pt.session_id = sess.session_id
                LEFT JOIN ParkingCards card_sess ON sess.card_id = card_sess.card_id

                -- 2. Tìm site_id nếu đây là giao dịch Vé Tháng (Subscription)
                LEFT JOIN Subscriptions sub ON pt.subscription_id = sub.subscription_id
                LEFT JOIN ParkingCards card_sub ON sub.card_id = card_sub.card_id -- ĐÃ SỬA: Join với ParkingCards

                -- 3. Tìm site_id nếu đây là giao dịch Đặt chỗ online (Booking)
                LEFT JOIN Bookings bk ON pt.booking_id = bk.booking_id
                LEFT JOIN ParkingCards card_bk ON bk.card_id = card_bk.card_id

                WHERE pt.payment_date >= DATEADD(DAY, 2 - DATEPART(WEEKDAY, GETDATE()), CAST(GETDATE() AS DATE))
                  AND pt.payment_date <  DATEADD(DAY, 9 - DATEPART(WEEKDAY, GETDATE()), CAST(GETDATE() AS DATE))
                  AND pt.payment_status = 'completed' -- Chỉ tính các giao dịch đã thu tiền thành công

                  -- ĐÃ SỬA: COALESCE quét qua 3 nguồn thẻ để lấy ra site_id
                  AND COALESCE(card_sess.site_id, card_sub.site_id, card_bk.site_id) = ?

                GROUP BY DATEPART(WEEKDAY, pt.payment_date)
                ORDER BY (DATEPART(WEEKDAY, pt.payment_date) + 5) % 7;
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, siteId); // Truyền tham số siteId vào dấu ?

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int dayId = rs.getInt("DayID"); // 1=CN, 2=T2, 3=T3...
                    long amount = rs.getLong("DailyRevenue");

                    // Chuyển DayID của SQL sang index của mảng Java (T2 là index 0)
                    int index = (dayId == 1) ? 6 : (dayId - 2);
                    weeklyData[index] = amount;
                }
            }
        } catch (Exception e) {
            System.out.println("Error PaymentTransactionDAO.getWeeklyRevenueBySiteId: " + e.getMessage());
        }

        return Arrays.asList(weeklyData);
    }
}
