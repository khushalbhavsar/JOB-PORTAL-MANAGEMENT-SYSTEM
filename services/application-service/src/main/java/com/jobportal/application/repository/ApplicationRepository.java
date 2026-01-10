package com.jobportal.application.repository;

import com.jobportal.application.entity.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<JobApplication, Long> {

    Page<JobApplication> findBySeekerId(Long seekerId, Pageable pageable);

    Page<JobApplication> findByJobId(Long jobId, Pageable pageable);

    Page<JobApplication> findByJobIdAndStatus(Long jobId, JobApplication.ApplicationStatus status, Pageable pageable);

    Optional<JobApplication> findByJobIdAndSeekerId(Long jobId, Long seekerId);

    Boolean existsByJobIdAndSeekerId(Long jobId, Long seekerId);

    @Query("SELECT COUNT(a) FROM JobApplication a WHERE a.jobId = :jobId")
    Long countByJobId(Long jobId);

    @Query("SELECT COUNT(a) FROM JobApplication a WHERE a.seekerId = :seekerId")
    Long countBySeekerId(Long seekerId);

    @Query("SELECT COUNT(a) FROM JobApplication a WHERE a.jobId = :jobId AND a.status = :status")
    Long countByJobIdAndStatus(Long jobId, JobApplication.ApplicationStatus status);

    List<JobApplication> findBySeekerIdAndStatus(Long seekerId, JobApplication.ApplicationStatus status);

    @Query("SELECT a.status, COUNT(a) FROM JobApplication a WHERE a.jobId = :jobId GROUP BY a.status")
    List<Object[]> getStatusCountsByJobId(Long jobId);
}
