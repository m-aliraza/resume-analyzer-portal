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
public class EducationDTO {

    private Long id; // For update operations
    
    @NotBlank(message = "Institution name is required")
    private String institution;
    
    @NotBlank(message = "Degree is required")
    private String degree;
    
    private String fieldOfStudy;
    
    @NotNull(message = "Graduation date is required")
    private LocalDate graduationDate;
    
    private Double gpa;
}
