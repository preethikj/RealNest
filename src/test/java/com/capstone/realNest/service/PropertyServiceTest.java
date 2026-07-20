package com.capstone.realNest.service;

import com.capstone.realNest.dto.request.PropertyRequest;
import com.capstone.realNest.dto.response.PropertyResponse;
import com.capstone.realNest.entity.Property;
import com.capstone.realNest.entity.User;
import com.capstone.realNest.exception.PropertyNotFoundException;
import com.capstone.realNest.exception.UserNotFoundException;
import com.capstone.realNest.mapper.PropertyMapper;
import com.capstone.realNest.repository.PropertyRepository;
import com.capstone.realNest.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PropertyServiceTest {

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PropertyMapper propertyMapper;

    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private PropertyService propertyService;

    private PropertyRequest propertyRequest;
    private Property property;
    private PropertyResponse propertyResponse;
    private User owner;

    @BeforeEach
    void setUp() {

        propertyRequest = mock(PropertyRequest.class);
        propertyResponse = mock(PropertyResponse.class);

        owner = new User();
        property = new Property();
    }

    @Test
    void createProperty_shouldCreatePropertySuccessfully() {

        Long ownerId = 1L;

        when(userRepository.findById(ownerId))
                .thenReturn(Optional.of(owner));

        when(propertyMapper.toEntity(propertyRequest, owner))
                .thenReturn(property);

        when(propertyRepository.save(property))
                .thenReturn(property);

        when(propertyMapper.toResponse(property))
                .thenReturn(propertyResponse);

        PropertyResponse result =
                propertyService.createProperty(propertyRequest, ownerId);

        assertNotNull(result);
        assertSame(propertyResponse, result);
        assertEquals("PENDING", property.getStatus());

        verify(userRepository).findById(ownerId);
        verify(propertyRepository).save(property);
        verify(propertyMapper).toResponse(property);
    }

    @Test
    void createProperty_shouldThrowExceptionWhenOwnerNotFound() {

        Long ownerId = 99L;

        when(userRepository.findById(ownerId))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> propertyService.createProperty(
                        propertyRequest,
                        ownerId
                )
        );

        verify(propertyRepository, never())
                .save(any(Property.class));
    }

    @Test
    void getPropertyById_shouldReturnPropertySuccessfully() {

        Long propertyId = 10L;

        when(propertyRepository.findById(propertyId))
                .thenReturn(Optional.of(property));

        when(propertyMapper.toResponse(property))
                .thenReturn(propertyResponse);

        PropertyResponse result =
                propertyService.getPropertyById(propertyId);

        assertSame(propertyResponse, result);

        verify(propertyRepository).findById(propertyId);
        verify(propertyMapper).toResponse(property);
    }

    @Test
    void getPropertyById_shouldThrowExceptionWhenPropertyNotFound() {

        Long propertyId = 99L;

        when(propertyRepository.findById(propertyId))
                .thenReturn(Optional.empty());

        assertThrows(
                PropertyNotFoundException.class,
                () -> propertyService.getPropertyById(propertyId)
        );

        verify(propertyMapper, never())
                .toResponse(any(Property.class));
    }

    @Test
    void approveProperty_shouldChangeStatusToApproved() {

        Long propertyId = 10L;

        when(propertyRepository.findById(propertyId))
                .thenReturn(Optional.of(property));

        when(propertyRepository.save(property))
                .thenReturn(property);

        when(propertyMapper.toResponse(property))
                .thenReturn(propertyResponse);

        PropertyResponse result =
                propertyService.approveProperty(propertyId);

        assertSame(propertyResponse, result);
        assertEquals("APPROVED", property.getStatus());

        verify(propertyRepository).save(property);
        verify(propertyMapper).toResponse(property);
    }

    @Test
    void rejectProperty_shouldChangeStatusToRejected() {

        Long propertyId = 10L;

        when(propertyRepository.findById(propertyId))
                .thenReturn(Optional.of(property));

        when(propertyRepository.save(property))
                .thenReturn(property);

        when(propertyMapper.toResponse(property))
                .thenReturn(propertyResponse);

        PropertyResponse result =
                propertyService.rejectProperty(propertyId);

        assertSame(propertyResponse, result);
        assertEquals("REJECTED", property.getStatus());

        verify(propertyRepository).save(property);
        verify(propertyMapper).toResponse(property);
    }
}