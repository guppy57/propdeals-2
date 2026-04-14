package com.guppy57.propdeals.repository;

import com.guppy57.propdeals.entity.FlipAnalysisInputs;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface FlipAnalysisInputsRepository extends CrudRepository<FlipAnalysisInputs, Long> {

    @Query("SELECT * FROM flip_analysis_inputs WHERE property_analysis_id = :propertyAnalysisId")
    Optional<FlipAnalysisInputs> findByPropertyAnalysisId(@Param("propertyAnalysisId") UUID propertyAnalysisId);
}
