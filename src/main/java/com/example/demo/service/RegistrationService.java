package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RegistrationService {

    @Autowired private UserRepository userRepository;
    @Autowired private ProfileRepository profileRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private EmailService emailService;
    @Autowired private PasswordEncoder passwordEncoder;

    // ─── Step 0: Initiate registration (email + role) ───────────────────
    public Map<String, Object> initiateRegistration(RegistrationStepOneDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already registered!");
        }

        // Create user with unverified status
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setUsername(generateUsername(dto.getEmail()));
        user.setPassword(passwordEncoder.encode("TEMP_" + UUID.randomUUID()));
        user.setVerificationToken(UUID.randomUUID().toString());
        user.setEmailVerified(false);
        user.setProfileCompleted(false);
        user.setFirstLogin(true);
        user.setRole(dto.getUserType().equals("CAFE_OWNER") ? "ROLE_CAFE_OWNER" : "ROLE_CUSTOMER");

        // Assign role
        Role.RoleName roleName = dto.getUserType().equals("CAFE_OWNER")
            ? Role.RoleName.ROLE_CAFE_OWNER
            : Role.RoleName.ROLE_CUSTOMER;

        Role role = roleRepository.findByName(roleName)
            .orElseThrow(() -> new RuntimeException("Role not found! Please insert roles first."));

        user.setRoles(Set.of(role));
        userRepository.save(user);

        // Send verification email
        emailService.sendVerificationEmail(user.getEmail(), user.getVerificationToken());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Verification email sent! Please verify your email to continue.");
        response.put("userId", user.getId());
        return response;
    }

    // ─── Email verification ──────────────────────────────────────────────
    public String verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token)
            .orElseThrow(() -> new RuntimeException("Invalid or expired token!"));

        user.setEmailVerified(true);
        user.setVerificationToken(null);
        userRepository.save(user);

        return "Email verified successfully! You can now complete your profile.";
    }

    // ─── Step 1: Personal Info ───────────────────────────────────────────
    public Map<String, Object> savePersonalInfo(PersonalInfoDto dto) {
        User user = userRepository.findById(dto.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found!"));

        if (!user.isEmailVerified()) {
            throw new RuntimeException("Please verify your email first!");
        }

        Profile profile = profileRepository.findByUserId(user.getId())
            .orElse(new Profile());

        profile.setUser(user);
        profile.setFirstName(dto.getFirstName());
        profile.setLastName(dto.getLastName());
        profile.setDob(dto.getDob());
        profile.setGender(dto.getGender());
        profile.setPhone(dto.getPhone());
        profile.setStreet(dto.getStreet());
        profile.setPlotNo(dto.getPlotNo());
        profile.setCity(dto.getCity());
        profile.setPincode(dto.getPincode());
        profile.setCompletionPercentage(25);

        profileRepository.save(profile);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Personal info saved!");
        response.put("nextStep", "education");
        response.put("completion", 25);
        return response;
    }

    // ─── Step 2: Education ───────────────────────────────────────────────
    public Map<String, Object> saveEducation(EducationDto dto) {
        User user = userRepository.findById(dto.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found!"));

        Profile profile = profileRepository.findByUserId(user.getId())
            .orElseThrow(() -> new RuntimeException("Please complete personal info first!"));

        List<Education> educationList = new ArrayList<>();
        for (EducationDto.EducationEntry entry : dto.getEducationList()) {
            Education edu = new Education();
            edu.setProfile(profile);
            edu.setDegree(entry.getDegree());
            edu.setInstitution(entry.getInstitution());
            edu.setFieldOfStudy(entry.getFieldOfStudy());
            edu.setStartYear(entry.getStartYear());
            edu.setEndYear(entry.getEndYear());
            educationList.add(edu);
        }

        profile.setEducationList(educationList);
        profile.setCompletionPercentage(50);
        profileRepository.save(profile);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Education details saved!");
        response.put("nextStep", "experience");
        response.put("completion", 50);
        return response;
    }

    // ─── Step 3: Experience (optional) ──────────────────────────────────
    public Map<String, Object> saveExperience(ExperienceDto dto) {
        User user = userRepository.findById(dto.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found!"));

        Profile profile = profileRepository.findByUserId(user.getId())
            .orElseThrow(() -> new RuntimeException("Please complete education first!"));

        if (dto.getExperienceList() != null && !dto.getExperienceList().isEmpty()) {
            List<Experience> experienceList = new ArrayList<>();
            for (ExperienceDto.ExperienceEntry entry : dto.getExperienceList()) {
                Experience exp = new Experience();
                exp.setProfile(profile);
                exp.setCompanyName(entry.getCompanyName());
                exp.setJobRole(entry.getJobRole());
                exp.setStartYear(entry.getStartYear());
                exp.setEndYear(entry.getEndYear());
                exp.setCurrentlyWorking(entry.isCurrentlyWorking());
                experienceList.add(exp);
            }
            profile.setExperienceList(experienceList);
        }

        profile.setCompletionPercentage(75);
        profileRepository.save(profile);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Experience saved!");
        response.put("nextStep", "verification");
        response.put("completion", 75);
        return response;
    }

    // ─── Step 4: Government Verification ────────────────────────────────
    public Map<String, Object> saveGovernmentVerification(GovernmentVerificationDto dto) {
        User user = userRepository.findById(dto.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found!"));

        Profile profile = profileRepository.findByUserId(user.getId())
            .orElseThrow(() -> new RuntimeException("Please complete previous steps first!"));

        profile.setAadhaarNumber(dto.getAadhaarNumber());
        profile.setPanCard(dto.getPanCard());
        profile.setCompletionPercentage(100);
        profileRepository.save(profile);

        // Generate credentials and send email
        String tempPassword = generateTempPassword();
        String username = user.getUsername();

        user.setPassword(passwordEncoder.encode(tempPassword));
        user.setProfileCompleted(true);
        userRepository.save(user);

        emailService.sendCredentials(user.getEmail(), username, tempPassword);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Registration complete! Check your email for login credentials.");
        response.put("completion", 100);
        return response;
    }

    // ─── Helpers ─────────────────────────────────────────────────────────
    private String generateUsername(String email) {
        String base = email.split("@")[0].replaceAll("[^a-zA-Z0-9]", "");
        return base + "_" + (int)(Math.random() * 9000 + 1000);
    }

    private String generateTempPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
