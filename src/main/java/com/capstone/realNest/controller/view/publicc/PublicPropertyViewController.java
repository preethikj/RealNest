package com.capstone.realNest.controller.view.publicc;

import com.capstone.realNest.dto.response.PropertyResponse;
import com.capstone.realNest.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/properties")
public class PublicPropertyViewController {

    private final PropertyService propertyService;

    @GetMapping("/{propertyId}")
    public String showPropertyDetails(
            @PathVariable Long propertyId,
            Model model
    ) {

        PropertyResponse property =
                propertyService.getApprovedPropertyById(
                        propertyId
                );

        model.addAttribute("property", property);
        model.addAttribute("portalMode", "PUBLIC");

        return "public/property-details";
    }
}