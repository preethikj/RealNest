package com.capstone.realNest.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PropertyResponse(

        Long id,

        String title,

        String description,

        BigDecimal price,

        String propertyType,

        String listingType,

        String status,

        Integer bedrooms,

        Integer bathrooms,

        BigDecimal area,

        String address,

        String city,

        String state,

        String country,

        String pincode,

        BigDecimal latitude,

        BigDecimal longitude,

        Long ownerId,

        String ownerName,

        List<PropertyImageResponse> images,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}