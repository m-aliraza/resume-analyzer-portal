package com.resume_analyzer.repository;

import com.resume_analyzer.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    List<Resume> findByCandidateProfileId(Long candidateProfileId);
}
