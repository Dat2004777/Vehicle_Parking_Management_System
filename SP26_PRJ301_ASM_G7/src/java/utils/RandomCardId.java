/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.util.UUID;

/**
 *
 * @author dat20
 */
public class RandomCardId {

    //base
    public static String generateCardId() {
        return "CARD-" + UUID.randomUUID().toString().toUpperCase();
    }
    
    public static String generateRandomUsername() {
        return UUID.randomUUID().toString().toUpperCase();
    }

    //them siteId vào để tỉ lệ trùng
    public static String generateCardId(int siteId) {
        return "CARD-" + siteId + "-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }
}
