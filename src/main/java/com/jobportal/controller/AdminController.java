package com.jobportal.controller;

import com.jobportal.dto.*;
import com.jobportal.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Admin Controller - Presentation Layer
 * 
 * Handles admin-specific endpoints for system management
 * 
 * Part of the Presentation Layer (Tier 1)
 */
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final RecruiterService recruiterService;
    private final DashboardService dashboardService;

    /**
     * Get system statistics
     * GET /api/admin/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<DashboardStats>> getSystemStats() {
        DashboardStats stats = dashboardService.getAdminStats();
        return ResponseEntity.ok(ApiResponse.success("System statistics retrieved", stats));
    }

    /**
     * Get all users with pagination
     * GET /api/admin/users
     */
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<Page<UserDTO>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<UserDTO> users = userService.getAllUsers(page, size);
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    }

    /**
     * Search users by name
     * GET /api/admin/users/search
     */
    @GetMapping("/users/search")
    public ResponseEntity<ApiResponse<List<UserDTO>>> searchUsers(@RequestParam String name) {
        List<UserDTO> users = userService.searchUsers(name);
        return ResponseEntity.ok(ApiResponse.success("Search results", users));
    }

    /**
     * Activate/Deactivate user
     * PUT /api/admin/users/{id}/toggle-status
     */
    @PutMapping("/users/{id}/toggle-status")
    public ResponseEntity<ApiResponse<UserDTO>> toggleUserStatus(@PathVariable("id") Long userId) {
        UserDTO user = userService.toggleUserStatus(userId);
        String message = user.getIsActive() ? "User activated successfully" : "User deactivated successfully";
        return ResponseEntity.ok(ApiResponse.success(message, user));
    }

    /**
     * Delete user
     * DELETE /api/admin/users/{id}
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable("id") Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
    }

    /**
     * Get all recruiters including unverified
     * GET /api/admin/recruiters
     */
    @GetMapping("/recruiters")
    public ResponseEntity<ApiResponse<Page<RecruiterDTO>>> getAllRecruiters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<RecruiterDTO> recruiters = recruiterService.getAllRecruiters(page, size);
        return ResponseEntity.ok(ApiResponse.success("Recruiters retrieved successfully", recruiters));
    }

    /**
     * Get unverified recruiters
     * GET /api/admin/recruiters/pending
     */
    @GetMapping("/recruiters/pending")
    public ResponseEntity<ApiResponse<List<RecruiterDTO>>> getPendingRecruiters() {
        List<RecruiterDTO> recruiters = recruiterService.getUnverifiedRecruiters();
        return ResponseEntity.ok(ApiResponse.success("Pending recruiters retrieved", recruiters));
    }

    /**
     * Verify recruiter
     * PUT /api/admin/recruiters/{id}/verify
     */
    @PutMapping("/recruiters/{id}/verify")
    public ResponseEntity<ApiResponse<RecruiterDTO>> verifyRecruiter(@PathVariable("id") Long recruiterId) {
        RecruiterDTO recruiter = recruiterService.verifyRecruiter(recruiterId);
        return ResponseEntity.ok(ApiResponse.success("Recruiter verified successfully", recruiter));
    }
}
