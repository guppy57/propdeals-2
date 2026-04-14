package com.guppy57.propdeals.repository;

import com.guppy57.propdeals.entity.Unit;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UnitRepository extends CrudRepository<Unit, Long> {

    @Query("SELECT * FROM units WHERE property_id = :propertyId ORDER BY unit_num ASC")
    List<Unit> findAllByPropertyId(@Param("propertyId") UUID propertyId);
}
