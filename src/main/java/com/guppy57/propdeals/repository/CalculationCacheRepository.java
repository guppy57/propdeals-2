package com.guppy57.propdeals.repository;

import com.guppy57.propdeals.entity.CalculationCache;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CalculationCacheRepository extends CrudRepository<CalculationCache, UUID> {

    @Query("SELECT * FROM calculation_cache WHERE property_analysis_id = :propertyAnalysisId")
    Optional<CalculationCache> findByPropertyAnalysisId(@Param("propertyAnalysisId") UUID propertyAnalysisId);

    @Query("SELECT * FROM calculation_cache WHERE is_stale = :isStale ORDER BY calculated_at DESC")
    List<CalculationCache> findAllByIsStale(@Param("isStale") boolean isStale);
}
