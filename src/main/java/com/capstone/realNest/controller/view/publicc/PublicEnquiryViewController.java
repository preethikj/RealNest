package com.capstone.realNest.controller.view.publicc;

import com.capstone.realNest.dto.request.EnquiryRequest;
import com.capstone.realNest.dto.response.PropertyResponse;
import com.capstone.realNest.security.CustomUserPrincipal;
import com.capstone.realNest.service.EnquiryService;
import com.capstone.realNest.service.PropertyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/properties/{propertyId}/enquiry")
public class PublicEnquiryViewController {

    private final PropertyService propertyService;
    private final EnquiryService enquiryService;

    @GetMapping
    public String showEnquiryForm(@PathVariable Long propertyId, @AuthenticationPrincipal
            CustomUserPrincipal principal, Model model) {

        PropertyResponse property = propertyService.getApprovedPropertyById(propertyId);

        if (isPropertyOwner(property, principal)) {
            return "redirect:/properties/" + propertyId;
        }

        EnquiryRequest enquiryRequest = createInitialRequest(principal);

        prepareFormModel(model, property, enquiryRequest);
        return "public/enquiry-form";
    }

    @PostMapping
    public String submitEnquiry(
            @PathVariable Long propertyId,
            @Valid
            @ModelAttribute("enquiryRequest")
            EnquiryRequest request,
            BindingResult bindingResult,
            @AuthenticationPrincipal
            CustomUserPrincipal principal,
            Model model
    ) {

        PropertyResponse property =
                propertyService.getApprovedPropertyById(
                        propertyId
                );

        if (isPropertyOwner(property, principal)) {
            return "redirect:/properties/" + propertyId;
        }

        if (bindingResult.hasErrors()) {

            prepareFormModel(
                    model,
                    property,
                    request
            );

            return "public/enquiry-form";
        }

        enquiryService.createEnquiry(
                propertyId,
                request,
                principal != null
                        ? principal.id()
                        : null
        );

        return "redirect:/properties/"
                + propertyId
                + "?enquirySent=true";
    }

    private boolean isPropertyOwner(
            PropertyResponse property,
            CustomUserPrincipal principal
    ) {

        return principal != null
                && property.ownerId().equals(principal.id());
    }

    private EnquiryRequest createInitialRequest(
            CustomUserPrincipal principal
    ) {

        if (principal == null) {
            return new EnquiryRequest(
                    "",
                    "",
                    "",
                    ""
            );
        }

        return new EnquiryRequest(
                principal.name(),
                principal.email(),
                "",
                ""
        );
    }

    private void prepareFormModel(
            Model model,
            PropertyResponse property,
            EnquiryRequest enquiryRequest
    ) {

        model.addAttribute(
                "pageTitle",
                "Send Enquiry"
        );

        model.addAttribute(
                "property",
                property
        );

        model.addAttribute(
                "enquiryRequest",
                enquiryRequest
        );
    }
}