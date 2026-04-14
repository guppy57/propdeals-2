package com.guppy57.propdeals.repository;

import com.guppy57.propdeals.entity.RentCompToUnit;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface RentCompToUnitRepository extends CrudRepository<RentCompToUnit, Long> {

    @Query("SELECT * FROM rent_comp_to_unit WHERE unit_id = :unitId")
    List<RentCompToUnit> findAllByUnitId(@Param("unitId") Long unitId);

    @Query("SELECT * FROM rent_comp_to_unit WHERE rent_comp_id = :rentCompId")
    List<RentCompToUnit> findAllByRentCompId(@Param("rentCompId") UUID rentCompId);
}
