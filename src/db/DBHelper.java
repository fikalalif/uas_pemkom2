/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Fikal Alif
 */
public class DBHelper {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/kebijakan_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = ""; // ganti kalau pakai password

    public static Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Connection getConnection() {
        return connect();
    }
}
