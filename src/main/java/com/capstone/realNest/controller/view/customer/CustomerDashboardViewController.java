package com.capstone.realNest.controller.view.customer;

import com.capstone.realNest.dto.request.UserProfileUpdateRequest;
import com.capstone.realNest.dto.response.PropertyResponse;
import com.capstone.realNest.dto.response.UserResponse;
import com.capstone.realNest.exception.UserAlreadyExistsException;
import com.capstone.realNest.security.CustomUserPrincipal;
import com.capstone.realNest.service.PropertyService;
import com.capstone.realNest.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerDashboardViewController {

    private final UserService userService;
    private final PropertyService propertyService;

    @GetMapping("/dashboard")
    public String showCustomerDashboard(@AuthenticationPrincipal CustomUserPrincipal currentUser,
            Model model) {

        UserResponse customer = userService.getUserById(currentUser.id());

        List<PropertyResponse> properties = propertyService.getPropertiesByOwner(currentUser.id());

        long pendingListings = countByStatus(properties, "PENDING");

        long approvedListings = countByStatus(properties, "APPROVED");

        long rejectedListings = countByStatus(properties, "REJECTED");

        model.addAttribute("activeMenu", "dashboard");
        model.addAttribute("pageTitle", "My Dashboard");
        model.addAttribute(
                "pageSubtitle",
                "Manage your properties and track their approval status.");

        model.addAttribute("customerName", customer.name());
        model.addAttribute("totalListings", properties.size());
        model.addAttribute("pendingListings", pendingListings);
        model.addAttribute("approvedListings", approvedListings);
        model.addAttribute("rejectedListings", rejectedListings);
        model.addAttribute("properties", properties);

        return "customer/customer-dashboard";
    }

    @GetMapping("/profile")
    public String showProfilePage(@AuthenticationPrincipal CustomUserPrincipal currentUser, Model model) {

        UserResponse customer = userService.getUserById(currentUser.id());

        UserProfileUpdateRequest profileRequest =
                UserProfileUpdateRequest.builder()
                        .name(customer.name())
                        .email(customer.email())
                        .phone(customer.phone())
                        .build();

        model.addAttribute("profileRequest", profileRequest);
        prepareProfilePage(model, customer.name());

        return "customer/profile-details";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@Valid @ModelAttribute("profileRequest") UserProfileUpdateRequest request,
            BindingResult bindingResult, @AuthenticationPrincipal CustomUserPrincipal currentUser,
            Model model, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {

            prepareProfilePage(model, request.name());
            return "customer/profile-details";
        }

        try {
            userService.updateProfile(currentUser.id(), request);
        } catch (UserAlreadyExistsException ex) {

            model.addAttribute("errorMessage", ex.getMessage());
            prepareProfilePage(model, request.name());
            return "customer/profile-details";
        }

        redirectAttributes.addFlashAttribute("successMessage",
                "Profile updated successfully.");
        return "redirect:/customer/profile";
    }

    private void prepareProfilePage(Model model, String customerName) {

        model.addAttribute("activeMenu", "profile");

        model.addAttribute("pageTitle", "Profile Details");
        model.addAttribute("pageSubtitle",
                "Manage your personal and contact information.");

        model.addAttribute("customerName", customerName);
    }

    private long countByStatus(List<PropertyResponse> properties, String status) {

        return properties.stream()
                .filter(property ->
                        status.equalsIgnoreCase(property.status())).count();
    }
}