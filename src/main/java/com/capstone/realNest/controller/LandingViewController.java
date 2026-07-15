package com.capstone.realNest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LandingViewController {

    @GetMapping("/")
    public String landingPage() {
        return "public/landing";
    }

//    @GetMapping("/properties/{id}")
//    public String propertyDetails() {
//        return "public/property-details";
//    }

    @GetMapping("/properties")
    public String propertyList() {
        return "public/property-list";
    }



}