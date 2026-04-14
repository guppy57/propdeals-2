package com.guppy57.propdeals.repository;

import com.guppy57.propdeals.entity.RentComp;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface RentCompRepository extends CrudRepository<RentComp, UUID> {
    // Access comps via RentCompToPropertyRepository or RentCompToUnitRepository
}
