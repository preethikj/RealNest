package com.capstone.realNest.dto.response;

import com.capstone.realNest.enums.Role;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
public record UserResponse(
        Long id,
        String name,
        String email,
        String phone,
        Role role,
        LocalDateTime createdAt
) {
}