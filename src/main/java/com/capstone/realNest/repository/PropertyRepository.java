package com.capstone.realNest.repository;

import com.capstone.realNest.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long>{

    List<Property> findByOwnerIdOrderByCreatedAtDesc(Long ownerId);

    List<Property> findByStatusOrderByCreatedAtDesc(String status);

    List<Property> findByOwnerIdAndStatusOrderByCreatedAtDesc(
            Long ownerId,
            String status
    );

    Optional<Property> findByIdAndStatus(Long propertyId, String status);

    long countByStatus(String status);

    long countByOwnerId(Long ownerId);

    @Query("""
        SELECT property
        FROM Property property
        WHERE property.status = :status
          AND (
                :listingType IS NULL
                OR LOWER(property.listingType) =
                   LOWER(:listingType)
              )
          AND (
                :location IS NULL
                OR LOWER(property.city) LIKE
                   LOWER(CONCAT('%', :location, '%'))
                OR LOWER(property.address) LIKE
                   LOWER(CONCAT('%', :location, '%'))
              )
          AND (
                :minPrice IS NULL
                OR property.price >= :minPrice
              )
          AND (
                :maxPrice IS NULL
                OR property.price <= :maxPrice
              )
        ORDER BY property.createdAt DESC
        """)
    List<Property> searchProperties(
            @Param("status") String status,
            @Param("listingType") String listingType,
            @Param("location") String location,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice
    );
}