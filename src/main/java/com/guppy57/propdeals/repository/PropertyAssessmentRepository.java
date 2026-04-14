package com.guppy57.propdeals.repository;

import com.guppy57.propdeals.entity.PropertyAssessment;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PropertyAssessmentRepository extends CrudRepository<PropertyAssessment, Long> {

    @Query("SELECT * FROM property_assessment WHERE property_id = :propertyId")
    Optional<PropertyAssessment> findByPropertyId(@Param("propertyId") UUID propertyId);
}
