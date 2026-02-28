package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Employee;

public class EmployeeDAO extends DBContext {

    public List<Employee> getAll() {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM Employees";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapRowToEmployee(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error EmployeeDAO.getAll: " + e.getMessage());
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
}
