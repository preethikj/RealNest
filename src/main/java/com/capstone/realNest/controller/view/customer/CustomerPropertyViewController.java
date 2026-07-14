package com.capstone.realNest.controller.view.customer;

import com.capstone.realNest.dto.request.PropertyRequest;
import com.capstone.realNest.dto.response.PropertyResponse;
import com.capstone.realNest.security.CustomUserPrincipal;
import com.capstone.realNest.service.PropertyImageService;
import com.capstone.realNest.service.PropertyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/customer/property")
public class CustomerPropertyViewController {

    private final PropertyService propertyService;
    private final PropertyImageService propertyImageService;

    @GetMapping("/create")
    public String showPostPropertyPage(Model model) {

        model.addAttribute(
                "propertyRequest",
                new PropertyRequest(
                        null, null, null, null, null,
                        null, null, null, null, null,
                        null, null, null, null, null));

        prepareFormPage(model);
        return "customer/post-property";
    }

    @PostMapping("/save")
    public String saveProperty(@Valid @ModelAttribute("propertyRequest")
                                   PropertyRequest request,
            BindingResult bindingResult, @RequestParam(
                    value = "propertyImages",
                    required = false)
            List<MultipartFile> propertyImages,
            @AuthenticationPrincipal
            CustomUserPrincipal currentUser,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            prepareFormPage(model);
            return "customer/post-property";
        }

        List<MultipartFile> validImages = propertyImages == null
                        ? List.of()
                        : propertyImages.stream()
                                .filter(image -> !image.isEmpty())
                                .toList();

        if (validImages.isEmpty()) {

            model.addAttribute("errorMessage", "Please upload at least one property image");
            prepareFormPage(model);

            return "customer/post-property";
        }

        if (validImages.size() > 8) {

            model.addAttribute("errorMessage", "A maximum of 8 property images is allowed");

            prepareFormPage(model);

            return "customer/post-property";
        }

        PropertyResponse property =
                propertyService.createProperty(request, currentUser.id());
        propertyImageService.uploadImages(property.id(), validImages);
        redirectAttributes.addFlashAttribute("successMessage",
                "Property submitted successfully for admin approval.");

        return "redirect:/customer/dashboard";
    }

    @PostMapping("/{propertyId}/delete")
    public String deleteProperty(@PathVariable Long propertyId,
                                 @AuthenticationPrincipal CustomUserPrincipal currentUser,
                                 RedirectAttributes redirectAttributes) {

        propertyService.deletePropertyByOwner(propertyId, currentUser.id());

        redirectAttributes.addFlashAttribute("successMessage",
                "Property deleted successfully.");
        return "redirect:/customer/dashboard";
    }

    @GetMapping("/{propertyId}")
    public String showCustomerPropertyDetails(@PathVariable Long propertyId,
            @AuthenticationPrincipal
            CustomUserPrincipal currentUser,
            Model model) {

        PropertyResponse property = propertyService.getPropertyByOwner(
                        propertyId,
                        currentUser.id());

        model.addAttribute("property", property);
        model.addAttribute("portalMode", "CUSTOMER");

        return "public/property-details";
    }

    private void prepareFormPage(Model model) {

        model.addAttribute("activeMenu", "post-property");
        model.addAttribute("pageTitle", "List Your Property");
        model.addAttribute("pageSubtitle",
                "Share your property details and submit the listing "
                        + "for admin approval.");
    }
}