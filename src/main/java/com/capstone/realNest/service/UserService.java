package com.capstone.realNest.service;

import com.capstone.realNest.dto.request.UserProfileUpdateRequest;
import com.capstone.realNest.dto.request.UserRegistrationRequest;
import com.capstone.realNest.dto.response.UserResponse;
import com.capstone.realNest.entity.PasswordResetToken;
import com.capstone.realNest.entity.User;
import com.capstone.realNest.enums.Role;
import com.capstone.realNest.exception.UserAlreadyExistsException;
import com.capstone.realNest.exception.UserNotFoundException;
import com.capstone.realNest.repository.PasswordResetTokenRepository;
import com.capstone.realNest.repository.UserRepository;
import com.capstone.realNest.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Value("${app.base-url}")
    private String appBaseUrl;

    @Transactional
    public UserResponse registerCustomer(
            UserRegistrationRequest request
    ) {

        String email =
                request.email().trim().toLowerCase();

        String phone =
                request.phone().trim();

        if (!request.password().equals(
                request.confirmPassword()
        )) {
            throw new IllegalArgumentException(
                    "Password and confirm password do not match"
            );
        }

        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException(
                    "A user with this email already exists"
            );
        }

        if (userRepository.existsByPhone(phone)) {
            throw new UserAlreadyExistsException(
                    "A user with this phone number already exists"
            );
        }

        User user =
                toEntity(request, email, phone);

        User savedUser =
                userRepository.save(user);

        return toResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long userId) {

        User user = findUserById(userId);

        return toResponse(user);
    }

    @Transactional
    public UserResponse updateProfile(
            Long userId,
            UserProfileUpdateRequest request
    ) {

        User user = findUserById(userId);

        String email =
                request.email().trim().toLowerCase();

        String phone =
                request.phone().trim();

        if (userRepository.existsByEmailAndIdNot(
                email,
                userId
        )) {
            throw new UserAlreadyExistsException(
                    "A user with this email already exists"
            );
        }

        if (userRepository.existsByPhoneAndIdNot(
                phone,
                userId
        )) {
            throw new UserAlreadyExistsException(
                    "A user with this phone number already exists"
            );
        }

        user.setName(request.name().trim());
        user.setEmail(email);
        user.setPhone(phone);

        User updatedUser =
                userRepository.save(user);

        return toResponse(updatedUser);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllCustomers() {

        return userRepository
                .findByRoleOrderByCreatedAtDesc(
                        Role.CUSTOMER
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public long countCustomers() {

        return userRepository.countByRole(
                Role.CUSTOMER
        );
    }

    @Transactional
    public void sendPasswordResetLink(String email) {

        String normalizedEmail =
                email.trim().toLowerCase();

        User user =
                userRepository
                        .findByEmailIgnoreCase(normalizedEmail)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "No account was found with this email."
                                )
                        );

        String token =
                UUID.randomUUID().toString();

        PasswordResetToken resetToken =
                PasswordResetToken.builder()
                        .token(token)
                        .userId(user.getId())
                        .expiryDate(
                                LocalDateTime.now()
                                        .plusMinutes(15)
                        )
                        .used(false)
                        .build();

        passwordResetTokenRepository.save(
                resetToken
        );

        String resetLink =
                appBaseUrl
                        + "/auth/reset-password?token="
                        + token;

        emailService.sendPasswordResetEmail(
                user,
                resetLink
        );
    }

    @Transactional
    public void resetPassword(
            String token,
            String newPassword
    ) {

        if (
                newPassword == null ||
                        newPassword.length() < 8
        ) {
            throw new IllegalArgumentException(
                    "Password must contain at least 8 characters."
            );
        }

        PasswordResetToken resetToken =
                passwordResetTokenRepository
                        .findByToken(token)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Invalid password reset link."
                                )
                        );

        if (resetToken.isUsed()) {
            throw new IllegalArgumentException(
                    "This password reset link has already been used."
            );
        }

        if (resetToken.getExpiryDate()
                .isBefore(LocalDateTime.now())) {

            throw new IllegalArgumentException(
                    "Password reset link has expired."
            );
        }

        User user =
                userRepository
                        .findById(resetToken.getUserId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "User account was not found."
                                )
                        );

        user.setPassword(
                passwordEncoder.encode(newPassword)
        );

        userRepository.save(user);

        resetToken.setUsed(true);

        passwordResetTokenRepository.save(
                resetToken
        );
    }

    private User findUserById(Long userId) {

        return userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(userId)
                );
    }

    private User toEntity(
            UserRegistrationRequest request,
            String email,
            String phone
    ) {

        return User.builder()
                .name(request.name().trim())
                .email(email)
                .password(
                        passwordEncoder.encode(
                                request.password()
                        )
                )
                .phone(phone)
                .role(Role.CUSTOMER)
                .build();
    }

    private UserResponse toResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}