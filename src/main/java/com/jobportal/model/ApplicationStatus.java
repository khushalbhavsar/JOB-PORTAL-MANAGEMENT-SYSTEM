package com.jobportal.model;

/**
 * Enum representing the status of a job application
 * 
 * APPLIED - Initial status when application is submitted
 * UNDER_REVIEW - Application is being reviewed by recruiter
 * SHORTLISTED - Candidate has been shortlisted
 * INTERVIEW_SCHEDULED - Interview has been scheduled
 * REJECTED - Application has been rejected
 * HIRED - Candidate has been hired
 */
public enum ApplicationStatus {
    APPLIED,
    UNDER_REVIEW,
    SHORTLISTED,
    INTERVIEW_SCHEDULED,
    REJECTED,
    HIRED
}
