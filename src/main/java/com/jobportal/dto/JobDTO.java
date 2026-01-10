package com.jobportal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for Job response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobDTO {

    private Long jobId;
    private String title;
    private String description;
    private String requirements;
    private String skills;
    private String location;
    private String jobType;
    private String experienceLevel;
    private Double minSalary;
    private Double maxSalary;
    private String salaryCurrency;
    private Integer vacancies;
    private LocalDate applicationDeadline;
    private Boolean isActive;
    private Boolean isRemote;
    private Integer viewsCount;
    private Integer applicationsCount;

    // Recruiter info
    private Long recruiterId;
    private String companyName;
    private String companyLogo;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
