package com.jobportal.auth.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Role Entity
 * 
 * Defines user roles for RBAC (Role-Based Access Control).
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 20)
    private RoleName name;

    @Column(length = 200)
    private String description;

    public enum RoleName {
        ROLE_ADMIN,
        ROLE_RECRUITER,
        ROLE_JOBSEEKER
    }
}
