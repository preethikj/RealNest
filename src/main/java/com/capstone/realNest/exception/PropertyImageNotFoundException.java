package com.capstone.realNest.exception;

public class PropertyImageNotFoundException extends RuntimeException {

    public PropertyImageNotFoundException(Long imageId, Long propertyId) {

        super("Image with ID " + imageId +
                " was not found for property " + propertyId);
    }
}