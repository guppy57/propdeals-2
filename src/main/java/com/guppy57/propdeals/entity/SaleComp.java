package com.guppy57.propdeals.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Maps to the public.sale_comps table.
 * Note: property_id was removed in V6 — use sale_comp_to_property junction table.
 * Additional columns (county, lat/lon, property_type) were added in V6.
 */
@Table("sale_comps")
public record SaleComp(

        @Id UUID id,

        String compAddress,           // NOT NULL
        Integer beds,
        Float baths,
        Integer sqft,
        Long salePrice,               // NOT NULL
        LocalDate saleDate,
        Double pricePerSqft,
        String source,                // DEFAULT 'MANUAL'
        Instant fetchedAt,

        // added V6
        String county,
        Double latitude,
        Double longitude,
        String propertyType
) {}
