package com.capstone.realNest.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EnquiryRequest(

        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Enter a valid email address")
        String email,

        @NotBlank(message = "Phone number is required")
        @Pattern(
                regexp = "^[0-9]{10,15}$",
                message = "Phone number must contain 10 to 15 digits"
        )
        String phone,

        @NotBlank(message = "Message is required")
        @Size(
                max = 2000,
                message = "Message cannot exceed 2000 characters"
        )
        String message
) {
}