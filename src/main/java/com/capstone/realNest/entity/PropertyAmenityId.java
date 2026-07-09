package com.capstone.realNest.entity;

import java.io.Serializable;
import java.util.Objects;

public class PropertyAmenityId implements Serializable {

    private Long propertyId;

    private Long amenityId;

    public PropertyAmenityId() {
    }

    public PropertyAmenityId(Long propertyId, Long amenityId) {
        this.propertyId = propertyId;
        this.amenityId = amenityId;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public Long getAmenityId() {
        return amenityId;
    }

    public void setAmenityId(Long amenityId) {
        this.amenityId = amenityId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof PropertyAmenityId that)) {
            return false;
        }
        return Objects.equals(propertyId, that.propertyId) && Objects.equals(amenityId, that.amenityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(propertyId, amenityId);
    }
}
