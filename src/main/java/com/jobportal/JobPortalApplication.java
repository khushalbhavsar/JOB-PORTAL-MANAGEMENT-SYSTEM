package com.jobportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Job Portal Management System
 * 
 * A 3-Tier Java Application using Spring Boot
 * 
 * Tier 1: Presentation Layer (REST APIs + Thymeleaf Templates)
 * Tier 2: Business Logic Layer (Services)
 * Tier 3: Data Layer (JPA Repositories + MySQL)
 * 
 * @author Khushal Bhavsar
 */
@SpringBootApplication
public class JobPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobPortalApplication.class, args);
        System.out.println("========================================");
        System.out.println("  Job Portal Management System Started  ");
        System.out.println("  Access: http://localhost:8080         ");
        System.out.println("========================================");
    }
}
