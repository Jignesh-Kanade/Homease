package com.homease.backend.controller;

import com.homease.backend.entity.Order;
import com.homease.backend.entity.OrderItem;
import com.homease.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*") // Allow all origins explicitly
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> payload) {
        try {
            System.out.println("Received create-order request: " + payload);

            Double amount = 0.0;
            try {
                if (payload.get("amount") != null) {
                    Object amountObj = payload.get("amount");
                    if (amountObj instanceof Number) {
                        amount = ((Number) amountObj).doubleValue();
                    } else {
                        amount = Double.parseDouble(amountObj.toString());
                    }
                }
            } catch (Exception e) {
                System.out.println("Failed to parse amount, defaulting to 200.0");
                amount = 200.0;
            }

            if (amount <= 0)
                amount = 200.0;

            // Mock Razorpay Order Creation
            String razorpayOrderId = "order_" + System.currentTimeMillis();
            System.out.println("Created mock order: " + razorpayOrderId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("id", razorpayOrderId);
            response.put("amount", amount);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error in create-order: ");
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Server Error: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @PostMapping("/verify-payment")
    public ResponseEntity<?> verifyPayment(@RequestBody Map<String, Object> payload) {
        try {
            // Extract Data
            String razorpayOrderId = (String) payload.get("orderId");
            String paymentId = (String) payload.get("paymentId");
            Double amount = Double.valueOf(payload.get("amount").toString());

            Map<String, Object> userDetails = (Map<String, Object>) payload.get("userDetails");
            List<Map<String, Object>> services = (List<Map<String, Object>>) payload.get("services");

            Order order = new Order();
            order.setOrderId(razorpayOrderId); // Store Razorpay Order ID for reference
            order.setPaymentId(paymentId);
            order.setAmount(amount);

            if (userDetails != null) {
                order.setFirstName((String) userDetails.get("firstName"));
                order.setLastName((String) userDetails.get("lastName"));
                order.setEmail((String) userDetails.get("email"));
                order.setPhone((String) userDetails.get("phone"));
                order.setStreet((String) userDetails.get("street"));
                order.setCity((String) userDetails.get("city"));
                order.setState((String) userDetails.get("state"));
                order.setPincode((String) userDetails.get("pincode"));
                order.setServiceDate((String) userDetails.get("serviceDate"));

                // Set UserId if email matches
                order.setUserId((String) userDetails.get("email"));
            }

            List<OrderItem> items = new ArrayList<>();
            if (services != null) {
                for (Map<String, Object> svc : services) {
                    OrderItem item = new OrderItem();
                    // Frontend sends 'name' as key (serviceId)
                    item.setServiceId((String) svc.get("name"));
                    item.setQuantity((Integer) svc.get("quantity"));
                    // We might want to fetch actual name/price using ServiceRepository, keeping it
                    // simple
                    item.setName("Service " + item.getServiceId());
                    item.setPrice(0.0); // Placeholder
                    items.add(item);
                }
            }

            Order savedOrder = orderService.placeOrder(order, items);

            // Return OTP for admin as requested by frontend console.log
            String otp = "1234"; // Mock OTP
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Order Placed");
            response.put("otp", otp);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/user/{email}")
    public List<Order> getUserOrders(@PathVariable String email) {
        return orderService.getUserOrders(email);
    }

    @GetMapping("")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        if (order != null)
            return ResponseEntity.ok(order);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/tracking")
    public ResponseEntity<?> getOrderTracking(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        if (order != null)
            return ResponseEntity.ok(order); // Return order itself as it has status
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String newStatus = payload.get("status");
        if (newStatus == null) {
            // Admin frontend sends 'trackingStatus' often, let's check that too
            newStatus = payload.get("trackingStatus");
        }

        if (newStatus != null) {
            Order updatedOrder = orderService.updateStatus(id, newStatus);
            if (updatedOrder != null) {
                return ResponseEntity.ok(updatedOrder);
            }
        }
        return ResponseEntity.notFound().build();
    }
}
