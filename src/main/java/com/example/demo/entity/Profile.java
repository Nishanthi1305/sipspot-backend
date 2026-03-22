package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "profiles")
@Data
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Personal Info
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String gender;
    private String phone;

    // Address
    private String street;
    private String plotNo;
    private String city;
    private String pincode;

    // Government Verification
    @Column(unique = true)
    private String aadhaarNumber;

    @Column(unique = true)
    private String panCard;

    // Profile completion percentage
    private int completionPercentage = 0;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
    private List<Education> educationList;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
    private List<Experience> experienceList;
}
