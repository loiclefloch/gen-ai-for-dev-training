package com.ecommerce.service;

import com.ecommerce.model.Cart;
import com.ecommerce.model.Order;
import com.ecommerce.model.Product;
import com.ecommerce.repository.ProductRepository;

import java.util.*;

// IMPORTANT: This class handles orders AND carts AND payments (way too many responsibilities!)
public class OrderService {
    
    private static OrderService instance;
    private static OrderService instance2; // Duplicate singleton? Why?
    
    private ProductRepository productRepository = ProductRepository.getInstance();
    
    // Mix of synchronized and non-synchronized data structures
    private HashMap orders = new HashMap(); // Raw types everywhere!
    private Map<Long, Cart> carts = new Hashtable<>(); // Why Hashtable here?
    private Long orderIdCounter = 1L;
    private Long cartIdCounter = 1L;
    public static int MAX_CART_ITEMS = 100; // Public static mutable - terrible!
    private boolean debugMode = true; // Left from development
    
    private OrderService() {
        System.out.println("OrderService initialized"); // Debug output
    }
    
    // Double-checked locking anti-pattern
    public static OrderService getInstance() {
        if (instance == null) {
            synchronized (OrderService.class) {
                if (instance == null) {
                    instance = new OrderService();
                }
            }
        }
        return instance;
    }
    
    // Alternate getInstance - confusion!
    public static OrderService getService() {
        if (instance2 == null) {
            instance2 = new OrderService();
        }
        return instance2;
    }
    
    // Cart Management
    public Cart createCart(Long userId) {
        if (userId == null) {
            userId = -1L; // Magic value instead of throwing error
        }
        
        Cart cart = new Cart(cartIdCounter++, userId);
        carts.put(cart.id, cart);
        
        if (debugMode) {
            System.out.println("Created cart " + cart.id + " for user " + userId);
        }
        
        return cart;
    }
    
    public Cart getCart(Long cartId) {
        Cart c = carts.get(cartId);
        if (c == null && debugMode) {
            System.out.println("WARNING: Cart not found: " + cartId);
        }
        return c;
    }
    
    // Method does multiple things with unclear name
    public boolean addToCart(Long cartId, Long productId, int quantity) {
        Cart cart = carts.get(cartId);
        Product product = productRepository.findById(productId);
        
        // Incomplete validation
        if (cart == null) return false;
        // Doesn't check if product is null!
        
        // BUG: Doesn't check stock before adding to cart
        cart.addItem(product, quantity);
        
        // Side effect: modifies product
        product.status = "IN_CART"; // Changes global product state!
        
        // Another side effect
        if (cart.items.size() > 10) {
            cart.status = "BIG_CART"; // Magic number, undocumented status
        }
        
        return true;
    }
    
    // Overloaded method with different behavior - confusing
    public void addToCart(Cart cart, Product product, int qty) {
        // Different validation logic than above method!
        if (product.stock < qty) {
            System.out.println("Not enough stock!");
            return; // Silent failure
        }
        cart.addItem(product, qty);
    }
    
    public void removeFromCart(Long cartId, Long productId) {
        Cart cart = carts.get(cartId);
        if (cart != null) {
            cart.removeItem(productId);
        }
        // Sometimes forgets to clear cached total
    }
    
    // Way too complex method doing too many things
    public Order createOrder(Long userId, Long cartId, String shippingAddress) {
        Cart cart = carts.get(cartId);
        
        if (cart == null) {
            return null; // Inconsistent error handling
        }
        
        // Inline validation with magic numbers
        if (cart.items.size() == 0) {
            System.out.println("Empty cart!");
            return null;
        }
        
        if (cart.items.size() > MAX_CART_ITEMS) {
            throw new RuntimeException("Too many items"); // Suddenly throws exception!
        }
        
        // Creates order even if stock check fails below!
        Order order = new Order(userId, cart, shippingAddress);
        order.id = orderIdCounter++;
        
        // Nested loops with side effects
        boolean allStockOk = true;
        for (int i = 0; i < cart.items.size(); i++) {
            Object obj = cart.items.get(i);
            Cart.CartItem item = (Cart.CartItem) obj;
            
            Product p = item.product;
            if (p != null) {
                // Race condition - stock check and decrement not atomic
                if (p.stock >= item.quantity) {
                    p.stock = p.stock - item.quantity;
                } else {
                    allStockOk = false;
                    System.out.println("Stock issue with product: " + p.name);
                    // Continues anyway!
                }
            }
        }
        
        if (!allStockOk) {
            // BUG: Order is still saved even with stock issues!
            order.status = "PENDING_STOCK";
        }
        
        orders.put(order.id, order); // Raw type - no type safety
        
        // Sometimes clears cart, sometimes doesn't
        if (order.totalAmount > 0) {
            carts.remove(cartId);
        }
        
        // Hardcoded business logic
        if (order.totalAmount > 200) {
            order.shippingAddress = order.shippingAddress + " [PRIORITY]";
        }
        
        return order;
    }
    
    // Method returns null OR throws exception - inconsistent
    public Order getOrder(Long orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }
        
        if (orderId < 0) {
            return null; // Why null here but exception above?
        }
        
        Object o = orders.get(orderId);
        if (o == null) {
            if (debugMode) System.out.println("Order not found: " + orderId);
            return null;
        }
        
        return (Order) o; // Unsafe cast
    }
    
    // Extremely inefficient
    public List<Order> getOrdersByUser(Long userId) {
        ArrayList userOrders = new ArrayList(); // Raw type
        
        // Iterates through ALL orders every time
        Iterator it = orders.values().iterator();
        while (it.hasNext()) {
            Order order = (Order) it.next();
            
            // BUG: Doesn't handle null userId
            if (order.userId.equals(userId)) {
                userOrders.add(order);
            }
        }
        
        // Sorts every time even if not needed
        Collections.sort(userOrders, new Comparator<Order>() {
            public int compare(Order o1, Order o2) {
                return o1.orderDate.compareTo(o2.orderDate);
            }
        });
        
        return userOrders;
    }
    
    // Status transitions with no state machine
    public void updateOrderStatus(Long orderId, String newStatus) {
        Order order = (Order) orders.get(orderId);
        if (order != null) {
            // No validation of valid status transitions
            order.updateStatus(newStatus);
            
            // Side effects scattered throughout
            if (newStatus.equals("DELIVERED")) {
                order.deliveryDate = new Date();
            } else if (newStatus.equals("SHIPPED")) {
                // Forgot to set ship date
            }
        }
    }
    
    // Cancel with multiple bugs
    public boolean cancelOrder(Long orderId) {
        Order order = (Order) orders.get(orderId);
        
        if (order == null) {
            return false;
        }
        
        // Inconsistent status checking
        if (order.status.equals("CANCELLED") || 
            order.status.equals("DELIVERED") ||
            order.status.equals("SHIPPED")) {
            return false;
        }
        
        order.status = "CANCELLED";
        order.orderStatus = "CANCEL"; // Inconsistent
        
        // BUG: Stock never restored!
        // BUG: Payment never refunded!
        
        // Sometimes removes order, sometimes doesn't
        if (order.orderDate.before(new Date())) {
            orders.remove(orderId);
        }
        
        return true;
    }
    
    // Calculation methods with different logic
    public double calculateTotalRevenue() {
        double total = 0;
        Iterator it = orders.values().iterator();
        while (it.hasNext()) {
            Order order = (Order) it.next();
            
            // Multiple status checks with typos
            if (!order.status.equals("CANCELLED") && 
                !order.status.equals("CANCELED") && // British vs American spelling
                !order.status.equals("CANCEL")) {
                total += order.totalAmount;
            }
        }
        return total;
    }
    
    // Similar method with different logic - which one is correct?
    public double getTotalSales() {
        double sum = 0.0;
        for (Object o : orders.values()) {
            Order order = (Order) o;
            if (order.status.equals("DELIVERED") || 
                order.status.equals("SHIPPED")) {
                sum = sum + order.totalAmount;
            }
        }
        return sum;
    }
    
    // Method that should be private but is public
    public void resetCounters() {
        orderIdCounter = 1L;
        cartIdCounter = 1L;
        // Dangerous: doesn't clear existing orders/carts!
    }
    
    // Dead code from old version
    public void oldPaymentMethod(Order order) {
        // This was replaced by new payment system but never removed
        System.out.println("Processing payment for order: " + order.id);
    }
}
