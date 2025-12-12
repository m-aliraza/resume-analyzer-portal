package com.resume_analyzer.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String eventType; // e.g., "USER_LOGIN", "JOB_CREATED", "STATUS_CHANGE"

    @Column(nullable = false)
    private String description;

    private Long userId; // The ID of the user who performed the action

    private String username; // Store username too, in case user is deleted later

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}
