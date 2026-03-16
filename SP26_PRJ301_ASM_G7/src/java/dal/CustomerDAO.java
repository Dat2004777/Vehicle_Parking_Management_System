/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import model.Customer;

/**
 *
 * @author ADMIN
 */
public class CustomerDAO extends DBContext {

    //Tạo 1 Customer object vào trong database
    public void insertCustomer(String firstname, String lastname, String phone, String email, long wallet, int accountID) {
        String sql
                = """
                INSERT INTO Customers(first_name,last_name,phone,email,wallet_amount,account_id)
                VALUES(?,?,?,?,?,?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, firstname);
            ps.setString(2, lastname);
            ps.setString(3, phone);
            ps.setString(4, email);
            ps.setLong(5, wallet);
            ps.setInt(6, accountID);

            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Lỗi thêm giá trị Customer");
        }
    }

    public Customer getCustomerProfile(int account_id) {
        String sql
                = """
                SELECT a.account_id, c.customer_id, c.first_name, c.last_name, c.phone, c.email, c.wallet_amount
                FROM Accounts a
                JOIN Customers c ON a.account_id = c.account_id
                WHERE a.account_id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, account_id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int customer_id = rs.getInt("customer_id");
                String firstname = rs.getString("first_name");
                String lastname = rs.getString("last_name");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                long wallet = rs.getLong("wallet_amount");
                Customer customer = new Customer(customer_id, firstname, lastname, phone, email, wallet, account_id);

                return customer;
            }
            return null;
        } catch (Exception e) {
            System.out.println("Lỗi lấy dữ liệu user");
            e.printStackTrace();
            return null;
        }
    }

    public boolean isEmailExist(String email) {
        String sql = "SELECT 1 FROM Customers WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            System.out.println("Lỗi lấy user-email");
            e.printStackTrace();
            return false;
        }
    }

    public Customer getByPhone(String phone) {
        String sql = "SELECT * FROM Customers WHERE phone = ? AND status = 'active'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Customer c = new Customer();
                c.setCustomerId(rs.getInt("customer_id"));
                // Gộp tên để dùng cho UI ParkEasy
                c.setFirstname(rs.getNString("first_name"));
                c.setLastname(rs.getNString("last_name"));
                c.setEmail(rs.getString("email"));
                return c;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Tạo mới một khách hàng và trả về ID tự tăng (Identity) vừa được tạo
     *
     * @param customer Đối tượng Customer chứa data
     * @return customer_id vừa được tạo, hoặc ném ra Exception nếu lỗi
     */
    public int insertAndReturnId(Customer customer) throws Exception {
        // Cột customer_id là tự tăng nên KHÔNG đưa vào câu INSERT
        String sql = "INSERT INTO [Customers] "
                + "([first_name], [last_name], [phone], [email], [wallet_amount], [account_id]) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        // CHÚ Ý: Phải có Statement.RETURN_GENERATED_KEYS ở đây
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // 1 & 2. Tên và Họ
            ps.setString(1, customer.getFirstname());
            ps.setString(2, customer.getLastname());

            // 3. Số điện thoại (Bắt buộc)
            ps.setString(3, customer.getPhone());

            // 4. Email (Có thể null do form nhân viên không nhập)
            if (customer.getEmail() != null && !customer.getEmail().isEmpty()) {
                ps.setString(4, customer.getEmail());
            } else {
                ps.setNull(4, Types.VARCHAR); // Set NULL chuẩn SQL
            }

            // 5. Số dư ví mặc định là 0 đ cho khách mới tinh
            ps.setLong(5, 0);

            // 6. Account_ID (Khách offline chưa có tài khoản đăng nhập -> NULL)
            // (Giả sử bạn dùng Integer object cho account_id, nếu là int nguyên thủy thì check > 0)
            ps.setNull(6, Types.INTEGER);

            // Thực thi INSERT
            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new Exception("Thêm khách hàng thất bại, không có dòng nào được tạo.");
            }

            // Hứng cái ID vừa sinh ra
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Trả về giá trị của cột tự tăng
                } else {
                    throw new Exception("Thêm khách hàng thành công nhưng không lấy được ID.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Lỗi Database khi tạo khách hàng: " + e.getMessage());
        }
    }

    public boolean updateWalletAmount(int customerId, long newAmount) {
        String sql = "UPDATE Customers SET wallet_amount = ? WHERE customer_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, newAmount);
            ps.setInt(2, customerId);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateCustomerInfo(Customer customer, String firstname, String lastname, String phone) {
        String sql
                = """
                UPDATE Customers
                SET first_name = ?, last_name = ?, phone = ?
                WHERE customer_id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, firstname);
            ps.setString(2, lastname);
            ps.setString(3, phone);
            ps.setInt(4, customer.getCustomerId());

            int row = ps.executeUpdate();
            return row > 0;
        } catch (Exception e) {
            System.out.println("Lỗi cập nhật giá trị của Customer");
            e.printStackTrace();
            return false;
        }
    }

    public boolean addBalance(int customerId, long amount) {
        String sql = "UPDATE Customers SET wallet_amount = wallet_amount + ? WHERE customer_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, amount);
            ps.setInt(2, customerId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Lỗi nạp tiền: " + e.getMessage());
        }
        return false;
    }
}
