package com.subscriptionzen.dao;

import com.subscriptionzen.config.DatabaseConfig;
import com.subscriptionzen.models.Payment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * JDBC implementation of PaymentDAO.
 * Extends BaseDAO, implements PaymentDAO.
 */
public class PaymentDAOImpl extends BaseDAO implements PaymentDAO {

    @Override
    public void insert(Payment payment) throws SQLException {
        String sql = "INSERT INTO PAYMENT (SUBSCRIPTION_ID, AMOUNT, PAYMENT_DATE, PAYMENT_METHOD) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, payment.getSubscriptionId());
            pstmt.setDouble(2, payment.getAmount());
            pstmt.setDate(3, payment.getPaymentDate());
            pstmt.setString(4, payment.getPaymentMethod());
            pstmt.executeUpdate();
            System.out.println("[INFO] Payment recorded successfully.");
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to insert payment: " + e.getMessage());
            throw e;
        } finally {
            closeResources(conn, pstmt);
        }
    }

    @Override
    public Payment findById(int paymentId) throws SQLException {
        String sql = "SELECT * FROM PAYMENT WHERE PAYMENT_ID = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, paymentId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }
            return null;
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to find payment: " + e.getMessage());
            throw e;
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    @Override
    public ArrayList<Payment> findBySubscriptionId(int subscriptionId) throws SQLException {
        String sql = "SELECT * FROM PAYMENT WHERE SUBSCRIPTION_ID = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<Payment> list = new ArrayList<Payment>();
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, subscriptionId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to find payments: " + e.getMessage());
            throw e;
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return list;
    }

    @Override
    public ArrayList<Payment> findAll() throws SQLException {
        String sql = "SELECT * FROM PAYMENT";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<Payment> list = new ArrayList<Payment>();
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to fetch all payments: " + e.getMessage());
            throw e;
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return list;
    }

    @Override
    public void delete(int paymentId) throws SQLException {
        String sql = "DELETE FROM PAYMENT WHERE PAYMENT_ID = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, paymentId);
            pstmt.executeUpdate();
            System.out.println("[INFO] Payment deleted successfully.");
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to delete payment: " + e.getMessage());
            throw e;
        } finally {
            closeResources(conn, pstmt);
        }
    }

    private Payment mapResultSet(ResultSet rs) throws SQLException {
        return new Payment(
                rs.getInt("PAYMENT_ID"),
                rs.getInt("SUBSCRIPTION_ID"),
                rs.getDouble("AMOUNT"),
                rs.getDate("PAYMENT_DATE"),
                rs.getString("PAYMENT_METHOD"));
    }
}
