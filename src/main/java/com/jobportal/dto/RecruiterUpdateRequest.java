package com.jobportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating recruiter/company profile
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecruiterUpdateRequest {

    private String companyName;
    private String companyDescription;
    private String companyWebsite;
    private String companyLogo;
    private String industry;
    private String companySize;
    private String headquarters;
    private Integer foundedYear;

    // User info
    private String name;
    private String phone;
}
