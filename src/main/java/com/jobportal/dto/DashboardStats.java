package com.jobportal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for dashboard statistics
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStats {

    // Admin stats
    private Long totalUsers;
    private Long totalJobSeekers;
    private Long totalRecruiters;
    private Long totalJobs;
    private Long activeJobs;
    private Long totalApplications;
    private Long pendingVerifications;

    // Recruiter stats
    private Long myJobs;
    private Long myActiveJobs;
    private Long myApplicationsReceived;
    private Long myShortlistedCandidates;
    private Long myPendingApplications;

    // Job seeker stats
    private Long appliedJobs;
    private Long shortlistedCount;
    private Long interviewScheduledCount;
    private Long rejectedCount;
}
