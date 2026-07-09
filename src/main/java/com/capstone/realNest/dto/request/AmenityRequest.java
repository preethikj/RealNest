package com.capstone.realNest.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AmenityRequest(
        @NotBlank(message = "Name is required")
        String name,

        String icon
) {
}
