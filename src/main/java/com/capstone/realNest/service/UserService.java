package com.capstone.realNest.service;

import com.capstone.realNest.dto.request.UserProfileUpdateRequest;
import com.capstone.realNest.dto.request.UserRegistrationRequest;
import com.capstone.realNest.dto.response.UserResponse;
import com.capstone.realNest.entity.User;
import com.capstone.realNest.enums.Role;
import com.capstone.realNest.exception.UserAlreadyExistsException;
import com.capstone.realNest.exception.UserNotFoundException;
import com.capstone.realNest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse registerCustomer(UserRegistrationRequest request) {

        String email = request.email().trim().toLowerCase();

        String phone = request.phone().trim();

        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("A user with this email already exists");
        }

        if (userRepository.existsByPhone(phone)) {
            throw new UserAlreadyExistsException("A user with this phone number already exists");
        }

        User user = toEntity(request, email, phone);

        User savedUser = userRepository.save(user);

        return toResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long userId) {

        User user = findUserById(userId);
        return toResponse(user);
    }

    @Transactional
    public UserResponse updateProfile(Long userId, UserProfileUpdateRequest request) {

        User user = findUserById(userId);

        String email = request.email().trim().toLowerCase();

        String phone = request.phone().trim();

        if (userRepository.existsByEmailAndIdNot(email, userId)) {

            throw new UserAlreadyExistsException("A user with this email already exists");
        }

        if (userRepository.existsByPhoneAndIdNot(phone, userId)) {

            throw new UserAlreadyExistsException("A user with this phone number already exists");
        }

        user.setName(request.name().trim());
        user.setEmail(email);
        user.setPhone(phone);

        User updatedUser = userRepository.save(user);

        return toResponse(updatedUser);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllCustomers() {

        return userRepository
                .findByRoleOrderByCreatedAtDesc(Role.CUSTOMER)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public long countCustomers() {
        return userRepository.countByRole(Role.CUSTOMER);
    }

    private User findUserById(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    private User toEntity(UserRegistrationRequest request, String email, String phone) {

        return User.builder()
                .name(request.name().trim())
                .email(email)
                .password(passwordEncoder.encode(request.password()))
                .phone(phone)
                .role(Role.CUSTOMER)
                .build();
    }

    private UserResponse toResponse(User user) {

        return  UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .createdAt(user.getCreatedAt()).build();
    }
}