package com.capstone.realNest.exception;

public class PropertyNotFoundException extends RuntimeException {

    public PropertyNotFoundException(Long propertyId) {
        super("Property not found with id: " + propertyId);
    }
}