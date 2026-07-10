package com.capstone.realNest.controller.view;

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
}