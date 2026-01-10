package com.jobportal.repository;

import com.jobportal.model.Recruiter;
import com.jobportal.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Recruiter Repository - Data Access Layer for Recruiter Entity
 * 
 * Provides CRUD operations and custom queries for Recruiter entity
 * 
 * Part of the Data Layer (Tier 3)
 */
@Repository
public interface RecruiterRepository extends JpaRepository<Recruiter, Long> {

    // Find recruiter by user
    Optional<Recruiter> findByUser(User user);

    // Find recruiter by user ID
    Optional<Recruiter> findByUserUserId(Long userId);

    // Check if recruiter exists for a user
    Boolean existsByUser(User user);

    // Find all verified recruiters
    List<Recruiter> findByIsVerifiedTrue();

    // Find all unverified recruiters (for admin approval)
    List<Recruiter> findByIsVerifiedFalse();

    // Find recruiters by company name (case-insensitive search)
    @Query("SELECT r FROM Recruiter r WHERE LOWER(r.companyName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Recruiter> searchByCompanyName(@Param("name") String name);

    // Find recruiters by industry
    List<Recruiter> findByIndustry(String industry);

    // Find recruiters by headquarters location
    List<Recruiter> findByHeadquarters(String headquarters);

    // Find all recruiters with pagination
    Page<Recruiter> findAll(Pageable pageable);

    // Find verified recruiters with pagination
    Page<Recruiter> findByIsVerifiedTrue(Pageable pageable);

    // Count verified recruiters
    Long countByIsVerifiedTrue();

    // Count unverified recruiters
    Long countByIsVerifiedFalse();

    // Find top recruiters by job count
    @Query("SELECT r FROM Recruiter r LEFT JOIN r.jobs j GROUP BY r ORDER BY COUNT(j) DESC")
    List<Recruiter> findTopRecruiters(Pageable pageable);
}
