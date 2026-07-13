package com.capstone.realNest.controller.rest;

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
}