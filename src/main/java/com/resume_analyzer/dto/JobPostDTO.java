package com.resume_analyzer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobPostDTO {

    private Long id; // For update operations

    @NotBlank(message = "Job title is required")
    private String title;

    private String description;
    private String requirements; // As a text field

    @NotBlank(message = "Job location is required")
    private String location;
    
    private String salaryRange;
    private LocalDateTime expirationDate;

    @NotNull(message = "Company ID is required for a job posting")
    private Long companyId;
}
