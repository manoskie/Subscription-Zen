package com.subscriptionzen.dao;

import com.subscriptionzen.models.Payment;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Interface defining CRUD operations for Payment entity.
 */
public interface PaymentDAO {

    void insert(Payment payment) throws SQLException;

    Payment findById(int paymentId) throws SQLException;

    ArrayList<Payment> findBySubscriptionId(int subscriptionId) throws SQLException;

    ArrayList<Payment> findAll() throws SQLException;

    void delete(int paymentId) throws SQLException;
}
