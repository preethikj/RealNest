package com.capstone.realNest.controller.rest;

import com.capstone.realNest.dto.request.UserProfileUpdateRequest;
import com.capstone.realNest.dto.request.UserRegistrationRequest;
import com.capstone.realNest.dto.response.UserResponse;
import com.capstone.realNest.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "APIs for registration, profiles and password management")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Register a customer")
    public ResponseEntity<UserResponse> registerCustomer(@Valid @RequestBody UserRegistrationRequest request) {

        UserResponse response = userService.registerCustomer(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Send a password reset link")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {

        userService.sendPasswordResetLink(email);

        return ResponseEntity.ok("Password reset link sent successfully");
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset a user password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {

        userService.resetPassword(token, newPassword);

        return ResponseEntity.ok("Password reset successfully");
    }


    @SecurityRequirement(name = "basicAuth")
    @GetMapping("/{userId}")
    @Operation(summary = "Get user profile")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {

        UserResponse response = userService.getUserById(userId);

        return ResponseEntity.ok(response);
    }


    @SecurityRequirement(name = "basicAuth")
    @PutMapping("/{userId}")
    @Operation(summary = "Update user profile")
    public ResponseEntity<UserResponse> updateProfile(@PathVariable Long userId, @Valid @RequestBody UserProfileUpdateRequest request) {

        UserResponse response = userService.updateProfile(userId, request);

        return ResponseEntity.ok(response);
    }


    @SecurityRequirement(name = "basicAuth")
    @GetMapping("/customers")
    @Operation(summary = "Get all customers")
    public ResponseEntity<List<UserResponse>> getAllCustomers() {

        List<UserResponse> responses = userService.getAllCustomers();

        return ResponseEntity.ok(responses);
    }


    @SecurityRequirement(name = "basicAuth")
    @GetMapping("/customers/count")
    @Operation(summary = "Get customer count")
    public ResponseEntity<Long> countCustomers() {

        long count = userService.countCustomers();
        return ResponseEntity.ok(count);
    }
}