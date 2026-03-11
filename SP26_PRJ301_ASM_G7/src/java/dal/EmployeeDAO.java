package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Employee;
import model.ParkingSite;
import model.dto.AdminListEmployeeDTO;

public class EmployeeDAO extends DBContext {

    public List<Employee> getAllEmployee() {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM Employees e WHERE e.status = 'active'";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapRowToEmployee(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error EmployeeDAO.getAllEmployee: " + e.getMessage());
        }
        return list;
    }

    public List<AdminListEmployeeDTO> getAllEmployeeWithTheirSite() {
        List<AdminListEmployeeDTO> list = new ArrayList<>();
        String sql = """
                     SELECT 
                         e.employee_id, 
                         e.firstname, 
                         e.lastname, 
                         e.phone, 
                         ps.site_name, 
                         CASE 
                             -- Chỉ khi role gốc là 'staff' VÀ trùng ID quản lý bãi xe thì mới thành 'manager'
                             WHEN a.role = 'staff' AND e.employee_id = ps.manager_id THEN 'manager'
                             
                             -- Tất cả các trường hợp còn lại (như admin, hoặc staff bình thường) đều giữ nguyên role gốc
                             ELSE a.role 
                         END AS role
                     FROM Employees e 
                     LEFT JOIN ParkingSites ps 
                         ON ps.site_id = e.site_id
                     LEFT JOIN Accounts a
                         ON a.account_id = e.account_id
                     WHERE e.status = 'active';
                     """;

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int employeeId = rs.getInt("employee_id");
                String firstName = rs.getString("firstname");
                String lastName = rs.getString("lastname");
                String phone = rs.getString("phone");
                String siteName = rs.getString("site_name");
                String role = rs.getString("role");
                AdminListEmployeeDTO adminListEmployeeDTO = new AdminListEmployeeDTO(
                        employeeId,
                        firstName,
                        lastName,
                        phone,
                        siteName,
                        AdminListEmployeeDTO.Role.valueOf(role.toUpperCase().trim())
                );

                list.add(adminListEmployeeDTO);
            }
        } catch (SQLException e) {
            System.out.println("Error EmployeeDAO.getAllEmployeeWithTheirSite: " + e.getMessage());
        }
        return list;
    }

    public List<AdminListEmployeeDTO> employeeSearch(String employeeSearch) {
        List<AdminListEmployeeDTO> list = new ArrayList<>();
        String sql = """
                     SELECT 
                         e.employee_id, 
                         e.firstname, 
                         e.lastname, 
                         e.phone, 
                         ps.site_name, 
                         CASE 
                             -- Chỉ khi role gốc là 'staff' VÀ trùng ID quản lý bãi xe thì mới thành 'manager'
                             WHEN a.role = 'staff' AND e.employee_id = ps.manager_id THEN 'manager'
                             
                             -- Tất cả các trường hợp còn lại (như admin, hoặc staff bình thường) đều giữ nguyên role gốc
                             ELSE a.role 
                         END AS role
                     FROM Employees e 
                     LEFT JOIN ParkingSites ps 
                         ON ps.site_id = e.site_id
                     LEFT JOIN Accounts a
                         ON a.account_id = e.account_id
                     WHERE CONCAT(e.lastname, ' ', e.firstname) LIKE ? AND e.status = 'active';
                     """;

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, "%" + employeeSearch + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int employeeId = rs.getInt("employee_id");
                String firstName = rs.getString("firstname");
                String lastName = rs.getString("lastname");
                String phone = rs.getString("phone");
                String siteName = rs.getString("site_name");
                String role = rs.getString("role");
                AdminListEmployeeDTO adminListEmployeeDTO = new AdminListEmployeeDTO(
                        employeeId,
                        firstName,
                        lastName,
                        phone,
                        siteName,
                        AdminListEmployeeDTO.Role.valueOf(role.toUpperCase().trim())
                );

                list.add(adminListEmployeeDTO);
            }
        } catch (SQLException e) {
            System.out.println("Error EmployeeDAO.employeeSearch: " + e.getMessage());
        }
        return list;
    }

    public List<AdminListEmployeeDTO> employeeSearchByEmployeeId(int employeeSearch) {
        List<AdminListEmployeeDTO> list = new ArrayList<>();
        String sql = """
                     SELECT 
                         e.employee_id, 
                         e.firstname, 
                         e.lastname, 
                         e.phone, 
                         ps.site_name, 
                         CASE 
                             -- Chỉ khi role gốc là 'staff' VÀ trùng ID quản lý bãi xe thì mới thành 'manager'
                             WHEN a.role = 'staff' AND e.employee_id = ps.manager_id THEN 'manager'
                             
                             -- Tất cả các trường hợp còn lại (như admin, hoặc staff bình thường) đều giữ nguyên role gốc
                             ELSE a.role 
                         END AS role
                     FROM Employees e 
                     LEFT JOIN ParkingSites ps 
                         ON ps.site_id = e.site_id
                     LEFT JOIN Accounts a
                         ON a.account_id = e.account_id
                     WHERE e.employee_id = ? AND e.status = 'active';
                     """;

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, employeeSearch);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int employeeId = rs.getInt("employee_id");
                String firstName = rs.getString("firstname");
                String lastName = rs.getString("lastname");
                String phone = rs.getString("phone");
                String siteName = rs.getString("site_name");
                String role = rs.getString("role");
                AdminListEmployeeDTO adminListEmployeeDTO = new AdminListEmployeeDTO(
                        employeeId,
                        firstName,
                        lastName,
                        phone,
                        siteName,
                        AdminListEmployeeDTO.Role.valueOf(role.toUpperCase().trim())
                );

                list.add(adminListEmployeeDTO);
            }
        } catch (SQLException e) {
            System.out.println("Error EmployeeDAO.employeeSearchByEmployeeId: " + e.getMessage());
        }
        return list;
    }

    public List<Employee> getAllEmployeeForDetailSite(int siteId) {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM Employees e WHERE e.status = 'active' AND e.site_id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, siteId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapRowToEmployee(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error EmployeeDAO.getAllEmployee: " + e.getMessage());
        }
        return list;
    }

    public List<Employee> getAllEmployeeWithNullSiteId() {
        List<Employee> list = new ArrayList<>();
        String sql = """
                     SELECT * FROM Employees e 
                     WHERE e.status = 'active' AND e.site_id IS NULL
                     """;

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapRowToEmployee(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error EmployeeDAO.getAllEmployeeWithNullSiteId: " + e.getMessage());
        }
        return list;
    }

    public Employee getById(int id) {
        String sql = "SELECT * FROM Employees WHERE employee_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRowToEmployee(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error EmployeeDAO.getById: " + e.getMessage());
        }
        return null;
    }

    public void addEmployee(Employee emp) {
        String sql = "INSERT INTO Employees (account_id, firstname, lastname, phone, site_id) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, emp.getAccountId());
            ps.setString(2, emp.getFirstName());
            ps.setString(3, emp.getLastName());
            ps.setString(4, emp.getPhone());

            // Kiểm tra xem có null hay không
            if (emp.getSiteId() == 0) {
                // Nếu null thì báo cho SQL biết là lưu giá trị NULL kiểu số nguyên
                ps.setNull(5, java.sql.Types.INTEGER);
            } else {
                // Nếu có giá trị thì setInt bình thường
                ps.setInt(5, emp.getSiteId());
            }

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error EmployeeDAO.addEmployee: " + e.getMessage());
        }
    }
// Hàm kiểm tra số điện thoại đã tồn tại hay chưa

    public boolean isPhoneExist(String phone) {
        // Kiểm tra trong bảng Employee (hoặc bảng Accounts tùy thiết kế của bạn)
        String sql = "SELECT 1 FROM Employees WHERE phone = ? AND status = 'active'";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true; // Đã tồn tại
            }
        } catch (SQLException e) {
            System.out.println("Error EmployeeDAO.isPhoneExist: " + e.getMessage());
        }
        return false; // Chưa tồn tại, an toàn để dùng
    }

    public void update(Employee emp) {
        String sql = "UPDATE Employees SET account_id = ?, firstname = ?, lastname = ?, phone = ?, site_id = ? WHERE employee_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, emp.getAccountId());
            ps.setString(2, emp.getFirstName());
            ps.setString(3, emp.getLastName());
            ps.setString(4, emp.getPhone());
            ps.setInt(5, emp.getSiteId());
            ps.setInt(6, emp.getEmployeeId());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error EmployeeDAO.update: " + e.getMessage());
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM Employees WHERE employee_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error EmployeeDAO.delete: " + e.getMessage());
        }
    }

    public List<Employee> getBySiteId(int siteId) {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM Employees WHERE site_id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, siteId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapRowToEmployee(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error EmployeeDAO.getBySiteId: " + e.getMessage());
        }
        return list;
    }

    private Employee mapRowToEmployee(ResultSet rs) throws SQLException {
        return new Employee(
                rs.getInt("employee_id"),
                rs.getInt("account_id"),
                rs.getString("firstname"),
                rs.getString("lastname"),
                rs.getString("phone"),
                rs.getInt("site_id")
        );
    }

    public void setSiteIdForEmployee(int siteId, int employeeId) {
        String sql = """
                     UPDATE Employees SET site_id = ? WHERE employee_id = ?
                     """;

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, siteId);
            ps.setInt(2, employeeId);
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("Error EmployeeDAO.setSiteIdForEmployee: " + e.getMessage());
        }
    }

    public void updateSiteIdForNewEmployeeAndRemoveForOldEmployee(ParkingSite parkingSite, int newEmployeeId) {

        int oldManagerId = 0;
        String sqlGetOldManagerId = "SELECT manager_id FROM ParkingSites WHERE site_id = ?";
        String removeSiteIdForOldEmployee = "UPDATE Employees SET site_id = ? WHERE employee_id = ?";
        String setSiteIdForNewEmployee = "UPDATE Employees SET site_id = ? WHERE employee_id = ?";

        try {
            // tim managerId cu
            try (PreparedStatement psGet = connection.prepareStatement(sqlGetOldManagerId)) {
                psGet.setInt(1, parkingSite.getSiteId());
                ResultSet rs = psGet.executeQuery();
                if (rs.next()) {
                    oldManagerId = rs.getInt("manager_id");
                }
            }

            // xoa site_id cho emp cu
            if (oldManagerId != 0 && oldManagerId != newEmployeeId) {
                try (PreparedStatement psRel = connection.prepareStatement(removeSiteIdForOldEmployee)) {
                    psRel.setInt(1, parkingSite.getSiteId());
                    psRel.setInt(2, oldManagerId);
                    psRel.executeUpdate();
                }
            }

            // set site_id cho emp moi
            if (newEmployeeId != 0) {
                try (PreparedStatement psSet = connection.prepareStatement(setSiteIdForNewEmployee)) {
                    psSet.setInt(1, parkingSite.getSiteId());
                    psSet.setInt(2, newEmployeeId);
                    psSet.executeUpdate();
                }
            }

        } catch (Exception e) {
            System.out.println("Error EmployeeDAO.updateSiteIdForNewEmployeeAndRemoveForOldEmployee: " + e.getMessage());
        }
    }

    public int getEmployeeId(int accountId, String role) {
        String sql
                = """
                SELECT e.employee_id
                FROM Employees e 
                JOIN Accounts a ON e.account_id = a.account_id
                WHERE a.role = ? AND a.account_id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, role);
            ps.setInt(2, accountId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int employeeId = rs.getInt("employee_id");
                return employeeId;
            } else {
                return -1;
            }

        } catch (Exception e) {
            System.out.println("Lỗi: Không lấy được empId");
            e.printStackTrace();
            return -1;
        }
    }

    public Employee getEmployeeById(int employeeId) {
        String sql = """
                     SELECT employee_id, account_id, firstname, lastname, phone, site_id FROM Employees
                     WHERE employee_id = ? AND status = 'active'
                     """;
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, employeeId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Employee employee = new Employee(
                        rs.getInt("employee_id"),
                        rs.getInt("account_id"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("phone"),
                        rs.getInt("site_id"));

                return employee;
            }

        } catch (Exception e) {
            System.out.println("Error EmployeeDAO.getEmployeeById: " + e.getMessage());
        }
        return null;
    }

    public void softDeleteEmployeeById(int employeeId) {
        String sql = """
                     UPDATE Employees SET status = 'inactive' WHERE employee_id = ?
                     """;

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, employeeId);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error EmployeeDAO.softDeleteEmployeeById: " + e.getMessage());
        }
    }

    public int getEmployeeAcountId(int employeeId) {
        String sql = """
                     SELECT account_id FROM Employees WHERE employee_id = ?
                     """;

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, employeeId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("account_id");
            }
        } catch (Exception e) {
            System.out.println("Error EmployeeDAO.softDeleteEmployeeById: " + e.getMessage());
        }

        return 0;
    }

    public void updateEmployee(Employee employee) {
        String sql = "UPDATE Employees SET firstname = ?, lastname = ?, phone = ?, site_id = ? WHERE employee_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, employee.getFirstName());
            ps.setString(2, employee.getLastName());
            ps.setString(3, employee.getPhone());

            // XỬ LÝ DỊCH SỐ 0 THÀNH NULL SQL
            if (employee.getSiteId() == 0) {
                ps.setNull(4, java.sql.Types.INTEGER); // Gửi chữ NULL xuống DB
            } else {
                ps.setInt(4, employee.getSiteId()); // Gửi số ID bãi xe xuống DB
            }

            ps.setInt(5, employee.getEmployeeId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error EmployeeDAO.updateEmployee: " + e.getMessage());
        }
    }

    /**
     * Dành riêng cho Manager: Lấy danh sách nhân viên bảo vệ thuộc bãi xe của
     * họ
     */
    public List<AdminListEmployeeDTO> getEmployeesBySiteId(int siteId, String search) {
        List<AdminListEmployeeDTO> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT e.employee_id, e.firstname, e.lastname, e.phone, a.username, a.status "
                + "FROM Employees e "
                + "JOIN Accounts a ON e.account_id = a.account_id "
                + "WHERE e.site_id = ? AND a.role = 'staff' AND e.status = 'active'"
        );

        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (e.firstname LIKE ? OR e.lastname LIKE ? OR e.phone LIKE ? OR a.username LIKE ?) ");
        }

        try {
            PreparedStatement ps = connection.prepareStatement(sql.toString());
            ps.setInt(1, siteId);

            if (search != null && !search.trim().isEmpty()) {
                String pattern = "%" + search.trim() + "%";
                ps.setString(2, pattern);
                ps.setString(3, pattern);
                ps.setString(4, pattern);
                ps.setString(5, pattern);
            }

            java.sql.ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                AdminListEmployeeDTO dto = new AdminListEmployeeDTO();
                // NẾU TÊN HÀM SETTER CỦA BẠN KHÁC, HÃY ĐỔI LẠI CHO KHỚP VỚI AdminListEmployeeDTO CỦA BẠN NHÉ
                dto.setEmployeeId(rs.getInt("employee_id"));
                dto.setFirstName(rs.getString("firstname"));
                dto.setLastName(rs.getString("lastname"));
                dto.setPhone(rs.getString("phone"));
                // Không cần Site Name vì đây là bãi xe của chính họ
                list.add(dto);
            }
        } catch (Exception e) {
            System.out.println("Error ManagerDAO.getEmployeesBySiteId: " + e.getMessage());
        }
        return list;
    }
}
