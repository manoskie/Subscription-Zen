package com.subscriptionzen.services;

import com.subscriptionzen.dao.UserDAO;
import com.subscriptionzen.dao.UserDAOImpl;
import com.subscriptionzen.exceptions.CustomUserNotFoundException;
import com.subscriptionzen.models.User;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Business logic layer for User operations.
 * Validates input and throws custom exceptions.
 * Demonstrates: throw, throws, custom exceptions, ArrayList.
 */
public class UserService {

    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAOImpl();
    }

    /**
     * Registers a new user after validation.
     *
     * @throws CustomUserNotFoundException if email is already taken
     */
    public void registerUser(String name, String email, String password)
            throws CustomUserNotFoundException, SQLException {

        if (name == null || name.trim().isEmpty()) {
            throw new CustomUserNotFoundException("User name cannot be empty.");
        }
        if (email == null || !email.contains("@")) {
            throw new CustomUserNotFoundException("Invalid email address: " + email);
        }

        // Check if email already exists
        User existing = userDAO.findByEmail(email);
        if (existing != null) {
            throw new CustomUserNotFoundException("A user with email '" + email + "' already exists.");
        }

        User user = new User(name.trim(), email.trim(), password);
        userDAO.insert(user);
    }

    /**
     * Finds a user by ID, throws custom exception if not found.
     */
    public User getUserById(int userId) throws CustomUserNotFoundException, SQLException {
        User user = userDAO.findById(userId);
        if (user == null) {
            throw new CustomUserNotFoundException("No user found with ID: " + userId);
        }
        return user;
    }

    /**
     * Returns all users as an ArrayList.
     */
    public ArrayList<User> getAllUsers() throws SQLException {
        return userDAO.findAll();
    }

    /**
     * Updates user details.
     */
    public void updateUser(User user) throws CustomUserNotFoundException, SQLException {
        User existing = userDAO.findById(user.getUserId());
        if (existing == null) {
            throw new CustomUserNotFoundException("Cannot update — user not found with ID: " + user.getUserId());
        }
        userDAO.update(user);
    }

    /**
     * Deletes a user by ID.
     */
    public void deleteUser(int userId) throws CustomUserNotFoundException, SQLException {
        User existing = userDAO.findById(userId);
        if (existing == null) {
            throw new CustomUserNotFoundException("Cannot delete — user not found with ID: " + userId);
        }
        userDAO.delete(userId);
    }
}
