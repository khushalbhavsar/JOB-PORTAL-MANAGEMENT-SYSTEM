package com.jobportal.application.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Job Application Entity
 */
@Entity
@Table(name = "job_applications", indexes = {
        @Index(name = "idx_application_job", columnList = "job_id"),
        @Index(name = "idx_application_seeker", columnList = "seeker_id"),
        @Index(name = "idx_application_status", columnList = "status")
}, uniqueConstraints = {
        @UniqueConstraint(columnNames = { "job_id", "seeker_id" })
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_id", nullable = false)
    private Long jobId;

    @Column(name = "seeker_id", nullable = false)
    private Long seekerId;

    @Column(name = "resume_url", length = 500)
    private String resumeUrl;

    @Column(name = "cover_letter", columnDefinition = "TEXT")
    private String coverLetter;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private ApplicationStatus status = ApplicationStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "recruiter_notes", columnDefinition = "TEXT")
    private String recruiterNotes;

    @Column(name = "expected_salary")
    private String expectedSalary;

    @Column(name = "notice_period")
    private String noticePeriod;

    @Column(name = "applied_at")
    private LocalDateTime appliedAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @Column(name = "reviewed_by")
    private Long reviewedBy;

    @PrePersist
    protected void onCreate() {
        appliedAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum ApplicationStatus {
        PENDING,
        REVIEWING,
        SHORTLISTED,
        INTERVIEW_SCHEDULED,
        INTERVIEWED,
        OFFERED,
        ACCEPTED,
        REJECTED,
        WITHDRAWN
    }
}
