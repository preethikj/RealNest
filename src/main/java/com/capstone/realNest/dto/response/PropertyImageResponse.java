package com.capstone.realNest.dto.response;

public record PropertyImageResponse(
        Long id,
        Long propertyId,
        String imageUrl,
        String publicId,
        Integer displayOrder
) {
}
