package com.jobportal.job.service;

import com.jobportal.job.dto.JobDTO;
import com.jobportal.job.entity.Company;
import com.jobportal.job.entity.Job;
import com.jobportal.job.repository.CompanyRepository;
import com.jobportal.job.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobService {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;

    @Transactional
    public JobDTO createJob(JobDTO dto) {
        Job job = mapToEntity(dto);
        job.setStatus(Job.JobStatus.DRAFT);
        Job saved = jobRepository.save(job);
        log.info("Job created: {} (ID: {})", saved.getTitle(), saved.getId());
        return mapToDTO(saved);
    }

    @Transactional
    public JobDTO updateJob(Long id, JobDTO dto) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found: " + id));

        updateEntity(job, dto);
        Job saved = jobRepository.save(job);
        return mapToDTO(saved);
    }

    @Transactional
    public JobDTO publishJob(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found: " + id));

        job.setStatus(Job.JobStatus.PUBLISHED);
        job.setPublishedAt(LocalDateTime.now());
        return mapToDTO(jobRepository.save(job));
    }

    @Transactional
    public void closeJob(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found: " + id));
        job.setStatus(Job.JobStatus.CLOSED);
        jobRepository.save(job);
    }

    @Transactional(readOnly = true)
    public Optional<JobDTO> getJobById(Long id) {
        return jobRepository.findById(id).map(job -> {
            jobRepository.incrementViewCount(id);
            return mapToDTO(job);
        });
    }

    @Transactional(readOnly = true)
    public Page<JobDTO> getPublishedJobs(Pageable pageable) {
        return jobRepository.findByStatus(Job.JobStatus.PUBLISHED, pageable)
                .map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    public Page<JobDTO> searchJobs(String keyword, Pageable pageable) {
        return jobRepository.searchJobs(keyword, Job.JobStatus.PUBLISHED, pageable)
                .map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    public Page<JobDTO> getJobsByCompany(Long companyId, Pageable pageable) {
        return jobRepository.findByCompanyId(companyId, pageable)
                .map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    public Page<JobDTO> getJobsByRecruiter(Long recruiterId, Pageable pageable) {
        return jobRepository.findByRecruiterId(recruiterId, pageable)
                .map(this::mapToDTO);
    }

    @Transactional
    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
        log.info("Job deleted: {}", id);
    }

    @Transactional
    public void incrementApplicationCount(Long jobId) {
        jobRepository.incrementApplicationCount(jobId);
    }

    private JobDTO mapToDTO(Job job) {
        JobDTO dto = JobDTO.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .requirements(job.getRequirements())
                .responsibilities(job.getResponsibilities())
                .companyId(job.getCompanyId())
                .recruiterId(job.getRecruiterId())
                .location(job.getLocation())
                .jobType(job.getJobType())
                .experienceLevel(job.getExperienceLevel())
                .salaryMin(job.getSalaryMin())
                .salaryMax(job.getSalaryMax())
                .salaryCurrency(job.getSalaryCurrency())
                .skills(job.getSkills())
                .benefits(job.getBenefits())
                .status(job.getStatus())
                .applicationDeadline(job.getApplicationDeadline())
                .maxApplications(job.getMaxApplications())
                .applicationCount(job.getApplicationCount())
                .viewCount(job.getViewCount())
                .isRemote(job.getIsRemote())
                .isFeatured(job.getIsFeatured())
                .createdAt(job.getCreatedAt())
                .publishedAt(job.getPublishedAt())
                .build();

        // Add company info
        companyRepository.findById(job.getCompanyId()).ifPresent(company -> {
            dto.setCompanyName(company.getName());
            dto.setCompanyLogoUrl(company.getLogoUrl());
        });

        return dto;
    }

    private Job mapToEntity(JobDTO dto) {
        return Job.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .requirements(dto.getRequirements())
                .responsibilities(dto.getResponsibilities())
                .companyId(dto.getCompanyId())
                .recruiterId(dto.getRecruiterId())
                .location(dto.getLocation())
                .jobType(dto.getJobType())
                .experienceLevel(dto.getExperienceLevel())
                .salaryMin(dto.getSalaryMin())
                .salaryMax(dto.getSalaryMax())
                .salaryCurrency(dto.getSalaryCurrency() != null ? dto.getSalaryCurrency() : "USD")
                .skills(dto.getSkills())
                .benefits(dto.getBenefits())
                .applicationDeadline(dto.getApplicationDeadline())
                .maxApplications(dto.getMaxApplications())
                .isRemote(dto.getIsRemote() != null ? dto.getIsRemote() : false)
                .isFeatured(dto.getIsFeatured() != null ? dto.getIsFeatured() : false)
                .build();
    }

    private void updateEntity(Job job, JobDTO dto) {
        if (dto.getTitle() != null)
            job.setTitle(dto.getTitle());
        if (dto.getDescription() != null)
            job.setDescription(dto.getDescription());
        if (dto.getRequirements() != null)
            job.setRequirements(dto.getRequirements());
        if (dto.getResponsibilities() != null)
            job.setResponsibilities(dto.getResponsibilities());
        if (dto.getLocation() != null)
            job.setLocation(dto.getLocation());
        if (dto.getJobType() != null)
            job.setJobType(dto.getJobType());
        if (dto.getExperienceLevel() != null)
            job.setExperienceLevel(dto.getExperienceLevel());
        if (dto.getSalaryMin() != null)
            job.setSalaryMin(dto.getSalaryMin());
        if (dto.getSalaryMax() != null)
            job.setSalaryMax(dto.getSalaryMax());
        if (dto.getSkills() != null)
            job.setSkills(dto.getSkills());
        if (dto.getBenefits() != null)
            job.setBenefits(dto.getBenefits());
        if (dto.getApplicationDeadline() != null)
            job.setApplicationDeadline(dto.getApplicationDeadline());
        if (dto.getIsRemote() != null)
            job.setIsRemote(dto.getIsRemote());
    }
}
