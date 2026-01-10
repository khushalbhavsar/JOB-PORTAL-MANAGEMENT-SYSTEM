package com.jobportal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for Application response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationDTO {

    private Long applicationId;
    private String coverLetter;
    private String resumeUrl;
    private String status;
    private String recruiterNotes;
    private LocalDateTime appliedDate;
    private LocalDateTime reviewedDate;
    private LocalDateTime interviewDate;

    // Job info
    private Long jobId;
    private String jobTitle;
    private String companyName;
    private String location;

    // Applicant info
    private Long userId;
    private String applicantName;
    private String applicantEmail;
    private String applicantPhone;
    private String applicantSkills;

    private LocalDateTime createdAt;
}
