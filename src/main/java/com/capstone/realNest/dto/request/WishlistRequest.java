package com.capstone.realNest.dto.request;

import jakarta.validation.constraints.NotNull;

public record WishlistRequest(
        @NotNull(message = "User id is required")
        Long userId,

        @NotNull(message = "Property id is required")
        Long propertyId
) {
}
