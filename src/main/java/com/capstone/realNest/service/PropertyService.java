package com.capstone.realNest.service;

import com.capstone.realNest.dto.request.PropertyRequest;
import com.capstone.realNest.dto.response.PropertyResponse;

import java.util.List;

public interface PropertyService {

    PropertyResponse createProperty(PropertyRequest request);

    List<PropertyResponse> getAllProperties();

    PropertyResponse getPropertyById(Long id);

    PropertyResponse updateProperty(Long id, PropertyRequest request);

    void deleteProperty(Long id);
}
