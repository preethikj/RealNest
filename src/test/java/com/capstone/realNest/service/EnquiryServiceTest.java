package com.capstone.realNest.service;

import com.capstone.realNest.dto.request.EnquiryRequest;
import com.capstone.realNest.dto.response.EnquiryResponse;
import com.capstone.realNest.entity.Enquiry;
import com.capstone.realNest.entity.Property;
import com.capstone.realNest.entity.User;
import com.capstone.realNest.exception.EnquiryNotFoundException;
import com.capstone.realNest.exception.PropertyNotFoundException;
import com.capstone.realNest.exception.SelfEnquiryNotAllowedException;
import com.capstone.realNest.repository.EnquiryRepository;
import com.capstone.realNest.repository.PropertyRepository;
import com.capstone.realNest.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnquiryServiceTest {

    @Mock
    private EnquiryRepository enquiryRepository;

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EnquiryRequest enquiryRequest;

    @InjectMocks
    private EnquiryService enquiryService;

    private Property property;
    private User owner;

    @BeforeEach
    void setUp() {

        owner = new User();
        owner.setId(1L);

        property = new Property();
        property.setId(10L);
        property.setTitle("Modern Family Villa");
        property.setOwner(owner);
    }

    private void stubEnquiryRequest() {

        when(enquiryRequest.name())
                .thenReturn("  Preethi  ");

        when(enquiryRequest.email())
                .thenReturn("  PREETHI@GMAIL.COM  ");

        when(enquiryRequest.phone())
                .thenReturn("  9876543210  ");

        when(enquiryRequest.message())
                .thenReturn(
                        "  I am interested in this property  "
                );
    }

    @Test
    void createEnquiry_shouldCreateEnquirySuccessfully() {

        Long propertyId = 10L;
        Long requesterId = 2L;

        stubEnquiryRequest();

        when(propertyRepository.findByIdAndStatus(
                propertyId,
                "APPROVED"
        )).thenReturn(Optional.of(property));

        when(enquiryRepository.save(any(Enquiry.class)))
                .thenAnswer(invocation -> {

                    Enquiry enquiry = invocation.getArgument(0);
                    enquiry.setId(100L);

                    return enquiry;
                });

        EnquiryResponse result = enquiryService.createEnquiry(
                propertyId,
                enquiryRequest,
                requesterId
        );

        assertNotNull(result);
        assertEquals(100L, result.id());
        assertEquals(propertyId, result.propertyId());
        assertEquals(
                "Modern Family Villa",
                result.propertyTitle()
        );
        assertEquals("Preethi", result.name());
        assertEquals("preethi@gmail.com", result.email());
        assertEquals("9876543210", result.phone());
        assertEquals(
                "I am interested in this property",
                result.message()
        );

        verify(enquiryRepository)
                .save(any(Enquiry.class));
    }

    @Test
    void createEnquiry_shouldSetCorrectProperty() {

        stubEnquiryRequest();

        when(propertyRepository.findByIdAndStatus(
                10L,
                "APPROVED"
        )).thenReturn(Optional.of(property));

        when(enquiryRepository.save(any(Enquiry.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0)
                );

        enquiryService.createEnquiry(
                10L,
                enquiryRequest,
                2L
        );

        ArgumentCaptor<Enquiry> enquiryCaptor =
                ArgumentCaptor.forClass(Enquiry.class);

        verify(enquiryRepository)
                .save(enquiryCaptor.capture());

        Enquiry savedEnquiry =
                enquiryCaptor.getValue();

        assertSame(
                property,
                savedEnquiry.getProperty()
        );
    }

    @Test
    void createEnquiry_shouldThrowExceptionWhenPropertyIsNotApproved() {

        Long propertyId = 10L;

        when(propertyRepository.findByIdAndStatus(
                propertyId,
                "APPROVED"
        )).thenReturn(Optional.empty());

        assertThrows(
                PropertyNotFoundException.class,
                () -> enquiryService.createEnquiry(
                        propertyId,
                        enquiryRequest,
                        2L
                )
        );

        verify(enquiryRepository, never())
                .save(any(Enquiry.class));
    }

    @Test
    void createEnquiry_shouldRejectOwnersOwnEnquiry() {

        Long propertyId = 10L;
        Long requesterId = 1L;

        when(propertyRepository.findByIdAndStatus(
                propertyId,
                "APPROVED"
        )).thenReturn(Optional.of(property));

        assertThrows(
                SelfEnquiryNotAllowedException.class,
                () -> enquiryService.createEnquiry(
                        propertyId,
                        enquiryRequest,
                        requesterId
                )
        );

        verify(enquiryRepository, never())
                .save(any(Enquiry.class));
    }

    @Test
    void getEnquiryById_shouldThrowExceptionWhenNotFound() {

        Long enquiryId = 99L;

        when(enquiryRepository.findById(enquiryId))
                .thenReturn(Optional.empty());

        assertThrows(
                EnquiryNotFoundException.class,
                () -> enquiryService.getEnquiryById(enquiryId)
        );
    }
}