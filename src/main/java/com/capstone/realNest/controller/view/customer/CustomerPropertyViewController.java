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

    //Show post property page
    @GetMapping("/create")
    public String showPostPropertyPage(Model model) {

        model.addAttribute(
                "propertyRequest",
                PropertyRequest.builder().build()
        );

        model.addAttribute("formAction", "/customer/property/save");
        model.addAttribute("editMode", false);
        model.addAttribute("existingImageCount", 0);

        prepareFormPage(model);

        return "customer/post-property";
    }

    //Save action
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
        propertyImageService.uploadImages(property.id(), currentUser.id(), validImages);
        redirectAttributes.addFlashAttribute("successMessage",
                "Property submitted successfully for admin approval.");

        return "redirect:/customer/dashboard";
    }

    //Delete action
    @PostMapping("/{propertyId}/delete")
    public String deleteProperty(@PathVariable Long propertyId,
                                 @AuthenticationPrincipal CustomUserPrincipal currentUser,
                                 RedirectAttributes redirectAttributes) {

        propertyService.deletePropertyByOwner(propertyId, currentUser.id());

        redirectAttributes.addFlashAttribute("successMessage",
                "Property deleted successfully.");
        return "redirect:/customer/dashboard";
    }

    //Show property details page
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

    //Show property page for edit
    @GetMapping("/{propertyId}/edit")
    public String showEditPropertyPage(@PathVariable Long propertyId, @AuthenticationPrincipal
            CustomUserPrincipal currentUser, Model model) {

        PropertyResponse property = propertyService.getPropertyByOwner(propertyId, currentUser.id());

        PropertyRequest propertyRequest =
                PropertyRequest.builder()
                        .title(property.title())
                        .description(property.description())
                        .price(property.price())
                        .propertyType(property.propertyType())
                        .listingType(property.listingType())
                        .bedrooms(property.bedrooms())
                        .bathrooms(property.bathrooms())
                        .area(property.area())
                        .address(property.address())
                        .city(property.city())
                        .state(property.state())
                        .country(property.country())
                        .pincode(property.pincode())
                        .latitude(property.latitude())
                        .longitude(property.longitude())
                        .build();

        model.addAttribute("propertyRequest", propertyRequest);

        model.addAttribute("formAction", "/customer/property/" + propertyId + "/update");
        model.addAttribute("editMode", true);

        model.addAttribute("propertyId", propertyId);
        model.addAttribute("existingImageCount", property.images().size());
        model.addAttribute("existingImages", property.images());

        model.addAttribute("activeMenu", "post-property");
        model.addAttribute("pageTitle", "Edit Property");
        model.addAttribute("pageSubtitle",
                "Update your property details and submit it " + "again for admin approval.");

        return "customer/post-property";
    }

    @PostMapping("/{propertyId}/images/{imageId}/delete")
    public String deletePropertyImage(@PathVariable Long propertyId, @PathVariable Long imageId,
            @AuthenticationPrincipal CustomUserPrincipal currentUser,
            RedirectAttributes redirectAttributes) {

        try {
            propertyImageService.deleteImage(propertyId, imageId, currentUser.id());
            redirectAttributes.addFlashAttribute("successMessage",
                    "Property image removed successfully.");

        } catch (IllegalStateException ex) {

            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/customer/property/" + propertyId + "/edit";
    }


    @PostMapping("/{propertyId}/update")
    public String updateProperty(@PathVariable Long propertyId,
                                 @Valid @ModelAttribute("propertyRequest") PropertyRequest request,
                                 BindingResult bindingResult,
                                 @RequestParam(value = "propertyImages", required = false)
                                     List<MultipartFile> propertyImages,
                                 @AuthenticationPrincipal CustomUserPrincipal currentUser,
                                 Model model, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {

            PropertyResponse property = propertyService.getPropertyByOwner(propertyId, currentUser.id());

            model.addAttribute("formAction", "/customer/property/"
                    + propertyId + "/update");

            model.addAttribute("editMode", true);
            model.addAttribute("propertyId", propertyId);
            model.addAttribute("existingImages", property.images());
            model.addAttribute("existingImageCount", property.images().size());

            model.addAttribute("activeMenu", "post-property");
            model.addAttribute("pageTitle", "Edit Property");
            model.addAttribute(
                    "pageSubtitle",
                    "Update your property details and submit it "
                            + "again for admin approval."
            );

            return "customer/post-property";
        }

        propertyService.updateProperty(
                propertyId,
                currentUser.id(),
                request);

        List<MultipartFile> newImages =
                propertyImages == null
                        ? List.of()
                        : propertyImages.stream()
                        .filter(image -> !image.isEmpty())
                        .toList();

        if (!newImages.isEmpty()) {

            propertyImageService.uploadImages(
                    propertyId,
                    currentUser.id(),
                    newImages
            );
        }

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Property updated and submitted for admin approval."
        );

        return "redirect:/customer/dashboard";
    }
    private void prepareFormPage(Model model) {

        model.addAttribute("activeMenu", "post-property");
        model.addAttribute("pageTitle", "List Your Property");
        model.addAttribute("pageSubtitle",
                "Share your property details and submit the listing "
                        + "for admin approval.");
    }
}