package com.subscriptionzen.dao;

import com.subscriptionzen.models.User;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Interface defining CRUD operations for User entity.
 * Demonstrates: Interface concept.
 */
public interface UserDAO {

    void insert(User user) throws SQLException;

    User findById(int userId) throws SQLException;

    User findByEmail(String email) throws SQLException;

    ArrayList<User> findAll() throws SQLException;

    void update(User user) throws SQLException;

    void delete(int userId) throws SQLException;
}
