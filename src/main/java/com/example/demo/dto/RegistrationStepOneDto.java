package com.example.demo.dto;

import lombok.Data;

@Data
public class RegistrationStepOneDto {
    private String email;
    private String userType; // "CUSTOMER" or "CAFE_OWNER"
}
