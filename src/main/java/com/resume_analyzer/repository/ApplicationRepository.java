package com.resume_analyzer.repository;

import com.resume_analyzer.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByJobPositionId(Long jobPositionId);
    List<Application> findByCandidateProfileId(Long candidateProfileId);
    Optional<Application> findByJobPositionIdAndCandidateProfileId(Long jobPositionId, Long candidateProfileId);
}
