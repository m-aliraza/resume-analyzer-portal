package com.resume_analyzer.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "job_positions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String requirements;

    private String location;
    private String salaryRange;

    @Builder.Default
    private LocalDateTime postedDate = LocalDateTime.now();

    private LocalDateTime expirationDate;
    
    @Builder.Default
    private boolean isActive = true;

    // A job belongs to a company
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    // A job has many applications
    @OneToMany(mappedBy = "jobPosition", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Application> applications;
}
