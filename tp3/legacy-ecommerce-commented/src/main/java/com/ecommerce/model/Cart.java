package com.ecommerce.model;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

// TODO: refactor this mess
public class Cart {
    
    public Long id;
    public Long userId;
    public ArrayList items; // Not generic! Can contain anything
    public Date createdAt;
    public Date updatedAt;
    public String status; // What is this for? Nobody knows
    private double cachedTotal = -1; // Cache that's never invalidated properly
    
    public Cart() {
        this.items = new ArrayList();
        this.createdAt = new Date();
        // Forgot to initialize status
    }
    
    public Cart(Long id, Long userId) {
        this.id = id;
        this.userId = userId;
        this.items = new ArrayList();
        this.createdAt = new Date();
        this.status = "NEW";
    }
    
    // Accepts null products, zero or negative quantities
    public void addItem(Product product, int quantity) {
        CartItem item = new CartItem();
        item.product = product;
        item.quantity = quantity;
        item.price = product.price; // Gets current price, not the price at time of adding
        items.add(item);
        this.updatedAt = new Date();
        // BUG: Doesn't update cachedTotal!
        
        // Magic number hardcoded
        if (items.size() > 10) {
            System.out.println("WARNING: Cart has more than 10 items!"); // Should be logged properly
        }
    }
    
    // Updates quantity if item exists, but sometimes adds duplicate instead
    public void updateQuantity(Long productId, int newQty) {
        boolean found = false;
        for (int i = 0; i < items.size(); i++) {
            CartItem item = (CartItem) items.get(i); // Unsafe cast!
            if (item.product.id == productId) { // Should use .equals()
                item.quantity = newQty;
                found = true;
            }
        }
        if (!found) {
            // BUG: Should throw error, instead creates new item with null product!
            CartItem item = new CartItem();
            item.quantity = newQty;
            items.add(item);
        }
    }
    
    // Inefficient nested loop
    public void removeItem(Long productId) {
        for (int i = 0; i < items.size(); i++) {
            for (int j = 0; j < items.size(); j++) { // Why double loop?!
                CartItem item = (CartItem) items.get(i);
                if (item.product != null && item.product.id.equals(productId)) {
                    items.remove(i);
                    // BUG: Doesn't break after removing, can cause IndexOutOfBounds
                }
            }
        }
        this.updatedAt = new Date();
        cachedTotal = -1; // At least this one is cleared
    }
    
    // Calculation logic all over the place
    public double calculateTotal() {
        if (cachedTotal >= 0) {
            return cachedTotal; // Returns stale cache
        }
        
        double total = 0;
        for (int i = 0; i < items.size(); i++) {
            Object obj = items.get(i);
            if (obj instanceof CartItem) { // Defensive check because items is not generic
                CartItem item = (CartItem) obj;
                if (item.product != null) {
                    // BUG: Uses current product price, not cart item price!
                    total += item.product.price * item.quantity;
                }
            }
        }
        
        // Hardcoded business rule with magic number
        if (total > 100) {
            total = total * 0.95; // 5% discount - not documented!
        }
        
        cachedTotal = total;
        return total;
    }
    
    // Duplicate calculation with different logic!
    public double getTotal() {
        double sum = 0.0;
        for (Object o : items) {
            CartItem ci = (CartItem) o;
            sum = sum + (ci.price * ci.quantity); // Different from calculateTotal!
        }
        return sum;
    }
    
    public int getTotalItems() {
        int count = 0;
        for (int i = 0; i < items.size(); i++) {
            CartItem item = (CartItem) items.get(i);
            count += item.quantity;
        }
        return count;
    }
    
    // Confusing method name
    public boolean isEmpty() {
        return getTotalItems() == 0; // Could be true even if items.size() > 0!
    }
    
    // Weird helper that modifies state
    public void clearEmptyItems() {
        ArrayList newItems = new ArrayList();
        for (int i = 0; i < items.size(); i++) {
            CartItem item = (CartItem) items.get(i);
            if (item.quantity > 0) {
                newItems.add(item);
            }
        }
        items = newItems;
        System.out.println("Cleared empty items"); // Debug output
    }
    
    // Inner class with inconsistent style
    public static class CartItem {
        public Long id;
        public Product product; // Can be null!
        public int quantity;
        public double price;
        public String notes; // Field that's never used
        
        public CartItem() {}
        
        // Weird helper method in inner class
        public double getSubtotal() {
            if (product == null) return 0;
            return price * quantity;
        }
    }
}
