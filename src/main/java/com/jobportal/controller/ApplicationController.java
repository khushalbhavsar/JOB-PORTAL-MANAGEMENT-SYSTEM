package com.jobportal.controller;

import com.jobportal.dto.*;
import com.jobportal.model.User;
import com.jobportal.service.ApplicationService;
import com.jobportal.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Application Controller - Presentation Layer
 * 
 * Handles job application endpoints
 * 
 * Part of the Presentation Layer (Tier 1)
 */
@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;
    private final AuthService authService;

    /**
     * Apply for a job (Job Seeker only)
     * POST /api/applications/apply
     */
    @PostMapping("/apply")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<ApiResponse<ApplicationDTO>> applyForJob(@Valid @RequestBody ApplicationRequest request) {
        User currentUser = authService.getCurrentUser();
        ApplicationDTO application = applicationService.applyForJob(currentUser.getUserId(), request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Application submitted successfully", application));
    }

    /**
     * Get application by ID
     * GET /api/applications/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ApplicationDTO>> getApplicationById(@PathVariable("id") Long applicationId) {
        User currentUser = authService.getCurrentUser();
        ApplicationDTO application = applicationService.getApplicationById(applicationId, currentUser.getUserId());
        return ResponseEntity.ok(ApiResponse.success("Application retrieved successfully", application));
    }

    /**
     * Get current user's applications (Job Seeker)
     * GET /api/applications/my-applications
     */
    @GetMapping("/my-applications")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<ApiResponse<Page<ApplicationDTO>>> getMyApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        User currentUser = authService.getCurrentUser();
        Page<ApplicationDTO> applications = applicationService.getApplicationsByUser(currentUser.getUserId(), page,
                size);
        return ResponseEntity.ok(ApiResponse.success("Applications retrieved successfully", applications));
    }

    /**
     * Get applications for a specific job (Recruiter)
     * GET /api/applications/job/{jobId}
     */
    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    public ResponseEntity<ApiResponse<Page<ApplicationDTO>>> getApplicationsByJob(
            @PathVariable("jobId") Long jobId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        User currentUser = authService.getCurrentUser();
        Page<ApplicationDTO> applications = applicationService.getApplicationsByJob(currentUser.getUserId(), jobId,
                page, size);
        return ResponseEntity.ok(ApiResponse.success("Applications retrieved successfully", applications));
    }

    /**
     * Get all applications for recruiter's jobs
     * GET /api/applications/recruiter
     */
    @GetMapping("/recruiter")
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    public ResponseEntity<ApiResponse<Page<ApplicationDTO>>> getRecruiterApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        User currentUser = authService.getCurrentUser();
        Page<ApplicationDTO> applications = applicationService.getApplicationsByRecruiter(currentUser.getUserId(), page,
                size);
        return ResponseEntity.ok(ApiResponse.success("Applications retrieved successfully", applications));
    }

    /**
     * Update application status (Recruiter)
     * PUT /api/applications/{id}/status
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    public ResponseEntity<ApiResponse<ApplicationDTO>> updateApplicationStatus(
            @PathVariable("id") Long applicationId,
            @Valid @RequestBody ApplicationStatusUpdateRequest request) {
        User currentUser = authService.getCurrentUser();
        ApplicationDTO application = applicationService.updateApplicationStatus(
                currentUser.getUserId(), applicationId, request);
        return ResponseEntity.ok(ApiResponse.success("Application status updated successfully", application));
    }

    /**
     * Withdraw application (Job Seeker)
     * DELETE /api/applications/{id}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<ApiResponse<Void>> withdrawApplication(@PathVariable("id") Long applicationId) {
        User currentUser = authService.getCurrentUser();
        applicationService.withdrawApplication(currentUser.getUserId(), applicationId);
        return ResponseEntity.ok(ApiResponse.success("Application withdrawn successfully"));
    }
}
