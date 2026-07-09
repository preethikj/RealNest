package com.capstone.realNest.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "property_amenities")
@IdClass(PropertyAmenityId.class)
public class PropertyAmenity {

    @Id
    @Column(nullable = false)
    private Long propertyId;

    @Id
    @Column(nullable = false)
    private Long amenityId;

}
