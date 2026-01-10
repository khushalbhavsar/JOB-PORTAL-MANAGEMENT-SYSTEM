package com.jobportal.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * Fallback Controller
 * 
 * Handles circuit breaker fallbacks when services are unavailable.
 */
@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/auth")
    public Mono<ResponseEntity<Map<String, Object>>> authFallback() {
        return Mono.just(createFallbackResponse("Auth Service", "Authentication service is currently unavailable"));
    }

    @GetMapping("/users")
    public Mono<ResponseEntity<Map<String, Object>>> usersFallback() {
        return Mono.just(createFallbackResponse("User Service", "User service is currently unavailable"));
    }

    @GetMapping("/jobs")
    public Mono<ResponseEntity<Map<String, Object>>> jobsFallback() {
        return Mono.just(createFallbackResponse("Job Service", "Job service is currently unavailable"));
    }

    @GetMapping("/applications")
    public Mono<ResponseEntity<Map<String, Object>>> applicationsFallback() {
        return Mono.just(createFallbackResponse("Application Service", "Application service is currently unavailable"));
    }

    @GetMapping("/admin")
    public Mono<ResponseEntity<Map<String, Object>>> adminFallback() {
        return Mono.just(createFallbackResponse("Admin Service", "Admin service is currently unavailable"));
    }

    private ResponseEntity<Map<String, Object>> createFallbackResponse(String service, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("service", service);
        response.put("message", message);
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
}
