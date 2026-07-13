package com.capstone.realNest.controller.rest;

import com.capstone.realNest.dto.request.PropertyRequest;
import com.capstone.realNest.dto.response.PropertyImageResponse;
import com.capstone.realNest.dto.response.PropertyResponse;
import com.capstone.realNest.service.PropertyImageService;
import com.capstone.realNest.service.PropertyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor


@RequestMapping("/api/properties")
@Tag(name = "Property Management", description = "APIs for creating and viewing properties")
public class PropertyController {

    private final PropertyService propertyService;
    private final PropertyImageService propertyImageService;


    @PostMapping
    @Operation(summary = "Create a new property")
    public ResponseEntity<PropertyResponse> createProperty(@RequestParam Long ownerId,
                                                           @Valid @RequestBody PropertyRequest request) {

        PropertyResponse response = propertyService.createProperty(request, ownerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(value = "/{propertyId}/images",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload images for a property")
    public ResponseEntity<List<PropertyImageResponse>> uploadImages(@PathVariable Long propertyId,
            @RequestPart("images") List<MultipartFile> images) {

        List<PropertyImageResponse> responses = propertyImageService.uploadImages(
                        propertyId,
                        images);

        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    @GetMapping("/{propertyId}")
    @Operation(summary = "Get property by ID")
    public ResponseEntity<PropertyResponse> getPropertyById(@PathVariable Long propertyId) {

        PropertyResponse response = propertyService.getPropertyById(propertyId);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/owner/{ownerId}")
    @Operation(summary = "Get properties by owner")
    public ResponseEntity<List<PropertyResponse>> getPropertiesByOwner(@PathVariable Long ownerId) {

        List<PropertyResponse> responses = propertyService.getPropertiesByOwner(ownerId);
        return ResponseEntity.ok(responses);
    }


    @GetMapping("/status/{status}")
    @Operation(summary = "Get properties by status")
    public ResponseEntity<List<PropertyResponse>> getPropertiesByStatus(@PathVariable String status) {

        List<PropertyResponse> responses = propertyService.getPropertiesByStatus(status.toUpperCase());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{propertyId}/images")
    @Operation(summary = "Get images for a property")
    public ResponseEntity<List<PropertyImageResponse>> getPropertyImages(@PathVariable Long propertyId) {

        List<PropertyImageResponse> responses = propertyImageService.getImagesByPropertyId(propertyId);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{propertyId}/images/{imageId}")
    @Operation(summary = "Delete a property image")
    public ResponseEntity<Void> deletePropertyImage(@PathVariable Long propertyId, @PathVariable Long imageId) {

        propertyImageService.deleteImage(propertyId, imageId);
        return ResponseEntity.noContent().build();
    }
}