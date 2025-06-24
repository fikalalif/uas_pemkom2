package dao;

import db.DBHelper;
import model.User;
import util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDAO {

    public static boolean tambahUser(User user) {
        String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (Connection conn = DBHelper.connect();  // gunakan .connect() bukan .getConnection()
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Gunakan class PasswordUtil langsung
            String hashedPassword = PasswordUtil.hashPassword(user.getPassword());

            stmt.setString(1, user.getUsername());
            stmt.setString(2, hashedPassword);
            stmt.setString(3, user.getRole());

            int result = stmt.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
