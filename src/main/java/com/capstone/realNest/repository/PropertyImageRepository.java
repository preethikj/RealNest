package com.capstone.realNest.repository;

import com.capstone.realNest.entity.PropertyImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyImageRepository
        extends JpaRepository<PropertyImage, Long> {

    List<PropertyImage> findByPropertyIdOrderByDisplayOrderAsc(
            Long propertyId
    );

    Optional<PropertyImage> findByIdAndPropertyId(
            Long imageId,
            Long propertyId
    );

    void deleteByPropertyId(Long propertyId);

    long countByPropertyId(Long propertyId);
}