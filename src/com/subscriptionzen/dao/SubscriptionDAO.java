package com.subscriptionzen.dao;

import com.subscriptionzen.models.Subscription;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Interface defining CRUD operations for Subscription entity.
 * Demonstrates: Interface concept.
 */
public interface SubscriptionDAO {

    void insert(Subscription subscription) throws SQLException;

    Subscription findById(int subscriptionId) throws SQLException;

    ArrayList<Subscription> findByUserId(int userId) throws SQLException;

    ArrayList<Subscription> findAll() throws SQLException;

    void update(Subscription subscription) throws SQLException;

    void delete(int subscriptionId) throws SQLException;

    /**
     * Calls the stored procedure sp_get_monthly_expense to get total monthly
     * expense.
     */
    double getMonthlyExpense(int userId) throws SQLException;
}
