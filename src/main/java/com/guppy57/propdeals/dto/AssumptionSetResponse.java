package com.guppy57.propdeals.dto;

import com.guppy57.propdeals.entity.AssumptionSegment;
import com.guppy57.propdeals.entity.AssumptionSet;

import java.time.Instant;

/** Outbound representation — excludes the internal {@code userId} field. */
public record AssumptionSetResponse(

        Long id,
        AssumptionSegment segment,
        String description,
        boolean isDefault,

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
) {
    public static AssumptionSetResponse from(AssumptionSet e) {
        return new AssumptionSetResponse(
                e.id(), e.segment(), e.description(), e.isDefault(),
                e.appreciationRate(), e.closingCostsRate(), e.federalTaxRate(),
                e.stateTaxCode(), e.landValuePrcnt(),
                e.rentAppreciationRate(), e.propertyTaxRate(), e.homeInsuranceRate(),
                e.vacancyRate(), e.repairSavingsRate(), e.capexReserveRate(),
                e.discountRate(), e.sellingCostsRate(), e.longtermCapitalGainsTaxRate(),
                e.residentialDepreciationPeriodYrs(), e.defaultPropertyConditionScore(),
                e.grossAnnualIncome(),
                e.utilityElectricBase(), e.utilityGasBase(), e.utilityWaterBase(),
                e.utilityTrashBase(), e.utilityInternetBase(), e.utilityBaselineSqft(),
                e.mfAppreciationRateOverride(),
                e.rehabContingencyPct(), e.holdingCostRateMonthly(), e.flipSellingCostsRate(),
                e.shorttermCapitalGainsRate(), e.minRoiPct(), e.minProfitAmt(),
                e.createdAt(), e.updatedAt()
        );
    }
}
