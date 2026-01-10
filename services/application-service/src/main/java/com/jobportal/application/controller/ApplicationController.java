package com.jobportal.application.controller;

import com.jobportal.application.dto.ApiResponse;
import com.jobportal.application.dto.ApplicationDTO;
import com.jobportal.application.entity.JobApplication;
import com.jobportal.application.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/applications")
@RequiredArgsConstructor
@Tag(name = "Applications", description = "Job application management APIs")
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping("/apply")
    @Operation(summary = "Apply for a job")
    public ResponseEntity<ApiResponse<ApplicationDTO>> apply(@Valid @RequestBody ApplicationDTO dto) {
        ApplicationDTO created = applicationService.applyForJob(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Application submitted successfully", created));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get application by ID")
    public ResponseEntity<ApiResponse<ApplicationDTO>> getApplication(@PathVariable Long id) {
        return applicationService.getApplicationById(id)
                .map(app -> ResponseEntity.ok(ApiResponse.success("Application retrieved", app)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update application status")
    public ResponseEntity<ApiResponse<ApplicationDTO>> updateStatus(
            @PathVariable Long id,
            @RequestParam JobApplication.ApplicationStatus status,
            @RequestParam Long reviewerId) {
        ApplicationDTO updated = applicationService.updateStatus(id, status, reviewerId);
        return ResponseEntity.ok(ApiResponse.success("Status updated", updated));
    }

    @PutMapping("/{id}/notes")
    @Operation(summary = "Add recruiter notes")
    public ResponseEntity<ApiResponse<ApplicationDTO>> addNotes(
            @PathVariable Long id,
            @RequestBody String notes) {
        ApplicationDTO updated = applicationService.addRecruiterNotes(id, notes);
        return ResponseEntity.ok(ApiResponse.success("Notes added", updated));
    }

    @PostMapping("/{id}/withdraw")
    @Operation(summary = "Withdraw application")
    public ResponseEntity<ApiResponse<Void>> withdraw(
            @PathVariable Long id,
            @RequestParam Long seekerId) {
        applicationService.withdrawApplication(id, seekerId);
        return ResponseEntity.ok(ApiResponse.success("Application withdrawn"));
    }

    @GetMapping("/job/{jobId}")
    @Operation(summary = "Get applications for a job")
    public ResponseEntity<ApiResponse<Page<ApplicationDTO>>> getByJob(
            @PathVariable Long jobId,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<ApplicationDTO> applications = applicationService.getApplicationsByJob(jobId, pageable);
        return ResponseEntity.ok(ApiResponse.success("Applications retrieved", applications));
    }

    @GetMapping("/seeker/{seekerId}")
    @Operation(summary = "Get applications by seeker")
    public ResponseEntity<ApiResponse<Page<ApplicationDTO>>> getBySeeker(
            @PathVariable Long seekerId,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<ApplicationDTO> applications = applicationService.getApplicationsBySeeker(seekerId, pageable);
        return ResponseEntity.ok(ApiResponse.success("Applications retrieved", applications));
    }

    @GetMapping("/check")
    @Operation(summary = "Check if already applied")
    public ResponseEntity<ApiResponse<Boolean>> hasApplied(
            @RequestParam Long jobId,
            @RequestParam Long seekerId) {
        boolean applied = applicationService.hasApplied(jobId, seekerId);
        return ResponseEntity.ok(ApiResponse.success("Check complete", applied));
    }

    @GetMapping("/count/{jobId}")
    @Operation(summary = "Get application count for job")
    public ResponseEntity<ApiResponse<Long>> getCount(@PathVariable Long jobId) {
        Long count = applicationService.getApplicationCount(jobId);
        return ResponseEntity.ok(ApiResponse.success("Count retrieved", count));
    }

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.success("Application service is healthy", "UP"));
    }
}
