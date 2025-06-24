/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package auth;

import db.DBHelper;
import java.sql.*;

public class AuthService {

    public static boolean login(String username, String password) {
        try (Connection conn = DBHelper.getConnection()) {
            String sql = "SELECT password, role FROM users WHERE username=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password");
                return storedHash.equals(PasswordUtils.hashPassword(password));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isAdmin(String username) {
        try (Connection conn = DBHelper.getConnection()) {
            String sql = "SELECT role FROM users WHERE username=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next() && "admin".equalsIgnoreCase(rs.getString("role"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean addUser(String username, String password, String role) {
        try (Connection conn = DBHelper.getConnection()) {
            String sql = "INSERT INTO users(username, password, role) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, PasswordUtils.hashPassword(password));
            ps.setString(3, role);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

