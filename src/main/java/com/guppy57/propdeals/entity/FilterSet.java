package com.guppy57.propdeals.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * Maps to the public.filter_sets table.
 * dealType null means the filter set applies to all deal types.
 */
@Table("filter_sets")
public record FilterSet(

        @Id Long id,

        String name,                  // NOT NULL
        String description,
        DealType dealType,            // nullable — null = applies to all
        boolean isDefault,            // NOT NULL DEFAULT false
        UUID userId,                  // NOT NULL — set from JWT, never from client

        Instant createdAt
) {}
