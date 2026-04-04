package com.subscriptionzen.services;

import com.subscriptionzen.dao.CategoryDAO;
import com.subscriptionzen.dao.CategoryDAOImpl;
import com.subscriptionzen.models.Category;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Business logic layer for Category operations.
 */
public class CategoryService {

    private CategoryDAO categoryDAO;

    public CategoryService() {
        this.categoryDAO = new CategoryDAOImpl();
    }

    /**
     * Adds a new category.
     */
    public void addCategory(String name, String description) throws SQLException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be empty.");
        }
        Category category = new Category(name.trim(), description);
        categoryDAO.insert(category);
    }

    /**
     * Returns all categories as an ArrayList.
     */
    public ArrayList<Category> getAllCategories() throws SQLException {
        return categoryDAO.findAll();
    }

    /**
     * Finds a category by ID.
     */
    public Category getCategoryById(int id) throws SQLException {
        return categoryDAO.findById(id);
    }

    /**
     * Deletes a category.
     */
    public void deleteCategory(int categoryId) throws SQLException {
        categoryDAO.delete(categoryId);
    }
}
