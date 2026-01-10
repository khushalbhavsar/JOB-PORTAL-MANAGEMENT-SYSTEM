package com.jobportal.controller;

import com.jobportal.dto.*;
import com.jobportal.model.User;
import com.jobportal.service.AuthService;
import com.jobportal.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Job Controller - Presentation Layer
 * 
 * Handles job posting and searching endpoints
 * 
 * Part of the Presentation Layer (Tier 1)
 */
@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;
    private final AuthService authService;

    /**
     * Create a new job posting (Recruiter only)
     * POST /api/jobs
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    public ResponseEntity<ApiResponse<JobDTO>> createJob(@Valid @RequestBody JobRequest request) {
        User currentUser = authService.getCurrentUser();
        JobDTO job = jobService.createJob(currentUser.getUserId(), request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Job posted successfully", job));
    }

    /**
     * Update a job posting (Recruiter only)
     * PUT /api/jobs/{id}
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    public ResponseEntity<ApiResponse<JobDTO>> updateJob(
            @PathVariable("id") Long jobId,
            @Valid @RequestBody JobRequest request) {
        User currentUser = authService.getCurrentUser();
        JobDTO job = jobService.updateJob(currentUser.getUserId(), jobId, request);
        return ResponseEntity.ok(ApiResponse.success("Job updated successfully", job));
    }

    /**
     * Delete a job posting (Recruiter only)
     * DELETE /api/jobs/{id}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteJob(@PathVariable("id") Long jobId) {
        User currentUser = authService.getCurrentUser();
        jobService.deleteJob(currentUser.getUserId(), jobId);
        return ResponseEntity.ok(ApiResponse.success("Job deleted successfully"));
    }

    /**
     * Get job by ID (Public)
     * GET /api/jobs/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JobDTO>> getJobById(@PathVariable("id") Long jobId) {
        JobDTO job = jobService.getJobById(jobId);
        return ResponseEntity.ok(ApiResponse.success("Job retrieved successfully", job));
    }

    /**
     * Get all active jobs (Public)
     * GET /api/jobs
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<JobDTO>>> getAllJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<JobDTO> jobs = jobService.getAllActiveJobs(page, size);
        return ResponseEntity.ok(ApiResponse.success("Jobs retrieved successfully", jobs));
    }

    /**
     * Search jobs with filters (Public)
     * POST /api/jobs/search
     */
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<Page<JobDTO>>> searchJobs(@RequestBody JobSearchRequest request) {
        Page<JobDTO> jobs = jobService.searchJobs(request);
        return ResponseEntity.ok(ApiResponse.success("Search results", jobs));
    }

    /**
     * Get jobs posted by current recruiter
     * GET /api/jobs/my-jobs
     */
    @GetMapping("/my-jobs")
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    public ResponseEntity<ApiResponse<Page<JobDTO>>> getMyJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        User currentUser = authService.getCurrentUser();
        Page<JobDTO> jobs = jobService.getJobsByRecruiter(currentUser.getUserId(), page, size);
        return ResponseEntity.ok(ApiResponse.success("Your jobs retrieved successfully", jobs));
    }

    /**
     * Toggle job status (active/inactive)
     * PUT /api/jobs/{id}/toggle-status
     */
    @PutMapping("/{id}/toggle-status")
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    public ResponseEntity<ApiResponse<JobDTO>> toggleJobStatus(@PathVariable("id") Long jobId) {
        User currentUser = authService.getCurrentUser();
        JobDTO job = jobService.toggleJobStatus(currentUser.getUserId(), jobId);
        return ResponseEntity.ok(ApiResponse.success("Job status updated successfully", job));
    }

    /**
     * Get recent jobs (Public)
     * GET /api/jobs/recent
     */
    @GetMapping("/recent")
    public ResponseEntity<ApiResponse<List<JobDTO>>> getRecentJobs(
            @RequestParam(defaultValue = "10") int limit) {
        List<JobDTO> jobs = jobService.getRecentJobs(limit);
        return ResponseEntity.ok(ApiResponse.success("Recent jobs retrieved successfully", jobs));
    }

    /**
     * Get most viewed jobs (Public)
     * GET /api/jobs/popular
     */
    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<List<JobDTO>>> getPopularJobs(
            @RequestParam(defaultValue = "10") int limit) {
        List<JobDTO> jobs = jobService.getMostViewedJobs(limit);
        return ResponseEntity.ok(ApiResponse.success("Popular jobs retrieved successfully", jobs));
    }
}
