package com.resume_analyzer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecruiterProfileDTO {

    private Long id; // For update operations

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    private String phone;
    private String department;

    @NotNull(message = "User ID is required for recruiter profile")
    private Long userId;

    @NotNull(message = "Company ID is required for recruiter profile")
    private Long companyId;
}
