package com.capstone.realNest.controller.view.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/enquiries")
public class AdminEnquiryViewController {

    @GetMapping
    public String showEnquiries(Model model) {

        model.addAttribute("pageTitle", "Enquiries");
        model.addAttribute("pageSubtitle", "View enquiries submitted for property listings.");

        model.addAttribute("activeMenu", "enquiries");
        model.addAttribute("pendingPropertyCount", 6);

        return "admin/enquiries";
    }

    @GetMapping("/{enquiryId}")
    public String showEnquiryDetails(
            @PathVariable Long enquiryId,
            Model model) {

        model.addAttribute("pageTitle", "Enquiry Details");

        model.addAttribute("activeMenu", "enquiries");

        model.addAttribute("pendingPropertyCount", 6);

        model.addAttribute("enquiryId", enquiryId);

        return "admin/enquiry-details";
    }
}