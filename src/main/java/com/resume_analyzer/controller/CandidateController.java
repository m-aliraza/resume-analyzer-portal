package com.resume_analyzer.controller;

import com.resume_analyzer.dto.CandidateProfileDTO;
import com.resume_analyzer.entity.CandidateProfile;
import com.resume_analyzer.entity.Resume;
import com.resume_analyzer.entity.User;
import com.resume_analyzer.repository.CandidateProfileRepository;
import com.resume_analyzer.repository.UserRepository;
import com.resume_analyzer.service.CandidateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/candidates")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;
    private final UserRepository userRepository;
    private final CandidateProfileRepository candidateProfileRepository;

    @PostMapping("/profile")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<CandidateProfile> createOrUpdateProfile(@Valid @RequestBody CandidateProfileDTO dto) {
        User user = getAuthenticatedUser();
        dto.setUserId(user.getId());
        
        CandidateProfile profile = candidateService.createOrUpdateProfile(dto);
        return new ResponseEntity<>(profile, HttpStatus.CREATED);
    }

    @PostMapping(value = "/resume", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Resume> uploadResume(@RequestParam("file") MultipartFile file) throws IOException {
        User user = getAuthenticatedUser();
        
        CandidateProfile profile = candidateProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Candidate profile not found for user: " + user.getUsername()));

        Resume resume = candidateService.uploadResume(profile.getId(), file);
        return new ResponseEntity<>(resume, HttpStatus.CREATED);
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
