package com.capstone.realNest.controller;

import com.capstone.realNest.dto.response.PropertyResponse;
import com.capstone.realNest.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class LandingViewController {

    private final PropertyService propertyService;

    @GetMapping("/")
    public String landingPage(Model model) {

        List<PropertyResponse> featuredProperties = propertyService.getFeaturedProperties(3);
        model.addAttribute("featuredProperties", featuredProperties);

        model.addAttribute("heroProperty",
                featuredProperties.isEmpty()
                        ? null
                        : featuredProperties.get(0));

        model.addAttribute("approvedPropertyCount", propertyService.countApprovedProperties());

        model.addAttribute("salePropertyCount",
                propertyService.countApprovedPropertiesByListingType("SALE"));

        model.addAttribute("rentPropertyCount",
                propertyService.countApprovedPropertiesByListingType("RENT"));

        return "public/landing";
    }

    @GetMapping("/properties")
    public String propertyList(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String listingType, Model model) {

        List<PropertyResponse> properties = propertyService.searchApprovedPropertiesByCity(
                                                            city, listingType);

        model.addAttribute("properties", properties);
        model.addAttribute("selectedCity", city);
        model.addAttribute("selectedListingType", listingType);

        return "public/property-list";
    }
}