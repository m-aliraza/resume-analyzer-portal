package com.resume_analyzer.repository;

import com.resume_analyzer.entity.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    List<Experience> findByCandidateProfileId(Long candidateProfileId);
}
