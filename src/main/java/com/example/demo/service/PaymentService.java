package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.Payment;
import com.example.demo.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    // ── Create Payment Order ─────────────────────────────────────────
    public PaymentResponseDto createOrder(PaymentOrderDto dto) {
        Payment payment = new Payment();
        payment.setUserId(dto.getUserId());
        payment.setAmount(dto.getAmount());
        payment.setType(dto.getType());
        payment.setBookingId(dto.getBookingId());
        payment.setOrderId(dto.getOrderId());
        payment.setStatus("PENDING");

        // Generate mock order ID
        String mockOrderId = "ORDER_" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();
        payment.setRazorpayOrderId(mockOrderId);
        paymentRepository.save(payment);

        return new PaymentResponseDto(mockOrderId, dto.getAmount(), "INR", "MOCK_KEY");
    }

    // ── Process Payment ──────────────────────────────────────────────
    public Map<String, Object> processPayment(PaymentVerifyDto dto) {
        Map<String, Object> response = new HashMap<>();

        Payment payment = paymentRepository
            .findByRazorpayOrderId(dto.getRazorpayOrderId())
            .orElseThrow(() -> new RuntimeException("Payment not found!"));

        // Simulate payment success
        payment.setRazorpayPaymentId("PAY_" + UUID.randomUUID().toString().substring(0, 12).toUpperCase());
        payment.setRazorpaySignature("SIG_" + UUID.randomUUID().toString().substring(0, 12).toUpperCase());
        payment.setStatus("SUCCESS");
        payment.setPaidAt(LocalDateTime.now());
        paymentRepository.save(payment);

        response.put("success", true);
        response.put("message", "Payment successful!");
        response.put("paymentId", payment.getRazorpayPaymentId());
        response.put("amount", payment.getAmount());
        response.put("type", payment.getType());
        return response;
    }

    // ── Get Payment History ──────────────────────────────────────────
    public Object getUserPayments(Long userId) {
        return paymentRepository.findByUserId(userId);
    }
}