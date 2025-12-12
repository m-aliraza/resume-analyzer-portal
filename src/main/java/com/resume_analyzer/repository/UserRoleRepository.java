package com.resume_analyzer.repository;

import com.resume_analyzer.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findByUserId(Long userId); // Custom query to get all roles for a given user
}