package com.resume_analyzer.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeStatusDTO {

    @NotBlank(message = "New status is required")
    private String newStatus;

    private String notes;
}
