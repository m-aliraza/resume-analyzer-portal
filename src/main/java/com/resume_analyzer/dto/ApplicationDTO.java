package com.resume_analyzer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationDTO {
    private Long id;
    private LocalDateTime applicationDate;
    private String status;
    private Long jobPositionId;
    private String jobTitle; // Added for convenience
    private Long candidateProfileId;
    private String candidateFullName; // Added for convenience
}
