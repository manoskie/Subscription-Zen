package com.subscriptionzen.models;

import java.sql.Timestamp;

/**
 * POJO representing a User entity.
 * Demonstrates: Encapsulation, Constructor Overloading, Method Overriding (toString).
 */
public class User {

    // ── Private fields (Encapsulation) ──────────────────────
    private int userId;
    private String name;
    private String email;
    private String passwordHash;
    private Timestamp createdAt;

    // ── Default Constructor ─────────────────────────────────
    public User() {
    }

    // ── Parameterized Constructor (without ID — for INSERT) ─
    public User(String name, String email, String passwordHash) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    // ── Fully Parameterized Constructor (for SELECT mapping) ─
    public User(int userId, String name, String email, String passwordHash, Timestamp createdAt) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
    }

    // ── Getters & Setters ───────────────────────────────────
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    // ── Method Overriding (toString) ────────────────────────
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
