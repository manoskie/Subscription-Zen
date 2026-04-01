package com.subscriptionzen.models;

/**
 * POJO representing a Category entity.
 * Demonstrates: Encapsulation, Constructor Overloading, Method Overriding.
 */
public class Category {

    private int categoryId;
    private String categoryName;
    private String description;

    // ── Default Constructor ─────────────────────────────────
    public Category() {
    }

    // ── Parameterized Constructor (for INSERT — no ID) ──────
    public Category(String categoryName, String description) {
        this.categoryName = categoryName;
        this.description = description;
    }

    // ── Fully Parameterized Constructor ─────────────────────
    public Category(int categoryId, String categoryName, String description) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.description = description;
    }

    // ── Getters & Setters ───────────────────────────────────
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // ── Method Overriding ───────────────────────────────────
    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", name='" + categoryName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
