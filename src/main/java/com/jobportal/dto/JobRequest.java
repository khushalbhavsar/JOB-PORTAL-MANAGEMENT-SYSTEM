package com.jobportal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for creating/updating a job posting
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobRequest {

    @NotBlank(message = "Job title is required")
    @Size(min = 3, max = 150, message = "Title must be between 3 and 150 characters")
    private String title;

    @NotBlank(message = "Job description is required")
    private String description;

    private String requirements;

    @NotBlank(message = "Skills are required")
    private String skills;

    @NotBlank(message = "Location is required")
    private String location;

    private String jobType; // FULL_TIME, PART_TIME, CONTRACT, INTERNSHIP

    private String experienceLevel; // ENTRY, MID, SENIOR, EXECUTIVE

    private Double minSalary;
    private Double maxSalary;
    private String salaryCurrency;

    private Integer vacancies;

    private LocalDate applicationDeadline;

    private Boolean isRemote;
}
