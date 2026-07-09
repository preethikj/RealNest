package com.capstone.realNest.dto.response;

import java.time.LocalDateTime;

public record EnquiryResponse(
        Long id,
        Long propertyId,
        Long customerId,
        String message,
        String status,
        LocalDateTime createdAt
) {
}
