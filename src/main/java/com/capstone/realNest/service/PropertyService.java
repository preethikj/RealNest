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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private static final String PENDING_STATUS = "PENDING";

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final PropertyMapper propertyMapper;

    @Transactional
    public PropertyResponse createProperty(PropertyRequest request, Long ownerId) {

        User owner = userRepository.findById(ownerId)
                .orElseThrow(() ->
                        new UserNotFoundException(ownerId)
                );

        Property property = propertyMapper.toEntity(request, owner);

        property.setStatus(PENDING_STATUS);

        Property savedProperty = propertyRepository.save(property);

        return propertyMapper.toResponse(savedProperty);
    }

    @Transactional(readOnly = true)
    public PropertyResponse getPropertyById(Long propertyId) {

        Property property = findPropertyById(propertyId);

        return propertyMapper.toResponse(property);
    }

    @Transactional(readOnly = true)
    public List<PropertyResponse> getPropertiesByOwner(Long ownerId) {

        if (!userRepository.existsById(ownerId)) {
            throw new UserNotFoundException(ownerId);
        }

        return propertyRepository
                .findByOwnerIdOrderByCreatedAtDesc(ownerId)
                .stream()
                .map(propertyMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PropertyResponse> getPropertiesByStatus(String status) {

        return propertyRepository
                .findByStatusOrderByCreatedAtDesc(status)
                .stream()
                .map(propertyMapper::toResponse)
                .toList();
    }

    private Property findPropertyById(Long propertyId) {

        return propertyRepository.findById(propertyId)
                .orElseThrow(() ->
                        new PropertyNotFoundException(propertyId)
                );
    }
}