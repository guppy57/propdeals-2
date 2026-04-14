package com.guppy57.propdeals.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * Maps to the public.property_analysis table.
 * Ties a property to a deal type, loan, and assumption set.
 * No userId — user scoping goes through propertyId → properties.user_id.
 */
@Table("property_analysis")
public record PropertyAnalysis(

        @Id UUID id,

        UUID propertyId,              // NOT NULL FK → properties(id)
        DealType dealType,            // NOT NULL
        boolean isPrimary,            // NOT NULL DEFAULT false
        AnalysisStatus status,        // NOT NULL DEFAULT ACTIVE
        Integer loanId,               // NOT NULL FK → loans(id)
        Long assumptionSetId,         // NOT NULL FK → assumption_sets(id)

        // price override: null = use properties.purchase_price
        Long purchasePriceOverride,
        Long sellerCredits,           // NOT NULL DEFAULT 0
        boolean sellerPaysBuyersAgentFee, // NOT NULL DEFAULT false

        String notes,

        Instant createdAt,
        Instant updatedAt
) {}
