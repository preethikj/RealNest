package com.capstone.realNest.controller;

import com.capstone.realNest.dto.response.PropertyResponse;
import com.capstone.realNest.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
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
    public String propertyList(@RequestParam(required = false) String city,
                                @RequestParam(required = false) String listingType,
                                @RequestParam(required = false) String locality,
                                @RequestParam(required = false) BigDecimal minPrice,
                                @RequestParam(required = false) BigDecimal maxPrice,
                                @RequestParam(required = false) String propertyType,
                                @RequestParam(required = false) Integer bedrooms,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "LATEST") String sort, Model model) {

        int safePage = Math.max(page, 0);
        Sort propertySort = getPropertySort(sort);

        PageRequest pageRequest = PageRequest.of(safePage, 6, propertySort);

        Page<PropertyResponse> propertyPage =
                propertyService.searchApprovedProperties(city, listingType, locality,
                        propertyType, bedrooms, minPrice, maxPrice, pageRequest);

        long resultStart = propertyPage.isEmpty() ? 0 : (long) propertyPage.getNumber()
                            * propertyPage.getSize() + 1;

        long resultEnd = propertyPage.isEmpty() ? 0 : resultStart
                        + propertyPage.getNumberOfElements() - 1;

        model.addAttribute("resultStart", resultStart);
        model.addAttribute("resultEnd", resultEnd);

        model.addAttribute("properties", propertyPage.getContent());

        model.addAttribute("propertyPage", propertyPage);
        model.addAttribute("currentPage", propertyPage.getNumber());
        model.addAttribute("totalPages", propertyPage.getTotalPages());
        model.addAttribute("totalProperties", propertyPage.getTotalElements());

        // Preserve selected search and filter values.
        model.addAttribute("selectedCity", city);
        model.addAttribute("selectedListingType", listingType);
        model.addAttribute("selectedLocality", locality);
        model.addAttribute("selectedMinPrice", minPrice);
        model.addAttribute("selectedMaxPrice", maxPrice);
        model.addAttribute("selectedPropertyType", propertyType);
        model.addAttribute("selectedBedrooms", bedrooms);
        model.addAttribute("selectedSort", normalizeSort(sort));

        model.addAttribute(
                "availableLocalities",
                propertyService.getApprovedLocalitiesByCity(city)
        );

        return "public/property-list";
    }

    /*
     * Converts the URL sort value into a safe Spring Data Sort.
     *
     * Only these three values are supported:
     * LATEST, PRICE_ASC and PRICE_DESC.
     */
    private Sort getPropertySort(String sort) {

        return switch (normalizeSort(sort)) {

            case "PRICE_ASC" ->
                    Sort.by(
                            Sort.Direction.ASC,
                            "price"
                    ).and(
                            Sort.by(
                                    Sort.Direction.DESC,
                                    "id"
                            )
                    );

            case "PRICE_DESC" ->
                    Sort.by(
                            Sort.Direction.DESC,
                            "price"
                    ).and(
                            Sort.by(
                                    Sort.Direction.DESC,
                                    "id"
                            )
                    );

            default ->
                    Sort.by(
                            Sort.Direction.DESC,
                            "createdAt"
                    );
        };
    }


    /*
     * Prevents unsupported sort values from reaching the repository.
     */
    private String normalizeSort(String sort) {

        if (
                "PRICE_ASC".equalsIgnoreCase(sort) ||
                        "PRICE_DESC".equalsIgnoreCase(sort)
        ) {
            return sort.toUpperCase();
        }

        return "LATEST";
    }
}