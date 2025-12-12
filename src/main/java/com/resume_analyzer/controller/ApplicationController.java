package com.resume_analyzer.controller;

import com.resume_analyzer.dto.ApplicationDTO;
import com.resume_analyzer.dto.ChangeStatusDTO;
import com.resume_analyzer.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping("/apply")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApplicationDTO> applyToJob(@RequestParam Long candidateId, @RequestParam Long jobId) {
        ApplicationDTO application = applicationService.applyToJob(candidateId, jobId);
        return new ResponseEntity<>(application, HttpStatus.CREATED);
    }

    @GetMapping("/job/{jobId}/applicants")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<List<ApplicationDTO>> getApplicantsForJob(
            @PathVariable Long jobId,
            @RequestParam(required = false) String skill,
            @RequestParam(required = false) Double gpa,
            Pageable pageable) { // Pageable can be used for fetching all records then filtering in Java
        List<ApplicationDTO> applicants = applicationService.getApplicantsForJob(jobId, skill, gpa);
        // Manual pagination if needed after Java-side filtering for simplicity, or return full list
        return ResponseEntity.ok(applicants);
    }

    @PatchMapping("/{applicationId}/status")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<ApplicationDTO> updateApplicationStatus(
            @PathVariable Long applicationId,
            @Valid @RequestBody ChangeStatusDTO dto) {
        ApplicationDTO updatedApplication = applicationService.updateApplicationStatus(applicationId, dto);
        return ResponseEntity.ok(updatedApplication);
    }
}
