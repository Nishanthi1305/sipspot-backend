package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "role")
    private String role;

    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified = false;

    @Column(name = "profile_completed", nullable = false)
    private boolean profileCompleted = false;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "is_first_login", nullable = false)
    private boolean firstLogin = true;

    private String verificationToken;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    // Explicit boolean getters — avoids Lombok naming conflicts
    public boolean isActive() { return active; }
    public boolean isEmailVerified() { return emailVerified; }
    public boolean isProfileCompleted() { return profileCompleted; }
    public boolean isFirstLogin() { return firstLogin; }

    // Explicit setters
    public void setActive(boolean active) { this.active = active; }
    public void setEmailVerified(boolean emailVerified) { this.emailVerified = emailVerified; }
    public void setProfileCompleted(boolean profileCompleted) { this.profileCompleted = profileCompleted; }
    public void setFirstLogin(boolean firstLogin) { this.firstLogin = firstLogin; }
}