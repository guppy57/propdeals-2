package com.guppy57.propdeals.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * Maps to the public.assumption_sets table.
 * Column names are derived via the snake_case NamingStrategy in DataConfig.
 */
@Table("assumption_sets")
public record AssumptionSet(

        @Id Long id,

        AssumptionSegment segment,        // NOT NULL
        String description,               // NOT NULL
        boolean isDefault,                // NOT NULL DEFAULT false
        UUID userId,                      // NOT NULL — set from JWT, never from client

        // ── Shared ───────────────────────────────────────────────────
        Float appreciationRate,
        Float closingCostsRate,
        Double federalTaxRate,
        String stateTaxCode,
        Float landValuePrcnt,

        // ── LTR-only ─────────────────────────────────────────────────
        Float rentAppreciationRate,
        Float propertyTaxRate,
        Float homeInsuranceRate,
        Float vacancyRate,
        Float repairSavingsRate,
        Double capexReserveRate,
        Float discountRate,
        Double sellingCostsRate,
        Double longtermCapitalGainsTaxRate,
        Double residentialDepreciationPeriodYrs,
        Integer defaultPropertyConditionScore,
        Integer grossAnnualIncome,
        Double utilityElectricBase,
        Double utilityGasBase,
        Double utilityWaterBase,
        Double utilityTrashBase,
        Double utilityInternetBase,
        Integer utilityBaselineSqft,
        Float mfAppreciationRateOverride,

        // ── FLIP-only ─────────────────────────────────────────────────
        Double rehabContingencyPct,
        Double holdingCostRateMonthly,
        Double flipSellingCostsRate,
        Double shorttermCapitalGainsRate,
        Double minRoiPct,
        Double minProfitAmt,

        Instant createdAt,
        Instant updatedAt
) {}
