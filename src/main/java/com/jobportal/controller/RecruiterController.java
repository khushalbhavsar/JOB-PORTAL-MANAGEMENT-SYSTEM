package com.jobportal.controller;

import com.jobportal.dto.*;
import com.jobportal.model.User;
import com.jobportal.service.AuthService;
import com.jobportal.service.RecruiterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Recruiter Controller - Presentation Layer
 * 
 * Handles recruiter/company profile endpoints
 * 
 * Part of the Presentation Layer (Tier 1)
 */
@RestController
@RequestMapping("/api/recruiters")
@RequiredArgsConstructor
public class RecruiterController {

    private final RecruiterService recruiterService;
    private final AuthService authService;

    /**
     * Get all verified recruiters (Public)
     * GET /api/recruiters/verified
     */
    @GetMapping("/verified")
    public ResponseEntity<ApiResponse<List<RecruiterDTO>>> getVerifiedRecruiters() {
        List<RecruiterDTO> recruiters = recruiterService.getAllVerifiedRecruiters();
        return ResponseEntity.ok(ApiResponse.success("Verified recruiters retrieved successfully", recruiters));
    }

    /**
     * Get recruiter by ID (Public)
     * GET /api/recruiters/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RecruiterDTO>> getRecruiterById(@PathVariable("id") Long recruiterId) {
        RecruiterDTO recruiter = recruiterService.getRecruiterById(recruiterId);
        return ResponseEntity.ok(ApiResponse.success("Recruiter retrieved successfully", recruiter));
    }

    /**
     * Get current recruiter's profile
     * GET /api/recruiters/profile
     */
    @GetMapping("/profile")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<ApiResponse<RecruiterDTO>> getMyProfile() {
        User currentUser = authService.getCurrentUser();
        RecruiterDTO recruiter = recruiterService.getRecruiterByUserId(currentUser.getUserId());
        return ResponseEntity.ok(ApiResponse.success("Profile retrieved successfully", recruiter));
    }

    /**
     * Update recruiter profile
     * PUT /api/recruiters/profile
     */
    @PutMapping("/profile")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<ApiResponse<RecruiterDTO>> updateProfile(@Valid @RequestBody RecruiterUpdateRequest request) {
        User currentUser = authService.getCurrentUser();
        RecruiterDTO recruiter = recruiterService.updateRecruiterProfile(currentUser.getUserId(), request);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", recruiter));
    }

    /**
     * Get all recruiters (Public)
     * GET /api/recruiters
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<RecruiterDTO>>> getAllRecruiters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<RecruiterDTO> recruiters = recruiterService.getVerifiedRecruiters(page, size);
        return ResponseEntity.ok(ApiResponse.success("Recruiters retrieved successfully", recruiters));
    }

    /**
     * Search recruiters by company name (Public)
     * GET /api/recruiters/search
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<RecruiterDTO>>> searchRecruiters(@RequestParam String companyName) {
        List<RecruiterDTO> recruiters = recruiterService.searchByCompanyName(companyName);
        return ResponseEntity.ok(ApiResponse.success("Search results", recruiters));
    }

    /**
     * Get unverified recruiters (Admin only)
     * GET /api/recruiters/unverified
     */
    @GetMapping("/unverified")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<RecruiterDTO>>> getUnverifiedRecruiters() {
        List<RecruiterDTO> recruiters = recruiterService.getUnverifiedRecruiters();
        return ResponseEntity.ok(ApiResponse.success("Unverified recruiters retrieved", recruiters));
    }

    /**
     * Verify a recruiter (Admin only)
     * PUT /api/recruiters/{id}/verify
     */
    @PutMapping("/{id}/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RecruiterDTO>> verifyRecruiter(@PathVariable("id") Long recruiterId) {
        RecruiterDTO recruiter = recruiterService.verifyRecruiter(recruiterId);
        return ResponseEntity.ok(ApiResponse.success("Recruiter verified successfully", recruiter));
    }
}
