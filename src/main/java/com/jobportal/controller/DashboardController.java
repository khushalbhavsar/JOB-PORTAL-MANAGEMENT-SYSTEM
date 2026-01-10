package com.jobportal.controller;

import com.jobportal.dto.*;
import com.jobportal.model.User;
import com.jobportal.service.AuthService;
import com.jobportal.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Dashboard Controller - Presentation Layer
 * 
 * Handles dashboard statistics endpoints
 * 
 * Part of the Presentation Layer (Tier 1)
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final AuthService authService;

    /**
     * Get admin dashboard statistics
     * GET /api/dashboard/admin
     */
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DashboardStats>> getAdminDashboard() {
        DashboardStats stats = dashboardService.getAdminStats();
        return ResponseEntity.ok(ApiResponse.success("Admin dashboard statistics", stats));
    }

    /**
     * Get recruiter dashboard statistics
     * GET /api/dashboard/recruiter
     */
    @GetMapping("/recruiter")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<ApiResponse<DashboardStats>> getRecruiterDashboard() {
        User currentUser = authService.getCurrentUser();
        DashboardStats stats = dashboardService.getRecruiterStats(currentUser.getUserId());
        return ResponseEntity.ok(ApiResponse.success("Recruiter dashboard statistics", stats));
    }

    /**
     * Get job seeker dashboard statistics
     * GET /api/dashboard/job-seeker
     */
    @GetMapping("/job-seeker")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<ApiResponse<DashboardStats>> getJobSeekerDashboard() {
        User currentUser = authService.getCurrentUser();
        DashboardStats stats = dashboardService.getJobSeekerStats(currentUser.getUserId());
        return ResponseEntity.ok(ApiResponse.success("Job seeker dashboard statistics", stats));
    }
}
