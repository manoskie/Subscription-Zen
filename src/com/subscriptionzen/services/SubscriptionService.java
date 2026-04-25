package com.subscriptionzen.services;

import com.subscriptionzen.dao.SubscriptionDAO;
import com.subscriptionzen.dao.SubscriptionDAOImpl;
import com.subscriptionzen.exceptions.InvalidSubscriptionException;
import com.subscriptionzen.models.Subscription;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Business logic layer for Subscription operations.
 * Demonstrates: ArrayList usage, throw/throws, custom exceptions, data validation.
 */
public class SubscriptionService {

    private SubscriptionDAO subscriptionDAO;

    public SubscriptionService() {
        this.subscriptionDAO = new SubscriptionDAOImpl();
    }

    /**
     * Adds a new subscription after validation.
     *
     * @throws InvalidSubscriptionException if validation fails
     */
    public void addSubscription(int userId, int categoryId, String serviceName,
                                double cost, Date startDate, int alertDays, String status)
            throws InvalidSubscriptionException, SQLException {

        // ── Validation ──────────────────────────────────────
        if (serviceName == null || serviceName.trim().isEmpty()) {
            throw new InvalidSubscriptionException("Service name cannot be empty.");
        }
        if (cost <= 0) {
            throw new InvalidSubscriptionException("Cost must be greater than zero. Provided: " + cost);
        }
        if (startDate == null) {
            throw new InvalidSubscriptionException("Start date cannot be null.");
        }
        if (alertDays < 0) {
            throw new InvalidSubscriptionException("Alert days cannot be negative.");
        }
        String upperStatus = status.toUpperCase();
        if (!upperStatus.equals("ACTIVE") && !upperStatus.equals("PAUSED") && !upperStatus.equals("CANCELLED")) {
            throw new InvalidSubscriptionException("Invalid status: '" + status + "'. Must be ACTIVE, PAUSED, or CANCELLED.");
        }

        Subscription sub = new Subscription(userId, categoryId, serviceName.trim(),
                cost, startDate, alertDays, upperStatus);
        subscriptionDAO.insert(sub);
    }

    /**
     * Returns a subscription by ID.
     *
     * @throws InvalidSubscriptionException if not found
     */
    public Subscription getSubscriptionById(int subscriptionId)
            throws InvalidSubscriptionException, SQLException {
        Subscription sub = subscriptionDAO.findById(subscriptionId);
        if (sub == null) {
            throw new InvalidSubscriptionException("No subscription found with ID: " + subscriptionId);
        }
        return sub;
    }

    /**
     * Returns all subscriptions for a user as an ArrayList.
     * Demonstrates: ArrayList as return type for collection processing.
     */
    public ArrayList<Subscription> getSubscriptionsByUserId(int userId) throws SQLException {
        return subscriptionDAO.findByUserId(userId);
    }

    /**
     * Returns all subscriptions.
     */
    public ArrayList<Subscription> getAllSubscriptions() throws SQLException {
        return subscriptionDAO.findAll();
    }

    /**
     * Updates a subscription.
     */
    public void updateSubscription(Subscription sub)
            throws InvalidSubscriptionException, SQLException {
        if (sub.getCost() <= 0) {
            throw new InvalidSubscriptionException("Cost must be positive.");
        }
        Subscription existing = subscriptionDAO.findById(sub.getSubscriptionId());
        if (existing == null) {
            throw new InvalidSubscriptionException(
                    "Cannot update — subscription not found with ID: " + sub.getSubscriptionId());
        }
        subscriptionDAO.update(sub);
    }

    /**
     * Deletes a subscription.
     */
    public void deleteSubscription(int subscriptionId)
            throws InvalidSubscriptionException, SQLException {
        Subscription existing = subscriptionDAO.findById(subscriptionId);
        if (existing == null) {
            throw new InvalidSubscriptionException(
                    "Cannot delete — subscription not found with ID: " + subscriptionId);
        }
        subscriptionDAO.delete(subscriptionId);
    }

    /**
     * Calls the stored procedure to get total monthly expense for a user.
     */
    public double getMonthlyExpense(int userId) throws SQLException {
        return subscriptionDAO.getMonthlyExpense(userId);
    }

    /**
     * Calls the stored procedure to get total monthly expense for all users.
     */
    public java.util.Map<Integer, Double> getAllUsersMonthlyExpense() throws SQLException {
        return subscriptionDAO.getAllUsersMonthlyExpense();
    }

    /**
     * Calls the MySQL function fn_days_until_renewal to get days remaining
     * until the next renewal date for a given subscription.
     */
    public int getDaysUntilRenewal(int subscriptionId) throws SQLException {
        return subscriptionDAO.getDaysUntilRenewal(subscriptionId);
    }
}
