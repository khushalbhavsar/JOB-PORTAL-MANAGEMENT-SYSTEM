package com.jobportal.job.dto;

import com.jobportal.job.entity.Job;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobDTO {
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 200)
    private String title;

    private String description;
    private String requirements;
    private String responsibilities;

    @NotNull(message = "Company ID is required")
    private Long companyId;

    private Long recruiterId;
    private String location;
    private Job.JobType jobType;
    private Job.ExperienceLevel experienceLevel;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String salaryCurrency;
    private Set<String> skills;
    private Set<String> benefits;
    private Job.JobStatus status;
    private LocalDateTime applicationDeadline;
    private Integer maxApplications;
    private Integer applicationCount;
    private Integer viewCount;
    private Boolean isRemote;
    private Boolean isFeatured;
    private LocalDateTime createdAt;
    private LocalDateTime publishedAt;

    // Company info (populated when needed)
    private String companyName;
    private String companyLogoUrl;
}
