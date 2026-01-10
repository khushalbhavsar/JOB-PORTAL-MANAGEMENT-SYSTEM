package com.jobportal.repository;

import com.jobportal.model.Role;
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
 * User Repository - Data Access Layer for User Entity
 * 
 * Provides CRUD operations and custom queries for User entity
 * 
 * Part of the Data Layer (Tier 3)
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by email (for authentication)
    Optional<User> findByEmail(String email);

    // Check if email exists (for registration validation)
    Boolean existsByEmail(String email);

    // Find all users by role
    List<User> findByRole(Role role);

    // Find all users by role with pagination
    Page<User> findByRole(Role role, Pageable pageable);

    // Find all active users
    List<User> findByIsActiveTrue();

    // Find all active users by role
    List<User> findByRoleAndIsActiveTrue(Role role);

    // Search users by name (case-insensitive)
    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<User> searchByName(@Param("name") String name);

    // Search users by name and role
    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%')) AND u.role = :role")
    List<User> searchByNameAndRole(@Param("name") String name, @Param("role") Role role);

    // Count users by role
    Long countByRole(Role role);

    // Count active users
    Long countByIsActiveTrue();

    // Find users with skills matching (for job seekers)
    @Query("SELECT u FROM User u WHERE u.role = 'JOB_SEEKER' AND LOWER(u.skills) LIKE LOWER(CONCAT('%', :skill, '%'))")
    List<User> findJobSeekersBySkill(@Param("skill") String skill);
}
