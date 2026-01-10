package com.jobportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for job search criteria
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobSearchRequest {

    private String keyword;
    private String location;
    private String skill;
    private String jobType;
    private String experienceLevel;
    private Double minSalary;
    private Double maxSalary;
    private Boolean isRemote;

    // Pagination
    private Integer page = 0;
    private Integer size = 10;
    private String sortBy = "createdAt";
    private String sortDirection = "DESC";
}
