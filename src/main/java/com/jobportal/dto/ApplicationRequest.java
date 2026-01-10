package com.jobportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for submitting a job application
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationRequest {

    private Long jobId;
    private String coverLetter;
    private String resumeUrl;
}
