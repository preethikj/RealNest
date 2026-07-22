package com.capstone.realNest.repository;

import com.capstone.realNest.entity.Property;
import com.capstone.realNest.enums.PropertyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyRepository
        extends JpaRepository<Property, Long> {

    /*
     * Customer property queries
     */

    List<Property> findByOwnerIdOrderByCreatedAtDesc(
            Long ownerId
    );

    List<Property> findByOwnerIdAndStatusOrderByCreatedAtDesc(
            Long ownerId,
            PropertyStatus status
    );

    Optional<Property> findByIdAndOwnerId(
            Long propertyId,
            Long ownerId
    );

    long countByOwnerId(Long ownerId);


    /*
     * Property status and admin queries
     */

    List<Property> findByStatusOrderByCreatedAtDesc(
            PropertyStatus status
    );

    Optional<Property> findByIdAndStatus(
            Long propertyId,
            PropertyStatus status
    );

    List<Property> findAllByOrderByCreatedAtDesc();

    long countByStatus(String status);

    long countByStatusAndListingType(
            PropertyStatus status,
            String listingType
    );


    /*
     * Public property search
     *
     * All filters are optional except status.
     *
     * Landing-page search:
     * - city
     * - listing type
     *
     * Detailed property search:
     * - city
     * - locality
     * - listing type
     * - minimum price
     * - maximum price
     */

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
            :propertyType IS NULL
            OR LOWER(property.propertyType) =
               LOWER(:propertyType)
          )

      AND (
            :bedrooms IS NULL
            OR property.bedrooms = :bedrooms
            OR (
                 :bedrooms = 4
                 AND property.bedrooms >= 4
               )
          )

      AND (
            :city IS NULL
            OR LOWER(property.city) LIKE
               LOWER(CONCAT('%', :city, '%'))
          )

      AND (
            :location IS NULL
            OR LOWER(property.locality) LIKE
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

    """)
    Page<Property> searchProperties(
            @Param("status") String status,
            @Param("listingType") String listingType,
            @Param("propertyType") String propertyType,
            @Param("bedrooms") Integer bedrooms,
            @Param("city") String city,
            @Param("location") String location,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable
    );


    /*
     * Dynamic city suggestions for the landing page.
     *
     * Only cities containing approved properties are returned.
     */

    @Query("""
        SELECT DISTINCT property.city
        FROM Property property
        WHERE property.status = :status
          AND property.city IS NOT NULL
          AND TRIM(property.city) <> ''
        ORDER BY property.city
        """)
    List<String> findDistinctCitiesByStatus(
            @Param("status") String status
    );


    /*
     * Dynamic locality suggestions for the property-list page.
     *
     * Only localities belonging to the selected city and containing
     * approved properties are returned.
     */

    @Query("""
        SELECT DISTINCT property.locality
        FROM Property property
        WHERE property.status = :status
          AND property.locality IS NOT NULL
          AND TRIM(property.locality) <> ''
          AND (
                :city IS NULL
                OR LOWER(property.city) = LOWER(:city)
              )
        ORDER BY property.locality
        """)
    List<String> findDistinctLocalitiesByStatusAndCity(
            @Param("status") String status,
            @Param("city") String city
    );
}