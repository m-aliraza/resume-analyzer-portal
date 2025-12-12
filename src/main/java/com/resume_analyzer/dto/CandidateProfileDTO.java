package com.resume_analyzer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateProfileDTO {

    private Long id; // For update operations

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    private String phone;
    private String summary;

    @NotNull(message = "User ID is required for candidate profile")
    private Long userId;

    // Nested DTOs for related data
    private List<EducationDTO> educationList;
    private List<ExperienceDTO> experienceList;
    private List<String> skillNames; // Simpler to handle as a list of strings
}
