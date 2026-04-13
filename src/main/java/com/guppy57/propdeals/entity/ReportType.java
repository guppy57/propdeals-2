package com.guppy57.propdeals.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * Maps to the public.report_types table.
 * Column names are derived via the snake_case NamingStrategy in DataConfig.
 */
@Table("report_types")
public record ReportType(

        @Id UUID id,

        String researchType,  // NOT NULL — maps to column research_type
        String prompt,        // NOT NULL

        UUID userId,          // NOT NULL — set from JWT, never from client

        Instant createdAt,
        Instant updatedAt
) {}
