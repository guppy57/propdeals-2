package com.guppy57.propdeals.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * Maps to the public.rent_comp_to_property junction table (created V6).
 * Links a rent comp to one or more properties with optional distance/correlation metadata.
 */
@Table("rent_comp_to_property")
public record RentCompToProperty(

        @Id Long id,

        UUID rentCompId,              // NOT NULL FK → rent_comps(id)
        UUID propertyId,              // NOT NULL FK → properties(id)
        Double distanceMiles,
        Double correlation,

        Instant createdAt
) {}
