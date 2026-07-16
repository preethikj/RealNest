package com.capstone.realNest.controller.view.admin;

import com.capstone.realNest.dto.response.PropertyResponse;
import com.capstone.realNest.service.PropertyService;
import com.capstone.realNest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/properties")
public class AdminPropertyViewController {

    private static final String PENDING_STATUS = "PENDING";

    private final PropertyService propertyService;
    private final UserService userService;

    @GetMapping("/pending")
    public String showPendingProperties(Model model) {

        List<PropertyResponse> pendingProperties = propertyService.getPropertiesByStatus(PENDING_STATUS);

        model.addAttribute("pageTitle", "Pending Approvals");
        model.addAttribute("pageSubtitle", "Review properties submitted by customers.");

        model.addAttribute("activeMenu", "pending-properties");

        model.addAttribute("pendingProperties", pendingProperties);
        model.addAttribute("pendingPropertyCount", pendingProperties.size());

        return "admin/pending-properties";
    }

    @GetMapping
    public String showAllProperties(Model model) {

        List<PropertyResponse> properties =
                propertyService.getAllProperties();

        model.addAttribute("pageTitle", "All Properties");
        model.addAttribute("pageSubtitle", "View every property submitted to RealNest.");

        model.addAttribute("activeMenu", "properties");

        model.addAttribute("properties", properties);

        model.addAttribute("totalPropertyCount", properties.size());

        model.addAttribute("pendingPropertyCount",
                propertyService.countPropertiesByStatus(PENDING_STATUS));

        return "admin/all-properties";
    }

    //Show property review page
    @GetMapping("/{propertyId}/review")
    public String showPropertyReview(@PathVariable Long propertyId, Model model) {

        PropertyResponse property = propertyService.getPropertyById(propertyId);

        model.addAttribute("pageTitle", "Review Property");

        model.addAttribute("activeMenu", "pending-properties");

        model.addAttribute("pendingPropertyCount", propertyService.countPropertiesByStatus(PENDING_STATUS));
        model.addAttribute("property", property);

        model.addAttribute("reviewMode", true);
        model.addAttribute("owner", userService.getUserById(property.ownerId()));

        return "admin/property-review";
    }

    //Approve action
    @PostMapping("/{propertyId}/approve")
    public String approveProperty(
            @PathVariable Long propertyId) {

        propertyService.approveProperty(propertyId);

        return "redirect:/admin/properties/pending";
    }

    //Reject action
    @PostMapping("/{propertyId}/reject")
    public String rejectProperty(
            @PathVariable Long propertyId) {

        propertyService.rejectProperty(propertyId);

        return "redirect:/admin/properties/pending";
    }

    @GetMapping("/{propertyId}")
    public String showPropertyDetails(@PathVariable Long propertyId, Model model) {

        PropertyResponse property = propertyService.getPropertyById(propertyId);

        model.addAttribute("pageTitle", "Property Details");

        model.addAttribute("activeMenu", "properties");

        model.addAttribute("pendingPropertyCount", propertyService.countPropertiesByStatus(PENDING_STATUS));

        model.addAttribute("property", property);

        model.addAttribute("owner", userService.getUserById(property.ownerId()));

        model.addAttribute("reviewMode", false);

        return "admin/property-review";
    }
}