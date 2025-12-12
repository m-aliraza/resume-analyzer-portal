package com.resume_analyzer.service;

import com.resume_analyzer.entity.AuditLog;
import com.resume_analyzer.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public AuditLog logEvent(String eventType, String description, Long userId, String username) {
        AuditLog auditLog = AuditLog.builder()
                .eventType(eventType)
                .description(description)
                .userId(userId)
                .username(username)
                .timestamp(LocalDateTime.now())
                .build();
        return auditLogRepository.save(auditLog);
    }
}
