package com.guppy57.propdeals.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * Maps to the public.loans table.
 * Column names are derived via the snake_case NamingStrategy in DataConfig.
 */
@Table("loans")
public record Loan(

        @Id Integer id,

        String name,               // NOT NULL
        LoanType loanType,         // NOT NULL DEFAULT CONVENTIONAL
        boolean isDefault,         // NOT NULL DEFAULT false
        UUID userId,               // NOT NULL — set from JWT, never from client

        Double interestRate,
        Double aprRate,
        Double downPaymentRate,
        Long years,

        // ── FHA-only ─────────────────────────────────────────────
        Double mipUpfrontRate,
        Double mipAnnualRate,

        Double upfrontDiscounts,   // NOT NULL DEFAULT 0
        Float lenderFees,          // DEFAULT 0
        Long pmiAmountOverride,    // monthly PMI flat amount

        // ── Hard money ───────────────────────────────────────────
        Double points,             // origination points (% of loan)

        Instant createdAt
) {}
