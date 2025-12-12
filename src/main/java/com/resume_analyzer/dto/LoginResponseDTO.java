package com.resume_analyzer.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class LoginResponseDTO {
    private Long userId;
    private String username;
    private String email;
    private List<String> roles;
    private String token; // Placeholder for JWT if implemented later
}
