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


    /*
     * Landing page
     *
     * Displays:
     * - Latest approved properties
     * - Featured hero property
     * - Dynamic property counts
     * - Available city suggestions
     */
    @GetMapping("/")
    public String landingPage(Model model) {

        List<PropertyResponse> featuredProperties =
                propertyService.getFeaturedProperties(3);

        model.addAttribute(
                "featuredProperties",
                featuredProperties
        );

        model.addAttribute(
                "heroProperty",
                featuredProperties.isEmpty()
                        ? null
                        : featuredProperties.get(0)
        );

        model.addAttribute(
                "approvedPropertyCount",
                propertyService.countApprovedProperties()
        );

        model.addAttribute(
                "salePropertyCount",
                propertyService
                        .countApprovedPropertiesByListingType("SALE")
        );

        model.addAttribute(
                "rentPropertyCount",
                propertyService
                        .countApprovedPropertiesByListingType("RENT")
        );

        // Cities used by the landing-page autocomplete.
        model.addAttribute(
                "availableCities",
                propertyService.getApprovedCities()
        );

        return "public/landing";
    }


    /*
     * Public property listing
     *
     * Receives city and listing type from the landing page.
     * Locality and budget filters are applied on this page.
     */
    @GetMapping("/properties")
    public String propertyList(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String listingType,
            @RequestParam(required = false) String locality,
            @RequestParam(required = false) java.math.BigDecimal minPrice,
            @RequestParam(required = false) java.math.BigDecimal maxPrice,
            @RequestParam(required = false) String propertyType,
            @RequestParam(required = false) Integer bedrooms,
            Model model) {

        List<PropertyResponse> properties = propertyService.searchApprovedProperties(city, listingType,
                        locality, propertyType, bedrooms, minPrice, maxPrice);

        model.addAttribute("properties", properties);

        // Preserve selected filters in the page.
        model.addAttribute("selectedCity", city);
        model.addAttribute("selectedListingType", listingType);
        model.addAttribute("selectedLocality", locality);
        model.addAttribute("selectedMinPrice", minPrice);
        model.addAttribute("selectedMaxPrice", maxPrice);
        model.addAttribute("selectedPropertyType", propertyType);
        model.addAttribute("selectedBedrooms", bedrooms);

        // Locality suggestions change according to the selected city.
        model.addAttribute(
                "availableLocalities",
                propertyService.getApprovedLocalitiesByCity(city)
        );

        return "public/property-list";
    }
}