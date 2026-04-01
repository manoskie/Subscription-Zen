package com.subscriptionzen.dao;

import com.subscriptionzen.config.DatabaseConfig;
import com.subscriptionzen.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * JDBC implementation of UserDAO.
 * Extends BaseDAO (abstract class) and implements UserDAO (interface).
 * Demonstrates: Abstract class inheritance, Interface implementation, try-catch-finally.
 */
public class UserDAOImpl extends BaseDAO implements UserDAO {

    @Override
    public void insert(User user) throws SQLException {
        String sql = "INSERT INTO USER (NAME, EMAIL, PASSWORD_HASH) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPasswordHash());
            pstmt.executeUpdate();
            System.out.println("[INFO] User inserted successfully.");
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to insert user: " + e.getMessage());
            throw e;
        } finally {
            closeResources(conn, pstmt);
        }
    }

    @Override
    public User findById(int userId) throws SQLException {
        String sql = "SELECT * FROM USER WHERE USER_ID = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
            return null;
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to find user by ID: " + e.getMessage());
            throw e;
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    @Override
    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM USER WHERE EMAIL = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
            return null;
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to find user by email: " + e.getMessage());
            throw e;
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    @Override
    public ArrayList<User> findAll() throws SQLException {
        String sql = "SELECT * FROM USER";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<User> users = new ArrayList<User>();
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to fetch all users: " + e.getMessage());
            throw e;
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return users;
    }

    @Override
    public void update(User user) throws SQLException {
        String sql = "UPDATE USER SET NAME = ?, EMAIL = ?, PASSWORD_HASH = ? WHERE USER_ID = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPasswordHash());
            pstmt.setInt(4, user.getUserId());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("[INFO] User updated successfully.");
            } else {
                System.out.println("[WARN] No user found with ID: " + user.getUserId());
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to update user: " + e.getMessage());
            throw e;
        } finally {
            closeResources(conn, pstmt);
        }
    }

    @Override
    public void delete(int userId) throws SQLException {
        String sql = "DELETE FROM USER WHERE USER_ID = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
            System.out.println("[INFO] User deleted successfully.");
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to delete user: " + e.getMessage());
            throw e;
        } finally {
            closeResources(conn, pstmt);
        }
    }

    // ── Helper: Map ResultSet row to User object ────────────
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("USER_ID"),
                rs.getString("NAME"),
                rs.getString("EMAIL"),
                rs.getString("PASSWORD_HASH"),
                rs.getTimestamp("CREATED_AT")
        );
    }
}
