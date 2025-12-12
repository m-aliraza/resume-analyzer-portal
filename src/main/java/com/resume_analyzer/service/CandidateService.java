package com.resume_analyzer.service;

import com.resume_analyzer.dto.CandidateProfileDTO;
import com.resume_analyzer.entity.*;
import com.resume_analyzer.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateProfileRepository candidateProfileRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final CandidateSkillRepository candidateSkillRepository;
    private final ResumeRepository resumeRepository;
    private final EducationRepository educationRepository;
    private final ExperienceRepository experienceRepository;

    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

    @Transactional
    public CandidateProfile createOrUpdateProfile(CandidateProfileDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        CandidateProfile profile = candidateProfileRepository.findByUserId(dto.getUserId())
                .orElse(CandidateProfile.builder().user(user).build());

        profile.setFirstName(dto.getFirstName());
        profile.setLastName(dto.getLastName());
        profile.setPhone(dto.getPhone());
        profile.setSummary(dto.getSummary());

        CandidateProfile savedProfile = candidateProfileRepository.save(profile);

        if (dto.getSkillNames() != null) {
            updateSkills(savedProfile, dto.getSkillNames());
        }
        
        // This is a simplified approach. In a real app, you'd handle updates/deletes for sub-lists more carefully.
        if (dto.getEducationList() != null) {
            educationRepository.findByCandidateProfileId(savedProfile.getId()).forEach(educationRepository::delete); // Clear old ones
            dto.getEducationList().forEach(eduDto -> {
                Education education = Education.builder()
                        .institution(eduDto.getInstitution())
                        .degree(eduDto.getDegree())
                        .fieldOfStudy(eduDto.getFieldOfStudy())
                        .graduationDate(eduDto.getGraduationDate())
                        .gpa(eduDto.getGpa())
                        .candidateProfile(savedProfile)
                        .build();
                educationRepository.save(education);
            });
        }

        if (dto.getExperienceList() != null) {
            experienceRepository.findByCandidateProfileId(savedProfile.getId()).forEach(experienceRepository::delete); // Clear old ones
            dto.getExperienceList().forEach(expDto -> {
                Experience experience = Experience.builder()
                        .jobTitle(expDto.getJobTitle())
                        .companyName(expDto.getCompanyName())
                        .startDate(expDto.getStartDate())
                        .endDate(expDto.getEndDate())
                        .description(expDto.getDescription())
                        .candidateProfile(savedProfile)
                        .build();
                experienceRepository.save(experience);
            });
        }


        return savedProfile;
    }

    private void updateSkills(CandidateProfile profile, List<String> skillNames) {
        candidateSkillRepository.findByCandidateProfileId(profile.getId()).forEach(candidateSkillRepository::delete); // Clear old ones

        for (String skillName : skillNames) {
            Skill skill = skillRepository.findByName(skillName)
                    .orElseGet(() -> skillRepository.save(Skill.builder().name(skillName).build()));
            
            CandidateSkill candidateSkill = CandidateSkill.builder()
                    .candidateProfile(profile)
                    .skill(skill)
                    .build();
            candidateSkillRepository.save(candidateSkill);
        }
    }

    @Transactional
    public Resume uploadResume(Long candidateId, MultipartFile file) throws IOException {
        CandidateProfile profile = candidateProfileRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate Profile not found"));

        if (!Files.exists(fileStorageLocation)) {
            Files.createDirectories(fileStorageLocation);
        }

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path targetLocation = fileStorageLocation.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        Tika tika = new Tika();
        String parsedContent = "";
        try {
            parsedContent = tika.parseToString(targetLocation.toFile());
        } catch (Exception e) {
            System.err.println("Failed to parse resume content: " + e.getMessage());
        }

        Resume resume = Resume.builder()
                .fileName(file.getOriginalFilename())
                .fileUrl(targetLocation.toString())
                .fileType(file.getContentType())
                .candidateProfile(profile)
                .build();
        
        return resumeRepository.save(resume);
    }
}
