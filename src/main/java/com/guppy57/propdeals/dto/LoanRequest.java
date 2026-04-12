package com.guppy57.propdeals.dto;

import com.guppy57.propdeals.entity.LoanType;

public record LoanRequest(
        String name,
        LoanType loanType,
        Boolean isDefault,

        Double interestRate,
        Double aprRate,
        Double downPaymentRate,
        Long years,

        // ── FHA-only ─────────────────────────────────────────────
        Double mipUpfrontRate,
        Double mipAnnualRate,

        Double upfrontDiscounts,
        Float lenderFees,
        Long pmiAmountOverride,

        // ── Hard money ───────────────────────────────────────────
        Double points
) {}
