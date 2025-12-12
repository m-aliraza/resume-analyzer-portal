package com.resume_analyzer.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private boolean enabled;
    private List<String> roles;
}
