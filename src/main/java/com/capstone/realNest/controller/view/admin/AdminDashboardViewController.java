package com.capstone.realNest.controller.view.admin;

import com.capstone.realNest.service.PropertyService;
import com.capstone.realNest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminDashboardViewController {

    private static final String PENDING_STATUS = "PENDING";
    private static final String APPROVED_STATUS = "APPROVED";

    private final PropertyService propertyService;
    private final UserService userService;

    @GetMapping("/dashboard")
    public String showAdminDashboard(Model model) {

        model.addAttribute("pageTitle", "Admin Dashboard");
        model.addAttribute("pageSubtitle", "Monitor property listings and platform activity.");

        model.addAttribute("activeMenu", "dashboard");

        model.addAttribute("totalProperties", propertyService.countAllProperties());
        model.addAttribute("pendingProperties", propertyService.countPropertiesByStatus(PENDING_STATUS));
        model.addAttribute("approvedProperties", propertyService.countPropertiesByStatus(APPROVED_STATUS));
        model.addAttribute("registeredCustomers", userService.countCustomers());
        model.addAttribute("latestPendingProperties", propertyService.getLatestPendingProperties(3));

        return "admin/admin-dashboard";
    }
}