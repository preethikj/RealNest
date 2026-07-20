package com.capstone.realNest.controller;

import com.capstone.realNest.controller.rest.EnquiryController;
import com.capstone.realNest.dto.request.EnquiryRequest;
import com.capstone.realNest.dto.response.EnquiryResponse;
import com.capstone.realNest.service.EnquiryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EnquiryControllerTest {

    @Mock
    private EnquiryService enquiryService;

    @InjectMocks
    private EnquiryController enquiryController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(enquiryController)
                .setCustomArgumentResolvers(
                        new AuthenticationPrincipalArgumentResolver()
                )
                .build();
    }

    @Test
    void createEnquiry_shouldReturnCreated() throws Exception {

        Long propertyId = 10L;

        EnquiryResponse response =
                mock(EnquiryResponse.class);

        when(response.id())
                .thenReturn(100L);

        when(response.propertyId())
                .thenReturn(propertyId);

        when(response.name())
                .thenReturn("Preethi");

        when(response.message())
                .thenReturn(
                        "I am interested in this property"
                );

        when(enquiryService.createEnquiry(
                eq(propertyId),
                any(EnquiryRequest.class),
                isNull()
        )).thenReturn(response);

        String requestBody = """
                {
                  "name": "Preethi",
                  "email": "preethi@gmail.com",
                  "phone": "9876543210",
                  "message": "I am interested in this property"
                }
                """;

        mockMvc.perform(
                        post(
                                "/api/properties/{propertyId}/enquiries",
                                propertyId
                        )
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
                        jsonPath("$.id").value(100L)
                )
                .andExpect(
                        jsonPath("$.propertyId")
                                .value(propertyId)
                )
                .andExpect(
                        jsonPath("$.name")
                                .value("Preethi")
                );

        verify(enquiryService).createEnquiry(
                eq(propertyId),
                any(EnquiryRequest.class),
                isNull()
        );
    }

    @Test
    void getEnquiriesByProperty_shouldReturnEnquiries()
            throws Exception {

        Long propertyId = 10L;

        EnquiryResponse response =
                mock(EnquiryResponse.class);

        when(response.id())
                .thenReturn(100L);

        when(response.propertyId())
                .thenReturn(propertyId);

        when(response.name())
                .thenReturn("Preethi");

        when(enquiryService.getEnquiriesByProperty(propertyId))
                .thenReturn(List.of(response));

        mockMvc.perform(
                        get(
                                "/api/enquiries/property/{propertyId}",
                                propertyId
                        )
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$[0].id").value(100L)
                )
                .andExpect(
                        jsonPath("$[0].propertyId")
                                .value(propertyId)
                )
                .andExpect(
                        jsonPath("$[0].name")
                                .value("Preethi")
                );

        verify(enquiryService)
                .getEnquiriesByProperty(propertyId);
    }
}