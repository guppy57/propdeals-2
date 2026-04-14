package com.guppy57.propdeals.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * Maps to the public.units table.
 * FK property_id (UUID) was migrated from address1 text in V5.
 * Note: the DB column is estimated_sqrft (matches V1 SQL spelling).
 */
@Table("units")
public record Unit(

        @Id Long id,

        UUID propertyId,              // NOT NULL FK → properties(id)
        Long unitNum,                 // NOT NULL
        Long beds,
        Float baths,
        Long estimatedSqrft,          // column: estimated_sqrft
        Long rentEstimate,
        Long rentEstimateLow,
        Long rentEstimateHigh,

        Instant createdAt,
        Instant updatedAt
) {}
