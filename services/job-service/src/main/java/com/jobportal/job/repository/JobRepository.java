package com.jobportal.job.repository;

import com.jobportal.job.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    Page<Job> findByStatus(Job.JobStatus status, Pageable pageable);

    Page<Job> findByCompanyId(Long companyId, Pageable pageable);

    Page<Job> findByRecruiterId(Long recruiterId, Pageable pageable);

    @Query("SELECT j FROM Job j WHERE j.status = :status AND " +
            "(LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Job> searchJobs(String keyword, Job.JobStatus status, Pageable pageable);

    @Query("SELECT j FROM Job j WHERE j.status = 'PUBLISHED' AND j.location = :location")
    Page<Job> findByLocation(String location, Pageable pageable);

    @Query("SELECT j FROM Job j WHERE j.status = 'PUBLISHED' AND j.jobType = :jobType")
    Page<Job> findByJobType(Job.JobType jobType, Pageable pageable);

    List<Job> findByStatusAndApplicationDeadlineBefore(Job.JobStatus status, LocalDateTime deadline);

    @Modifying
    @Query("UPDATE Job j SET j.viewCount = j.viewCount + 1 WHERE j.id = :jobId")
    void incrementViewCount(Long jobId);

    @Modifying
    @Query("UPDATE Job j SET j.applicationCount = j.applicationCount + 1 WHERE j.id = :jobId")
    void incrementApplicationCount(Long jobId);

    @Query("SELECT j FROM Job j WHERE j.status = 'PUBLISHED' AND j.isFeatured = true ORDER BY j.publishedAt DESC")
    List<Job> findFeaturedJobs(Pageable pageable);

    @Query("SELECT COUNT(j) FROM Job j WHERE j.companyId = :companyId AND j.status = 'PUBLISHED'")
    Long countActiveJobsByCompany(Long companyId);
}
