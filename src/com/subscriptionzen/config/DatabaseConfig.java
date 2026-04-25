// d:\Manan codes\JDBMS project\Subscription-Zen\src\com\subscriptionzen\config\DatabaseConfig.java
package com.subscriptionzen.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database configuration class.
 * Establishes a JDBC connection to MySQL using DriverManager.
 */
public class DatabaseConfig {

    // ── Connection parameters (loaded from environment variables) ──
    private static final String URL = System.getenv("DB_URL") != null
            ? System.getenv("DB_URL")
            : "jdbc:mysql://localhost:3306/subscription_zen";
    private static final String USER = System.getenv("DB_USER") != null
            ? System.getenv("DB_USER")
            : "root";
    private static final String PASSWORD = System.getenv("DB_PASSWORD") != null
            ? System.getenv("DB_PASSWORD")
            : "1612";

    // ── Static block to load the JDBC driver ────────────────
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("[ERROR] MySQL JDBC Driver not found.");
            e.printStackTrace();
        }
    }

    /**
     * Returns a new JDBC Connection to the subscription_zen database.
     *
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Silently closes a Connection (null-safe).
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("[WARN] Failed to close connection: " + e.getMessage());
            }
        }
    }
}
