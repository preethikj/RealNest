package com.capstone.realNest.controller;

import com.capstone.realNest.dto.request.PropertyRequest;
import com.capstone.realNest.dto.response.PropertyResponse;
import com.capstone.realNest.service.PropertyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PropertyResponse createProperty(@Valid @RequestBody PropertyRequest request) {
        return propertyService.createProperty(request);
    }

    @GetMapping
    public List<PropertyResponse> getAllProperties() {
        return propertyService.getAllProperties();
    }

    @GetMapping("/{id}")
    public PropertyResponse getPropertyById(@PathVariable Long id) {
        return propertyService.getPropertyById(id);
    }

    @PutMapping("/{id}")
    public PropertyResponse updateProperty(@PathVariable Long id, @Valid @RequestBody PropertyRequest request) {
        return propertyService.updateProperty(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProperty(@PathVariable Long id) {
        propertyService.deleteProperty(id);
    }
}
