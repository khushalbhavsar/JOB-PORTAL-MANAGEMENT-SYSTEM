package com.jobportal.job.controller;

import com.jobportal.job.dto.ApiResponse;
import com.jobportal.job.dto.JobDTO;
import com.jobportal.job.service.JobService;
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
@RequestMapping("/api/v1/jobs")
@RequiredArgsConstructor
@Tag(name = "Jobs", description = "Job management APIs")
public class JobController {

    private final JobService jobService;

    @PostMapping
    @Operation(summary = "Create a new job")
    public ResponseEntity<ApiResponse<JobDTO>> createJob(@Valid @RequestBody JobDTO dto) {
        JobDTO created = jobService.createJob(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Job created successfully", created));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get job by ID")
    public ResponseEntity<ApiResponse<JobDTO>> getJob(@PathVariable Long id) {
        return jobService.getJobById(id)
                .map(job -> ResponseEntity.ok(ApiResponse.success("Job retrieved", job)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a job")
    public ResponseEntity<ApiResponse<JobDTO>> updateJob(
            @PathVariable Long id,
            @Valid @RequestBody JobDTO dto) {
        JobDTO updated = jobService.updateJob(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Job updated", updated));
    }

    @PostMapping("/{id}/publish")
    @Operation(summary = "Publish a job")
    public ResponseEntity<ApiResponse<JobDTO>> publishJob(@PathVariable Long id) {
        JobDTO published = jobService.publishJob(id);
        return ResponseEntity.ok(ApiResponse.success("Job published", published));
    }

    @PostMapping("/{id}/close")
    @Operation(summary = "Close a job")
    public ResponseEntity<ApiResponse<Void>> closeJob(@PathVariable Long id) {
        jobService.closeJob(id);
        return ResponseEntity.ok(ApiResponse.success("Job closed"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a job")
    public ResponseEntity<ApiResponse<Void>> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.ok(ApiResponse.success("Job deleted"));
    }

    @GetMapping
    @Operation(summary = "Get all published jobs")
    public ResponseEntity<ApiResponse<Page<JobDTO>>> getJobs(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<JobDTO> jobs = jobService.getPublishedJobs(pageable);
        return ResponseEntity.ok(ApiResponse.success("Jobs retrieved", jobs));
    }

    @GetMapping("/search")
    @Operation(summary = "Search jobs")
    public ResponseEntity<ApiResponse<Page<JobDTO>>> searchJobs(
            @RequestParam String keyword,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<JobDTO> jobs = jobService.searchJobs(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success("Search results", jobs));
    }

    @GetMapping("/company/{companyId}")
    @Operation(summary = "Get jobs by company")
    public ResponseEntity<ApiResponse<Page<JobDTO>>> getJobsByCompany(
            @PathVariable Long companyId,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<JobDTO> jobs = jobService.getJobsByCompany(companyId, pageable);
        return ResponseEntity.ok(ApiResponse.success("Company jobs retrieved", jobs));
    }

    @GetMapping("/recruiter/{recruiterId}")
    @Operation(summary = "Get jobs by recruiter")
    public ResponseEntity<ApiResponse<Page<JobDTO>>> getJobsByRecruiter(
            @PathVariable Long recruiterId,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<JobDTO> jobs = jobService.getJobsByRecruiter(recruiterId, pageable);
        return ResponseEntity.ok(ApiResponse.success("Recruiter jobs retrieved", jobs));
    }

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.success("Job service is healthy", "UP"));
    }
}
