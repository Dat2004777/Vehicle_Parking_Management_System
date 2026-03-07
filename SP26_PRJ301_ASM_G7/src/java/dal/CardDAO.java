/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import utils.RandomCardId;

/**
 *
 * @author dat20
 */
public class CardDAO extends DBContext {

    public void addMultipleCards(int siteId, int quantity) {
        String sql = """
                     INSERT INTO ParkingCards(card_id, site_id) VALUES (?, ?)
                     """;

        try {
            PreparedStatement ps = connection.prepareStatement(sql);

            for (int i = 0; i < quantity; i++) {
                String cardId = RandomCardId.generateCardId(siteId);
                ps.setString(1, cardId);
                ps.setInt(2, siteId);
                ps.executeUpdate();
            }

        } catch (Exception e) {
            System.out.println("Error CardDAO.addMultipleCards: " + e.getMessage());
        }

    }

    public void softDeleteAllCardBySiteId(int siteId) {
        String sql = """
                     UPDATE ParkingCards SET status = 'inactive'
                     WHERE site_id = ?
                     """;

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, siteId);
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("Error CardDAO.softDeleteAllCardBySiteId: " + e.getMessage());
        }
    }
}
