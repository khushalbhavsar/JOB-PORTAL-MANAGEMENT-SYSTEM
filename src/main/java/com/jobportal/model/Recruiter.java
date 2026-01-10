package com.jobportal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Recruiter Entity - Stores company/recruiter profile information
 * 
 * Relationship: One User (with RECRUITER role) -> One Recruiter profile
 * Relationship: One Recruiter -> Many Jobs
 * 
 * Part of the Data Layer (Tier 3)
 */
@Entity
@Table(name = "recruiters")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recruiter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruiter_id")
    private Long recruiterId;

    @NotBlank(message = "Company name is required")
    @Size(min = 2, max = 150, message = "Company name must be between 2 and 150 characters")
    @Column(name = "company_name", nullable = false, length = 150)
    private String companyName;

    @Column(name = "company_description", columnDefinition = "TEXT")
    private String companyDescription;

    @Column(name = "company_website", length = 255)
    private String companyWebsite;

    @Column(name = "company_logo")
    private String companyLogo;

    @Column(name = "industry", length = 100)
    private String industry;

    @Column(name = "company_size", length = 50)
    private String companySize;

    @Column(name = "headquarters", length = 150)
    private String headquarters;

    @Column(name = "founded_year")
    private Integer foundedYear;

    @Column(name = "is_verified")
    private Boolean isVerified = false;

    // One-to-One relationship with User
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // One-to-Many relationship with Jobs
    @OneToMany(mappedBy = "recruiter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Job> jobs = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructor for creating recruiter profile
    public Recruiter(String companyName, String companyDescription, User user) {
        this.companyName = companyName;
        this.companyDescription = companyDescription;
        this.user = user;
        this.isVerified = false;
    }
}
