package com.capstone.realNest.controller.view.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/properties")
public class AdminPropertyViewController {

    @GetMapping("/pending")
    public String showPendingProperties(Model model) {

        model.addAttribute("pageTitle", "Pending Approvals");

        model.addAttribute(
                "pageSubtitle",
                "Review properties submitted by customers."
        );

        model.addAttribute(
                "activeMenu",
                "pending-properties"
        );

        // Temporary UI value
        model.addAttribute(
                "pendingPropertyCount",
                6
        );

        return "admin/pending-properties";
    }

    @GetMapping
    public String showAllProperties(Model model) {

        model.addAttribute(
                "pageTitle",
                "All Properties"
        );

        model.addAttribute(
                "pageSubtitle",
                "View every property submitted to RealNest."
        );

        model.addAttribute(
                "activeMenu",
                "properties"
        );

        model.addAttribute(
                "pendingPropertyCount",
                6
        );

        return "admin/all-properties";
    }

    @GetMapping("/{propertyId}/review")
    public String showPropertyReview(
            @PathVariable Long propertyId,
            Model model) {

        model.addAttribute(
                "pageTitle",
                "Review Property"
        );

        model.addAttribute(
                "activeMenu",
                "pending-properties"
        );

        model.addAttribute(
                "pendingPropertyCount",
                6
        );

        model.addAttribute(
                "propertyId",
                propertyId
        );

        return "admin/property-review";
    }
}