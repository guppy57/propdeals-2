package com.guppy57.propdeals.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * Maps to the public.calculation_cache table.
 * One row per property_analysis. All computed outputs live here.
 * Controllers read this table only — they never call the calculator.
 *
 * Layout:
 *   Section 1: Cache control + input snapshots
 *   Section 2: Shared acquisition costs (LTR + flip)
 *   Section 3: LTR results — NULL for flip rows
 *   Section 4: Flip results — NULL for LTR rows
 *   Section 5: Deal scores (both deal types, different sub-scores)
 *
 * loanSnapshot and assumptionSnapshot are jsonb in Postgres; mapped to String here.
 */
@Table("calculation_cache")
public record CalculationCache(

        @Id UUID id,

        UUID propertyAnalysisId,      // NOT NULL FK → property_analysis(id), UNIQUE

        // ── SECTION 1: Cache control ──────────────────────────────────────
        boolean isStale,              // NOT NULL DEFAULT true
        String staleReason,
        Instant calculatedAt,
        Integer calculationVersion,   // NOT NULL DEFAULT 1
        Long purchasePriceUsed,
        String loanSnapshot,          // jsonb
        String assumptionSnapshot,    // jsonb

        // ── SECTION 2: Shared acquisition costs ──────────────────────────
        Double downPayment,
        Double loanAmount,
        Double monthlyMortgage,
        Double monthlyMip,            // FHA only, else 0
        Double monthlyTaxes,
        Double monthlyInsurance,
        Double piti,                  // principal+interest+tax+insurance
        Double cashNeeded,            // down + closing - discounts
        Double amortizationEstimate,  // monthly principal paydown estimate
        Double closingCosts,
        Double closingCostsPrcnt,

        // Closing cost line items — lender costs
        Double ccLoanOriginationFee,
        Double ccProcessingFee,
        Double ccUnderwritingFee,
        Double ccCreditReportingFee,
        Double ccTotalLenderCosts,

        // Title costs
        Double ccTaxServiceFee,
        Double ccFloodCertificationFee,
        Double ccAppraisalFee,
        Double ccAbstractUpdateFee,
        Double ccTitleExaminationFee,
        Double ccTitleGuarantyCert,
        Double ccOwnersTitleInsurance,
        Double ccSettlementFee,
        Double ccTotalTitleCosts,

        // Government costs
        Double ccDeedRecordingFee,
        Double ccMortgageRecordingFee,
        Double ccTotalGovernmentCosts,

        // Prepaids
        Double ccPrepaidHomeInsurance,
        Double ccPropertyTaxProration,
        Double ccPrepaidInterest,
        Double ccTotalPrepaidCosts,

        // Escrow
        Double ccInsuranceReserve,
        Double ccTaxReserve,
        Double ccAggregateAdjustment,
        Double ccTotalEscrowCosts,

        // Optional / miscellaneous
        Double ccHomeInspectionFee,
        Double ccPropertySurveyFee,
        Double ccPestInspectionFee,
        Double ccStructuralEngineerFee,
        Double ccSewerInspectionFee,
        Double ccKellerWilliamsFee,
        Double ccCourierFees,
        Double ccNotaryFees,
        Double ccTotalOptionalCosts,

        // IFA / second loan info (Iowa-specific)
        String secondLoanType,        // 'reduced_dp' | 'reduced_loan' | null

        // ── SECTION 3: LTR results (NULL for flip rows) ──────────────────

        // Quick estimate
        Double quickMonthlyRentEstimate,
        Double qeTotalRent,
        Double qeMonthlyVacancyCosts,
        Double qeMonthlyRepairCosts,
        Double qeMonthlyCapexCosts,
        Double qeOperatingExpenses,
        Double qeTotalMonthlyCost,
        Double qeMonthlyCashFlow,
        Double qeAnnualCashFlow,

        // Utility breakdown
        Double monthlyUtilityElectric,
        Double monthlyUtilityGas,
        Double monthlyUtilityWater,
        Double monthlyUtilityTrash,
        Double monthlyUtilityInternet,
        Double monthlyUtilityTotal,
        // House hack utility splits
        Double roommateUtilities,
        Double ownerUtilities,
        Double emergencyFund3m,       // 3-month PITI + utilities reserve

        // Market rent section
        Double grossRentMonthly,      // total from units table
        Double y1OpexRentBase,        // rent basis for Y1 expense calc
        Double y2RentBase,            // rent basis for Y2+ (full occupancy)
        String y2RentBaseSource,      // 'whole_property' | 'room_sum'

        // House hack income splits
        Double mrNetRentY1,           // rent net of owner unit
        Double mrNetRentY2,           // rent fully occupied
        Double mrAnnualRentY1,
        Double mrAnnualRentY2,

        // Operating expenses (market rent basis, monthly)
        Double mrMonthlyVacancyCosts,
        Double mrMonthlyRepairCosts,
        Double mrMonthlyCapexCosts,
        Double mrExpensePropertyTax,
        Double mrExpenseInsurance,
        Double mrOperatingExpenses,
        Double mrTotalMonthlyCost,

        // NOI
        Double mrMonthlyNoiY1,
        Double mrMonthlyNoiY2,
        Double mrAnnualNoiY1,
        Double mrAnnualNoiY2,

        // Cash flow
        Double mrMonthlyCashFlowY1,
        Double mrMonthlyCashFlowY2,
        Double mrAnnualCashFlowY1,
        Double mrAnnualCashFlowY2,

        // House hack utility detail by year
        Double roommateUtilitiesY1,
        Double roommateUtilitiesY2,
        Double ownerUtilitiesY1,
        Double ownerUtilitiesY2,
        Double ownerMonthlyCost,      // owner's net out-of-pocket

        // Core investment ratios
        Double capRateY1,
        Double capRateY2,
        Double cocY1,                 // cash-on-cash Y1
        Double cocY2,                 // cash-on-cash Y2
        Double grmY1,                 // gross rent multiplier Y1
        Double grmY2,
        Double mgrPp,                 // monthly gross rent / purchase price (1% rule)
        Double opexRent,              // operating expenses / rent (50% rule)
        Double dscr,                  // debt service coverage ratio
        Double ltvRatio,
        Double pricePerDoor,
        Double rentPerSqft,
        Double breakEvenOccupancy,    // occupancy needed to cover all costs
        Double breakEvenVacancy,      // max vacancy before going negative
        Double oer,                   // operating expense ratio
        Double egi,                   // effective gross income (monthly)
        Double debtYield,             // annual NOI / loan amount
        Double fhaSelfSufficiencyRatio, // (y2_rent * 0.75) / piti — FHA MF test
        Boolean passesOnePct,         // mgr_pp >= 0.01
        Boolean passesFiftyPct,       // opex_rent <= 0.50

        // Mobility
        Double mobilityScore,         // walk*0.6 + transit*0.3 + bike*0.1
        Double costsToIncome,         // piti / after_tax_monthly_income

        // Tax & depreciation
        Double monthlyDepreciation,
        Double taxSavingsMonthly,
        Double afterTaxCashFlowY1,
        Double afterTaxCashFlowY2,

        // Long-term value projections
        Double futureValue5yr,
        Double futureValue10yr,
        Double futureValue20yr,
        Double netProceeds5yr,        // after selling costs + cap gains
        Double netProceeds10yr,
        Double netProceeds20yr,

        // Total return forecasts (cash flow + equity + appreciation)
        Double forecast5yr,
        Double forecast10yr,
        Double forecast20yr,

        // Return metrics
        Double equityMultiple5yr,
        Double equityMultiple10yr,
        Double equityMultiple20yr,
        Double avgAnnualReturn5yr,
        Double avgAnnualReturn10yr,
        Double avgAnnualReturn20yr,
        Double roeY2,                 // return on equity year 2
        Double leverageBenefit,       // CoC_y2 - cap_rate_y2
        Double paybackPeriodYears,

        // IRR
        Double irr5yr,
        Double irr10yr,
        Double irr20yr,

        // NPV
        Double npv5yr,
        Double npv10yr,
        Double npv20yr,

        // Fair value (purchase_price + NPV)
        Double fairValue5yr,
        Double fairValue10yr,
        Double fairValue20yr,

        // Value gap % (NPV / cash_needed * 100)
        Double valueGapPct5yr,
        Double valueGapPct10yr,
        Double valueGapPct20yr,

        Boolean beatsMarket,          // npv_10yr > 0

        // Downside scenario (10% rent reduction stress test)
        Double cashFlowY1Downside10pct,
        Double cashFlowY2Downside10pct,

        // ── SECTION 4: Flip results (NULL for LTR rows) ──────────────────

        // Cost basis build-up
        Double flipPurchaseClosingCosts,
        Double flipRehabBase,
        Double flipRehabContingencyAmt,
        Double flipRehabTotal,         // base + contingency
        Double flipHoldingCostsMonthly,
        Double flipHoldingCostsTotal,  // monthly * holding_period
        Double flipHardMoneyPointsAmt, // null if conventional
        Double flipSellingCostsAmt,    // agent fees + transfer tax
        Double flipTotalCostBasis,     // all costs combined

        // Returns
        Double flipArvUsed,
        Double flipExpectedSalePrice,
        Double flipGrossProfit,        // sale - total_cost_basis
        Double flipTaxEstimate,        // short-term cap gains
        Double flipNetProfit,          // gross - taxes
        Double flipRoiPct,
        Double flipAnnualizedRoiPct,   // accounts for hold period
        Double flipEquityMultiple,
        Double flipProfitPerMonth,

        // Deal rules
        Double flipMao,                // max allowable offer = ARV*70% - rehab
        Boolean flipPasses70PctRule,
        Double flipArvToPriceRatio,
        Double flipLtvRatio,           // loan / arv (lender underwrites on ARV)

        // ── SECTION 5: Deal scores (0–100 integers) ──────────────────────
        Integer scoreOverall,
        // LTR scores
        Integer scoreCashflow,
        Integer scoreLocation,
        Integer scoreCondition,
        Integer scoreRentUpside,
        Integer scoreMarket,
        // Flip scores
        Integer scoreMargin,
        Integer scoreRehabRisk,
        Integer scoreMarketVelocity
) {}
