package com.jobportal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Job Entity - Stores job posting information
 * 
 * Relationship: Many Jobs -> One Recruiter
 * Relationship: One Job -> Many Applications
 * 
 * Part of the Data Layer (Tier 3)
 */
@Entity
@Table(name = "jobs", indexes = {
    @Index(name = "idx_job_title", columnList = "title"),
    @Index(name = "idx_job_location", columnList = "location"),
    @Index(name = "idx_job_status", columnList = "is_active")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private Long jobId;

    @NotBlank(message = "Job title is required")
    @Size(min = 3, max = 150, message = "Title must be between 3 and 150 characters")
    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @NotBlank(message = "Job description is required")
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "requirements", columnDefinition = "TEXT")
    private String requirements;

    @NotBlank(message = "Skills are required")
    @Column(name = "skills", nullable = false, columnDefinition = "TEXT")
    private String skills;

    @NotBlank(message = "Location is required")
    @Column(name = "location", nullable = false, length = 150)
    private String location;

    @Column(name = "job_type", length = 50)
    private String jobType; // FULL_TIME, PART_TIME, CONTRACT, INTERNSHIP

    @Column(name = "experience_level", length = 50)
    private String experienceLevel; // ENTRY, MID, SENIOR, EXECUTIVE

    @Column(name = "min_salary")
    private Double minSalary;

    @Column(name = "max_salary")
    private Double maxSalary;

    @Column(name = "salary_currency", length = 10)
    private String salaryCurrency = "INR";

    @Column(name = "vacancies")
    private Integer vacancies = 1;

    @Column(name = "application_deadline")
    private LocalDate applicationDeadline;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "is_remote")
    private Boolean isRemote = false;

    @Column(name = "views_count")
    private Integer viewsCount = 0;

    @Column(name = "applications_count")
    private Integer applicationsCount = 0;

    // Many-to-One relationship with Recruiter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id", nullable = false)
    private Recruiter recruiter;

    // One-to-Many relationship with Applications
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructor for creating a job posting
    public Job(String title, String description, String skills, String location, Recruiter recruiter) {
        this.title = title;
        this.description = description;
        this.skills = skills;
        this.location = location;
        this.recruiter = recruiter;
        this.isActive = true;
        this.viewsCount = 0;
        this.applicationsCount = 0;
    }
}
