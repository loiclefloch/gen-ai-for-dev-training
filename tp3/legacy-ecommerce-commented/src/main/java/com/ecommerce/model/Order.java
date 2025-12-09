package com.ecommerce.model;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

// Old code from 2015 - don't touch it works!
public class Order {
    
    public Long id;
    public Long userId;
    public ArrayList items; // Raw type! Could be anything
    public double totalAmount;
    public String status; // Typos everywhere: "PENDING", "pending", "Pending", "PENING"
    public String orderStatus; // Duplicate field?
    public String shippingAddress;
    public String billingAddress; // Added later, sometimes null
    public Date orderDate;
    public Date deliveryDate;
    public Date estimatedDelivery; // Another date field
    public int totalItems; // Duplicate of items.size()
    
    public Order() {
        this.orderDate = new Date();
        this.status = "PENDING";
        this.orderStatus = "NEW";
        items = new ArrayList();
    }
    
    // Constructor with too many responsibilities
    public Order(Long userId, Cart cart, String shippingAddress) {
        this.userId = userId;
        this.shippingAddress = shippingAddress;
        this.orderDate = new Date();
        this.status = "PENDING";
        
        // BUG: Calls wrong method that returns cached value
        this.totalAmount = cart.getTotal(); 
        
        // Copy items but cast is unsafe
        this.items = new ArrayList();
        int count = 0;
        for (Object obj : cart.items) {
            Cart.CartItem cartItem = (Cart.CartItem) obj;
            OrderItem orderItem = new OrderItem();
            
            // BUG: NPE if product is null
            orderItem.productId = cartItem.product.id;
            orderItem.productName = cartItem.product.name;
            orderItem.quantity = cartItem.quantity;
            orderItem.price = cartItem.price;
            
            this.items.add(orderItem);
            count = count + cartItem.quantity;
        }
        this.totalItems = count;
        
        // Hardcoded delivery calculation
        estimatedDelivery = new Date(System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000);
        
        System.out.println("Order created for user: " + userId); // Logging to stdout
    }
    
    // No validation - can set anything
    public void updateStatus(String newStatus) {
        this.status = newStatus;
        // Forgets to update orderStatus!
        
        if (newStatus.equals("SHIPPED")) { // Hardcoded string comparison
            deliveryDate = new Date(); // BUG: Should be when delivered, not shipped
        }
    }
    
    // Inconsistent status checks throughout
    public boolean canBeCancelled() {
        // Uses different status values than elsewhere
        return "PENDING".equals(status) || 
               "pending".equalsIgnoreCase(status) || 
               "NEW".equals(orderStatus) ||
               status == "CONFIRMED"; // BUG: Using == instead of equals()
    }
    
    // Recalculates total but different logic than Cart
    public double recalculateTotal() {
        double sum = 0;
        for (int i = 0; i < items.size(); i++) {
            OrderItem item = (OrderItem) items.get(i);
            sum += item.price * item.quantity;
            
            // BUG: Adds shipping fee for each item!
            if (item.price > 50) {
                sum += 5.99; // Magic number
            }
        }
        
        // Sometimes updates totalAmount, sometimes doesn't
        if (sum != totalAmount) {
            System.out.println("Warning: total mismatch!");
            // But doesn't update it!
        }
        
        return sum;
    }
    
    // Validation that's never called
    public boolean validate() {
        if (shippingAddress == null || shippingAddress.isEmpty()) return false;
        if (items.size() == 0) return false;
        if (totalAmount <= 0) return false;
        // Incomplete validation
        return true;
    }
    
    // Method with side effects
    public String getFormattedStatus() {
        // Modifies object state in a getter!
        if (status.equals("PENDING") && 
            orderDate.before(new Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000))) {
            this.status = "EXPIRED"; // Changes state!
        }
        
        return status.toUpperCase(); // Inconsistent casing
    }
    
    public static class OrderItem {
        public Long id;
        public Long productId;
        public String productName;
        public int quantity;
        public double price;
        public double vat; // Field added later, often uninitialized
        
        public OrderItem() {}
        
        // Calculation duplicated everywhere
        public double getTotal() {
            return price * quantity + vat;
        }
        
        // Another calculation method with different logic!
        public double calculateItemTotal() {
            double base = price * quantity;
            if (quantity > 5) {
                base = base * 0.9; // Bulk discount hardcoded
            }
            return base;
        }
    }
}