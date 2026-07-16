package com.capstone.realNest.controller.view.admin;

import com.capstone.realNest.dto.response.EnquiryResponse;
import com.capstone.realNest.service.EnquiryService;
import com.capstone.realNest.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/enquiries")
public class AdminEnquiryViewController {

    private static final String PENDING_STATUS = "PENDING";

    private final EnquiryService enquiryService;
    private final PropertyService propertyService;

    @GetMapping
    public String showEnquiries(Model model) {

        List<EnquiryResponse> enquiries =
                enquiryService.getAllEnquiries();

        model.addAttribute(
                "pageTitle",
                "Enquiries"
        );

        model.addAttribute(
                "pageSubtitle",
                "View enquiries submitted for property listings."
        );

        model.addAttribute(
                "activeMenu",
                "enquiries"
        );

        model.addAttribute(
                "enquiries",
                enquiries
        );

        model.addAttribute(
                "enquiryCount",
                enquiries.size()
        );

        model.addAttribute(
                "pendingPropertyCount",
                propertyService.countPropertiesByStatus(
                        PENDING_STATUS
                )
        );

        return "admin/enquiries";
    }

    @GetMapping("/{enquiryId}")
    public String showEnquiryDetails(
            @PathVariable Long enquiryId,
            Model model) {

        EnquiryResponse enquiry =
                enquiryService.getEnquiryById(enquiryId);

        model.addAttribute(
                "pageTitle",
                "Enquiry Details"
        );

        model.addAttribute(
                "activeMenu",
                "enquiries"
        );

        model.addAttribute(
                "pendingPropertyCount",
                propertyService.countPropertiesByStatus(
                        PENDING_STATUS
                )
        );

        model.addAttribute(
                "enquiry",
                enquiry
        );

        model.addAttribute(
                "property",
                propertyService.getPropertyById(
                        enquiry.propertyId()
                )
        );

        return "admin/enquiry-details";
    }
}