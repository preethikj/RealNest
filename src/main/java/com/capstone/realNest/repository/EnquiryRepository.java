package com.capstone.realNest.repository;

import com.capstone.realNest.entity.Enquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnquiryRepository
        extends JpaRepository<Enquiry, Long> {

    List<Enquiry>
    findByPropertyOwnerIdOrderByCreatedAtDesc(Long ownerId);

    List<Enquiry>
    findByPropertyIdOrderByCreatedAtDesc(Long propertyId);
}