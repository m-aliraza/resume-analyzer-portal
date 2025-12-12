package com.resume_analyzer.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "education")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String institution; // e.g., University of X
    private String degree;      // e.g., Bachelor of Science
    private String fieldOfStudy; // e.g., Computer Science
    
    private LocalDate graduationDate;
    private Double gpa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    private CandidateProfile candidateProfile;
}
