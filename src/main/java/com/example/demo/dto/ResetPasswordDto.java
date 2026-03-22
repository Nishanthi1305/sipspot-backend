package com.example.demo.dto;

import lombok.Data;

@Data
public class ResetPasswordDto {
    private String username;
    private String temporaryPassword;
    private String newPassword;
    private String confirmPassword;
}
