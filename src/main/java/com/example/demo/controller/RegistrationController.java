package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    // Step 0 — Initiate registration
    @PostMapping("/register/initiate")
    public ResponseEntity<?> initiateRegistration(@RequestBody RegistrationStepOneDto dto) {
        return ResponseEntity.ok(registrationService.initiateRegistration(dto));
    }

    // Email verification
    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        return ResponseEntity.ok(registrationService.verifyEmail(token));
    }

    // Step 1 — Personal info
    @PostMapping("/register/personal-info")
    public ResponseEntity<?> savePersonalInfo(@RequestBody PersonalInfoDto dto) {
        return ResponseEntity.ok(registrationService.savePersonalInfo(dto));
    }

    // Step 2 — Education
    @PostMapping("/register/education")
    public ResponseEntity<?> saveEducation(@RequestBody EducationDto dto) {
        return ResponseEntity.ok(registrationService.saveEducation(dto));
    }

    // Step 3 — Experience
    @PostMapping("/register/experience")
    public ResponseEntity<?> saveExperience(@RequestBody ExperienceDto dto) {
        return ResponseEntity.ok(registrationService.saveExperience(dto));
    }

    // Step 4 — Government verification
    @PostMapping("/register/verification")
    public ResponseEntity<?> saveVerification(@RequestBody GovernmentVerificationDto dto) {
        return ResponseEntity.ok(registrationService.saveGovernmentVerification(dto));
    }
}
