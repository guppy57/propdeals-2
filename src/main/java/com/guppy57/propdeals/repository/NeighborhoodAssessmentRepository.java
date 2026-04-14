package com.guppy57.propdeals.repository;

import com.guppy57.propdeals.entity.NeighborhoodAssessment;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface NeighborhoodAssessmentRepository extends CrudRepository<NeighborhoodAssessment, Long> {

    @Query("SELECT * FROM neighborhood_assessment WHERE property_id = :propertyId")
    Optional<NeighborhoodAssessment> findByPropertyId(@Param("propertyId") UUID propertyId);
}
