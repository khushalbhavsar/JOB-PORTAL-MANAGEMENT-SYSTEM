package com.jobportal.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application Service - Job Application Management Microservice
 * 
 * Handles:
 * - Job application submission
 * - Application status tracking
 * - Resume/document management
 * - Application history
 * 
 * @author Khushal Bhavsar
 * @version 1.0.0
 */
@SpringBootApplication
public class ApplicationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationServiceApplication.class, args);
    }
}
