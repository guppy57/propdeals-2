package com.guppy57.propdeals.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * Maps to the public.property_neighborhood join table.
 * property_id (UUID) was migrated from address1 text in V5.
 */
@Table("property_neighborhood")
public record PropertyNeighborhood(

        @Id Long id,

        UUID propertyId,              // NOT NULL FK → properties(id)
        Long neighborhoodId,          // NOT NULL FK → neighborhoods(id)

        Instant createdAt,
        Instant updatedAt
) {}
