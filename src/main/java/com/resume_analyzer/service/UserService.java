package com.resume_analyzer.service;

import com.resume_analyzer.dto.UserRegistrationDTO;
import com.resume_analyzer.dto.UserResponseDTO;
import com.resume_analyzer.entity.Role;
import com.resume_analyzer.entity.User;
import com.resume_analyzer.entity.UserRole;
import com.resume_analyzer.repository.RoleRepository;
import com.resume_analyzer.repository.UserRepository;
import com.resume_analyzer.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDTO registerUser(UserRegistrationDTO registrationDTO) {
        if (userRepository.existsByUsername(registrationDTO.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Create User
        User user = User.builder()
                .username(registrationDTO.getUsername())
                .email(registrationDTO.getEmail())
                .password(passwordEncoder.encode(registrationDTO.getPassword()))
                .enabled(true)
                .build();
        
        User savedUser = userRepository.save(user);

        // Assign Role (Default to CANDIDATE if not provided)
        String roleName = registrationDTO.getRole() != null ? registrationDTO.getRole() : "ROLE_CANDIDATE";
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        UserRole userRole = UserRole.builder()
                .user(savedUser)
                .role(role)
                .build();
        
        userRoleRepository.save(userRole);

        // Convert to DTO
        List<String> roles = Collections.singletonList(role.getName());

        return UserResponseDTO.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .enabled(savedUser.isEnabled())
                .roles(roles)
                .build();
    }
}
