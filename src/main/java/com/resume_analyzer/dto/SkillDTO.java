package com.resume_analyzer.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillDTO {
    private Long id;
    
    @NotBlank(message = "Skill name is required")
    private String name;
}
