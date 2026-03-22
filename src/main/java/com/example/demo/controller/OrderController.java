package com.example.demo.controller;

import com.example.demo.dto.OrderRequestDto;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/order")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Customer places order
    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequestDto dto) {
        return ResponseEntity.ok(orderService.placeOrder(dto));
    }

    // Customer gets their orders
    @GetMapping("/my/{userId}")
    public ResponseEntity<?> getMyOrders(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getMyOrders(userId));
    }

    // Owner/Chef gets café orders
    @GetMapping("/cafe/{cafeId}")
    public ResponseEntity<?> getCafeOrders(@PathVariable Long cafeId) {
        return ResponseEntity.ok(orderService.getCafeOrders(cafeId));
    }

    // Chef/Waiter updates order status
    @PutMapping("/status/{orderId}")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long orderId,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, body.get("status")));
    }
}