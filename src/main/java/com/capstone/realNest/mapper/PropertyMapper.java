package com.capstone.realNest.mapper;

import com.capstone.realNest.dto.request.PropertyRequest;
import com.capstone.realNest.dto.response.PropertyImageResponse;
import com.capstone.realNest.dto.response.PropertyResponse;
import com.capstone.realNest.entity.Property;
import com.capstone.realNest.entity.PropertyImage;
import com.capstone.realNest.entity.User;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class PropertyMapper {


    private void mapRequestToProperty(Property property, PropertyRequest request) {

        property.setTitle(request.title());
        property.setDescription(request.description());
        property.setPrice(request.price());
        property.setPropertyType(request.propertyType());
        property.setListingType(request.listingType());

        property.setBedrooms(request.bedrooms());
        property.setBathrooms(request.bathrooms());
        property.setArea(request.area());

        property.setAddress(request.address());
        property.setLocality(request.locality());
        property.setCity(request.city());
        property.setState(request.state());
        property.setCountry(request.country());
        property.setPincode(request.pincode());

        property.setLatitude(request.latitude());
        property.setLongitude(request.longitude());
    }

    public Property toEntity(PropertyRequest request, User owner) {

        Property property = new Property();

        mapRequestToProperty(property, request);

        property.setOwner(owner);

        return property;
    }

    public void updateEntity(Property property, PropertyRequest request) {

        mapRequestToProperty(property, request);
    }

    public PropertyResponse toResponse(Property property) {

        List<PropertyImageResponse> imageResponses =
                property.getImages()
                        .stream()
                        .sorted(
                                Comparator.comparing(
                                        PropertyImage::getDisplayOrder
                                )
                        )
                        .map(this::toImageResponse)
                        .toList();

        return new PropertyResponse(
                property.getId(),
                property.getTitle(),
                property.getDescription(),
                property.getPrice(),
                property.getPropertyType(),
                property.getListingType(),
                property.getStatus().name(),
                property.getBedrooms(),
                property.getBathrooms(),
                property.getArea(),
                property.getAddress(),
                property.getLocality(),
                property.getCity(),
                property.getState(),
                property.getCountry(),
                property.getPincode(),
                property.getLatitude(),
                property.getLongitude(),
                property.getOwner().getId(),
                property.getOwner().getName(),
                imageResponses,
                property.getCreatedAt(),
                property.getUpdatedAt()
        );
    }

    public PropertyImageResponse toImageResponse(
            PropertyImage propertyImage
    ) {

        return new PropertyImageResponse(
                propertyImage.getId(),
                propertyImage.getProperty().getId(),
                propertyImage.getImageUrl(),
                propertyImage.getPublicId(),
                propertyImage.getDisplayOrder()
        );
    }
}