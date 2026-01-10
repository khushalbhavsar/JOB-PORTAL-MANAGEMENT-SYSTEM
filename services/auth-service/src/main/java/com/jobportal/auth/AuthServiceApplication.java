package com.jobportal.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Auth Service - Authentication & Authorization Microservice
 * 
 * Handles:
 * - User registration and login
 * - JWT token generation and validation
 * - Role-based access control (RBAC)
 * - Token refresh and revocation
 * - Password reset functionality
 * 
 * @author Khushal Bhavsar
 * @version 1.0.0
 */
@SpringBootApplication
public class AuthServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }
}
