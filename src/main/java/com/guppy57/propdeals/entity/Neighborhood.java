package com.guppy57.propdeals.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * Maps to the public.neighborhoods table.
 * Column names are derived via the snake_case NamingStrategy in DataConfig.
 */
@Table("neighborhoods")
public record Neighborhood(

        @Id Long id,

        String name,                  // NOT NULL
        String nicheComMappedName,
        String letterGrade,
        String nicheComLetterGrade,

        String city,
        String state,
        String country,

        UUID userId,                  // NOT NULL — set from JWT, never from client

        Instant createdAt,
        Instant updatedAt
) {}
