package com.jobportal.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Application Entity - Stores job application information
 * 
 * Relationship: Many Applications -> One User (Job Seeker)
 * Relationship: Many Applications -> One Job
 * 
 * This entity acts as a join table with additional attributes
 * 
 * Part of the Data Layer (Tier 3)
 */
@Entity
@Table(name = "applications", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "job_id"}, name = "uk_user_job_application")
}, indexes = {
    @Index(name = "idx_application_status", columnList = "status"),
    @Index(name = "idx_application_date", columnList = "applied_date")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Long applicationId;

    @Column(name = "cover_letter", columnDefinition = "TEXT")
    private String coverLetter;

    @Column(name = "resume_url")
    private String resumeUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private ApplicationStatus status = ApplicationStatus.APPLIED;

    @Column(name = "recruiter_notes", columnDefinition = "TEXT")
    private String recruiterNotes;

    @Column(name = "applied_date", nullable = false)
    private LocalDateTime appliedDate;

    @Column(name = "reviewed_date")
    private LocalDateTime reviewedDate;

    @Column(name = "interview_date")
    private LocalDateTime interviewDate;

    // Many-to-One relationship with User (Job Seeker)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Many-to-One relationship with Job
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructor for creating an application
    public Application(User user, Job job, String coverLetter, String resumeUrl) {
        this.user = user;
        this.job = job;
        this.coverLetter = coverLetter;
        this.resumeUrl = resumeUrl;
        this.status = ApplicationStatus.APPLIED;
        this.appliedDate = LocalDateTime.now();
    }

    // Pre-persist to set applied date
    @PrePersist
    protected void onCreate() {
        if (this.appliedDate == null) {
            this.appliedDate = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = ApplicationStatus.APPLIED;
        }
    }
}
