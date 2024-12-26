package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.service.RateLimitService;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final RateLimitService rateLimitService;

    public ApiController(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @GetMapping("/api1")
    public ResponseEntity<String> api1(@RequestHeader("user-id") String userId) {
        if (rateLimitService.allowRequest(userId, "api1")) {
            return ResponseEntity.ok("api1 response");
        } else {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Rate limit exceeded");
        }
    }

    @PostMapping("/api2")
    public ResponseEntity<String> api2(@RequestHeader("user-id") String userId) {
        if (rateLimitService.allowRequest(userId, "api2")) {
            return ResponseEntity.ok("api2 response");
        } else {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Rate limit exceeded");
        }
    }

    @PutMapping("/api3")
    public ResponseEntity<String> api3(@RequestHeader("user-id") String userId) {
        if (rateLimitService.allowRequest(userId, "api3")) {
            return ResponseEntity.ok("api3 response");
        } else {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Rate limit exceeded");
        }
    }
}