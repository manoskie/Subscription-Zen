package com.subscriptionzen.dao;

import com.subscriptionzen.config.DatabaseConfig;
import com.subscriptionzen.models.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * JDBC implementation of CategoryDAO.
 * Extends BaseDAO, implements CategoryDAO.
 */
public class CategoryDAOImpl extends BaseDAO implements CategoryDAO {

    @Override
    public void insert(Category category) throws SQLException {
        String sql = "INSERT INTO CATEGORY (CATEGORY_NAME, DESCRIPTION) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, category.getCategoryName());
            pstmt.setString(2, category.getDescription());
            pstmt.executeUpdate();
            System.out.println("[INFO] Category inserted successfully.");
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to insert category: " + e.getMessage());
            throw e;
        } finally {
            closeResources(conn, pstmt);
        }
    }

    @Override
    public Category findById(int categoryId) throws SQLException {
        String sql = "SELECT * FROM CATEGORY WHERE CATEGORY_ID = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, categoryId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }
            return null;
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to find category: " + e.getMessage());
            throw e;
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    @Override
    public ArrayList<Category> findAll() throws SQLException {
        String sql = "SELECT * FROM CATEGORY";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<Category> list = new ArrayList<Category>();
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to fetch categories: " + e.getMessage());
            throw e;
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return list;
    }

    @Override
    public void update(Category category) throws SQLException {
        String sql = "UPDATE CATEGORY SET CATEGORY_NAME = ?, DESCRIPTION = ? WHERE CATEGORY_ID = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, category.getCategoryName());
            pstmt.setString(2, category.getDescription());
            pstmt.setInt(3, category.getCategoryId());
            pstmt.executeUpdate();
            System.out.println("[INFO] Category updated successfully.");
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to update category: " + e.getMessage());
            throw e;
        } finally {
            closeResources(conn, pstmt);
        }
    }

    @Override
    public void delete(int categoryId) throws SQLException {
        String sql = "DELETE FROM CATEGORY WHERE CATEGORY_ID = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, categoryId);
            pstmt.executeUpdate();
            System.out.println("[INFO] Category deleted successfully.");
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to delete category: " + e.getMessage());
            throw e;
        } finally {
            closeResources(conn, pstmt);
        }
    }

    private Category mapResultSet(ResultSet rs) throws SQLException {
        return new Category(
                rs.getInt("CATEGORY_ID"),
                rs.getString("CATEGORY_NAME"),
                rs.getString("DESCRIPTION")
        );
    }
}
