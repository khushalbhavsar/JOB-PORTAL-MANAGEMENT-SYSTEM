package com.jobportal.application.service;

import com.jobportal.application.dto.ApplicationDTO;
import com.jobportal.application.entity.JobApplication;
import com.jobportal.application.repository.ApplicationRepository;
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
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    @Transactional
    public ApplicationDTO applyForJob(ApplicationDTO dto) {
        // Check if already applied
        if (applicationRepository.existsByJobIdAndSeekerId(dto.getJobId(), dto.getSeekerId())) {
            throw new RuntimeException("Already applied to this job");
        }

        JobApplication application = JobApplication.builder()
                .jobId(dto.getJobId())
                .seekerId(dto.getSeekerId())
                .resumeUrl(dto.getResumeUrl())
                .coverLetter(dto.getCoverLetter())
                .expectedSalary(dto.getExpectedSalary())
                .noticePeriod(dto.getNoticePeriod())
                .notes(dto.getNotes())
                .status(JobApplication.ApplicationStatus.PENDING)
                .build();

        JobApplication saved = applicationRepository.save(application);
        log.info("Application submitted: Job {} by Seeker {}", dto.getJobId(), dto.getSeekerId());
        return mapToDTO(saved);
    }

    @Transactional
    public ApplicationDTO updateStatus(Long id, JobApplication.ApplicationStatus status, Long reviewerId) {
        JobApplication application = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found: " + id));

        application.setStatus(status);
        application.setReviewedAt(LocalDateTime.now());
        application.setReviewedBy(reviewerId);

        log.info("Application {} status updated to {}", id, status);
        return mapToDTO(applicationRepository.save(application));
    }

    @Transactional
    public ApplicationDTO addRecruiterNotes(Long id, String notes) {
        JobApplication application = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found: " + id));

        application.setRecruiterNotes(notes);
        return mapToDTO(applicationRepository.save(application));
    }

    @Transactional
    public void withdrawApplication(Long id, Long seekerId) {
        JobApplication application = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found: " + id));

        if (!application.getSeekerId().equals(seekerId)) {
            throw new RuntimeException("Unauthorized to withdraw this application");
        }

        application.setStatus(JobApplication.ApplicationStatus.WITHDRAWN);
        applicationRepository.save(application);
        log.info("Application {} withdrawn by seeker {}", id, seekerId);
    }

    @Transactional(readOnly = true)
    public Optional<ApplicationDTO> getApplicationById(Long id) {
        return applicationRepository.findById(id).map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    public Page<ApplicationDTO> getApplicationsByJob(Long jobId, Pageable pageable) {
        return applicationRepository.findByJobId(jobId, pageable).map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    public Page<ApplicationDTO> getApplicationsBySeeker(Long seekerId, Pageable pageable) {
        return applicationRepository.findBySeekerId(seekerId, pageable).map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    public boolean hasApplied(Long jobId, Long seekerId) {
        return applicationRepository.existsByJobIdAndSeekerId(jobId, seekerId);
    }

    @Transactional(readOnly = true)
    public Long getApplicationCount(Long jobId) {
        return applicationRepository.countByJobId(jobId);
    }

    private ApplicationDTO mapToDTO(JobApplication app) {
        return ApplicationDTO.builder()
                .id(app.getId())
                .jobId(app.getJobId())
                .seekerId(app.getSeekerId())
                .resumeUrl(app.getResumeUrl())
                .coverLetter(app.getCoverLetter())
                .status(app.getStatus())
                .notes(app.getNotes())
                .recruiterNotes(app.getRecruiterNotes())
                .expectedSalary(app.getExpectedSalary())
                .noticePeriod(app.getNoticePeriod())
                .appliedAt(app.getAppliedAt())
                .reviewedAt(app.getReviewedAt())
                .reviewedBy(app.getReviewedBy())
                .build();
    }
}
