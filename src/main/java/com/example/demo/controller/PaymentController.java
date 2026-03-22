package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "http://localhost:5173")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody PaymentOrderDto dto) {
        return ResponseEntity.ok(paymentService.createOrder(dto));
    }

    @PostMapping("/process")
    public ResponseEntity<?> processPayment(@RequestBody PaymentVerifyDto dto) {
        return ResponseEntity.ok(paymentService.processPayment(dto));
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<?> getHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(paymentService.getUserPayments(userId));
    }
}