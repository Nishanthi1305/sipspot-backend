package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;
    

    public LoginResponseDto login(LoginDto dto) {
        

        // Find user
        User user = userRepository.findByUsername(dto.getUsername())
            .orElseThrow(() -> new RuntimeException("Invalid username or password!"));

        // Check password
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password!");
        }

        // Check account active
        if (!user.isActive()) {
            throw new RuntimeException("Your account has been deactivated. Contact admin.");
        }

        // Check email verified
        if (!user.isEmailVerified()) {
            throw new RuntimeException("Please verify your email first!");
        }

        // Check profile completed
        if (!user.isProfileCompleted()) {
            throw new RuntimeException("Please complete your profile first!");
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

        // Decide redirect based on role
        // Find the switch case in AuthService.java
String redirectTo = switch (user.getRole()) {
    case "ROLE_ADMIN"      -> "/admin/dashboard";
    case "ROLE_CAFE_OWNER" -> "/owner/dashboard";
    case "ROLE_CHEF"       -> "/owner/dashboard";  // handled inside owner dashboard
    case "ROLE_WAITER"     -> "/owner/dashboard";  // handled inside owner dashboard
    case "ROLE_CUSTOMER"   -> "/customer/home";
    default -> "/login";
};

        return new LoginResponseDto(
            token,
            user.getRole(),
            user.getUsername(),
            user.isFirstLogin(),
            redirectTo
        );
    }

    public String resetPassword(ResetPasswordDto dto) {

        User user = userRepository.findByUsername(dto.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found!"));

        // Verify temp password
        if (!passwordEncoder.matches(dto.getTemporaryPassword(), user.getPassword())) {
            throw new RuntimeException("Incorrect temporary password!");
        }

        // Check new password match
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match!");
        }

        // Validate new password strength
        if (dto.getNewPassword().length() < 8) {
            throw new RuntimeException("Password must be at least 8 characters!");
        }

        // Save new password
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        user.setFirstLogin(false);
        userRepository.save(user);

        return "Password reset successful! You can now login with your new password.";
    }
}