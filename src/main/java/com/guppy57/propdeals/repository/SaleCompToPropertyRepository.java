package com.guppy57.propdeals.repository;

import com.guppy57.propdeals.entity.SaleCompToProperty;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SaleCompToPropertyRepository extends CrudRepository<SaleCompToProperty, Long> {

    @Query("SELECT * FROM sale_comp_to_property WHERE property_id = :propertyId")
    List<SaleCompToProperty> findAllByPropertyId(@Param("propertyId") UUID propertyId);

    @Query("SELECT * FROM sale_comp_to_property WHERE sale_comp_id = :saleCompId")
    List<SaleCompToProperty> findAllBySaleCompId(@Param("saleCompId") UUID saleCompId);
}
