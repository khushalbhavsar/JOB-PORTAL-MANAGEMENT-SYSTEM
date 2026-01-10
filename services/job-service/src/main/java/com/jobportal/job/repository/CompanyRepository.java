package com.jobportal.job.repository;

import com.jobportal.job.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByName(String name);

    Page<Company> findByVerified(Boolean verified, Pageable pageable);

    Page<Company> findByIndustry(String industry, Pageable pageable);

    @Query("SELECT c FROM Company c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Company> searchByName(String keyword, Pageable pageable);

    List<Company> findByOwnerId(Long ownerId);

    Boolean existsByName(String name);

    @Query("SELECT DISTINCT c.industry FROM Company c WHERE c.industry IS NOT NULL")
    List<String> findAllIndustries();
}
