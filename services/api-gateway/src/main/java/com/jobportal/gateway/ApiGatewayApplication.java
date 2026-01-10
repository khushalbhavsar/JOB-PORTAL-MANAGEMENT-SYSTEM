package com.jobportal.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * API Gateway Application
 * 
 * Central entry point for all microservices.
 * Handles routing, rate limiting, and load balancing.
 */
@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
        System.out.println("========================================");
        System.out.println("  API Gateway Started Successfully!");
        System.out.println("  Port: 8080");
        System.out.println("========================================");
    }
}
