/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.util.List;
import model.Vehicle;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author dat20
 */
public class VehicleDAO extends DBContext {

    public List<Vehicle> getAllVehicle() {
        String sql = """
                     SELECT * FROM VehicleTypes 
                     WHERE status = 'active'
                     """;
        List<Vehicle> vehicles = new ArrayList<>();

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int vehicleId = rs.getInt("vehicle_type_id");
                String nameStr = rs.getString("name");

                Vehicle vehicle = new Vehicle(vehicleId, Vehicle.VehicleName.valueOf(nameStr.toUpperCase()));
                vehicles.add(vehicle);
            }

            return vehicles;
        } catch (Exception e) {
            System.out.println("Error VehicleDAO.getAllVehicle: " + e.getMessage());
        }
        return vehicles;
    }
}
