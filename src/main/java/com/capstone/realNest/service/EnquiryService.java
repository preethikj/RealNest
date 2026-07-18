package com.capstone.realNest.service;

import com.capstone.realNest.dto.request.EnquiryRequest;
import com.capstone.realNest.dto.response.EnquiryResponse;
import com.capstone.realNest.entity.Enquiry;
import com.capstone.realNest.entity.Property;
import com.capstone.realNest.exception.EnquiryNotFoundException;
import com.capstone.realNest.exception.PropertyNotFoundException;
import com.capstone.realNest.exception.SelfEnquiryNotAllowedException;
import com.capstone.realNest.exception.UserNotFoundException;
import com.capstone.realNest.repository.EnquiryRepository;
import com.capstone.realNest.repository.PropertyRepository;
import com.capstone.realNest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnquiryService {

    private static final String APPROVED_STATUS = "APPROVED";

    private final EnquiryRepository enquiryRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    @Transactional
    public EnquiryResponse createEnquiry(Long propertyId, EnquiryRequest request, Long requesterId) {

        Property property = propertyRepository
                .findByIdAndStatus(propertyId, APPROVED_STATUS)
                .orElseThrow(() -> new PropertyNotFoundException(propertyId));

        if (requesterId != null && property.getOwner().getId().equals(requesterId)) {
            throw new SelfEnquiryNotAllowedException();
        }

        Enquiry enquiry = toEntity(request, property);
        Enquiry savedEnquiry = enquiryRepository.save(enquiry);

        return toResponse(savedEnquiry);
    }


    @Transactional(readOnly = true)
    public List<EnquiryResponse> getEnquiriesByOwner(Long ownerId) {

        if (!userRepository.existsById(ownerId)) {
            throw new UserNotFoundException(ownerId);
        }

        return enquiryRepository
                .findByPropertyOwnerIdOrderByCreatedAtDesc(ownerId)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    @Transactional(readOnly = true)
    public List<EnquiryResponse> getEnquiriesByProperty(Long propertyId) {

        if (!propertyRepository.existsById(propertyId)) {
            throw new PropertyNotFoundException(propertyId);
        }

        return enquiryRepository
                .findByPropertyIdOrderByCreatedAtDesc(propertyId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private Enquiry toEntity(EnquiryRequest request, Property property) {

        Enquiry enquiry = new Enquiry();

        enquiry.setProperty(property);
        enquiry.setName(request.name().trim());
        enquiry.setEmail(request.email().trim().toLowerCase());
        enquiry.setPhone(request.phone().trim());
        enquiry.setMessage(request.message().trim());

        return enquiry;
    }

    private EnquiryResponse toResponse(Enquiry enquiry) {

        return new EnquiryResponse(
                enquiry.getId(),
                enquiry.getProperty().getId(),
                enquiry.getProperty().getTitle(),
                enquiry.getName(),
                enquiry.getEmail(),
                enquiry.getPhone(),
                enquiry.getMessage(),
                enquiry.getCreatedAt());
    }

    @Transactional(readOnly = true)
    public List<EnquiryResponse> getAllEnquiries() {

        return enquiryRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public EnquiryResponse getEnquiryById(Long enquiryId) {

        Enquiry enquiry = enquiryRepository
                .findById(enquiryId)
                .orElseThrow(() ->
                        new EnquiryNotFoundException(enquiryId)
                );

        return toResponse(enquiry);
    }
}