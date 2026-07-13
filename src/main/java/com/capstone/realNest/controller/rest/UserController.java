package com.capstone.realNest.controller.rest;

import com.capstone.realNest.dto.request.UserProfileUpdateRequest;
import com.capstone.realNest.dto.request.UserRegistrationRequest;
import com.capstone.realNest.dto.response.UserResponse;
import com.capstone.realNest.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor


@RequestMapping("/api/users")
@Tag(name = "User Management", description = "APIs for user registration")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Register a customer")
    public ResponseEntity<UserResponse> registerCustomer(@Valid @RequestBody UserRegistrationRequest request) {

        UserResponse response = userService.registerCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user profile")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {

        UserResponse response = userService.getUserById(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Update user profile")
    public ResponseEntity<UserResponse> updateProfile(
            @PathVariable Long userId, @Valid @RequestBody UserProfileUpdateRequest request) {

        UserResponse response = userService.updateProfile(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customers")
    @Operation(summary = "Get all customers")
    public ResponseEntity<List<UserResponse>> getAllCustomers() {

        List<UserResponse> responses = userService.getAllCustomers();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/customers/count")
    @Operation(summary = "Get customer count")
    public ResponseEntity<Long> countCustomers() {

        long count = userService.countCustomers();
        return ResponseEntity.ok(count);
    }
}