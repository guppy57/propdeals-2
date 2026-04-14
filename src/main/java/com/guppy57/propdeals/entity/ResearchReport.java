package com.guppy57.propdeals.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Maps to the public.research_reports table.
 * property_id (UUID) was migrated from text in V5.
 * status is a text CHECK constraint (pending, processing, completed, failed) — not a PG enum.
 */
@Table("research_reports")
public record ResearchReport(

        @Id UUID id,

        UUID propertyId,              // NOT NULL FK → properties(id)
        UUID reportTypeId,            // NOT NULL FK → report_types(id)
        String reportContent,         // NOT NULL
        String status,                // DEFAULT 'pending'
        BigDecimal apiCost,           // DEFAULT 0.0

        Instant createdAt
) {}
