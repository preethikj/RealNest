package com.capstone.realNest.controller.view.publicc;

import com.capstone.realNest.dto.response.PropertyResponse;
import com.capstone.realNest.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.capstone.realNest.security.CustomUserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/properties")
public class PublicPropertyViewController {

    private final PropertyService propertyService;

    @GetMapping("/{propertyId}")
    public String showPropertyDetails(
            @PathVariable Long propertyId,
            @RequestParam(defaultValue = "false")
            boolean enquirySent,
            @AuthenticationPrincipal
            CustomUserPrincipal principal,
            Model model) {

        PropertyResponse property = propertyService.getApprovedPropertyById(propertyId);

        boolean isOwnProperty =
                principal != null && property.ownerId().equals(principal.id());

        boolean canSendEnquiry = principal == null ||
                                (principal.role()
                                == com.capstone.realNest.enums.Role.CUSTOMER
                                && !isOwnProperty);

        model.addAttribute("isOwnProperty", isOwnProperty);
        model.addAttribute("canSendEnquiry", canSendEnquiry);
        model.addAttribute("enquirySent", enquirySent);

        model.addAttribute("property", property);
        model.addAttribute("portalMode", "PUBLIC");

        return "public/property-details";
    }
}