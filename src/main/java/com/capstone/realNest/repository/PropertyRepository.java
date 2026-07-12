package com.capstone.realNest.repository;

import com.capstone.realNest.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository
        extends JpaRepository<Property, Long>,
                JpaSpecificationExecutor<Property> {

    List<Property> findByOwnerIdOrderByCreatedAtDesc(Long ownerId);

    List<Property> findByStatusOrderByCreatedAtDesc(String status);

    List<Property> findByOwnerIdAndStatusOrderByCreatedAtDesc(
            Long ownerId,
            String status
    );

    long countByStatus(String status);

    long countByOwnerId(Long ownerId);
}