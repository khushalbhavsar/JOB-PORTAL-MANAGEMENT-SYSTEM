package com.jobportal.service;

import com.jobportal.dto.DashboardStats;
import com.jobportal.model.ApplicationStatus;
import com.jobportal.model.Role;
import com.jobportal.model.User;
import com.jobportal.repository.ApplicationRepository;
import com.jobportal.repository.JobRepository;
import com.jobportal.repository.RecruiterRepository;
import com.jobportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Dashboard Service - Business Logic Layer
 * 
 * Provides statistics and metrics for different user dashboards
 * 
 * Part of the Business Logic Layer (Tier 2)
 */
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final RecruiterRepository recruiterRepository;
    private final ApplicationRepository applicationRepository;
    private final RecruiterService recruiterService;
    private final ApplicationService applicationService;

    /**
     * Get admin dashboard statistics
     */
    public DashboardStats getAdminStats() {
        return DashboardStats.builder()
                .totalUsers(userRepository.count())
                .totalJobSeekers(userRepository.countByRole(Role.JOB_SEEKER))
                .totalRecruiters(userRepository.countByRole(Role.RECRUITER))
                .totalJobs(jobRepository.count())
                .activeJobs(jobRepository.countByIsActiveTrue())
                .totalApplications(applicationRepository.count())
                .pendingVerifications(recruiterRepository.countByIsVerifiedFalse())
                .build();
    }

    /**
     * Get recruiter dashboard statistics
     */
    public DashboardStats getRecruiterStats(Long userId) {
        var recruiter = recruiterService.getRecruiterEntityByUserId(userId);
        Long recruiterId = recruiter.getRecruiterId();

        long myJobs = jobRepository.countByRecruiter(recruiter);
        long activeJobs = recruiter.getJobs().stream()
                .filter(job -> job.getIsActive())
                .count();

        var applications = applicationRepository.findByRecruiterId(recruiterId);
        long totalApplications = applications.size();
        long shortlisted = applications.stream()
                .filter(a -> a.getStatus() == ApplicationStatus.SHORTLISTED)
                .count();
        long pending = applications.stream()
                .filter(a -> a.getStatus() == ApplicationStatus.APPLIED ||
                        a.getStatus() == ApplicationStatus.UNDER_REVIEW)
                .count();

        return DashboardStats.builder()
                .myJobs(myJobs)
                .myActiveJobs(activeJobs)
                .myApplicationsReceived(totalApplications)
                .myShortlistedCandidates(shortlisted)
                .myPendingApplications(pending)
                .build();
    }

    /**
     * Get job seeker dashboard statistics
     */
    public DashboardStats getJobSeekerStats(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        long applied = applicationService.countApplicationsByUserAndStatus(userId, ApplicationStatus.APPLIED) +
                applicationService.countApplicationsByUserAndStatus(userId, ApplicationStatus.UNDER_REVIEW);
        long shortlisted = applicationService.countApplicationsByUserAndStatus(userId, ApplicationStatus.SHORTLISTED);
        long interviews = applicationService.countApplicationsByUserAndStatus(userId,
                ApplicationStatus.INTERVIEW_SCHEDULED);
        long rejected = applicationService.countApplicationsByUserAndStatus(userId, ApplicationStatus.REJECTED);

        return DashboardStats.builder()
                .appliedJobs(applicationRepository.countByUser(user))
                .shortlistedCount(shortlisted)
                .interviewScheduledCount(interviews)
                .rejectedCount(rejected)
                .activeJobs(jobRepository.countByIsActiveTrue())
                .build();
    }
}
