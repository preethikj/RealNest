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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class LoginViewController {

    private final UserService userService;

    @GetMapping("/login")
    public String showLoginPage() {

        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegisterPage() {

        return "auth/register";
    }

    @PostMapping("/register")
    public String registerCustomer(@Valid @ModelAttribute UserRegistrationRequest request, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {

            String errorMessage = bindingResult.getAllErrors().getFirst().getDefaultMessage();

            model.addAttribute("errorMessage", errorMessage);

            return "auth/register";
        }

        try {
            userService.registerCustomer(request);

        } catch (UserAlreadyExistsException exception) {

            model.addAttribute("errorMessage", exception.getMessage());

            return "auth/register";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Registration successful. Please log in.");

        return "redirect:/auth/login";
    }

    @PostMapping("/forgot-password")
    public String sendPasswordResetLink(@RequestParam String email, RedirectAttributes redirectAttributes) {

        try {
            userService.sendPasswordResetLink(email);

            redirectAttributes.addFlashAttribute("successMessage", "A password reset link has been sent to your email.");

        } catch (IllegalArgumentException | IllegalStateException exception) {

            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }

        return "redirect:/auth/login";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordPage(
            @RequestParam(required = false) String token,
            Model model
    ) {

        model.addAttribute("token", token);

        try {
            userService.validatePasswordResetToken(token);
            model.addAttribute("tokenValid", true);

        } catch (IllegalArgumentException exception) {
            model.addAttribute("tokenValid", false);
            model.addAttribute(
                    "errorMessage",
                    exception.getMessage()
            );
        }

        return "auth/reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            Model model,
            RedirectAttributes redirectAttributes
    ) {

        model.addAttribute("token", token);

        try {
            userService.validatePasswordResetToken(token);

        } catch (IllegalArgumentException exception) {
            model.addAttribute("tokenValid", false);
            model.addAttribute(
                    "errorMessage",
                    exception.getMessage()
            );

            return "auth/reset-password";
        }

        model.addAttribute("tokenValid", true);

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute(
                    "errorMessage",
                    "Password and confirm password do not match."
            );

            return "auth/reset-password";
        }

        try {
            userService.resetPassword(token, newPassword);

        } catch (IllegalArgumentException exception) {
            model.addAttribute(
                    "errorMessage",
                    exception.getMessage()
            );

            return "auth/reset-password";
        }

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Password reset successful. Please log in."
        );

        return "redirect:/auth/login";
    }

}