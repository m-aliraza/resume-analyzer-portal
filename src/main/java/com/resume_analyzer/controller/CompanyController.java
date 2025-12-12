package com.resume_analyzer.controller;

import com.resume_analyzer.dto.CompanyDTO;
import com.resume_analyzer.dto.JobPostDTO;
import com.resume_analyzer.dto.RecruiterProfileDTO;
import com.resume_analyzer.entity.Company;
import com.resume_analyzer.entity.JobPosition;
import com.resume_analyzer.entity.RecruiterProfile;
import com.resume_analyzer.repository.JobPositionRepository;
import com.resume_analyzer.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final JobPositionRepository jobPositionRepository;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Company> createCompany(@Valid @RequestBody CompanyDTO companyDTO) {
        Company company = companyService.createCompany(companyDTO);
        return new ResponseEntity<>(company, HttpStatus.CREATED);
    }

    @PostMapping("/recruiters")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RecruiterProfile> addRecruiter(@Valid @RequestBody RecruiterProfileDTO dto) {
        RecruiterProfile profile = companyService.addRecruiter(dto);
        return new ResponseEntity<>(profile, HttpStatus.CREATED);
    }

    @PostMapping("/{companyId}/jobs")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<JobPosition> postJob(@PathVariable Long companyId, @Valid @RequestBody JobPostDTO dto) {
        dto.setCompanyId(companyId);
        JobPosition job = companyService.postJob(dto);
        return new ResponseEntity<>(job, HttpStatus.CREATED);
    }

    @GetMapping("/jobs/active")
    public ResponseEntity<Page<JobPosition>> listActiveJobs(Pageable pageable) {
        Page<JobPosition> jobs = jobPositionRepository.findByIsActiveTrue(pageable);
        return ResponseEntity.ok(jobs);
    }
}