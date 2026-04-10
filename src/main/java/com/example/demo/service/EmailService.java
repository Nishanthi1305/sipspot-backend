package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.backend.url}")
    private String backendUrl;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public void sendCredentials(String toEmail, String username, String tempPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Welcome to SipSpot ☕");

        message.setText(
            "Hello " + username + ",\n\n" +
            "Username: " + username + "\n" +
            "Password: " + tempPassword + "\n\n" +
            "Login: " + frontendUrl + "/login\n\n" +
            "SipSpot Team ☕"
        );

        mailSender.send(message);
    }

    public void sendVerificationEmail(String toEmail, String token) {
        String url = backendUrl + "/api/auth/verify?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("SipSpot Email Verification");

        message.setText(
            "Click to verify your email:\n\n" +
            url + "\n\n" +
            "Link expires in 24 hours.\n\n" +
            "SipSpot Team ☕"
        );

        mailSender.send(message);
    }
}