package com.subscriptionzen.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Abstract base class for all DAO implementations.
 * Provides shared resource-cleanup helpers.
 * Demonstrates: Abstract Class concept.
 */
public abstract class BaseDAO {

    /**
     * Safely closes Connection, Statement, and ResultSet in a finally block.
     * Each close is wrapped in its own try-catch to ensure all resources are
     * attempted.
     */
    protected void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("[WARN] Failed to close ResultSet: " + e.getMessage());
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.err.println("[WARN] Failed to close Statement: " + e.getMessage());
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("[WARN] Failed to close Connection: " + e.getMessage());
            }
        }
    }

    /**
     * Overloaded helper — closes only Connection and Statement (no ResultSet).
     */
    protected void closeResources(Connection conn, Statement stmt) {
        closeResources(conn, stmt, null);
    }
}
