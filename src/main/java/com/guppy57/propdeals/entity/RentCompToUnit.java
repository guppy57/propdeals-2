package com.guppy57.propdeals.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * Maps to the public.rent_comp_to_unit junction table (created V6).
 * Links a rent comp to one or more units with optional distance/correlation metadata.
 */
@Table("rent_comp_to_unit")
public record RentCompToUnit(

        @Id Long id,

        UUID rentCompId,              // NOT NULL FK → rent_comps(id)
        Long unitId,                  // NOT NULL FK → units(id)
        Double distanceMiles,
        Double correlation,

        Instant createdAt
) {}
