package com.capstone.realNest.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EnquiryRequest(
        @NotNull(message = "Property id is required")
        Long propertyId,

        @NotNull(message = "Customer id is required")
        Long customerId,

        @NotBlank(message = "Message is required")
        String message,

        @NotBlank(message = "Status is required")
        String status
) {
}
