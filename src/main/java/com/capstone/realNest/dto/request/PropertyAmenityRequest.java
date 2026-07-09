package com.capstone.realNest.dto.request;

import jakarta.validation.constraints.NotNull;

public record PropertyAmenityRequest(
        @NotNull(message = "Property id is required")
        Long propertyId,

        @NotNull(message = "Amenity id is required")
        Long amenityId
) {
}
