package com.subscriptionzen.models;

import java.sql.Date;

/**
 * POJO representing a Payment entity.
 * Demonstrates: Encapsulation, Constructor Overloading, Method Overriding.
 */
public class Payment {

    private int paymentId;
    private int subscriptionId;
    private double amount;
    private Date paymentDate;
    private String paymentMethod;

    // ── Default Constructor ─────────────────────────────────
    public Payment() {
    }

    // ── Parameterized Constructor (for INSERT — no ID) ──────
    public Payment(int subscriptionId, double amount, Date paymentDate, String paymentMethod) {
        this.subscriptionId = subscriptionId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
    }

    // ── Fully Parameterized Constructor ─────────────────────
    public Payment(int paymentId, int subscriptionId, double amount,
                   Date paymentDate, String paymentMethod) {
        this.paymentId = paymentId;
        this.subscriptionId = subscriptionId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
    }

    // ── Getters & Setters ───────────────────────────────────
    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(int subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    // ── Method Overriding ───────────────────────────────────
    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", subscriptionId=" + subscriptionId +
                ", amount=" + amount +
                ", date=" + paymentDate +
                ", method='" + paymentMethod + '\'' +
                '}';
    }
}
