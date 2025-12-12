package com.resume_analyzer.service;

import com.resume_analyzer.dto.LoginRequest;
import com.resume_analyzer.dto.LoginResponseDTO;
import com.resume_analyzer.dto.UserRegistrationDTO;
import com.resume_analyzer.dto.UserResponseDTO;
import com.resume_analyzer.entity.Role;
import com.resume_analyzer.entity.User;
import com.resume_analyzer.entity.UserRole;
import com.resume_analyzer.repository.RoleRepository;
import com.resume_analyzer.repository.UserRepository;
import com.resume_analyzer.repository.UserRoleRepository;
import com.resume_analyzer.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Transactional
    public UserResponseDTO registerUser(UserRegistrationDTO registrationDTO) {
        if (userRepository.existsByUsername(registrationDTO.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .username(registrationDTO.getUsername())
                .email(registrationDTO.getEmail())
                .password(passwordEncoder.encode(registrationDTO.getPassword()))
                .enabled(true)
                .build();
        
        User savedUser = userRepository.save(user);

        String roleName = registrationDTO.getRole() != null ? registrationDTO.getRole() : "ROLE_CANDIDATE";
        Role role = roleRepository.findByName(roleName)
                .orElseGet(() -> roleRepository.findByName("ROLE_CANDIDATE")
                            .orElseThrow(() -> new RuntimeException("Default ROLE_CANDIDATE not found.")));

        UserRole userRole = UserRole.builder()
                .user(savedUser)
                .role(role)
                .build();
        
        userRoleRepository.save(userRole);

        List<String> roles = Collections.singletonList(role.getName());

        return UserResponseDTO.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .enabled(savedUser.isEnabled())
                .roles(roles)
                .build();
    }

    public LoginResponseDTO login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found after authentication."));

        List<String> roles = user.getUserRoles().stream()
                .map(userRole -> userRole.getRole().getName())
                .collect(Collectors.toList());

        return LoginResponseDTO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(roles)
                .token(jwt)
                .build();
    }
}