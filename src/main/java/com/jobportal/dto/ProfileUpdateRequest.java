package com.jobportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating user profile
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdateRequest {

    private String name;
    private String phone;
    private String address;
    private String skills;
    private String experience;
    private String education;
    private String profilePicture;
    private String resumeUrl;
}
