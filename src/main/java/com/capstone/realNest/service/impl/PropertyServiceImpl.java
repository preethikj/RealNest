package com.capstone.realNest.service.impl;

import com.capstone.realNest.dto.request.PropertyRequest;
import com.capstone.realNest.dto.response.PropertyResponse;
import com.capstone.realNest.entity.Property;
import com.capstone.realNest.exception.ResourceNotFoundException;
import com.capstone.realNest.repository.PropertyRepository;
import com.capstone.realNest.service.PropertyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;

    public PropertyServiceImpl(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @Override
    public PropertyResponse createProperty(PropertyRequest request) {
        Property property = new Property();
        updateEntity(property, request);
        return toResponse(propertyRepository.save(property));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PropertyResponse> getAllProperties() {
        return propertyRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PropertyResponse getPropertyById(Long id) {
        return toResponse(findProperty(id));
    }

    @Override
    public PropertyResponse updateProperty(Long id, PropertyRequest request) {
        Property property = findProperty(id);
        updateEntity(property, request);
        return toResponse(propertyRepository.save(property));
    }

    @Override
    public void deleteProperty(Long id) {
        Property property = findProperty(id);
        propertyRepository.delete(property);
    }

    private Property findProperty(Long id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));
    }

    private void updateEntity(Property property, PropertyRequest request) {
        property.setTitle(request.title());
        property.setDescription(request.description());
        property.setPrice(request.price());
        property.setPropertyType(request.propertyType());
        property.setListingType(request.listingType());
        property.setStatus(request.status());
        property.setBedrooms(request.bedrooms());
        property.setBathrooms(request.bathrooms());
        property.setArea(request.area());
        property.setAddress(request.address());
        property.setCity(request.city());
        property.setState(request.state());
        property.setCountry(request.country());
        property.setPincode(request.pincode());
        property.setLatitude(request.latitude());
        property.setLongitude(request.longitude());
        property.setOwnerId(request.ownerId());
    }

    private PropertyResponse toResponse(Property property) {
        return new PropertyResponse(
                property.getId(),
                property.getTitle(),
                property.getDescription(),
                property.getPrice(),
                property.getPropertyType(),
                property.getListingType(),
                property.getStatus(),
                property.getBedrooms(),
                property.getBathrooms(),
                property.getArea(),
                property.getAddress(),
                property.getCity(),
                property.getState(),
                property.getCountry(),
                property.getPincode(),
                property.getLatitude(),
                property.getLongitude(),
                property.getOwnerId(),
                property.getCreatedAt(),
                property.getUpdatedAt()
        );
    }
}
