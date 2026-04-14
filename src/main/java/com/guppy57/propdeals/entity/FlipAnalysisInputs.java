package com.guppy57.propdeals.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * Maps to the public.flip_analysis_inputs table.
 * Deal-specific inputs for fix & flip analysis; one row per property_analysis.
 */
@Table("flip_analysis_inputs")
public record FlipAnalysisInputs(

        @Id Long id,

        UUID propertyAnalysisId,      // NOT NULL FK → property_analysis(id), UNIQUE

        Long arvEstimate,             // after-repair value
        Long rehabEstimate,           // base rehab cost estimate
        Integer holdingPeriodMonths,

        // null = use arv_estimate as expected sale price
        Long salePriceOverride,

        // null = use assumption_set.rehab_contingency_pct
        Double rehabContingencyOverride,

        ContractorType contractorType, // NOT NULL DEFAULT GC

        // hard money financing detail
        boolean usesHardMoney,        // NOT NULL DEFAULT false
        // null if usesHardMoney = false
        Double hardMoneyPoints,

        Instant createdAt
) {}
