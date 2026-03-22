package com.example.demo.dto;

import lombok.Data;
import java.util.List;

@Data
public class ExperienceDto {
    private Long userId;
    private List<ExperienceEntry> experienceList;

    @Data
    public static class ExperienceEntry {
        private String companyName;
        private String jobRole;
        private int startYear;
        private int endYear;
        private boolean currentlyWorking;
    }
}
