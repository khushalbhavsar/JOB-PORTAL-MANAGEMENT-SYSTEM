package com.jobportal.repository;

import com.jobportal.model.Application;
import com.jobportal.model.ApplicationStatus;
import com.jobportal.model.Job;
import com.jobportal.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Application Repository - Data Access Layer for Application Entity
 * 
 * Provides CRUD operations and custom queries for Application entity
 * 
 * Part of the Data Layer (Tier 3)
 */
@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    // Find applications by user (job seeker)
    List<Application> findByUser(User user);

    // Find applications by user with pagination
    Page<Application> findByUser(User user, Pageable pageable);

    // Find applications by job
    List<Application> findByJob(Job job);

    // Find applications by job with pagination
    Page<Application> findByJob(Job job, Pageable pageable);

    // Find application by user and job (to prevent duplicate applications)
    Optional<Application> findByUserAndJob(User user, Job job);

    // Check if application exists for user and job
    Boolean existsByUserAndJob(User user, Job job);

    // Find applications by status
    List<Application> findByStatus(ApplicationStatus status);

    // Find applications by user and status
    List<Application> findByUserAndStatus(User user, ApplicationStatus status);

    // Find applications by job and status
    List<Application> findByJobAndStatus(Job job, ApplicationStatus status);

    // Find applications for recruiter's jobs
    @Query("SELECT a FROM Application a WHERE a.job.recruiter.recruiterId = :recruiterId")
    List<Application> findByRecruiterId(@Param("recruiterId") Long recruiterId);

    // Find applications for recruiter's jobs with pagination
    @Query("SELECT a FROM Application a WHERE a.job.recruiter.recruiterId = :recruiterId")
    Page<Application> findByRecruiterId(@Param("recruiterId") Long recruiterId, Pageable pageable);

    // Find applications for recruiter's jobs by status
    @Query("SELECT a FROM Application a WHERE a.job.recruiter.recruiterId = :recruiterId AND a.status = :status")
    List<Application> findByRecruiterIdAndStatus(
            @Param("recruiterId") Long recruiterId, 
            @Param("status") ApplicationStatus status);

    // Count applications by user
    Long countByUser(User user);

    // Count applications by job
    Long countByJob(Job job);

    // Count applications by status
    Long countByStatus(ApplicationStatus status);

    // Count applications by job and status
    Long countByJobAndStatus(Job job, ApplicationStatus status);

    // Find recent applications for a user
    @Query("SELECT a FROM Application a WHERE a.user = :user ORDER BY a.appliedDate DESC")
    List<Application> findRecentApplicationsByUser(@Param("user") User user, Pageable pageable);

    // Find applications within date range
    @Query("SELECT a FROM Application a WHERE a.appliedDate BETWEEN :startDate AND :endDate")
    List<Application> findApplicationsInDateRange(
            @Param("startDate") LocalDateTime startDate, 
            @Param("endDate") LocalDateTime endDate);

    // Get application statistics for a recruiter
    @Query("SELECT a.status, COUNT(a) FROM Application a WHERE a.job.recruiter.recruiterId = :recruiterId GROUP BY a.status")
    List<Object[]> getApplicationStatsByRecruiter(@Param("recruiterId") Long recruiterId);

    // Find shortlisted candidates for a job
    @Query("SELECT a FROM Application a WHERE a.job.jobId = :jobId AND a.status = 'SHORTLISTED' ORDER BY a.appliedDate")
    List<Application> findShortlistedForJob(@Param("jobId") Long jobId);
}
