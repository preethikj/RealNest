package com.capstone.realNest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LandingViewController {

    @GetMapping("/")
    public String landingPage() {
        return "customer/landing";
    }

    @GetMapping("/properties/{id}")
    public String propertyDetails() {
        return "customer/property-details";
    }

}