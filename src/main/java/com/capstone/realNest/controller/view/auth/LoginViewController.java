package com.capstone.realNest.controller.view.auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class LoginViewController {

    @GetMapping("/login")
    public String showLoginPage(){
        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegisterPage(){
        return "auth/register";
    }
}
