package com.jobportal.application.dto;

import com.jobportal.application.entity.JobApplication;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationDTO {
    private Long id;

    @NotNull(message = "Job ID is required")
    private Long jobId;

    @NotNull(message = "Seeker ID is required")
    private Long seekerId;

    private String resumeUrl;
    private String coverLetter;
    private JobApplication.ApplicationStatus status;
    private String notes;
    private String recruiterNotes;
    private String expectedSalary;
    private String noticePeriod;
    private LocalDateTime appliedAt;
    private LocalDateTime reviewedAt;
    private Long reviewedBy;

    // Additional info (populated when needed)
    private String jobTitle;
    private String companyName;
    private String seekerName;
}
