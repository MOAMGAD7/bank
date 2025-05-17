
        package com.banking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:sqlite:bank.db";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(DB_URL);
        } catch (ClassNotFoundException e) {
            System.out.println("❌ SQLite JDBC Driver not found: " + e.getMessage());
            throw new SQLException("Failed to load SQLite JDBC Driver", e);
        }
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("❌ Error closing connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
