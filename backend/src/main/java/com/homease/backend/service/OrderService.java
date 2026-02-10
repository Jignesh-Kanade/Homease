package com.homease.backend.service;

import com.homease.backend.entity.Order;
import com.homease.backend.entity.OrderItem;
import com.homease.backend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order createOrderIntent(Double amount) {
        Order order = new Order();
        order.setAmount(amount);
        order.setPaymentStatus("PENDING"); // Internal status
        order.setTrackingStatus("order_placed");
        // Create a dummy Razorpay order ID
        String razorpayOrderId = "order_" + UUID.randomUUID().toString().substring(0, 10);
        order.setId(System.currentTimeMillis()); // Temporary ID or let DB handle it
        // We'll return a simple object or just the ID but frontend expects { id: ... }
        // mapped to Razorpay order_id
        // We'll create a transient object or just reuse Order

        // In a real integration, we'd call RazorpayClient.orders.create()

        // For now, we return a mock response structure in the controller
        return order;
    }

    public Order placeOrder(Order orderData, List<OrderItem> items) {
        // Save order
        orderData.setTrackingStatus("order_placed");
        orderData.setPaymentStatus("COMPLETED");
        orderData.setPaymentMethod("Razorpay"); // Default
        orderData.setPaymentDate(LocalDateTime.now());

        Order savedOrder = orderRepository.save(orderData);

        for (OrderItem item : items) {
            item.setOrder(savedOrder);
        }
        savedOrder.setOrderItems(items);

        return orderRepository.save(savedOrder);
    }

    public List<Order> getUserOrders(String email) {
        return orderRepository.findByEmail(email);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public Order updateStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setTrackingStatus(status);
            return orderRepository.save(order);
        }
        return null; // or throw exception
    }
}
