package com.resume_analyzer.repository;

import com.resume_analyzer.entity.JobPosition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobPositionRepository extends JpaRepository<JobPosition, Long> {
    List<JobPosition> findByCompanyId(Long companyId);
    Page<JobPosition> findByIsActiveTrue(Pageable pageable);
}