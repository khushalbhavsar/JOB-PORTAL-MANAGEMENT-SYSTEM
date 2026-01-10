package com.jobportal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for User response (without sensitive data like password)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long userId;
    private String name;
    private String email;
    private String role;
    private String phone;
    private String address;
    private String profilePicture;
    private String resumeUrl;
    private String skills;
    private String experience;
    private String education;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
