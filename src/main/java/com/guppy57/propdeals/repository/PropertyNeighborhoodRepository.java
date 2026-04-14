package com.guppy57.propdeals.repository;

import com.guppy57.propdeals.entity.PropertyNeighborhood;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PropertyNeighborhoodRepository extends CrudRepository<PropertyNeighborhood, Long> {

    @Query("SELECT * FROM property_neighborhood WHERE property_id = :propertyId")
    List<PropertyNeighborhood> findAllByPropertyId(@Param("propertyId") UUID propertyId);

    @Query("SELECT * FROM property_neighborhood WHERE neighborhood_id = :neighborhoodId")
    List<PropertyNeighborhood> findAllByNeighborhoodId(@Param("neighborhoodId") Long neighborhoodId);
}
