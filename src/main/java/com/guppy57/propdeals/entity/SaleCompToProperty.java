package com.guppy57.propdeals.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * Maps to the public.sale_comp_to_property junction table (created V6).
 * Links a sale comp to one or more properties with optional distance/correlation metadata.
 */
@Table("sale_comp_to_property")
public record SaleCompToProperty(

        @Id Long id,

        UUID saleCompId,              // NOT NULL FK → sale_comps(id)
        UUID propertyId,              // NOT NULL FK → properties(id)
        Double distanceMiles,
        Double correlation,

        Instant createdAt
) {}
