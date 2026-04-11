package com.guppy57.propdeals.dto;

import com.guppy57.propdeals.entity.AssumptionSegment;

/**
 * Request body for creating or replacing an assumption set.
 * {@code segment} and {@code description} are required; all other fields are optional.
 */
public record AssumptionSetRequest(

        AssumptionSegment segment,
        String description,
        Boolean isDefault,

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
        Double minProfitAmt
) {}
