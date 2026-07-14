package com.capstone.realNest.controller.view.auth;

import com.capstone.realNest.dto.request.UserRegistrationRequest;
import com.capstone.realNest.exception.UserAlreadyExistsException;
import com.capstone.realNest.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor

@RequestMapping("/auth")
public class LoginViewController {

    private final UserService userService;

    @GetMapping("/login")
    public String showLoginPage(){
        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegisterPage(){
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerCustomer(@Valid @ModelAttribute UserRegistrationRequest request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {

            String errorMessage = bindingResult.getAllErrors().getFirst().getDefaultMessage();

            model.addAttribute("errorMessage", errorMessage);

            return "auth/register";
        }

        try {
            userService.registerCustomer(request);

        } catch (UserAlreadyExistsException ex) {

            model.addAttribute("errorMessage", ex.getMessage());

            return "auth/register";
        }

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Registration successful. Please log in."
        );

        return "redirect:/auth/login";
    }
}
