package com.subscriptionzen.models;

import java.sql.Date;

/**
 * POJO representing a Subscription entity.
 * Demonstrates: Encapsulation, Constructor Overloading, Method Overriding (toString).
 */
public class Subscription {

    private int subscriptionId;
    private int userId;
    private int categoryId;
    private String serviceName;
    private double cost;
    private Date startDate;
    private Date nextRenewalDate;
    private int alertDays;
    private String status;

    // ── Default Constructor ─────────────────────────────────
    public Subscription() {
    }

    // ── Parameterized Constructor (for INSERT — no ID, no renewal date) ─
    public Subscription(int userId, int categoryId, String serviceName,
                        double cost, Date startDate, int alertDays, String status) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.serviceName = serviceName;
        this.cost = cost;
        this.startDate = startDate;
        this.alertDays = alertDays;
        this.status = status;
    }

    // ── Fully Parameterized Constructor (for SELECT mapping) ─
    public Subscription(int subscriptionId, int userId, int categoryId,
                        String serviceName, double cost, Date startDate,
                        Date nextRenewalDate, int alertDays, String status) {
        this.subscriptionId = subscriptionId;
        this.userId = userId;
        this.categoryId = categoryId;
        this.serviceName = serviceName;
        this.cost = cost;
        this.startDate = startDate;
        this.nextRenewalDate = nextRenewalDate;
        this.alertDays = alertDays;
        this.status = status;
    }

    // ── Getters & Setters ───────────────────────────────────
    public int getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(int subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getNextRenewalDate() {
        return nextRenewalDate;
    }

    public void setNextRenewalDate(Date nextRenewalDate) {
        this.nextRenewalDate = nextRenewalDate;
    }

    public int getAlertDays() {
        return alertDays;
    }

    public void setAlertDays(int alertDays) {
        this.alertDays = alertDays;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // ── Method Overriding ───────────────────────────────────
    @Override
    public String toString() {
        return "Subscription{" +
                "id=" + subscriptionId +
                ", service='" + serviceName + '\'' +
                ", cost=" + cost +
                ", startDate=" + startDate +
                ", nextRenewal=" + nextRenewalDate +
                ", status='" + status + '\'' +
                '}';
    }
}
