package com.capstone.realNest.controller.view.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminDashboardViewController {

    @GetMapping("/dashboard")
    public String showAdminDashboard(Model model) {

        model.addAttribute("pageTitle", "Admin Dashboard");
        model.addAttribute(
                "pageSubtitle",
                "Monitor property listings and platform activity."
        );

        model.addAttribute("activeMenu", "dashboard");

        model.addAttribute("totalProperties", 128);
        model.addAttribute("pendingProperties", 6);
        model.addAttribute("approvedProperties", 114);
        model.addAttribute("registeredCustomers", 42);

        model.addAttribute("pendingPropertyCount", 6);

        return "admin/admin-dashboard";
    }
}