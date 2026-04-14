package com.guppy57.propdeals.repository;

import com.guppy57.propdeals.entity.RentCompToProperty;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface RentCompToPropertyRepository extends CrudRepository<RentCompToProperty, Long> {

    @Query("SELECT * FROM rent_comp_to_property WHERE property_id = :propertyId")
    List<RentCompToProperty> findAllByPropertyId(@Param("propertyId") UUID propertyId);

    @Query("SELECT * FROM rent_comp_to_property WHERE rent_comp_id = :rentCompId")
    List<RentCompToProperty> findAllByRentCompId(@Param("rentCompId") UUID rentCompId);
}
