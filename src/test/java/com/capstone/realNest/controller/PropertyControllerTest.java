package com.capstone.realNest.controller;

import com.capstone.realNest.controller.rest.PropertyController;
import com.capstone.realNest.dto.response.PropertyResponse;
import com.capstone.realNest.service.PropertyImageService;
import com.capstone.realNest.service.PropertyService;
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
class PropertyControllerTest {

    @Mock
    private PropertyService propertyService;

    @Mock
    private PropertyImageService propertyImageService;

    @InjectMocks
    private PropertyController propertyController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders.standaloneSetup(propertyController).build();
    }

    @Test
    void getPropertyById_shouldReturnProperty() throws Exception {

        Long propertyId = 10L;

        PropertyResponse response = mock(PropertyResponse.class);

        when(response.id()).thenReturn(propertyId);

        when(response.title()).thenReturn("Modern Family Villa");

        when(response.status()).thenReturn("APPROVED");

        when(propertyService.getPropertyById(propertyId)).thenReturn(response);

        mockMvc.perform(get("/api/properties/{propertyId}", propertyId)).andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.id").value(propertyId)).andExpect(jsonPath("$.title").value("Modern Family Villa")).andExpect(jsonPath("$.status").value("APPROVED"));

        verify(propertyService).getPropertyById(propertyId);
    }

    @Test
    void approveProperty_shouldReturnApprovedProperty() throws Exception {

        Long propertyId = 10L;

        PropertyResponse response = mock(PropertyResponse.class);

        when(response.id()).thenReturn(propertyId);

        when(response.status()).thenReturn("APPROVED");

        when(propertyService.approveProperty(propertyId)).thenReturn(response);

        mockMvc.perform(patch("/api/properties/{propertyId}/approve", propertyId)).andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.id").value(propertyId)).andExpect(jsonPath("$.status").value("APPROVED"));

        verify(propertyService).approveProperty(propertyId);
    }

    @Test
    void deleteProperty_shouldReturnNoContent() throws Exception {

        Long propertyId = 10L;

        doNothing().when(propertyService).deleteProperty(propertyId);

        mockMvc.perform(delete("/api/properties/{propertyId}", propertyId)).andExpect(status().isNoContent()).andExpect(content().string(""));

        verify(propertyService).deleteProperty(propertyId);
    }
}