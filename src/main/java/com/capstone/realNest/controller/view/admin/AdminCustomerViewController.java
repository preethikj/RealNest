package com.capstone.realNest.controller.view.admin;

import com.capstone.realNest.dto.response.PropertyResponse;
import com.capstone.realNest.dto.response.UserResponse;
import com.capstone.realNest.service.PropertyService;
import com.capstone.realNest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/customers")
public class AdminCustomerViewController {

    private static final String PENDING_STATUS = "PENDING";

    private final UserService userService;
    private final PropertyService propertyService;

    @GetMapping
    public String showCustomers(Model model) {

        List<UserResponse> customers = userService.getAllCustomers();

        Map<Long, Long> propertyCounts = new LinkedHashMap<>();

        for (UserResponse customer : customers) {

            propertyCounts.put(customer.id(),
                    propertyService.countPropertiesByOwner(customer.id()));
        }

        model.addAttribute("pageTitle", "Customers");
        model.addAttribute("pageSubtitle", "View customers registered with RealNest.");

        model.addAttribute("activeMenu", "customers");

        model.addAttribute("customers", customers);

        model.addAttribute("propertyCounts", propertyCounts);

        model.addAttribute("registeredCustomers", customers.size());

        model.addAttribute("pendingPropertyCount",
                propertyService.countPropertiesByStatus(PENDING_STATUS));

        return "admin/customer-list";
    }

    /*@GetMapping("/{customerId}")
    public String showCustomerDetails(
            @PathVariable Long customerId,
            Model model) {

        model.addAttribute("pageTitle", "Customer Details");

        model.addAttribute("activeMenu", "customers");

        model.addAttribute("pendingPropertyCount",
                propertyService.countPropertiesByStatus(PENDING_STATUS));

        model.addAttribute("customer", userService.getUserById(customerId));

        model.addAttribute("properties",
                propertyService.getPropertiesByOwner(customerId));

        return "admin/customer-details";
    }*/

    @GetMapping("/{customerId}")
    public String showCustomerDetails(@PathVariable Long customerId, Model model) {

        UserResponse customer = userService.getUserById(customerId);

        List<PropertyResponse> properties =
                propertyService.getPropertiesByOwner(customerId);

        long pendingCount = properties.stream()
                .filter(property ->
                        "PENDING".equals(property.status())).count();

        long approvedCount = properties.stream()
                .filter(property ->
                        "APPROVED".equals(property.status())).count();

        long rejectedCount = properties.stream()
                .filter(property ->
                        "REJECTED".equals(property.status())).count();

        model.addAttribute("pageTitle", "Customer Details");

        model.addAttribute("activeMenu", "customers");

        model.addAttribute("pendingPropertyCount",
                propertyService.countPropertiesByStatus(PENDING_STATUS));

        model.addAttribute("customer", customer);

        model.addAttribute("properties", properties);

        model.addAttribute("totalListings", properties.size());

        model.addAttribute("pendingListings", pendingCount);

        model.addAttribute("approvedListings", approvedCount);

        model.addAttribute("rejectedListings", rejectedCount);

        return "admin/customer-details";
    }
}