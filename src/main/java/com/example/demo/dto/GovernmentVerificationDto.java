package com.example.demo.dto;

import lombok.Data;

@Data
public class GovernmentVerificationDto {
    private Long userId;
    private String aadhaarNumber;
    private String panCard;
}
