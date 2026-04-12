package com.guppy57.propdeals.dto;

import com.guppy57.propdeals.entity.Loan;
import com.guppy57.propdeals.entity.LoanType;

import java.time.Instant;

public record LoanResponse(
        Integer id,
        String name,
        LoanType loanType,
        boolean isDefault,

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
        Double points,

        Instant createdAt
) {
    public static LoanResponse from(Loan e) {
        return new LoanResponse(
                e.id(),
                e.name(),
                e.loanType(),
                e.isDefault(),
                e.interestRate(),
                e.aprRate(),
                e.downPaymentRate(),
                e.years(),
                e.mipUpfrontRate(),
                e.mipAnnualRate(),
                e.upfrontDiscounts(),
                e.lenderFees(),
                e.pmiAmountOverride(),
                e.points(),
                e.createdAt()
        );
    }
}
