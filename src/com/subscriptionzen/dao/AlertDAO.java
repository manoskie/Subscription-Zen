package com.subscriptionzen.dao;

import com.subscriptionzen.models.Alert;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Interface defining CRUD operations for Alert entity.
 */
public interface AlertDAO {

    void insert(Alert alert) throws SQLException;

    Alert findById(int alertId) throws SQLException;

    ArrayList<Alert> findBySubscriptionId(int subscriptionId) throws SQLException;

    ArrayList<Alert> findAll() throws SQLException;

    void markAsSent(int alertId) throws SQLException;

    void delete(int alertId) throws SQLException;
}
