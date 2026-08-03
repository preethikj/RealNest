package com.capstone.realNest.controller.rest;

import com.capstone.realNest.dto.request.EnquiryRequest;
import com.capstone.realNest.dto.response.EnquiryResponse;
import com.capstone.realNest.service.EnquiryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.capstone.realNest.security.CustomUserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequiredArgsConstructor

@RequestMapping("/api")
@Tag(name = "Enquiry Management", description = "APIs for sending and viewing property enquiries")
public class EnquiryController {

    private final EnquiryService enquiryService;

    @PostMapping("/properties/{propertyId}/enquiries")
    @Operation(summary = "Send an enquiry for a property")
    public ResponseEntity<EnquiryResponse> createEnquiry(@PathVariable Long propertyId,
            @Valid @RequestBody EnquiryRequest request,
            @AuthenticationPrincipal CustomUserPrincipal principal) {

        Long requesterId = principal != null ? principal.id() : null;

        EnquiryResponse response =
                enquiryService.createEnquiry(propertyId, request, requesterId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @SecurityRequirement(name = "basicAuth")
    @GetMapping("/enquiries/owner/{ownerId}")
    @Operation(summary = "Get enquiries received by an owner")
    public ResponseEntity<List<EnquiryResponse>> getEnquiriesByOwner(@PathVariable Long ownerId) {

        List<EnquiryResponse> responses = enquiryService.getEnquiriesByOwner(ownerId);
        return ResponseEntity.ok(responses);
    }


    @SecurityRequirement(name = "basicAuth")
    @GetMapping("/enquiries/property/{propertyId}")
    @Operation(summary = "Get enquiries for a property")
    public ResponseEntity<List<EnquiryResponse>> getEnquiriesByProperty(@PathVariable Long propertyId) {

        List<EnquiryResponse> responses =
                enquiryService.getEnquiriesByProperty(propertyId);
        return ResponseEntity.ok(responses);
    }
}