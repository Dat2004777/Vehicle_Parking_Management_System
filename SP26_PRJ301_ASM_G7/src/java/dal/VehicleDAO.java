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
import model.dto.VehicleConfigDTO;

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

    public List<VehicleConfigDTO> getAllVehicleForDetailSite(int siteId) {
        String sql = """
                     SELECT 
                         a.vehicle_type_id, 
                         vt.name AS vehicleName, 
                         a.totalSlots AS capacity,
                         MAX(CASE WHEN p.type = 'hourly' THEN p.base_price ELSE 0 END) AS hourlyPrice,
                         MAX(CASE WHEN p.type = 'monthly' THEN p.base_price ELSE 0 END) AS monthlyPrice
                     FROM ParkingAreas a
                     JOIN VehicleTypes vt ON a.vehicle_type_id = vt.vehicle_type_id
                     LEFT JOIN PriceConfigs p ON a.site_id = p.site_id AND a.vehicle_type_id = p.vehicle_type_id
                     WHERE a.site_id = ? AND a.status = 'active'
                     GROUP BY a.vehicle_type_id, vt.name, a.totalSlots;
                     """;

        List<VehicleConfigDTO> vehicleConfigDTOs = new ArrayList<>();

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, siteId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int vehicleId = rs.getInt("vehicle_type_id");
                String vehicleName = rs.getString("vehicleName");
                int capacity = rs.getInt("capacity");
                long hourlyPrice = rs.getLong("hourlyPrice");
                long monthlyPrice = rs.getLong("monthlyPrice");

                VehicleConfigDTO vehicleConfigDTO = new VehicleConfigDTO(vehicleId,
                        VehicleConfigDTO.VehicleName.valueOf(vehicleName.toUpperCase()),
                        capacity,
                        hourlyPrice,
                        monthlyPrice);
                vehicleConfigDTOs.add(vehicleConfigDTO);
            }

            return vehicleConfigDTOs;
        } catch (Exception e) {
            System.out.println("Error VehicleDAO.getAllVehicleForDetailSite: " + e.getMessage());
        }
        return vehicleConfigDTOs;
    }
}
