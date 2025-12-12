package com.resume_analyzer.service;

import com.resume_analyzer.dto.ApplicationDTO;
import com.resume_analyzer.dto.ChangeStatusDTO;
import com.resume_analyzer.entity.*;
import com.resume_analyzer.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobPositionRepository jobPositionRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final ApplicationStatusHistoryRepository statusHistoryRepository;
    private final SkillRepository skillRepository; // For filtering
    private final CandidateSkillRepository candidateSkillRepository; // For filtering
    private final EducationRepository educationRepository; // For filtering
    private final ExperienceRepository repository; // For filtering

    @Transactional
    public ApplicationDTO applyToJob(Long candidateId, Long jobPositionId) {
        CandidateProfile candidate = candidateProfileRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));
        JobPosition jobPosition = jobPositionRepository.findById(jobPositionId)
                .orElseThrow(() -> new RuntimeException("Job Position not found"));

        if (applicationRepository.findByJobPositionIdAndCandidateProfileId(jobPositionId, candidateId).isPresent()) {
            throw new RuntimeException("Candidate has already applied to this job.");
        }

        Application application = Application.builder()
                .candidateProfile(candidate)
                .jobPosition(jobPosition)
                .status("APPLIED")
                .applicationDate(LocalDateTime.now())
                .build();
        
        Application savedApplication = applicationRepository.save(application);

        ApplicationStatusHistory history = ApplicationStatusHistory.builder()
                .application(savedApplication)
                .status("APPLIED")
                .changeDate(LocalDateTime.now())
                .notes("Candidate applied to the job.")
                .build();
        statusHistoryRepository.save(history);

        return mapToApplicationDTO(savedApplication);
    }

    @Transactional
    public ApplicationDTO updateApplicationStatus(Long applicationId, ChangeStatusDTO dto) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        application.setStatus(dto.getNewStatus());
        Application updatedApplication = applicationRepository.save(application);

        ApplicationStatusHistory history = ApplicationStatusHistory.builder()
                .application(updatedApplication)
                .status(dto.getNewStatus())
                .changeDate(LocalDateTime.now())
                .notes(dto.getNotes())
                .build();
        statusHistoryRepository.save(history);

        return mapToApplicationDTO(updatedApplication);
    }

    public List<ApplicationDTO> getApplicantsForJob(Long jobPositionId, String skillFilter, Double gpaFilter) {
        List<Application> applications = applicationRepository.findByJobPositionId(jobPositionId);

        return applications.stream()
                .filter(app -> skillFilter == null || checkCandidateSkills(app.getCandidateProfile(), skillFilter))
                .filter(app -> gpaFilter == null || checkCandidateGPA(app.getCandidateProfile(), gpaFilter))
                .map(this::mapToApplicationDTO)
                .collect(Collectors.toList());
    }

    private boolean checkCandidateSkills(CandidateProfile candidate, String skillName) {
        Skill skill = skillRepository.findByName(skillName).orElse(null);
        if (skill == null) return false;
        return candidateSkillRepository.findByCandidateProfileId(candidate.getId()).stream()
                .anyMatch(cs -> cs.getSkill().getId().equals(skill.getId()));
    }

    private boolean checkCandidateGPA(CandidateProfile candidate, Double requiredGpa) {
        return educationRepository.findByCandidateProfileId(candidate.getId()).stream()
                .anyMatch(edu -> edu.getGpa() != null && edu.getGpa() >= requiredGpa);
    }

    private ApplicationDTO mapToApplicationDTO(Application application) {
        return ApplicationDTO.builder()
                .id(application.getId())
                .applicationDate(application.getApplicationDate())
                .status(application.getStatus())
                .jobPositionId(application.getJobPosition().getId())
                .jobTitle(application.getJobPosition().getTitle())
                .candidateProfileId(application.getCandidateProfile().getId())
                .candidateFullName(application.getCandidateProfile().getFirstName() + " " + application.getCandidateProfile().getLastName())
                .build();
    }
}
