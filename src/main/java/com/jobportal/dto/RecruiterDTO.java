package com.jobportal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for Recruiter response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruiterDTO {

    private Long recruiterId;
    private String companyName;
    private String companyDescription;
    private String companyWebsite;
    private String companyLogo;
    private String industry;
    private String companySize;
    private String headquarters;
    private Integer foundedYear;
    private Boolean isVerified;

    // User info
    private Long userId;
    private String name;
    private String email;
    private String phone;

    // Stats
    private Long jobCount;

    private LocalDateTime createdAt;
}
