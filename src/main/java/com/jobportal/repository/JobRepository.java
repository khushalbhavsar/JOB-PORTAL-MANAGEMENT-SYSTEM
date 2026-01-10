package com.jobportal.repository;

import com.jobportal.model.Job;
import com.jobportal.model.Recruiter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Job Repository - Data Access Layer for Job Entity
 * 
 * Provides CRUD operations and custom queries for Job entity
 * 
 * Part of the Data Layer (Tier 3)
 */
@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    // Find all active jobs
    List<Job> findByIsActiveTrue();

    // Find all active jobs with pagination
    Page<Job> findByIsActiveTrue(Pageable pageable);

    // Find jobs by recruiter
    List<Job> findByRecruiter(Recruiter recruiter);

    // Find jobs by recruiter with pagination
    Page<Job> findByRecruiter(Recruiter recruiter, Pageable pageable);

    // Find active jobs by recruiter
    List<Job> findByRecruiterAndIsActiveTrue(Recruiter recruiter);

    // Search jobs by title (case-insensitive)
    @Query("SELECT j FROM Job j WHERE j.isActive = true AND LOWER(j.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    Page<Job> searchByTitle(@Param("title") String title, Pageable pageable);

    // Search jobs by location
    @Query("SELECT j FROM Job j WHERE j.isActive = true AND LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))")
    Page<Job> searchByLocation(@Param("location") String location, Pageable pageable);

    // Search jobs by skills
    @Query("SELECT j FROM Job j WHERE j.isActive = true AND LOWER(j.skills) LIKE LOWER(CONCAT('%', :skill, '%'))")
    Page<Job> searchBySkill(@Param("skill") String skill, Pageable pageable);

    // Advanced search with multiple criteria
    @Query("SELECT j FROM Job j WHERE j.isActive = true " +
           "AND (:title IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
           "AND (:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))) " +
           "AND (:skill IS NULL OR LOWER(j.skills) LIKE LOWER(CONCAT('%', :skill, '%'))) " +
           "AND (:jobType IS NULL OR j.jobType = :jobType) " +
           "AND (:experienceLevel IS NULL OR j.experienceLevel = :experienceLevel)")
    Page<Job> advancedSearch(
            @Param("title") String title,
            @Param("location") String location,
            @Param("skill") String skill,
            @Param("jobType") String jobType,
            @Param("experienceLevel") String experienceLevel,
            Pageable pageable);

    // Find jobs by job type
    List<Job> findByJobTypeAndIsActiveTrue(String jobType);

    // Find remote jobs
    List<Job> findByIsRemoteTrueAndIsActiveTrue();

    // Find jobs by salary range
    @Query("SELECT j FROM Job j WHERE j.isActive = true AND j.minSalary >= :minSalary AND j.maxSalary <= :maxSalary")
    List<Job> findBySalaryRange(@Param("minSalary") Double minSalary, @Param("maxSalary") Double maxSalary);

    // Find jobs with approaching deadline
    @Query("SELECT j FROM Job j WHERE j.isActive = true AND j.applicationDeadline BETWEEN :today AND :deadline")
    List<Job> findJobsWithApproachingDeadline(@Param("today") LocalDate today, @Param("deadline") LocalDate deadline);

    // Find expired jobs
    @Query("SELECT j FROM Job j WHERE j.isActive = true AND j.applicationDeadline < :today")
    List<Job> findExpiredJobs(@Param("today") LocalDate today);

    // Count active jobs
    Long countByIsActiveTrue();

    // Count jobs by recruiter
    Long countByRecruiter(Recruiter recruiter);

    // Increment view count
    @Modifying
    @Query("UPDATE Job j SET j.viewsCount = j.viewsCount + 1 WHERE j.jobId = :jobId")
    void incrementViewCount(@Param("jobId") Long jobId);

    // Increment application count
    @Modifying
    @Query("UPDATE Job j SET j.applicationsCount = j.applicationsCount + 1 WHERE j.jobId = :jobId")
    void incrementApplicationCount(@Param("jobId") Long jobId);

    // Find most viewed jobs
    @Query("SELECT j FROM Job j WHERE j.isActive = true ORDER BY j.viewsCount DESC")
    List<Job> findMostViewedJobs(Pageable pageable);

    // Find recently posted jobs
    @Query("SELECT j FROM Job j WHERE j.isActive = true ORDER BY j.createdAt DESC")
    List<Job> findRecentJobs(Pageable pageable);
}
