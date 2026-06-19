package com.ecommerce.backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.backend.entity.Order;
import com.ecommerce.backend.repository.OrderRepository;

@RestController
@RequestMapping("/api/orders")

public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderRepository.findAll());
    }

    
    @PostMapping("/add")
    public ResponseEntity<Order> addOrder(@RequestBody Order order) {
        Order savedOrder = orderRepository.save(order);
        return ResponseEntity.ok(savedOrder);
    }

  
    @GetMapping("/metrics")
    public ResponseEntity<Map<String, Object>> getOrderMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        long totalOrders = orderRepository.count();
        double totalRevenue = orderRepository.calculateTotalRevenue();
        
        long deliveredOrders = orderRepository.countByStatus("Delivered");

        metrics.put("totalRevenue", totalRevenue);
        metrics.put("totalOrders", totalOrders);
        metrics.put("deliveredOrders", deliveredOrders);

        return ResponseEntity.ok(metrics);
    }
}
