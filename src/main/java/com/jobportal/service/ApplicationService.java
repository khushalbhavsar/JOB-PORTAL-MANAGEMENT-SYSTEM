package com.jobportal.service;

import com.jobportal.dto.ApplicationDTO;
import com.jobportal.dto.ApplicationRequest;
import com.jobportal.dto.ApplicationStatusUpdateRequest;
import com.jobportal.exception.BadRequestException;
import com.jobportal.exception.ResourceNotFoundException;
import com.jobportal.exception.UnauthorizedException;
import com.jobportal.model.*;
import com.jobportal.repository.ApplicationRepository;
import com.jobportal.repository.JobRepository;
import com.jobportal.repository.RecruiterRepository;
import com.jobportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Application Service - Business Logic Layer
 * 
 * Handles job application submission and management
 * 
 * Part of the Business Logic Layer (Tier 2)
 */
@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final RecruiterRepository recruiterRepository;

    /**
     * Apply for a job
     */
    @Transactional
    public ApplicationDTO applyForJob(Long userId, ApplicationRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Verify user is a job seeker
        if (user.getRole() != Role.JOB_SEEKER) {
            throw new BadRequestException("Only job seekers can apply for jobs");
        }

        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        // Check if job is active
        if (!job.getIsActive()) {
            throw new BadRequestException("This job is no longer accepting applications");
        }

        // Check if user already applied
        if (applicationRepository.existsByUserAndJob(user, job)) {
            throw new BadRequestException("You have already applied for this job");
        }

        // Create application
        Application application = new Application();
        application.setUser(user);
        application.setJob(job);
        application.setCoverLetter(request.getCoverLetter());
        application.setResumeUrl(request.getResumeUrl() != null ? request.getResumeUrl() : user.getResumeUrl());
        application.setStatus(ApplicationStatus.APPLIED);
        application.setAppliedDate(LocalDateTime.now());

        Application savedApplication = applicationRepository.save(application);

        // Increment job application count
        jobRepository.incrementApplicationCount(job.getJobId());

        return mapToApplicationDTO(savedApplication);
    }

    /**
     * Get application by ID
     */
    public ApplicationDTO getApplicationById(Long applicationId, Long userId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        // Verify user is applicant or recruiter
        boolean isApplicant = application.getUser().getUserId().equals(userId);
        boolean isRecruiter = application.getJob().getRecruiter().getUser().getUserId().equals(userId);

        if (!isApplicant && !isRecruiter) {
            throw new UnauthorizedException("You are not authorized to view this application");
        }

        return mapToApplicationDTO(application);
    }

    /**
     * Get applications by job seeker
     */
    public Page<ApplicationDTO> getApplicationsByUser(Long userId, int page, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("appliedDate").descending());
        Page<Application> applications = applicationRepository.findByUser(user, pageable);
        return applications.map(this::mapToApplicationDTO);
    }

    /**
     * Get applications for a specific job (for recruiter)
     */
    public Page<ApplicationDTO> getApplicationsByJob(Long userId, Long jobId, int page, int size) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        // Verify recruiter owns the job
        if (!job.getRecruiter().getUser().getUserId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to view applications for this job");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("appliedDate").descending());
        Page<Application> applications = applicationRepository.findByJob(job, pageable);
        return applications.map(this::mapToApplicationDTO);
    }

    /**
     * Get all applications for recruiter's jobs
     */
    public Page<ApplicationDTO> getApplicationsByRecruiter(Long userId, int page, int size) {
        Recruiter recruiter = recruiterRepository.findByUserUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter profile not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("appliedDate").descending());
        Page<Application> applications = applicationRepository.findByRecruiterId(recruiter.getRecruiterId(), pageable);
        return applications.map(this::mapToApplicationDTO);
    }

    /**
     * Update application status (for recruiter)
     */
    @Transactional
    public ApplicationDTO updateApplicationStatus(Long userId, Long applicationId,
            ApplicationStatusUpdateRequest request) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        // Verify recruiter owns the job
        if (!application.getJob().getRecruiter().getUser().getUserId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to update this application");
        }

        // Update status
        try {
            ApplicationStatus status = ApplicationStatus.valueOf(request.getStatus().toUpperCase());
            application.setStatus(status);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid status value");
        }

        // Update notes and dates
        if (request.getRecruiterNotes() != null) {
            application.setRecruiterNotes(request.getRecruiterNotes());
        }

        application.setReviewedDate(LocalDateTime.now());

        if (request.getInterviewDate() != null) {
            application.setInterviewDate(LocalDateTime.parse(request.getInterviewDate()));
        }

        Application updatedApplication = applicationRepository.save(application);
        return mapToApplicationDTO(updatedApplication);
    }

    /**
     * Withdraw application (for job seeker)
     */
    @Transactional
    public void withdrawApplication(Long userId, Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        // Verify user owns the application
        if (!application.getUser().getUserId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to withdraw this application");
        }

        // Can only withdraw if status is APPLIED or UNDER_REVIEW
        if (application.getStatus() != ApplicationStatus.APPLIED &&
                application.getStatus() != ApplicationStatus.UNDER_REVIEW) {
            throw new BadRequestException("Cannot withdraw application at this stage");
        }

        applicationRepository.delete(application);
    }

    /**
     * Get application statistics for recruiter
     */
    public List<Object[]> getApplicationStatsByRecruiter(Long userId) {
        Recruiter recruiter = recruiterRepository.findByUserUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter profile not found"));

        return applicationRepository.getApplicationStatsByRecruiter(recruiter.getRecruiterId());
    }

    /**
     * Count applications by status for a user
     */
    public long countApplicationsByUserAndStatus(Long userId, ApplicationStatus status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return applicationRepository.findByUserAndStatus(user, status).size();
    }

    /**
     * Map Application entity to ApplicationDTO
     */
    private ApplicationDTO mapToApplicationDTO(Application application) {
        User user = application.getUser();
        Job job = application.getJob();

        return ApplicationDTO.builder()
                .applicationId(application.getApplicationId())
                .coverLetter(application.getCoverLetter())
                .resumeUrl(application.getResumeUrl())
                .status(application.getStatus().name())
                .recruiterNotes(application.getRecruiterNotes())
                .appliedDate(application.getAppliedDate())
                .reviewedDate(application.getReviewedDate())
                .interviewDate(application.getInterviewDate())
                .jobId(job.getJobId())
                .jobTitle(job.getTitle())
                .companyName(job.getRecruiter().getCompanyName())
                .location(job.getLocation())
                .userId(user.getUserId())
                .applicantName(user.getName())
                .applicantEmail(user.getEmail())
                .applicantPhone(user.getPhone())
                .applicantSkills(user.getSkills())
                .createdAt(application.getCreatedAt())
                .build();
    }
}
