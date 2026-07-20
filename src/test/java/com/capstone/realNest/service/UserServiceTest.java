package com.capstone.realNest.service;

import com.capstone.realNest.dto.request.UserRegistrationRequest;
import com.capstone.realNest.dto.response.UserResponse;
import com.capstone.realNest.entity.User;
import com.capstone.realNest.enums.Role;
import com.capstone.realNest.exception.UserAlreadyExistsException;
import com.capstone.realNest.exception.UserNotFoundException;
import com.capstone.realNest.repository.PasswordResetTokenRepository;
import com.capstone.realNest.repository.UserRepository;
import com.capstone.realNest.service.email.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @Mock
    private UserRegistrationRequest registrationRequest;

    @InjectMocks
    private UserService userService;

    @Test
    void registerCustomer_shouldRegisterCustomerSuccessfully() {

        when(registrationRequest.name()).thenReturn("  Preethi  ");

        when(registrationRequest.email()).thenReturn("  PREETHI@GMAIL.COM  ");

        when(registrationRequest.phone()).thenReturn("  9876543210  ");

        when(registrationRequest.password()).thenReturn("password123");

        when(registrationRequest.confirmPassword()).thenReturn("password123");

        when(userRepository.existsByEmail("preethi@gmail.com")).thenReturn(false);

        when(userRepository.existsByPhone("9876543210")).thenReturn(false);

        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {

            User user = invocation.getArgument(0);
            user.setId(1L);

            return user;
        });

        UserResponse result = userService.registerCustomer(registrationRequest);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Preethi", result.name());
        assertEquals("preethi@gmail.com", result.email());
        assertEquals("9876543210", result.phone());
        assertEquals(Role.CUSTOMER, result.role());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals(Role.CUSTOMER, savedUser.getRole());
    }

    @Test
    void registerCustomer_shouldThrowExceptionWhenPasswordsDoNotMatch() {

        when(registrationRequest.email()).thenReturn("preethi@gmail.com");

        when(registrationRequest.phone()).thenReturn("9876543210");

        when(registrationRequest.password()).thenReturn("password123");

        when(registrationRequest.confirmPassword()).thenReturn("differentPassword");

        assertThrows(IllegalArgumentException.class, () -> userService.registerCustomer(registrationRequest));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerCustomer_shouldThrowExceptionWhenEmailAlreadyExists() {

        when(registrationRequest.email()).thenReturn("preethi@gmail.com");

        when(registrationRequest.phone()).thenReturn("9876543210");

        when(registrationRequest.password()).thenReturn("password123");

        when(registrationRequest.confirmPassword()).thenReturn("password123");

        when(userRepository.existsByEmail("preethi@gmail.com")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> userService.registerCustomer(registrationRequest));

        verify(userRepository, never()).save(any(User.class));

        verify(userRepository, never()).existsByPhone(anyString());
    }

    @Test
    void getUserById_shouldThrowExceptionWhenUserNotFound() {

        Long userId = 99L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));
    }
}