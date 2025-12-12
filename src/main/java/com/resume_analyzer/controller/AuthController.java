package com.resume_analyzer.controller;

import com.resume_analyzer.dto.LoginRequest;
import com.resume_analyzer.dto.LoginResponseDTO;
import com.resume_analyzer.dto.UserRegistrationDTO;
import com.resume_analyzer.dto.UserResponseDTO;
import com.resume_analyzer.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        UserResponseDTO createdUser = authService.registerUser(registrationDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponseDTO response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
}
