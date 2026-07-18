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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private static final String PENDING_STATUS = "PENDING";
    private static final String APPROVED_STATUS = "APPROVED";
    private static final String REJECTED_STATUS = "REJECTED";

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final PropertyMapper propertyMapper;
    private final CloudinaryService cloudinaryService;

    @Transactional
    public PropertyResponse createProperty(PropertyRequest request, Long ownerId) {

        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException(ownerId));

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

    @Transactional
    public PropertyResponse approveProperty(Long propertyId) {

        return updatePropertyStatus(propertyId, APPROVED_STATUS);
    }

    @Transactional
    public PropertyResponse rejectProperty(Long propertyId) {

        return updatePropertyStatus(propertyId, REJECTED_STATUS);
    }

    private PropertyResponse updatePropertyStatus(Long propertyId, String status) {

        Property property = findPropertyById(propertyId);

        property.setStatus(status);

        Property updatedProperty = propertyRepository.save(property);

        return propertyMapper.toResponse(updatedProperty);
    }

    @Transactional(readOnly = true)
    public Page<PropertyResponse> searchApprovedProperties(
            String city,
            String listingType,
            String locality,
            String propertyType,
            Integer bedrooms,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable) {

        return propertyRepository.searchProperties(
                        APPROVED_STATUS,
                        normalizeFilter(listingType),
                        normalizeFilter(propertyType),
                        bedrooms,
                        normalizeFilter(city),
                        normalizeFilter(locality),
                        minPrice,
                        maxPrice,
                        pageable).map(propertyMapper::toResponse);
    }

    private String normalizeFilter(String value) {

        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    @Transactional
    public PropertyResponse updateProperty(Long propertyId, Long ownerId, PropertyRequest request) {

        Property property = propertyRepository.findByIdAndOwnerId(propertyId, ownerId)
                .orElseThrow(() -> new PropertyNotFoundException(propertyId));

        propertyMapper.updateEntity(property, request);

        property.setStatus(PENDING_STATUS);

        Property updatedProperty = propertyRepository.save(property);

        return propertyMapper.toResponse(updatedProperty);
    }

    @Transactional
    public void deleteProperty(Long propertyId) {

        Property property = findPropertyById(propertyId);

        property.getImages().forEach(propertyImage ->
                cloudinaryService.deleteImage(propertyImage.getPublicId()));
        propertyRepository.delete(property);
    }

    @Transactional
    public void deletePropertyByOwner(Long propertyId, Long ownerId) {

        Property property = propertyRepository
                .findByIdAndOwnerId(propertyId, ownerId)
                .orElseThrow(() -> new PropertyNotFoundException(propertyId));

        property.getImages().forEach(propertyImage ->
                cloudinaryService.deleteImage(propertyImage.getPublicId()));
        propertyRepository.delete(property);
    }

    @Transactional(readOnly = true)
    public PropertyResponse getApprovedPropertyById(Long propertyId) {

        Property property = propertyRepository
                .findByIdAndStatus(propertyId, APPROVED_STATUS)
                .orElseThrow(() -> new PropertyNotFoundException(propertyId));

        return propertyMapper.toResponse(property);
    }

    @Transactional(readOnly = true)
    public PropertyResponse getPropertyByOwner(Long propertyId, Long ownerId) {

        Property property = propertyRepository
                .findByIdAndOwnerId(propertyId, ownerId)
                .orElseThrow(() -> new PropertyNotFoundException(propertyId));
        return propertyMapper.toResponse(property);
    }


    /* Dashboard methods */
    @Transactional(readOnly = true)
    public long countAllProperties() {
        return propertyRepository.count();
    }

    @Transactional(readOnly = true)
    public long countPropertiesByStatus(String status) {
        return propertyRepository.countByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<PropertyResponse> getLatestPendingProperties(int limit) {

        return propertyRepository
                .findByStatusOrderByCreatedAtDesc(PENDING_STATUS)
                .stream()
                .limit(limit)
                .map(propertyMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PropertyResponse> getAllProperties() {

        return propertyRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(propertyMapper::toResponse)
                .toList();
    }

    //Customer list
    @Transactional(readOnly = true)
    public long countPropertiesByOwner(Long ownerId) {
        return propertyRepository.countByOwnerId(ownerId);
    }

    //For landing page
    @Transactional(readOnly = true)
    public List<PropertyResponse> getFeaturedProperties(int limit) {

        return propertyRepository
                .findByStatusOrderByCreatedAtDesc(APPROVED_STATUS)
                .stream()
                .limit(limit)
                .map(propertyMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public long countApprovedProperties() {
        return propertyRepository.countByStatus(APPROVED_STATUS);
    }

    @Transactional(readOnly = true)
    public long countApprovedPropertiesByListingType(String listingType) {

        return propertyRepository.countByStatusAndListingType(APPROVED_STATUS, listingType);
    }

    /*
     * Returns cities that currently contain approved properties.
     */
    @Transactional(readOnly = true)
    public List<String> getApprovedCities() {

        return propertyRepository.findDistinctCitiesByStatus(APPROVED_STATUS);
    }


    /*
     * Returns approved-property localities belonging to a selected city.
     */
    @Transactional(readOnly = true)
    public List<String> getApprovedLocalitiesByCity(String city) {

        return propertyRepository
                .findDistinctLocalitiesByStatusAndCity(APPROVED_STATUS, normalizeFilter(city));
    }
}