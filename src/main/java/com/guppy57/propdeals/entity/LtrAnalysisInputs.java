package com.guppy57.propdeals.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Maps to the public.ltr_analysis_inputs table.
 * Behavioral overrides for LTR calculations; one row per property_analysis.
 */
@Table("ltr_analysis_inputs")
public record LtrAnalysisInputs(

        @Id Long id,

        UUID propertyAnalysisId,      // NOT NULL FK → property_analysis(id), UNIQUE

        // house hacking flags
        boolean liveInUnit,           // NOT NULL DEFAULT false
        boolean includeRoomRental,    // NOT NULL DEFAULT false
        // null = calculate from min_rent unit's bed count
        BigDecimal roomRentalIncomeOverride,

        // management
        boolean mgmtSelfManaged,      // NOT NULL DEFAULT true

        // rent override: null = roll up from units table
        BigDecimal rentOverrideTotal,

        Instant createdAt
) {}
