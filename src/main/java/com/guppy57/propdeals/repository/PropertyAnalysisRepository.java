package com.guppy57.propdeals.repository;

import com.guppy57.propdeals.entity.DealType;
import com.guppy57.propdeals.entity.PropertyAnalysis;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PropertyAnalysisRepository extends CrudRepository<PropertyAnalysis, UUID> {

    @Query("SELECT * FROM property_analysis WHERE property_id = :propertyId ORDER BY created_at DESC")
    List<PropertyAnalysis> findAllByPropertyId(@Param("propertyId") UUID propertyId);

    @Query("SELECT * FROM property_analysis WHERE property_id = :propertyId AND deal_type = :dealType")
    Optional<PropertyAnalysis> findByPropertyIdAndDealType(@Param("propertyId") UUID propertyId,
                                                           @Param("dealType") DealType dealType);
}
