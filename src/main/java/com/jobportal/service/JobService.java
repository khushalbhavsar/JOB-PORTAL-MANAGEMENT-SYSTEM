package com.jobportal.service;

import com.jobportal.dto.JobDTO;
import com.jobportal.dto.JobRequest;
import com.jobportal.dto.JobSearchRequest;
import com.jobportal.exception.BadRequestException;
import com.jobportal.exception.ResourceNotFoundException;
import com.jobportal.exception.UnauthorizedException;
import com.jobportal.model.Job;
import com.jobportal.model.Recruiter;
import com.jobportal.model.User;
import com.jobportal.repository.JobRepository;
import com.jobportal.repository.RecruiterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Job Service - Business Logic Layer
 * 
 * Handles job posting, searching, and management
 * 
 * Part of the Business Logic Layer (Tier 2)
 */
@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final RecruiterRepository recruiterRepository;

    /**
     * Create a new job posting
     */
    @Transactional
    public JobDTO createJob(Long userId, JobRequest request) {
        Recruiter recruiter = recruiterRepository.findByUserUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Recruiter profile not found. Please complete your profile first."));

        Job job = new Job();
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setRequirements(request.getRequirements());
        job.setSkills(request.getSkills());
        job.setLocation(request.getLocation());
        job.setJobType(request.getJobType());
        job.setExperienceLevel(request.getExperienceLevel());
        job.setMinSalary(request.getMinSalary());
        job.setMaxSalary(request.getMaxSalary());
        job.setSalaryCurrency(request.getSalaryCurrency() != null ? request.getSalaryCurrency() : "INR");
        job.setVacancies(request.getVacancies() != null ? request.getVacancies() : 1);
        job.setApplicationDeadline(request.getApplicationDeadline());
        job.setIsRemote(request.getIsRemote() != null ? request.getIsRemote() : false);
        job.setIsActive(true);
        job.setRecruiter(recruiter);

        Job savedJob = jobRepository.save(job);
        return mapToJobDTO(savedJob);
    }

    /**
     * Update an existing job
     */
    @Transactional
    public JobDTO updateJob(Long userId, Long jobId, JobRequest request) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));

        // Verify ownership
        if (!job.getRecruiter().getUser().getUserId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to update this job");
        }

        if (request.getTitle() != null) {
            job.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            job.setDescription(request.getDescription());
        }
        if (request.getRequirements() != null) {
            job.setRequirements(request.getRequirements());
        }
        if (request.getSkills() != null) {
            job.setSkills(request.getSkills());
        }
        if (request.getLocation() != null) {
            job.setLocation(request.getLocation());
        }
        if (request.getJobType() != null) {
            job.setJobType(request.getJobType());
        }
        if (request.getExperienceLevel() != null) {
            job.setExperienceLevel(request.getExperienceLevel());
        }
        if (request.getMinSalary() != null) {
            job.setMinSalary(request.getMinSalary());
        }
        if (request.getMaxSalary() != null) {
            job.setMaxSalary(request.getMaxSalary());
        }
        if (request.getSalaryCurrency() != null) {
            job.setSalaryCurrency(request.getSalaryCurrency());
        }
        if (request.getVacancies() != null) {
            job.setVacancies(request.getVacancies());
        }
        if (request.getApplicationDeadline() != null) {
            job.setApplicationDeadline(request.getApplicationDeadline());
        }
        if (request.getIsRemote() != null) {
            job.setIsRemote(request.getIsRemote());
        }

        Job updatedJob = jobRepository.save(job);
        return mapToJobDTO(updatedJob);
    }

    /**
     * Delete a job (soft delete - set inactive)
     */
    @Transactional
    public void deleteJob(Long userId, Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));

        // Verify ownership
        if (!job.getRecruiter().getUser().getUserId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to delete this job");
        }

        job.setIsActive(false);
        jobRepository.save(job);
    }

    /**
     * Get job by ID
     */
    @Transactional
    public JobDTO getJobById(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));

        // Increment view count
        jobRepository.incrementViewCount(jobId);

        return mapToJobDTO(job);
    }

    /**
     * Get job entity by ID
     */
    public Job getJobEntityById(Long jobId) {
        return jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));
    }

    /**
     * Get all active jobs with pagination
     */
    public Page<JobDTO> getAllActiveJobs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Job> jobs = jobRepository.findByIsActiveTrue(pageable);
        return jobs.map(this::mapToJobDTO);
    }

    /**
     * Search jobs with filters
     */
    public Page<JobDTO> searchJobs(JobSearchRequest request) {
        Sort sort = request.getSortDirection().equalsIgnoreCase("ASC")
                ? Sort.by(request.getSortBy()).ascending()
                : Sort.by(request.getSortBy()).descending();

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Page<Job> jobs = jobRepository.advancedSearch(
                request.getKeyword(),
                request.getLocation(),
                request.getSkill(),
                request.getJobType(),
                request.getExperienceLevel(),
                pageable);

        return jobs.map(this::mapToJobDTO);
    }

    /**
     * Get jobs by recruiter
     */
    public Page<JobDTO> getJobsByRecruiter(Long userId, int page, int size) {
        Recruiter recruiter = recruiterRepository.findByUserUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter profile not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Job> jobs = jobRepository.findByRecruiter(recruiter, pageable);
        return jobs.map(this::mapToJobDTO);
    }

    /**
     * Get recent jobs
     */
    public List<JobDTO> getRecentJobs(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<Job> jobs = jobRepository.findRecentJobs(pageable);
        return jobs.stream()
                .map(this::mapToJobDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get most viewed jobs
     */
    public List<JobDTO> getMostViewedJobs(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<Job> jobs = jobRepository.findMostViewedJobs(pageable);
        return jobs.stream()
                .map(this::mapToJobDTO)
                .collect(Collectors.toList());
    }

    /**
     * Toggle job status (active/inactive)
     */
    @Transactional
    public JobDTO toggleJobStatus(Long userId, Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        // Verify ownership
        if (!job.getRecruiter().getUser().getUserId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to modify this job");
        }

        job.setIsActive(!job.getIsActive());
        Job updatedJob = jobRepository.save(job);
        return mapToJobDTO(updatedJob);
    }

    /**
     * Map Job entity to JobDTO
     */
    private JobDTO mapToJobDTO(Job job) {
        Recruiter recruiter = job.getRecruiter();
        return JobDTO.builder()
                .jobId(job.getJobId())
                .title(job.getTitle())
                .description(job.getDescription())
                .requirements(job.getRequirements())
                .skills(job.getSkills())
                .location(job.getLocation())
                .jobType(job.getJobType())
                .experienceLevel(job.getExperienceLevel())
                .minSalary(job.getMinSalary())
                .maxSalary(job.getMaxSalary())
                .salaryCurrency(job.getSalaryCurrency())
                .vacancies(job.getVacancies())
                .applicationDeadline(job.getApplicationDeadline())
                .isActive(job.getIsActive())
                .isRemote(job.getIsRemote())
                .viewsCount(job.getViewsCount())
                .applicationsCount(job.getApplicationsCount())
                .recruiterId(recruiter.getRecruiterId())
                .companyName(recruiter.getCompanyName())
                .companyLogo(recruiter.getCompanyLogo())
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .build();
    }
}
