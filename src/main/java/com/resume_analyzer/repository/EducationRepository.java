package com.resume_analyzer.repository;

import com.resume_analyzer.entity.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EducationRepository extends JpaRepository<Education, Long> {
    List<Education> findByCandidateProfileId(Long candidateProfileId);
}
