package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PersonalInfoDto {
    private Long userId;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String gender;
    private String phone;
    private String street;
    private String plotNo;
    private String city;
    private String pincode;
}