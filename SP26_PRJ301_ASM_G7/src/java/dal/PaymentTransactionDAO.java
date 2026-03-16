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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.dto.AdminTransactionDTO;
import model.dto.AdminTransactionHistoryDTO;
import model.dto.PaymentHistoryDTO;
import model.dto.TransactionHistoryDTO;

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
        Long[] weeklyData = {0L, 0L, 0L, 0L, 0L, 0L, 0L};

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

    /**
     * Lấy danh sách giao dịch VÉ THÁNG kèm bộ lọc. (Chỉ lấy các
     * PaymentTransactions có subscription_id != NULL)
     */
    public List<AdminTransactionDTO> getTransactionsWithFilter(String search, String status, String type) {
        List<AdminTransactionDTO> list = new ArrayList<>();
        List<Object> parameters = new ArrayList<>();

        // Câu SQL sử dụng chính xác các bảng: PaymentTransactions, Subscriptions, Customers, VehicleTypes
        StringBuilder sql = new StringBuilder("""
            SELECT 
                CONCAT('TRX-', YEAR(pt.payment_date), '-', FORMAT(pt.transaction_id, '000')) AS transaction_code,
                CONCAT(c.last_name, ' ', c.first_name) AS customer_name,
                s.license_plate,
                vt.name AS vehicle_type,
                s.start_date,
                s.end_date,
                pt.total_amount,
                pt.payment_status AS status
            FROM PaymentTransactions pt
            JOIN Subscriptions s ON pt.subscription_id = s.subscription_id
            JOIN Customers c ON s.customer_id = c.customer_id
            JOIN VehicleTypes vt ON s.vehicle_type_id = vt.vehicle_type_id
            WHERE pt.subscription_id IS NOT NULL 
              AND pt.status = 'active'
        """);

        // 1. Bộ lọc tìm kiếm (Tìm theo Tên khách hàng hoặc Biển số xe)
        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (CONCAT(c.last_name, ' ', c.first_name) LIKE ? OR s.license_plate LIKE ?) ");
            String searchPattern = "%" + search.trim() + "%";
            parameters.add(searchPattern);
            parameters.add(searchPattern);
        }

        // 2. Bộ lọc trạng thái thanh toán (accepted, completed, failed)
        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND pt.payment_status = ? ");
            parameters.add(status);
        }

        // 3. Bộ lọc loại xe (car, motorbike)
        if (type != null && !type.trim().isEmpty()) {
            sql.append(" AND vt.name = ? ");
            parameters.add(type);
        }

        // Sắp xếp giao dịch mới nhất lên đầu
        sql.append(" ORDER BY pt.payment_date DESC ");

        try {
            PreparedStatement ps = connection.prepareStatement(sql.toString());

            for (int i = 0; i < parameters.size(); i++) {
                ps.setObject(i + 1, parameters.get(i));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // Sử dụng DTO đã tạo ở bước trước
                AdminTransactionDTO dto = new AdminTransactionDTO(
                        rs.getString("transaction_code"),
                        rs.getString("customer_name"),
                        rs.getString("license_plate"),
                        rs.getString("vehicle_type"),
                        rs.getDate("start_date"),
                        rs.getDate("end_date"),
                        rs.getLong("total_amount"),
                        rs.getString("status")
                );
                list.add(dto);
            }
        } catch (Exception e) {
            System.out.println("Error TransactionDAO.getTransactionsWithFilter: " + e.getMessage());
        }

        return list;
    }

    public List<AdminTransactionHistoryDTO> getAllTransactionHistory(String search, String serviceType, String status) {
        List<AdminTransactionHistoryDTO> list = new ArrayList<>();
        List<Object> parameters = new ArrayList<>();

        // Sử dụng LEFT JOIN để lấy thông tin từ cả 3 luồng: Subscriptions, Bookings, ParkingSessions
        StringBuilder sql = new StringBuilder("""
            SELECT 
                CONCAT('#TX-', FORMAT(pt.transaction_id, '00000')) AS transaction_code,
                pt.payment_date,
                pt.total_amount,
                pt.payment_status,
                
                -- Xác định Loại dịch vụ gốc
                CASE 
                    WHEN pt.subscription_id IS NOT NULL THEN N'Vé tháng'
                    WHEN pt.booking_id IS NOT NULL THEN N'Đặt trước'
                    WHEN pt.session_id IS NOT NULL THEN N'Gửi theo giờ'
                    ELSE N'Khác'
                END AS base_service_type,
                
                -- Lấy tên Khách hàng (Nếu vãng lai thì để Khách vãng lai)
                COALESCE(
                    NULLIF(CONCAT(c_sub.last_name, ' ', c_sub.first_name), ' '), 
                    NULLIF(CONCAT(c_book.last_name, ' ', c_book.first_name), ' '), 
                    N'Khách vãng lai'
                ) AS customer_name,
                
                -- Lấy loại xe (car/motorbike)
                COALESCE(v_sub.name, v_book.name, v_sess.name) AS vehicle_type_name
                
            FROM PaymentTransactions pt
            LEFT JOIN Subscriptions s ON pt.subscription_id = s.subscription_id
            LEFT JOIN Customers c_sub ON s.customer_id = c_sub.customer_id
            LEFT JOIN VehicleTypes v_sub ON s.vehicle_type_id = v_sub.vehicle_type_id
            
            LEFT JOIN Bookings b ON pt.booking_id = b.booking_id
            LEFT JOIN Customers c_book ON b.customer_id = c_book.customer_id
            LEFT JOIN VehicleTypes v_book ON b.vehicle_type_id = v_book.vehicle_type_id
            
            LEFT JOIN ParkingSessions ps ON pt.session_id = ps.session_id
            LEFT JOIN VehicleTypes v_sess ON ps.vehicle_type_id = v_sess.vehicle_type_id
            
            WHERE pt.status = 'active'
        """);

        // Bộ lọc Tìm kiếm
        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (CONCAT(c_sub.last_name, ' ', c_sub.first_name) LIKE ? OR CONCAT('#TX-', FORMAT(pt.transaction_id, '00000')) LIKE ?) ");
            String searchPattern = "%" + search.trim() + "%";
            parameters.add(searchPattern);
            parameters.add(searchPattern);
        }

        // Bộ lọc Trạng thái
        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND pt.payment_status = ? ");
            parameters.add(status);
        }

        // Sắp xếp
        sql.append(" ORDER BY pt.payment_date DESC ");

        try {
            PreparedStatement ps = connection.prepareStatement(sql.toString());
            for (int i = 0; i < parameters.size(); i++) {
                ps.setObject(i + 1, parameters.get(i));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // Xử lý chuỗi Tên Dịch Vụ: "Vé tháng (Ô tô)"
                String baseType = rs.getString("base_service_type");
                String vType = rs.getString("vehicle_type_name");
                String vehicleNameVN = "car".equals(vType) ? "Ô tô" : ("motorbike".equals(vType) ? "Xe máy" : "");
                String fullServiceType = baseType + (vehicleNameVN.isEmpty() ? "" : " (" + vehicleNameVN + ")");

                // Random Phương thức thanh toán (Vì DB chưa có cột này)
                String[] methods = {"Tiền mặt", "Momo", "VNPay"};
                String randomMethod = methods[rs.getInt("total_amount") % 3];

                AdminTransactionHistoryDTO dto = new AdminTransactionHistoryDTO(
                        rs.getString("transaction_code"),
                        rs.getString("customer_name"),
                        fullServiceType,
                        rs.getLong("total_amount"),
                        randomMethod,
                        rs.getTimestamp("payment_date"),
                        rs.getString("payment_status")
                );

                // Áp dụng bộ lọc Loại dịch vụ (Lọc bằng Java vì câu SQL JOIN phức tạp)
                if (serviceType == null || serviceType.isEmpty()
                        || ("subscription".equals(serviceType) && "Vé tháng".equals(baseType))
                        || ("casual".equals(serviceType) && "Gửi theo giờ".equals(baseType))) {
                    list.add(dto);
                }
            }
        } catch (Exception e) {
            System.out.println("Error TransactionHistoryDAO.getAllTransactionHistory: " + e.getMessage());
        }
        return list;
    }

    /**
     * Dành riêng cho Manager: Lấy danh sách giao dịch vé tháng theo bãi xe
     */
    public List<AdminTransactionDTO> getSubscriptionsBySiteId(int siteId, String search, String status, String type) {
        List<AdminTransactionDTO> list = new java.util.ArrayList<>();
        List<Object> params = new java.util.ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT "
                + "   CONCAT('TRX-', FORMAT(pt.transaction_id, '000000')) AS transactionCode, "
                + "   CONCAT(c.last_name, ' ', c.first_name) AS customerName, "
                + "   s.license_plate AS licensePlate, "
                + "   v.name AS vehicleType, "
                + "   s.start_date AS startDate, "
                + "   s.end_date AS endDate, "
                + "   pt.total_amount AS totalAmount, "
                + "   pt.payment_status AS status "
                + "FROM PaymentTransactions pt "
                + "JOIN Subscriptions s ON pt.subscription_id = s.subscription_id "
                + "JOIN Customers c ON s.customer_id = c.customer_id "
                + "JOIN VehicleTypes v ON s.vehicle_type_id = v.vehicle_type_id "
                + "JOIN ParkingCards pc ON s.card_id = pc.card_id "
                + "WHERE pc.site_id = ? AND pt.status = 'active' AND s.status = 'active' "
        );
        params.add(siteId);

        // Lọc theo tìm kiếm (Mã GD, Tên khách hàng, Biển số)
        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (CONCAT(c.last_name, ' ', c.first_name) LIKE ? OR s.license_plate LIKE ? OR CONCAT('TRX-', FORMAT(pt.transaction_id, '000000')) LIKE ?) ");
            String searchPattern = "%" + search.trim() + "%";
            params.add(searchPattern);
            params.add(searchPattern);
            params.add(searchPattern);
        }

        // Lọc theo trạng thái thanh toán
        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND pt.payment_status = ? ");
            params.add(status);
        }

        // Lọc theo loại xe
        if (type != null && !type.trim().isEmpty()) {
            sql.append(" AND v.name = ? ");
            params.add(type);
        }

        sql.append(" ORDER BY pt.payment_date DESC");

        try {
            PreparedStatement ps = connection.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            java.sql.ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // Đảm bảo khớp với constructor/setter của AdminTransactionDTO mà bạn đang dùng
                AdminTransactionDTO dto = new AdminTransactionDTO(
                        rs.getString("transactionCode"),
                        rs.getString("customerName"),
                        rs.getString("licensePlate"),
                        rs.getString("vehicleType"),
                        rs.getTimestamp("startDate"),
                        rs.getTimestamp("endDate"),
                        rs.getLong("totalAmount"),
                        rs.getString("status")
                );
                list.add(dto);
            }
        } catch (Exception e) {
            System.out.println("Error getSubscriptionsBySiteId: " + e.getMessage());
        }
        return list;
    }

    /**
     * Dành riêng cho Manager: Lấy lịch sử giao dịch theo bãi xe
     */
    public List<PaymentHistoryDTO> getTransactionHistoryBySiteId(int siteId, String search, String serviceType, String status) {
        List<PaymentHistoryDTO> list = new java.util.ArrayList<>();
        List<Object> parameters = new java.util.ArrayList<>();

        StringBuilder sql = new StringBuilder("""
            SELECT 
                CONCAT('#TX-', FORMAT(pt.transaction_id, '00000')) AS transaction_code,
                pt.payment_date,
                pt.total_amount,
                pt.payment_status,
                
                -- Xác định Loại dịch vụ gốc
                CASE 
                    WHEN pt.subscription_id IS NOT NULL THEN N'Vé tháng'
                    WHEN pt.booking_id IS NOT NULL THEN N'Đặt trước'
                    WHEN pt.session_id IS NOT NULL THEN N'Gửi theo giờ'
                    ELSE N'Khác'
                END AS base_service_type,
                
                -- Lấy tên Khách hàng (Nếu vãng lai thì để Khách vãng lai)
                COALESCE(
                    NULLIF(CONCAT(c_sub.last_name, ' ', c_sub.first_name), ' '), 
                    NULLIF(CONCAT(c_book.last_name, ' ', c_book.first_name), ' '), 
                    N'Khách vãng lai'
                ) AS customer_name,
                
                -- Lấy loại xe (car/motorbike)
                COALESCE(v_sub.name, v_book.name, v_sess.name) AS vehicle_type_name
                
            FROM PaymentTransactions pt
            
            -- Luồng Subscriptions
            LEFT JOIN Subscriptions s ON pt.subscription_id = s.subscription_id
            LEFT JOIN Customers c_sub ON s.customer_id = c_sub.customer_id
            LEFT JOIN VehicleTypes v_sub ON s.vehicle_type_id = v_sub.vehicle_type_id
            LEFT JOIN ParkingCards pc_sub ON s.card_id = pc_sub.card_id
            
            -- Luồng Bookings
            LEFT JOIN Bookings b ON pt.booking_id = b.booking_id
            LEFT JOIN Customers c_book ON b.customer_id = c_book.customer_id
            LEFT JOIN VehicleTypes v_book ON b.vehicle_type_id = v_book.vehicle_type_id
            LEFT JOIN ParkingCards pc_book ON b.card_id = pc_book.card_id
            
            -- Luồng ParkingSessions
            LEFT JOIN ParkingSessions ps ON pt.session_id = ps.session_id
            LEFT JOIN VehicleTypes v_sess ON ps.vehicle_type_id = v_sess.vehicle_type_id
            LEFT JOIN ParkingCards pc_sess ON ps.card_id = pc_sess.card_id
            
            WHERE pt.status = 'active'
              -- ĐIỀU KIỆN QUAN TRỌNG: CHỈ LẤY GIAO DỊCH CỦA SITE NÀY
              AND (pc_sub.site_id = ? OR pc_book.site_id = ? OR pc_sess.site_id = ?)
        """);

        parameters.add(siteId);
        parameters.add(siteId);
        parameters.add(siteId);

        // Bộ lọc Tìm kiếm
        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (CONCAT(c_sub.last_name, ' ', c_sub.first_name) LIKE ? OR CONCAT('#TX-', FORMAT(pt.transaction_id, '00000')) LIKE ?) ");
            String searchPattern = "%" + search.trim() + "%";
            parameters.add(searchPattern);
            parameters.add(searchPattern);
        }

        // Bộ lọc Trạng thái
        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND pt.payment_status = ? ");
            parameters.add(status);
        }

        sql.append(" ORDER BY pt.payment_date DESC ");

        try {
            java.sql.PreparedStatement ps = connection.prepareStatement(sql.toString());
            for (int i = 0; i < parameters.size(); i++) {
                ps.setObject(i + 1, parameters.get(i));
            }

            java.sql.ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String baseType = rs.getString("base_service_type");
                String vType = rs.getString("vehicle_type_name");
                String vehicleNameVN = "car".equals(vType) ? "Ô tô" : ("motorbike".equals(vType) ? "Xe máy" : "");
                String fullServiceType = baseType + (vehicleNameVN.isEmpty() ? "" : " (" + vehicleNameVN + ")");

                // Mặc định phương thức thanh toán
                String[] methods = {"Tiền mặt", "Momo", "VNPay"};
                String randomMethod = methods[rs.getInt("total_amount") % 3];

                PaymentHistoryDTO dto = new PaymentHistoryDTO(
                        rs.getString("transaction_code"),
                        rs.getString("customer_name"),
                        fullServiceType,
                        rs.getLong("total_amount"),
                        randomMethod,
                        rs.getTimestamp("payment_date"),
                        rs.getString("payment_status")
                );

                // Lọc theo Loại dịch vụ (Lọc bằng Java cho dễ)
                if (serviceType == null || serviceType.isEmpty()
                        || ("subscription".equals(serviceType) && "Vé tháng".equals(baseType))
                        || ("casual".equals(serviceType) && "Gửi theo giờ".equals(baseType))) {
                    list.add(dto);
                }
            }
        } catch (Exception e) {
            System.out.println("Error getTransactionHistoryBySiteId: " + e.getMessage());
        }
        return list;
    }
}
