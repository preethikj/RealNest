package com.capstone.realNest.controller;

import com.capstone.realNest.controller.rest.UserController;
import com.capstone.realNest.dto.request.UserRegistrationRequest;
import com.capstone.realNest.dto.response.UserResponse;
import com.capstone.realNest.enums.Role;
import com.capstone.realNest.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();
    }

    @Test
    void registerCustomer_shouldReturnCreated()
            throws Exception {

        UserResponse response =
                mock(UserResponse.class);

        when(response.id())
                .thenReturn(1L);

        when(response.name())
                .thenReturn("Preethi");

        when(response.email())
                .thenReturn("preethi@gmail.com");

        when(response.phone())
                .thenReturn("9876543210");

        when(response.role())
                .thenReturn(Role.CUSTOMER);

        when(userService.registerCustomer(
                any(UserRegistrationRequest.class)
        )).thenReturn(response);

        String requestBody = """
                {
                  "name": "Preethi",
                  "email": "preethi@gmail.com",
                  "phone": "9876543210",
                  "password": "password123",
                  "confirmPassword": "password123"
                }
                """;

        mockMvc.perform(
                        post("/api/users/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isCreated())
                .andExpect(
                        content().contentTypeCompatibleWith(
                                MediaType.APPLICATION_JSON
                        )
                )
                .andExpect(
                        jsonPath("$.id").value(1L)
                )
                .andExpect(
                        jsonPath("$.name").value("Preethi")
                )
                .andExpect(
                        jsonPath("$.email")
                                .value("preethi@gmail.com")
                )
                .andExpect(
                        jsonPath("$.role")
                                .value("CUSTOMER")
                );

        verify(userService).registerCustomer(
                any(UserRegistrationRequest.class)
        );
    }

    @Test
    void getUserById_shouldReturnUser()
            throws Exception {

        Long userId = 1L;

        UserResponse response =
                mock(UserResponse.class);

        when(response.id())
                .thenReturn(userId);

        when(response.name())
                .thenReturn("Preethi");

        when(response.email())
                .thenReturn("preethi@gmail.com");

        when(response.role())
                .thenReturn(Role.CUSTOMER);

        when(userService.getUserById(userId))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/users/{userId}", userId)
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.id").value(userId)
                )
                .andExpect(
                        jsonPath("$.name").value("Preethi")
                )
                .andExpect(
                        jsonPath("$.email")
                                .value("preethi@gmail.com")
                )
                .andExpect(
                        jsonPath("$.role")
                                .value("CUSTOMER")
                );

        verify(userService)
                .getUserById(userId);
    }
}