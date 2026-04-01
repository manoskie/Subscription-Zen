package com.subscriptionzen.models;

import java.sql.Date;

/**
 * POJO representing an Alert entity.
 * Demonstrates: Encapsulation, Constructor Overloading, Method Overriding.
 */
public class Alert {

    private int alertId;
    private int subscriptionId;
    private String alertMessage;
    private Date alertDate;
    private boolean isSent;

    // ── Default Constructor ─────────────────────────────────
    public Alert() {
    }

    // ── Parameterized Constructor (for INSERT — no ID) ──────
    public Alert(int subscriptionId, String alertMessage, Date alertDate) {
        this.subscriptionId = subscriptionId;
        this.alertMessage = alertMessage;
        this.alertDate = alertDate;
        this.isSent = false;
    }

    // ── Fully Parameterized Constructor ─────────────────────
    public Alert(int alertId, int subscriptionId, String alertMessage,
                 Date alertDate, boolean isSent) {
        this.alertId = alertId;
        this.subscriptionId = subscriptionId;
        this.alertMessage = alertMessage;
        this.alertDate = alertDate;
        this.isSent = isSent;
    }

    // ── Getters & Setters ───────────────────────────────────
    public int getAlertId() {
        return alertId;
    }

    public void setAlertId(int alertId) {
        this.alertId = alertId;
    }

    public int getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(int subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getAlertMessage() {
        return alertMessage;
    }

    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

    public Date getAlertDate() {
        return alertDate;
    }

    public void setAlertDate(Date alertDate) {
        this.alertDate = alertDate;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    // ── Method Overriding ───────────────────────────────────
    @Override
    public String toString() {
        return "Alert{" +
                "alertId=" + alertId +
                ", subscriptionId=" + subscriptionId +
                ", message='" + alertMessage + '\'' +
                ", date=" + alertDate +
                ", sent=" + isSent +
                '}';
    }
}
