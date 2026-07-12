package com.capstone.realNest.controller.rest;

import com.capstone.realNest.dto.request.PropertyRequest;
import com.capstone.realNest.dto.response.PropertyResponse;
import com.capstone.realNest.service.PropertyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/properties")
@Tag(
        name = "Property Management",
        description = "APIs for creating and viewing properties"
)
public class PropertyController {

    private final PropertyService propertyService;

    @PostMapping
    @Operation(summary = "Create a new property")
    public ResponseEntity<PropertyResponse> createProperty(
            @RequestParam Long ownerId,
            @Valid @RequestBody PropertyRequest request) {

        PropertyResponse response =
                propertyService.createProperty(request, ownerId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{propertyId}")
    @Operation(summary = "Get property by ID")
    public ResponseEntity<PropertyResponse> getPropertyById(
            @PathVariable Long propertyId
    ) {

        PropertyResponse response =
                propertyService.getPropertyById(propertyId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/owner/{ownerId}")
    @Operation(summary = "Get properties by owner")
    public ResponseEntity<List<PropertyResponse>>
    getPropertiesByOwner(
            @PathVariable Long ownerId
    ) {

        List<PropertyResponse> responses =
                propertyService.getPropertiesByOwner(ownerId);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get properties by status")
    public ResponseEntity<List<PropertyResponse>>
    getPropertiesByStatus(
            @PathVariable String status
    ) {

        List<PropertyResponse> responses =
                propertyService.getPropertiesByStatus(
                        status.toUpperCase()
                );

        return ResponseEntity.ok(responses);
    }
}