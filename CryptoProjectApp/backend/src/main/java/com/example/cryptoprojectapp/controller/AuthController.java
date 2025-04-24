package com.example.cryptoprojectapp.controller;

import com.example.cryptoprojectapp.model.User;
import com.example.cryptoprojectapp.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user, BindingResult bindingResult) {
        try {
            logger.info("Attempting to register user: {}", user.getUsername());
            
            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                for (FieldError error : bindingResult.getFieldErrors()) {
                    errors.put(error.getField(), error.getDefaultMessage());
                }
                logger.error("Validation errors during registration: {}", errors);
                return ResponseEntity.badRequest().body(errors);
            }

            User registeredUser = authService.register(user);
            logger.info("User registered successfully: {}", user.getUsername());
            return ResponseEntity.ok(registeredUser);
        } catch (IllegalArgumentException e) {
            logger.error("Registration failed for user {}: {}", user.getUsername(), e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error during registration for user {}: {}", user.getUsername(), e.getMessage(), e);
            return ResponseEntity.internalServerError().body("An unexpected error occurred during registration");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        try {
            logger.info("Attempting login for user: {}", loginRequest.getUsername());
            
            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                for (FieldError error : bindingResult.getFieldErrors()) {
                    errors.put(error.getField(), error.getDefaultMessage());
                }
                logger.error("Validation errors during login: {}", errors);
                return ResponseEntity.badRequest().body(errors);
            }

            User user = authService.login(loginRequest.getUsername(), loginRequest.getPassword());
            logger.info("Login successful for user: {}", loginRequest.getUsername());
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            logger.error("Login failed for user {}: {}", loginRequest.getUsername(), e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error during login for user {}: {}", loginRequest.getUsername(), e.getMessage(), e);
            return ResponseEntity.internalServerError().body("An unexpected error occurred during login");
        }
    }

    @GetMapping("/status")
    public ResponseEntity<?> checkAuthStatus() {
        try {
            logger.info("Checking auth status");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error checking auth status: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error checking authentication status");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        try {
            logger.info("Logout requested");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error during logout: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error during logout");
        }
    }
}

class LoginRequest {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
} 