package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Employee;
import model.ParkingSite;

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

    public List<Employee> getAllEmployeeForDetailSite(int siteId) {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM Employees e WHERE e.status = 'active' AND (e.site_id = ? OR e.site_id IS NULL)";

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

    public void add(Employee emp) {
        String sql = "INSERT INTO Employees (account_id, firstname, lastname, phone, site_id) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, emp.getAccountId());
            ps.setString(2, emp.getFirstName());
            ps.setString(3, emp.getLastName());
            ps.setString(4, emp.getPhone());
            ps.setInt(5, emp.getSiteId());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error EmployeeDAO.add: " + e.getMessage());
        }
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
        String removeSiteIdForOldEmployee = "UPDATE Employees SET site_id = NULL WHERE employee_id = ?";
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
                    psRel.setInt(1, oldManagerId);
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
}
