package com.capstone.realNest.repository;

import com.capstone.realNest.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepository extends JpaRepository<Property, Long> {
}
