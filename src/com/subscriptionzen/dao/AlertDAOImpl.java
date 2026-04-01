package com.subscriptionzen.dao;

import com.subscriptionzen.config.DatabaseConfig;
import com.subscriptionzen.models.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * JDBC implementation of AlertDAO.
 * Extends BaseDAO, implements AlertDAO.
 */
public class AlertDAOImpl extends BaseDAO implements AlertDAO {

    @Override
    public void insert(Alert alert) throws SQLException {
        String sql = "INSERT INTO ALERTS (SUBSCRIPTION_ID, ALERT_MESSAGE, ALERT_DATE, IS_SENT) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, alert.getSubscriptionId());
            pstmt.setString(2, alert.getAlertMessage());
            pstmt.setDate(3, alert.getAlertDate());
            pstmt.setBoolean(4, alert.isSent());
            pstmt.executeUpdate();
            System.out.println("[INFO] Alert created successfully.");
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to insert alert: " + e.getMessage());
            throw e;
        } finally {
            closeResources(conn, pstmt);
        }
    }

    @Override
    public Alert findById(int alertId) throws SQLException {
        String sql = "SELECT * FROM ALERTS WHERE ALERT_ID = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, alertId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }
            return null;
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to find alert: " + e.getMessage());
            throw e;
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    @Override
    public ArrayList<Alert> findBySubscriptionId(int subscriptionId) throws SQLException {
        String sql = "SELECT * FROM ALERTS WHERE SUBSCRIPTION_ID = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<Alert> list = new ArrayList<Alert>();
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, subscriptionId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to find alerts: " + e.getMessage());
            throw e;
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return list;
    }

    @Override
    public ArrayList<Alert> findAll() throws SQLException {
        String sql = "SELECT * FROM ALERTS";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<Alert> list = new ArrayList<Alert>();
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to fetch all alerts: " + e.getMessage());
            throw e;
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return list;
    }

    @Override
    public void markAsSent(int alertId) throws SQLException {
        String sql = "UPDATE ALERTS SET IS_SENT = TRUE WHERE ALERT_ID = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, alertId);
            pstmt.executeUpdate();
            System.out.println("[INFO] Alert marked as sent.");
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to mark alert as sent: " + e.getMessage());
            throw e;
        } finally {
            closeResources(conn, pstmt);
        }
    }

    @Override
    public void delete(int alertId) throws SQLException {
        String sql = "DELETE FROM ALERTS WHERE ALERT_ID = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, alertId);
            pstmt.executeUpdate();
            System.out.println("[INFO] Alert deleted successfully.");
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to delete alert: " + e.getMessage());
            throw e;
        } finally {
            closeResources(conn, pstmt);
        }
    }

    private Alert mapResultSet(ResultSet rs) throws SQLException {
        return new Alert(
                rs.getInt("ALERT_ID"),
                rs.getInt("SUBSCRIPTION_ID"),
                rs.getString("ALERT_MESSAGE"),
                rs.getDate("ALERT_DATE"),
                rs.getBoolean("IS_SENT")
        );
    }
}
