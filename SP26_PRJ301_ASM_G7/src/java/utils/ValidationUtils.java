package utils;

/**
 * Lớp tiện ích (Utility class) hỗ trợ việc xác thực (validate) và ép kiểu dữ
 * liệu đầu vào. Lớp này được thiết kế theo tư duy Fail-Fast: Ném ra
 * {@link IllegalArgumentException} ngay lập tức nếu dữ liệu không hợp lệ, giúp
 * bảo vệ các lớp logic nghiệp vụ (Service/Model) khỏi dữ liệu "rác".
 */
public class ValidationUtils {

    // ==========================================
    // 1. KIỂM TRA CHUỖI (STRING)
    // ==========================================
    /**
     * Kiểm tra chuỗi đầu vào không được null và không được rỗng (sau khi đã
     * loại bỏ khoảng trắng).
     *
     * @param value Chuỗi cần kiểm tra.
     * @param errorMessage Thông báo lỗi cơ bản sẽ được ném ra nếu xác thực thất
     * bại.
     * @return Chuỗi đã được loại bỏ khoảng trắng thừa ở hai đầu (trimmed
     * string).
     * @throws IllegalArgumentException Nếu chuỗi bị null hoặc chỉ chứa khoảng
     * trắng.
     */
    public static String requireNonEmpty(String value, String errorMessage) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMessage);
        }
        return value.trim();
    }

    /**
     * Kiểm tra độ dài của chuỗi đầu vào xem có nằm trong khoảng cho phép hay
     * không. Phù hợp để kiểm tra Mật khẩu, CCCD, Số điện thoại...
     *
     * @param value Chuỗi cần kiểm tra.
     * @param minLength Độ dài tối thiểu cho phép (bao gồm cả giá trị này).
     * @param maxLength Độ dài tối đa cho phép (bao gồm cả giá trị này).
     * @param errorMessage Thông báo lỗi cơ bản sẽ được ném ra nếu xác thực thất
     * bại.
     * @return Chuỗi đã được loại bỏ khoảng trắng thừa ở hai đầu.
     * @throws IllegalArgumentException Nếu chuỗi bị null, rỗng, hoặc độ dài nằm
     * ngoài khoảng [minLength, maxLength].
     */
    public static String requireMinLength(String value, int minLength, int maxLength, String errorMessage) {
        String cleanValue = requireNonEmpty(value, errorMessage);
        if (cleanValue.length() < minLength || cleanValue.length() > maxLength) {
            throw new IllegalArgumentException(errorMessage + " (Yêu cầu từ " + minLength + " đến " + maxLength + " ký tự)");
        }
        return cleanValue;
    }

    // ==========================================
    // 2. KIỂM TRA SỐ (INT, DOUBLE)
    // ==========================================
    /**
     * Kiểm tra và ép kiểu chuỗi đầu vào sang số nguyên (int).
     *
     * @param value Chuỗi cần ép kiểu sang số nguyên.
     * @param errorMessage Thông báo lỗi cơ bản sẽ được ném ra nếu xác thực thất
     * bại.
     * @return Giá trị số nguyên (int) đã được ép kiểu thành công.
     * @throws IllegalArgumentException Nếu chuỗi bị rỗng hoặc chứa ký tự không
     * phải là số hợp lệ.
     */
    public static int requireValidInt(String value, String errorMessage) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMessage + " (Không được để trống)");
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(errorMessage + " (Vui lòng nhập số nguyên hợp lệ)");
        }
    }

    /**
     * Kiểm tra và ép kiểu chuỗi đầu vào sang số thực (double). Thường dùng để
     * kiểm tra các trường như Giá tiền, Diện tích, Cân nặng...
     *
     * @param value Chuỗi cần ép kiểu sang số thực.
     * @param errorMessage Thông báo lỗi cơ bản sẽ được ném ra nếu xác thực thất
     * bại.
     * @return Giá trị số thực (double) đã được ép kiểu thành công.
     * @throws IllegalArgumentException Nếu chuỗi bị rỗng hoặc chứa ký tự không
     * thể định dạng thành số thực.
     */
    public static double requireValidDouble(String value, String errorMessage) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMessage + " (Không được để trống)");
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(errorMessage + " (Vui lòng nhập số hợp lệ)");
        }
    }

    /**
     * Kiểm tra chuỗi đầu vào phải là số nguyên (int) hợp lệ VÀ phải lớn hơn một
     * giá trị tối thiểu (min). Thường dùng để kiểm tra logic nghiệp vụ như: ID
     * phải > 0, Giá tiền phải > 0...
     *
     * @param value Chuỗi cần kiểm tra và ép kiểu.
     * @param min Giá trị tối thiểu mà số nguyên này phải vượt qua (so sánh lớn
     * hơn nghiêm ngặt: >).
     * @param max Giá trị tôí đa mà số nguyên này có thể đạt được (so sánh nhỏ
     * hơn nghiêm ngặt: <).
     * @param errorMessage Thông báo lỗi cơ bản sẽ được ném ra nếu xác thực thất
     * bại.
     * @return Giá trị số nguyên hợp lệ và lớn hơn {@code min}.
     * @throws IllegalArgumentException Nếu chuỗi không phải số nguyên hợp lệ,
     * hoặc nhỏ hơn/bằng giá trị {@code min}.
     */
    public static int requireIntGreaterThan(String value, int min, int max, String errorMessage) {
        int parsedValue = requireValidInt(value, errorMessage);
        if (parsedValue < min || parsedValue > max) {
            throw new IllegalArgumentException(errorMessage + " (Phải lớn hơn " + min + ")");
        }
        return parsedValue;
    }

    // ==========================================
    // 3. KIỂM TRA ENUM (RẤT QUAN TRỌNG)
    // ==========================================
    /**
     * Kiểm tra chuỗi đầu vào có khớp với một hằng số đã định nghĩa trong một
     * Enum hay không. Quá trình kiểm tra không phân biệt chữ hoa, chữ thường
     * (tự động chuyển sang UpperCase).
     *
     * @param <T> Kiểu của Enum.
     * @param enumClass Đối tượng Class của Enum cần đối chiếu (ví dụ:
     * {@code ParkingSite.Status.class}).
     * @param value Chuỗi giá trị cần ánh xạ sang Enum.
     * @param errorMessage Thông báo lỗi cơ bản sẽ được ném ra nếu xác thực thất
     * bại.
     * @return Hằng số Enum tương ứng với chuỗi đầu vào.
     * @throws IllegalArgumentException Nếu chuỗi rỗng hoặc không tồn tại hằng
     * số Enum nào khớp với chuỗi đó.
     */
    public static <T extends Enum<T>> T requireValidEnum(Class<T> enumClass, String value, String errorMessage) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMessage + " (Vui lòng chọn một giá trị)");
        }
        try {
            // Tự động tìm giá trị Enum tương ứng với chuỗi (đã bỏ qua khoảng trắng và chuyển thành IN HOA)
            return Enum.valueOf(enumClass, value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(errorMessage + " (Giá trị lựa chọn không hợp lệ)");
        }
    }
}
