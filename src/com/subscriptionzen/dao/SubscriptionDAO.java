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

    /**
     * Calls the stored procedure sp_get_all_users_monthly_expense to get total monthly
     * expense for all users.
     */
    java.util.Map<Integer, Double> getAllUsersMonthlyExpense() throws SQLException;

    /**
     * Calls the MySQL function fn_days_until_renewal to get the number of
     * days remaining until the next renewal date.
     */
    int getDaysUntilRenewal(int subscriptionId) throws SQLException;
}
