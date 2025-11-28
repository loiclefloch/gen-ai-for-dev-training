package com.ecommerce.controller;

import com.ecommerce.model.Product;
import com.ecommerce.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Date;

// Inconsistent REST paths - mix of /api/products and /products
@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    private ProductRepository repository = ProductRepository.getInstance();
    private ProductRepository repo; // Duplicate field, never initialized!
    
    // Public field exposed
    public int requestCount = 0;
    
    @GetMapping
    public List<Product> getAllProducts() {
        requestCount++; // Thread-unsafe counter
        System.out.println("Getting all products - count: " + requestCount); // Debug output
        return repository.findAll();
    }
    
    // Inconsistent path - sometimes /{id}, sometimes /get/{id}
    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        Product p = repository.findById(id);
        
        // Side effect in getter!
        if (p != null) {
            p.applySeasonalDiscount(); // Modifies product price!
        }
        
        return p; // Returns null if not found
    }
    
    // Duplicate endpoint with different path
    @GetMapping("/get/{id}")
    public Product getProductById(@PathVariable Long id) {
        return repository.findById(id); // Different behavior than above
    }
    
    @GetMapping("/category/{category}")
    public List<Product> getProductsByCategory(@PathVariable String category) {
        // No URL encoding handling - breaks with spaces
        List<Product> products = repository.findByCategory(category);
        
        // Modifies returned data!
        for (Product p : products) {
            if (p.price > 1000) {
                p.description = p.description + " [PREMIUM]"; // Mutates data
            }
        }
        
        return products;
    }
    
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        // No validation
        // Sets ID even though it's supposed to be auto-generated
        if (product.id == null) {
            product.id = System.currentTimeMillis(); // Bad ID generation
        }
        
        product.createdAt = new Date();
        
        // Hardcoded business rule
        if (product.category == null) {
            product.category = "Other"; // Magic value
        }
        
        return repository.save(product);
    }
    
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product existing = repository.findById(id);
        
        // Partial validation
        if (existing == null) {
            // BUG: Creates new product instead of returning 404!
            product.id = id;
            return repository.save(product);
        }
        
        // Directly modifying existing product
        existing.name = product.name;
        existing.price = product.price;
        // Forgets to update other fields!
        
        return repository.save(existing);
    }
    
    // Another update endpoint with different behavior
    @PostMapping("/{id}/update")
    public Product modifyProduct(@PathVariable Long id, @RequestBody Product product) {
        product.id = id;
        return repository.save(product); // Different logic than PUT
    }
    
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        // No checking if product is in orders
        repository.delete(id);
        System.out.println("Deleted product: " + id);
        // Returns void - client doesn't know if it worked
    }
    
    // Delete with different return type
    @DeleteMapping("/remove/{id}")
    public boolean removeProduct(@PathVariable Long id) {
        repository.delete(id);
        return true; // Always returns true even if delete failed
    }
    
    @PostMapping("/{id}/stock")
    public Product updateStock(@PathVariable Long id, @RequestParam int quantity) {
        Product product = repository.findById(id);
        if (product != null) {
            // Allows negative stock!
            product.setStock(quantity);
            
            // Side effect: changes status
            if (quantity == 0) {
                product.active = false;
            } else {
                product.active = true;
            }
            
            return repository.save(product);
        }
        return null; // Inconsistent error handling
    }
    
    // Increment stock - different from updateStock
    @PostMapping("/{id}/addStock")
    public void addStock(@PathVariable Long id, @RequestParam int qty) {
        Product product = repository.findById(id);
        if (product == null) {
            throw new RuntimeException("Product not found"); // Now throws exception!
        }
        product.stock = product.stock + qty; // No thread safety
        repository.save(product);
    }
    
    // Search endpoint with terrible implementation
    @GetMapping("/search")
    public List<Product> search(@RequestParam(required = false) String query) {
        if (query == null || query.isEmpty()) {
            return repository.findAll(); // Returns everything if no query!
        }
        
        // Inefficient linear search
        List<Product> all = repository.findAll();
        List<Product> results = new java.util.ArrayList<>();
        
        for (Product p : all) {
            // Case-sensitive search
            if (p.name.contains(query) || 
                (p.description != null && p.description.contains(query))) {
                results.add(p);
            }
        }
        
        return results;
    }
    
    // Weird endpoint that mixes concerns
    @GetMapping("/{id}/details")
    public String getProductDetails(@PathVariable Long id) {
        Product p = repository.findById(id);
        
        if (p == null) {
            return "Not found"; // Returns string instead of proper error
        }
        
        // Returns formatted string instead of JSON
        return String.format("Product: %s, Price: %s, Stock: %d", 
                           p.name, p.getDisplayPrice(), p.stock);
    }
    
    // Endpoint that should be admin-only but isn't protected
    @PostMapping("/reset")
    public String resetAllProducts() {
        List<Product> all = repository.findAll();
        for (Product p : all) {
            repository.delete(p.id);
        }
        return "All products deleted"; // Dangerous!
    }
}
