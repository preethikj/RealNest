package com.capstone.realNest.controller.view.customer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customer")
public class CustomerDashboardViewController {

    @GetMapping("/dashboard")
    public String showCustomerDashboard(Model model) {

        model.addAttribute("pageTitle", "My Dashboard");
        model.addAttribute("pageSubtitle", "Manage your properties and track their approval status.");

        //Temporary static values for UI development
        model.addAttribute("customerName", "Preethi");
        model.addAttribute("totalListings", 8);
        model.addAttribute("pendingListings", 3);
        model.addAttribute("approvedListings", 4);
        model.addAttribute("rejectedListings", 1);

        return "customer/customer-dashboard";
    }

    @GetMapping("/create")
    public String showPostPropertyPage(Model model) {

        model.addAttribute("activeMenu", "post-property");
        model.addAttribute("pageTitle", "List Your Property");
        model.addAttribute(
                "pageSubtitle",
                "Share your property details and submit the listing for admin approval."
        );

        return "customer/post-property";
    }

    @GetMapping("/profile")
    public String showProfilePage(Model model) {

        model.addAttribute("activeMenu", "profile");
        model.addAttribute("pageTitle", "Profile Details");
        model.addAttribute(
                "pageSubtitle",
                "Manage your personal and contact information."
        );

        // Temporary static values for UI development
        model.addAttribute("customerName", "Preethi KJ");
        model.addAttribute("customerEmail", "preethi@example.com");
        model.addAttribute("customerPhone", "9876543210");
        model.addAttribute("customerAddress", "Bengaluru, Karnataka");

        return "customer/profile-details";
    }
}