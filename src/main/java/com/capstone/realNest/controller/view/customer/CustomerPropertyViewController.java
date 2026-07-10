package com.capstone.realNest.controller.view.customer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customer/property")
public class CustomerPropertyViewController {

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
}