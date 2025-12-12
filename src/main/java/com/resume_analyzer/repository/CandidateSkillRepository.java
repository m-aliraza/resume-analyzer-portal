package com.resume_analyzer.repository;

import com.resume_analyzer.entity.CandidateSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CandidateSkillRepository extends JpaRepository<CandidateSkill, Long> {
    List<CandidateSkill> findByCandidateProfileId(Long candidateProfileId);
}
