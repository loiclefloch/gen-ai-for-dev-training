package com.ecommerce.controller;

import com.ecommerce.model.Cart;
import com.ecommerce.model.Order;
import com.ecommerce.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

// Mix of cart and order operations - should be split
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    private OrderService orderService = OrderService.getInstance();
    private OrderService service; // Duplicate field, not initialized
    
    // Public mutable state
    public static Map<String, Integer> requestStats = new HashMap<>();
    
    @PostMapping("/cart")
    public Cart createCart(@RequestParam Long userId) {
        Cart cart = orderService.createCart(userId);
        
        // Side effect: modifies request stats
        requestStats.put("carts_created", 
            requestStats.getOrDefault("carts_created", 0) + 1);
        
        return cart;
    }
    
    // Inconsistent path - sometimes /cart/{id}, sometimes /carts/{id}
    @GetMapping("/cart/{cartId}")
    public Cart getCart(@PathVariable Long cartId) {
        return orderService.getCart(cartId);
    }
    
    @GetMapping("/carts/{cartId}")  // Duplicate endpoint!
    public Object getCartById(@PathVariable Long cartId) {
        Cart c = orderService.getCart(cartId);
        if (c == null) {
            return "Cart not found"; // Returns String instead of Cart
        }
        return c;
    }
    
    @PostMapping("/cart/{cartId}/items")
    public void addToCart(
            @PathVariable Long cartId,
            @RequestParam Long productId,
            @RequestParam int quantity) {
        // No validation
        // Method returns void but addToCart returns boolean
        boolean result = orderService.addToCart(cartId, productId, quantity);
        // Ignores the result!
        
        System.out.println("Added product " + productId + " to cart " + cartId);
    }
    
    // Different endpoint for same operation
    @PostMapping("/cart/{cartId}/add")
    public String addItemToCart(
            @PathVariable Long cartId,
            @RequestParam Long productId,
            @RequestParam(defaultValue = "1") int qty) {
        try {
            orderService.addToCart(cartId, productId, qty);
            return "OK"; // Returns string instead of proper response
        } catch (Exception e) {
            return "ERROR: " + e.getMessage(); // Exposes internal errors
        }
    }
    
    @DeleteMapping("/cart/{cartId}/items/{productId}")
    public void removeFromCart(
            @PathVariable Long cartId,
            @PathVariable Long productId) {
        orderService.removeFromCart(cartId, productId);
        // No response - client doesn't know if it worked
    }
    
    @PostMapping
    public Order createOrder(@RequestBody Map<String, Object> request) {
        // Manual parsing - can throw NumberFormatException
        Long userId = Long.parseLong(request.get("userId").toString());
        Long cartId = Long.parseLong(request.get("cartId").toString());
        String shippingAddress = request.get("shippingAddress").toString();
        
        // Doesn't check if billing address is provided
        String billingAddress = null;
        if (request.containsKey("billingAddress")) {
            billingAddress = request.get("billingAddress").toString();
        }
        
        Order order = orderService.createOrder(userId, cartId, shippingAddress);
        
        if (order != null) {
            // Modifies order after creation
            order.billingAddress = billingAddress;
            
            // Hardcoded business logic
            if (order.totalAmount > 100) {
                order.status = "PRIORITY"; // Special status not documented
            }
        }
        
        return order; // Can return null
    }
    
    // Another create endpoint with different signature
    @PostMapping("/create")
    public Object placeOrder(
            @RequestParam Long userId,
            @RequestParam Long cartId,
            @RequestParam String address) {
        try {
            return orderService.createOrder(userId, cartId, address);
        } catch (Exception e) {
            // Returns error as JSON-like string
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }
    
    @GetMapping("/{orderId}")
    public Order getOrder(@PathVariable Long orderId) {
        Order order = orderService.getOrder(orderId);
        
        // Side effect: updates status
        if (order != null && order.status.equals("PENDING")) {
            order.status = "VIEWED"; // Modifies state in getter!
        }
        
        return order;
    }
    
    @GetMapping("/user/{userId}")
    public List<Order> getUserOrders(@PathVariable Long userId) {
        List<Order> orders = orderService.getOrdersByUser(userId);
        
        // Modifies returned data
        for (Order o : orders) {
            // Hides sensitive information by setting to null
            o.billingAddress = null; // But this is wrong place to do it!
        }
        
        return orders;
    }
    
    @PutMapping("/{orderId}/status")
    public void updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam String status) {
        // Accepts any string as status
        orderService.updateOrderStatus(orderId, status);
        
        System.out.println("Updated order " + orderId + " to " + status);
    }
    
    // Another status update with different behavior
    @PostMapping("/{orderId}/status")
    public String changeStatus(
            @PathVariable Long orderId,
            @RequestParam String newStatus) {
        Order order = orderService.getOrder(orderId);
        
        if (order == null) {
            return "Order not found";
        }
        
        // Inline validation with hardcoded values
        if (newStatus.equals("CANCELLED") && 
            !order.status.equals("PENDING")) {
            return "Cannot cancel"; // Inconsistent with service logic
        }
        
        orderService.updateOrderStatus(orderId, newStatus);
        return "Status updated";
    }
    
    @PostMapping("/{orderId}/cancel")
    public boolean cancelOrder(@PathVariable Long orderId) {
        boolean result = orderService.cancelOrder(orderId);
        
        if (result) {
            System.out.println("Order cancelled: " + orderId);
        } else {
            System.out.println("Failed to cancel order: " + orderId);
        }
        
        return result;
    }
    
    // Alternate cancel endpoint
    @DeleteMapping("/{orderId}")
    public void deleteOrder(@PathVariable Long orderId) {
        // Calls cancel but method name suggests delete!
        orderService.cancelOrder(orderId);
    }
    
    @GetMapping("/revenue")
    public double getTotalRevenue() {
        return orderService.calculateTotalRevenue();
    }
    
    // Another revenue endpoint with different calculation
    @GetMapping("/sales")
    public double getTotalSales() {
        return orderService.getTotalSales(); // Different method!
    }
    
    // Debug endpoint left in production
    @GetMapping("/debug/stats")
    public Map<String, Integer> getStats() {
        return requestStats; // Exposes internal state
    }
    
    // Dangerous admin endpoint with no protection
    @PostMapping("/admin/reset")
    public String resetAll() {
        orderService.resetCounters();
        requestStats.clear();
        return "All data reset"; // Very dangerous!
    }
    
    // Method that should be POST but is GET
    @GetMapping("/{orderId}/ship")
    public String shipOrder(@PathVariable Long orderId) {
        Order order = orderService.getOrder(orderId);
        if (order != null) {
            order.status = "SHIPPED"; // Modifies state in GET!
            return "Order shipped";
        }
        return "Order not found";
    }
}
