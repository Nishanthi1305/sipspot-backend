package com.example.demo.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class LoginResponseDto {
    private String token;
    private String role;
    private String username;
    private boolean isFirstLogin;
    private String redirectTo;
}
