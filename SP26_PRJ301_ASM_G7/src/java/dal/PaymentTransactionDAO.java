/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author dat20
 */
public class PaymentTransactionDAO extends DBContext {

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
                    SELECT SUM(pt.amount) as total_amount
                    FROM PaymentTransactions pt
                    LEFT JOIN Booking b ON pt.booking_id = b.booking_id
                    LEFT JOIN MonthlySubscription ms ON pt.subscription_id = ms.subscription_id
                    LEFT JOIN ParkingSession ps ON pt.session_id = ps.session_id
                    LEFT JOIN ParkingCard pc ON pc.card_id = COALESCE(b.card_id, ms.card_id, ps.card_id)
                    WHERE MONTH(pt.payment_date) = MONTH(GETDATE()) 
                        AND YEAR(pt.payment_date) = YEAR(GETDATE())
                        AND pc.site_id = ?
                    """;

        long totalAmount = 0;
        System.out.println(siteId);
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
        Long[] weeklyData = {0L, 0L, 0L, 0L, 0L, 0L, 0L};
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
                     """; // Câu query ở trên

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
            e.printStackTrace();
        }
        return Arrays.asList(weeklyData);
    }
}
