package com.capstone.realNest.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRegistrationRequest(

        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Enter a valid email address")
        String email,

        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^\\+?[0-9]{10,15}$",
                message = "Phone number must contain 10 to 15 digits")
        String phone,

        @NotBlank(message = "Password is required")
        @Size(min = 8,
                message = "Password must contain at least 8 characters")
        String password,

        @NotBlank(message = "Please confirm your password")
        String confirmPassword) {

    @AssertTrue(message = "Password and confirm password do not match")
    public boolean isPasswordMatching() {

        if (password == null || confirmPassword == null) {
            return true;
        }

        return password.equals(confirmPassword);
    }
}