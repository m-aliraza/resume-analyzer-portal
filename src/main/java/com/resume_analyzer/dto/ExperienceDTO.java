package com.resume_analyzer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExperienceDTO {

    private Long id; // For update operations

    @NotBlank(message = "Job title is required")
    private String jobTitle;
    
    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    
    private LocalDate endDate; // Null if currently employed

    private String description;
}
