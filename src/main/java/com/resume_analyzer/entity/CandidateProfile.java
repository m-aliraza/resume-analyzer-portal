package com.resume_analyzer.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "candidate_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String phone;
    
    @Column(columnDefinition = "TEXT")
    private String summary;

    // Link to the Login Account
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // A candidate can have multiple resumes
    @OneToMany(mappedBy = "candidateProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Resume> resumes;

    // Education entries
    @OneToMany(mappedBy = "candidateProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Education> educationList;

    // Experience entries
    @OneToMany(mappedBy = "candidateProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Experience> experienceList;

    // Skills (mapped via CandidateSkill)
    @OneToMany(mappedBy = "candidateProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CandidateSkill> candidateSkills;
}
