package com.jobportal.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Job Service - Job Posting and Management Microservice
 * 
 * Handles:
 * - Job posting creation and management
 * - Job search and filtering
 * - Job categories and tags
 * - Company job listings
 * 
 * @author Khushal Bhavsar
 * @version 1.0.0
 */
@SpringBootApplication
public class JobServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(JobServiceApplication.class, args);
    }
}
