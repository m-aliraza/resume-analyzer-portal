package com.resume_analyzer.service;

import com.resume_analyzer.dto.CompanyDTO;
import com.resume_analyzer.dto.JobPostDTO;
import com.resume_analyzer.dto.RecruiterProfileDTO;
import com.resume_analyzer.entity.Company;
import com.resume_analyzer.entity.JobPosition;
import com.resume_analyzer.entity.RecruiterProfile;
import com.resume_analyzer.entity.User;
import com.resume_analyzer.repository.CompanyRepository;
import com.resume_analyzer.repository.JobPositionRepository;
import com.resume_analyzer.repository.RecruiterProfileRepository;
import com.resume_analyzer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final JobPositionRepository jobPositionRepository;
    private final UserRepository userRepository;

    @Transactional
    public Company createCompany(CompanyDTO companyDTO) {
        if (companyRepository.findByName(companyDTO.getName()).isPresent()) {
            throw new RuntimeException("Company with this name already exists");
        }

        Company company = Company.builder()
                .name(companyDTO.getName())
                .description(companyDTO.getDescription())
                .website(companyDTO.getWebsite())
                .street(companyDTO.getStreet())
                .city(companyDTO.getCity())
                .state(companyDTO.getState())
                .zipCode(companyDTO.getZipCode())
                .country(companyDTO.getCountry())
                .build();

        return companyRepository.save(company);
    }

    @Transactional
    public RecruiterProfile addRecruiter(RecruiterProfileDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        RecruiterProfile profile = RecruiterProfile.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .phone(dto.getPhone())
                .department(dto.getDepartment())
                .user(user)
                .company(company)
                .build();

        return recruiterProfileRepository.save(profile);
    }

    @Transactional
    public JobPosition postJob(JobPostDTO dto) {
        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        JobPosition job = JobPosition.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .requirements(dto.getRequirements())
                .location(dto.getLocation())
                .salaryRange(dto.getSalaryRange())
                .expirationDate(dto.getExpirationDate())
                .company(company)
                .isActive(true)
                .build();

        return jobPositionRepository.save(job);
    }
}
