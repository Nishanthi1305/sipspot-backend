package com.example.demo.dto;

import lombok.Data;

@Data
public class PaymentOrderDto {
    private Long userId;
    private Double amount;
    private String type; // "BOOKING" or "ORDER"
    private Long bookingId;
    private Long orderId;
}

