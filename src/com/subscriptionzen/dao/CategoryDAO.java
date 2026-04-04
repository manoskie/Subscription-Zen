package com.subscriptionzen.dao;

import com.subscriptionzen.models.Category;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Interface defining CRUD operations for Category entity.
 */
public interface CategoryDAO {

    void insert(Category category) throws SQLException;

    Category findById(int categoryId) throws SQLException;

    ArrayList<Category> findAll() throws SQLException;

    void update(Category category) throws SQLException;

    void delete(int categoryId) throws SQLException;
}
