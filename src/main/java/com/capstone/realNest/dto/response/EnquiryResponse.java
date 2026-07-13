package com.capstone.realNest.dto.response;

import java.time.LocalDateTime;

public record EnquiryResponse(
        Long id,
        Long propertyId,
        String propertyTitle,
        String name,
        String email,
        String phone,
        String message,
        LocalDateTime createdAt
) {
}