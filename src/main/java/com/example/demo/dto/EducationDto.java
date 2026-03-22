package com.example.demo.dto;

import lombok.Data;
import java.util.List;

@Data
public class EducationDto {
    private Long userId;
    private List<EducationEntry> educationList;

    @Data
    public static class EducationEntry {
        private String degree;
        private String institution;
        private String fieldOfStudy;
        private int startYear;
        private int endYear;
    }
}