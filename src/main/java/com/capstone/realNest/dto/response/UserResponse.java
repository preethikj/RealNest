package com.capstone.realNest.dto.response;

import com.capstone.realNest.enums.Role;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String name,
        String email,
        String phone,
        Role role,
        LocalDateTime createdAt
) {
}
