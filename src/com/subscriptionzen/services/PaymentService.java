package com.subscriptionzen.services;

import com.subscriptionzen.dao.PaymentDAO;
import com.subscriptionzen.dao.PaymentDAOImpl;
import com.subscriptionzen.models.Payment;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Business logic layer for Payment operations.
 */
public class PaymentService {

    private PaymentDAO paymentDAO;

    public PaymentService() {
        this.paymentDAO = new PaymentDAOImpl();
    }

    /**
     * Records a new payment.
     */
    public void recordPayment(int subscriptionId, double amount, Date paymentDate, String method)
            throws SQLException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive.");
        }
        Payment payment = new Payment(subscriptionId, amount, paymentDate, method);
        paymentDAO.insert(payment);
    }

    /**
     * Returns all payments for a given subscription.
     */
    public ArrayList<Payment> getPaymentsBySubscription(int subscriptionId) throws SQLException {
        return paymentDAO.findBySubscriptionId(subscriptionId);
    }

    /**
     * Returns all payments.
     */
    public ArrayList<Payment> getAllPayments() throws SQLException {
        return paymentDAO.findAll();
    }

    /**
     * Deletes a payment.
     */
    public void deletePayment(int paymentId) throws SQLException {
        paymentDAO.delete(paymentId);
    }
}
