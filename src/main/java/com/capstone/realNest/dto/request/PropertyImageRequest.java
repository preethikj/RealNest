package com.capstone.realNest.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PropertyImageRequest(
        @NotNull(message = "Property id is required")
        Long propertyId,

        @NotBlank(message = "Image URL is required")
        String imageUrl,

        @NotBlank(message = "Cloudinary public id is required")
        String publicId,

        @NotNull(message = "Display order is required")
        Integer displayOrder
) {
}
