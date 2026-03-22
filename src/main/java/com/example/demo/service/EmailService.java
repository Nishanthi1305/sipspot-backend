package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendCredentials(String toEmail, String username, String tempPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Welcome to SipSpot ☕ — Your Login Credentials");
        message.setText(
            "Hello " + username + ",\n\n" +
            "Welcome to SipSpot! Your account has been created successfully.\n\n" +
            "Your login credentials:\n" +
            "Username : " + username + "\n" +
            "Password : " + tempPassword + "\n\n" +
            "Please login and reset your password immediately.\n\n" +
            "Login here: http://localhost:5173/login\n\n" +
            "regards,\n" +
            "SipSpot Team ☕"
        );
        mailSender.send(message);
    }

    public void sendVerificationEmail(String toEmail, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("SipSpot — Verify Your Email");
        message.setText(
            "Please verify your email by clicking the link below:\n\n" +
            "http://localhost:8080/api/auth/verify?token=" + token + "\n\n" +
            "This link expires in 24 hours.\n\n" +
            "regards,\n" +
            "SipSpot Team ☕"
        );
        mailSender.send(message);
    }
}
