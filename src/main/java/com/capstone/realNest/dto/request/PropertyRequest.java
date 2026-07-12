package com.capstone.realNest.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PropertyRequest(

        @NotBlank(message = "Title is required")
        String title,

        String description,

        @NotNull(message = "Price is required")
        @DecimalMin(
                value = "0.0",
                inclusive = false,
                message = "Price must be greater than zero"
        )
        BigDecimal price,

        @NotBlank(message = "Property type is required")
        String propertyType,

        @NotBlank(message = "Listing type is required")
        String listingType,

        Integer bedrooms,

        Integer bathrooms,

        BigDecimal area,

        @NotBlank(message = "Address is required")
        String address,

        @NotBlank(message = "City is required")
        String city,

        @NotBlank(message = "State is required")
        String state,

        @NotBlank(message = "Country is required")
        String country,

        @NotBlank(message = "Pincode is required")
        String pincode,

        BigDecimal latitude,

        BigDecimal longitude
) {
}