package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentResponseDto {
    private String razorpayOrderId;
    private Double amount;
    private String currency;
    private String keyId;
}