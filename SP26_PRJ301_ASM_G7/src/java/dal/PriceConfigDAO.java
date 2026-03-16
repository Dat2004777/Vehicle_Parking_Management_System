/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.VehicleType;
import model.dto.VehicleBasePriceDTO;

/**
 *
 * @author ADMIN
 */
public class PriceConfigDAO extends DBContext {

    public List<VehicleBasePriceDTO> getBasePriceHour(int siteId) {
        String sql
                = """
                SELECT v.vehicle_type_id, v.name, pr.base_price
                FROM VehicleTypes v
                JOIN PriceConfigs pr ON v.vehicle_type_id = pr.vehicle_type_id
                WHERE pr.site_id = ? and pr.type = 'hourly' and v.status = 'active' and pr.status = 'active'
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, siteId);

            ResultSet rs = ps.executeQuery();
            List<VehicleBasePriceDTO> list = new ArrayList<>();
            while (rs.next()) {

                VehicleType vehicle = new VehicleType();

                vehicle.setVehicleTypeId(rs.getInt("vehicle_type_id"));

                String name = rs.getString("name");
                vehicle.setVehicleName(VehicleType.VehicleName.valueOf(name.toUpperCase())
                );

                list.add(new VehicleBasePriceDTO(vehicle, rs.getInt("base_price")));
            }

            return list;
        } catch (Exception e) {
            System.out.println("Lỗi lấy ra basePrice của Hour");
            e.printStackTrace();
            return null;
        }
    }

    public List<VehicleBasePriceDTO> getBasePriceMonth(int siteId) {
        String sql
                = """
                SELECT v.vehicle_type_id, v.name, pr.base_price
                FROM VehicleTypes v
                JOIN PriceConfigs pr ON v.vehicle_type_id = pr.vehicle_type_id
                WHERE pr.site_id = ? and pr.type = 'monthly' and v.status = 'active' and pr.status = 'active'
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, siteId);

            ResultSet rs = ps.executeQuery();
            List<VehicleBasePriceDTO> list = new ArrayList<>();
            while (rs.next()) {

                VehicleType vehicle = new VehicleType();

                vehicle.setVehicleTypeId(rs.getInt("vehicle_type_id"));

                String name = rs.getString("name");
                vehicle.setVehicleName(VehicleType.VehicleName.valueOf(name.toUpperCase())
                );

                list.add(new VehicleBasePriceDTO(vehicle, rs.getInt("base_price")));
            }

            return list;
        } catch (Exception e) {
            System.out.println("Lỗi lấy ra basePrice của Month");
            e.printStackTrace();
            return null;
        }
    }

    public long getPriceByVehicleAndSite(String cardType, int siteId, int vehicleTypeId) {
        String sql
                = """
                SELECT pr.base_price
                FROM PriceCOnfigs pr
                WHERE pr.site_id = ? AND pr.vehicle_type_id = ? AND pr.type = ? AND pr.status = 'active'
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, siteId);
            ps.setInt(2, vehicleTypeId);
            ps.setString(3, cardType);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getLong("base_price");
            }
            return -1;
        } catch (Exception e) {
            System.out.println("Lỗi lấy ra giá tiền của 1 xe");
            e.printStackTrace();
            return -1;
        }
    }
}
