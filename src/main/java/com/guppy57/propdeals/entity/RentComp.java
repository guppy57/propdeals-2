package com.guppy57.propdeals.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * Maps to the public.rent_comps table.
 * Note: property_id was removed in V6 — use rent_comp_to_property junction table.
 * Additional columns (county, lat/lon, property_type, etc.) were added in V6.
 */
@Table("rent_comps")
public record RentComp(

        @Id UUID id,

        String compAddress,           // NOT NULL
        Integer beds,
        Float baths,
        Integer sqft,
        Integer rentAmount,           // NOT NULL
        String source,                // DEFAULT 'RENTCAST'
        Instant fetchedAt,

        // added V6
        String county,
        Double latitude,
        Double longitude,
        String propertyType,
        Integer lotSize,
        Integer builtIn,
        String status,
        Integer daysOld
) {}
