package com.jobportal.service;

import com.jobportal.dto.RecruiterDTO;
import com.jobportal.dto.RecruiterUpdateRequest;
import com.jobportal.exception.ResourceNotFoundException;
import com.jobportal.model.Recruiter;
import com.jobportal.model.User;
import com.jobportal.repository.RecruiterRepository;
import com.jobportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Recruiter Service - Business Logic Layer
 * 
 * Handles recruiter/company profile management
 * 
 * Part of the Business Logic Layer (Tier 2)
 */
@Service
@RequiredArgsConstructor
public class RecruiterService {

    private final RecruiterRepository recruiterRepository;
    private final UserRepository userRepository;

    /**
     * Get recruiter by ID
     */
    public RecruiterDTO getRecruiterById(Long recruiterId) {
        Recruiter recruiter = recruiterRepository.findById(recruiterId)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found with id: " + recruiterId));
        return mapToRecruiterDTO(recruiter);
    }

    /**
     * Get recruiter by user ID
     */
    public RecruiterDTO getRecruiterByUserId(Long userId) {
        Recruiter recruiter = recruiterRepository.findByUserUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter profile not found for user id: " + userId));
        return mapToRecruiterDTO(recruiter);
    }

    /**
     * Get recruiter entity by user ID
     */
    public Recruiter getRecruiterEntityByUserId(Long userId) {
        return recruiterRepository.findByUserUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter profile not found"));
    }

    /**
     * Update recruiter profile
     */
    @Transactional
    public RecruiterDTO updateRecruiterProfile(Long userId, RecruiterUpdateRequest request) {
        Recruiter recruiter = recruiterRepository.findByUserUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter profile not found"));

        User user = recruiter.getUser();

        // Update recruiter fields
        if (request.getCompanyName() != null) {
            recruiter.setCompanyName(request.getCompanyName());
        }
        if (request.getCompanyDescription() != null) {
            recruiter.setCompanyDescription(request.getCompanyDescription());
        }
        if (request.getCompanyWebsite() != null) {
            recruiter.setCompanyWebsite(request.getCompanyWebsite());
        }
        if (request.getCompanyLogo() != null) {
            recruiter.setCompanyLogo(request.getCompanyLogo());
        }
        if (request.getIndustry() != null) {
            recruiter.setIndustry(request.getIndustry());
        }
        if (request.getCompanySize() != null) {
            recruiter.setCompanySize(request.getCompanySize());
        }
        if (request.getHeadquarters() != null) {
            recruiter.setHeadquarters(request.getHeadquarters());
        }
        if (request.getFoundedYear() != null) {
            recruiter.setFoundedYear(request.getFoundedYear());
        }

        // Update user fields
        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }

        userRepository.save(user);
        Recruiter updatedRecruiter = recruiterRepository.save(recruiter);
        return mapToRecruiterDTO(updatedRecruiter);
    }

    /**
     * Get all recruiters with pagination
     */
    public Page<RecruiterDTO> getAllRecruiters(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Recruiter> recruiters = recruiterRepository.findAll(pageable);
        return recruiters.map(this::mapToRecruiterDTO);
    }

    /**
     * Get all verified recruiters
     */
    public Page<RecruiterDTO> getVerifiedRecruiters(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Recruiter> recruiters = recruiterRepository.findByIsVerifiedTrue(pageable);
        return recruiters.map(this::mapToRecruiterDTO);
    }

    /**
     * Search recruiters by company name
     */
    public List<RecruiterDTO> searchByCompanyName(String companyName) {
        List<Recruiter> recruiters = recruiterRepository.searchByCompanyName(companyName);
        return recruiters.stream()
                .map(this::mapToRecruiterDTO)
                .collect(Collectors.toList());
    }

    /**
     * Verify recruiter (admin)
     */
    @Transactional
    public RecruiterDTO verifyRecruiter(Long recruiterId) {
        Recruiter recruiter = recruiterRepository.findById(recruiterId)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));

        recruiter.setIsVerified(true);
        Recruiter updatedRecruiter = recruiterRepository.save(recruiter);
        return mapToRecruiterDTO(updatedRecruiter);
    }

    /**
     * Get all verified recruiters (for public listing)
     */
    public List<RecruiterDTO> getAllVerifiedRecruiters() {
        List<Recruiter> recruiters = recruiterRepository.findByIsVerifiedTrue();
        return recruiters.stream()
                .map(this::mapToRecruiterDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get unverified recruiters (admin)
     */
    public List<RecruiterDTO> getUnverifiedRecruiters() {
        List<Recruiter> recruiters = recruiterRepository.findByIsVerifiedFalse();
        return recruiters.stream()
                .map(this::mapToRecruiterDTO)
                .collect(Collectors.toList());
    }

    /**
     * Map Recruiter entity to RecruiterDTO
     */
    private RecruiterDTO mapToRecruiterDTO(Recruiter recruiter) {
        User user = recruiter.getUser();
        return RecruiterDTO.builder()
                .recruiterId(recruiter.getRecruiterId())
                .companyName(recruiter.getCompanyName())
                .companyDescription(recruiter.getCompanyDescription())
                .companyWebsite(recruiter.getCompanyWebsite())
                .companyLogo(recruiter.getCompanyLogo())
                .industry(recruiter.getIndustry())
                .companySize(recruiter.getCompanySize())
                .headquarters(recruiter.getHeadquarters())
                .foundedYear(recruiter.getFoundedYear())
                .isVerified(recruiter.getIsVerified())
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .jobCount((long) recruiter.getJobs().size())
                .createdAt(recruiter.getCreatedAt())
                .build();
    }
}
