package com.homease.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Entity
@Data
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // To display nicely like "Order #12345"
    private String orderId; 

    private String userId; // Email or User ID

    private Double amount;
    
    private Double shippingCost;
    private Double tax;
    private Double discount;

    // Address Details embedded or flattened
    private String street;
    private String city;
    private String state;
    private String pincode;
    
    // User details snapshot
    private String firstName;
    private String lastName;
    private String phone;
    private String email; 
    
    // Payment
    private String paymentId;
    private String paymentMethod;
    private String paymentStatus; // PENDING, COMPLETED, FAILED
    private LocalDateTime paymentDate;

    // Tracking
    private String trackingStatus; // order_placed, confirmed, processing, dispatched, out_for_delivery, delivered, cancelled
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    private String serviceDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (orderId == null) {
            orderId = String.valueOf(System.currentTimeMillis());
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
