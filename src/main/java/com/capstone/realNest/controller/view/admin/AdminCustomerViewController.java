package com.capstone.realNest.controller.view.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/customers")
public class AdminCustomerViewController {

    @GetMapping
    public String showCustomers(Model model) {

        model.addAttribute("pageTitle", "Customers");

        model.addAttribute(
                "pageSubtitle",
                "View customers registered with RealNest."
        );

        model.addAttribute(
                "activeMenu",
                "customers"
        );

        model.addAttribute(
                "registeredCustomers",
                42
        );

        model.addAttribute(
                "pendingPropertyCount",
                6
        );

        return "admin/customer-list";
    }

    @GetMapping("/{customerId}")
    public String showCustomerDetails(
            @PathVariable Long customerId,
            Model model) {

        model.addAttribute(
                "pageTitle",
                "Customer Details"
        );

        model.addAttribute(
                "activeMenu",
                "customers"
        );

        model.addAttribute(
                "pendingPropertyCount",
                6
        );

        model.addAttribute(
                "customerId",
                customerId
        );

        return "admin/customer-details";
    }
}