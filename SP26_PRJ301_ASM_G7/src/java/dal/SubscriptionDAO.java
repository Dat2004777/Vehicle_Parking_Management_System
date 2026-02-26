/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author dat20
 */
public class SubscriptionDAO extends DBContext {

    public int getTotalSubscriptionByCurrentMonth() {
        String sql = """
                     SELECT COUNT(s.subscription_id) as total_subscription
                     FROM Subscriptions s
                     WHERE MONTH(s.start_date) = MONTH(GETDATE()) 
                     	AND YEAR(s.start_date) = YEAR(GETDATE())
                     """;

        int totalSubscription = 0;

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                totalSubscription = rs.getInt("total_subscription");
            }
        } catch (Exception e) {
            System.out.println("Error SubscriptionDAO.getTotalSubscriptionByCurrentMonth: " + e.getMessage());
        }

        return totalSubscription;
    }
    
    public int getTotalSubscriptionByCurrentMonth(int siteId) {
        String sql = """
                     SELECT COUNT(s.subscription_id) as total_subscription
                     FROM Subscriptions s
                     JOIN ParkingCard pc ON s.card_id = pc.card_id
                     WHERE MONTH(s.start_date) = MONTH(GETDATE()) 
                     	AND YEAR(s.start_date) = YEAR(GETDATE())
                        AND pc.site_id = ?
                     """;

        int totalSubscription = 0;

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, siteId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                totalSubscription = rs.getInt("total_subscription");
            }
        } catch (Exception e) {
            System.out.println("Error SubscriptionDAO.getTotalSubscriptionByCurrentMonth: " + e.getMessage());
        }

        return totalSubscription;
    }
}
