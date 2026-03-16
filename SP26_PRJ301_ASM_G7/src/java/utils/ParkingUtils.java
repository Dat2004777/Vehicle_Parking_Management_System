package utils;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDateTime;

public class ParkingUtils {

    // CHỈ nhận đơn giá đã lấy từ DAO
    public static long calculateSubscriptionPrice(long basePrice) {
        return basePrice; // Có thể thêm logic thuế/phí dịch vụ tại đây nếu cần
    }

    public static long calculateBookingPrice(LocalDateTime timeIn, LocalDateTime timeOut, long hourlyBasePrice) {
        return calculateHourlyFee(timeIn, timeOut, hourlyBasePrice);
    }

    public static long calculateSessionPrice(String sessionType, LocalDateTime timeIn, LocalDateTime timeOut, 
                                             LocalDateTime expectedTimeOut, long hourlyBasePrice) {
        if (!"casual".equalsIgnoreCase(sessionType)) {
            if (expectedTimeOut != null && timeOut.isAfter(expectedTimeOut)) {
                return calculateHourlyFee(expectedTimeOut, timeOut, hourlyBasePrice);
            }
            return 0;
        } else {
            return calculateHourlyFee(timeIn, timeOut, hourlyBasePrice);
        }
    }

    public static long calculateHourlyFee(LocalDateTime start, LocalDateTime end, long basePricePerHour) {
        long hours = calculateBillableHours(start, end);
        return hours * basePricePerHour;
    }

    public static long calculateBillableHours(LocalDateTime start, LocalDateTime end) {
        final int GRACE_PERIOD = 5;
        if (start == null || end == null) return 0;
        
        // Sử dụng $Duration$ để tính toán khoảng cách thời gian
        Duration d = Duration.between(start, end).minusMinutes(GRACE_PERIOD);
        if (d.isNegative()) return 0;

        return d.toHours(); 
    }

    public static String formatActualDuration(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) return "0 phút";
        Duration d = Duration.between(start, end);
        long hours = d.toHours();
        long minutes = d.toMinutesPart(); 

        StringBuilder sb = new StringBuilder();
        if (hours > 0) sb.append(hours).append(" giờ ");
        sb.append(minutes).append(" phút");
        return sb.toString();
    }

    public static String formatCurrency(long amount) {
        return new DecimalFormat("#,###").format(amount) + " VNĐ";
    }
}