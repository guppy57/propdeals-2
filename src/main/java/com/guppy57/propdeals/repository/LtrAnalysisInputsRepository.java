package com.guppy57.propdeals.repository;

import com.guppy57.propdeals.entity.LtrAnalysisInputs;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface LtrAnalysisInputsRepository extends CrudRepository<LtrAnalysisInputs, Long> {

    @Query("SELECT * FROM ltr_analysis_inputs WHERE property_analysis_id = :propertyAnalysisId")
    Optional<LtrAnalysisInputs> findByPropertyAnalysisId(@Param("propertyAnalysisId") UUID propertyAnalysisId);
}
