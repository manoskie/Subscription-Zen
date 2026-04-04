package com.subscriptionzen.dao;

import com.subscriptionzen.config.DatabaseConfig;
import com.subscriptionzen.models.Subscription;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

/**
 * JDBC implementation of SubscriptionDAO.
 * Extends BaseDAO, implements SubscriptionDAO.
 * Demonstrates: Interface implementation, Abstract class inheritance,
 * try-catch-finally, PreparedStatement, CallableStatement (stored procedure).
 */
public class SubscriptionDAOImpl extends BaseDAO implements SubscriptionDAO {

    @Override
    public void insert(Subscription sub) throws SQLException {
        String sql = "INSERT INTO SUBSCRIPTION (USER_ID, CATEGORY_ID, SERVICE_NAME, COST, START_DATE, ALERT_DAYS, STATUS) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, sub.getUserId());
            pstmt.setInt(2, sub.getCategoryId());
            pstmt.setString(3, sub.getServiceName());
            pstmt.setDouble(4, sub.getCost());
            pstmt.setDate(5, sub.getStartDate());
            pstmt.setInt(6, sub.getAlertDays());
            pstmt.setString(7, sub.getStatus());
            pstmt.executeUpdate();
            System.out.println("[INFO] Subscription inserted successfully.");
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to insert subscription: " + e.getMessage());
            throw e;
        } finally {
            closeResources(conn, pstmt);
        }
    }

    @Override
    public Subscription findById(int subscriptionId) throws SQLException {
        String sql = "SELECT * FROM SUBSCRIPTION WHERE SUBSCRIPTION_ID = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, subscriptionId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }
            return null;
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to find subscription: " + e.getMessage());
            throw e;
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    @Override
    public ArrayList<Subscription> findByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM SUBSCRIPTION WHERE USER_ID = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<Subscription> list = new ArrayList<Subscription>();
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to find subscriptions by user: " + e.getMessage());
            throw e;
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return list;
    }

    @Override
    public ArrayList<Subscription> findAll() throws SQLException {
        String sql = "SELECT * FROM SUBSCRIPTION";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<Subscription> list = new ArrayList<Subscription>();
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to fetch all subscriptions: " + e.getMessage());
            throw e;
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return list;
    }

    @Override
    public void update(Subscription sub) throws SQLException {
        String sql = "UPDATE SUBSCRIPTION SET SERVICE_NAME = ?, COST = ?, ALERT_DAYS = ?, STATUS = ?, CATEGORY_ID = ? "
                + "WHERE SUBSCRIPTION_ID = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, sub.getServiceName());
            pstmt.setDouble(2, sub.getCost());
            pstmt.setInt(3, sub.getAlertDays());
            pstmt.setString(4, sub.getStatus());
            pstmt.setInt(5, sub.getCategoryId());
            pstmt.setInt(6, sub.getSubscriptionId());
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("[INFO] Subscription updated successfully.");
            } else {
                System.out.println("[WARN] No subscription found with ID: " + sub.getSubscriptionId());
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to update subscription: " + e.getMessage());
            throw e;
        } finally {
            closeResources(conn, pstmt);
        }
    }

    @Override
    public void delete(int subscriptionId) throws SQLException {
        String sql = "DELETE FROM SUBSCRIPTION WHERE SUBSCRIPTION_ID = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, subscriptionId);
            pstmt.executeUpdate();
            System.out.println("[INFO] Subscription deleted successfully.");
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to delete subscription: " + e.getMessage());
            throw e;
        } finally {
            closeResources(conn, pstmt);
        }
    }

    /**
     * Calls the stored procedure sp_get_monthly_expense.
     * Demonstrates: CallableStatement usage with OUT parameter.
     */
    @Override
    public double getMonthlyExpense(int userId) throws SQLException {
        String sql = "{CALL sp_get_monthly_expense(?, ?)}";
        Connection conn = null;
        CallableStatement cstmt = null;
        try {
            conn = DatabaseConfig.getConnection();
            cstmt = conn.prepareCall(sql);
            cstmt.setInt(1, userId);
            cstmt.registerOutParameter(2, Types.DECIMAL);
            cstmt.execute();
            return cstmt.getDouble(2);
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to get monthly expense: " + e.getMessage());
            throw e;
        } finally {
            closeResources(conn, cstmt);
        }
    }

    // ── Helper ──────────────────────────────────────────────
    private Subscription mapResultSet(ResultSet rs) throws SQLException {
        return new Subscription(
                rs.getInt("SUBSCRIPTION_ID"),
                rs.getInt("USER_ID"),
                rs.getInt("CATEGORY_ID"),
                rs.getString("SERVICE_NAME"),
                rs.getDouble("COST"),
                rs.getDate("START_DATE"),
                rs.getDate("NEXT_RENEWAL_DATE"),
                rs.getInt("ALERT_DAYS"),
                rs.getString("STATUS"));
    }
}
