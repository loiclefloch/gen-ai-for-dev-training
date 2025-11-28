package com.ecommerce.model;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

// No javadoc!
public class Product {
    
    // Mix of public fields and getters/setters - inconsistent!
    public Long id;
    public String name;
    public String description;
    public double price; // Can be negative!
    public int stock;
    public String category;
    public Date createdAt;
    private boolean active; // This one is private for some reason
    public String status; // Duplicate with active? Who knows!
    
    public Product() {
    }
    
    // Constructor doesn't initialize all fields
    public Product(Long id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
        // Forgot to set active!
    }
    
    // Another constructor with different signature - confusing
    public Product(String name, double price, int stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.active = true;
        this.status = "ACTIVE"; // Inconsistent with boolean active
    }
    
    // Setter without validation - can set negative price!
    public void setPrice(double price) {
        this.price = price;
    }
    
    // Can set negative stock
    public void setStock(int stock) {
        this.stock = stock;
    }
    
    // Incomplete getter/setter pairs
    public boolean isActive() {
        return active;
    }
    
    // This modifies the object - side effect!
    public boolean checkAndDecrementStock(int qty) {
        if (stock >= qty) {
            stock = stock - qty;
            return true;
        }
        return false;
    }
    
    // Business logic AND formatting in entity!
    public String getDisplayPrice() {
        if (price > 1000) {
            return String.format("%.2f EUR (Expensive!)", price);
        } else if (price > 100) {
            return String.format("%.2f EUR", price);
        } else {
            return price + " EUR"; // Inconsistent formatting
        }
    }
    
    // Mixes availability check with output
    public boolean isAvailable() {
        boolean avail = active && stock > 0;
        System.out.println("Checking availability for " + name + ": " + avail); // Debug left in production!
        return avail;
    }
    
    // Hardcoded business rules
    public double calculateDiscountedPrice(double discountPercent) {
        double discounted = price - (price * discountPercent / 100);
        // BUG: No check if discount makes price negative!
        // BUG: No check if discountPercent is valid (could be > 100 or negative)
        if (category.equals("Electronics")) { // Hardcoded category
            discounted = discounted * 0.95; // Extra 5% off - not documented anywhere!
        }
        return discounted;
    }
    
    // Method that modifies global state - terrible!
    public void applySeasonalDiscount() {
        Date now = new Date();
        if (now.getMonth() == 11) { // December = month 11 (confusing!)
            this.price = this.price * 0.8; // Permanently modifies price!
        }
    }
    
    // Dead code - never used
    public void someOldMethod() {
        // This was used in version 1.0 but nobody removed it
    }
}
