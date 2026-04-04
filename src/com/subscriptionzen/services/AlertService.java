package com.subscriptionzen.services;

import com.subscriptionzen.dao.AlertDAO;
import com.subscriptionzen.dao.AlertDAOImpl;
import com.subscriptionzen.models.Alert;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Business logic layer for Alert operations.
 */
public class AlertService {

    private AlertDAO alertDAO;

    public AlertService() {
        this.alertDAO = new AlertDAOImpl();
    }

    /**
     * Creates a new alert for a subscription.
     */
    public void createAlert(int subscriptionId, String message, Date alertDate) throws SQLException {
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Alert message cannot be empty.");
        }
        Alert alert = new Alert(subscriptionId, message.trim(), alertDate);
        alertDAO.insert(alert);
    }

    /**
     * Returns all alerts for a given subscription.
     */
    public ArrayList<Alert> getAlertsBySubscription(int subscriptionId) throws SQLException {
        return alertDAO.findBySubscriptionId(subscriptionId);
    }

    /**
     * Returns all alerts.
     */
    public ArrayList<Alert> getAllAlerts() throws SQLException {
        return alertDAO.findAll();
    }

    /**
     * Marks an alert as sent.
     */
    public void markAlertAsSent(int alertId) throws SQLException {
        alertDAO.markAsSent(alertId);
    }

    /**
     * Deletes an alert.
     */
    public void deleteAlert(int alertId) throws SQLException {
        alertDAO.delete(alertId);
    }
}
