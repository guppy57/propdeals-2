package com.guppy57.propdeals.repository;

import com.guppy57.propdeals.entity.SaleComp;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface SaleCompRepository extends CrudRepository<SaleComp, UUID> {
    // Access comps via SaleCompToPropertyRepository
}
