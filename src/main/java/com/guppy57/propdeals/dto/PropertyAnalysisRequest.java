package com.guppy57.propdeals.dto;

import com.guppy57.propdeals.entity.AnalysisStatus;
import com.guppy57.propdeals.entity.DealType;
import com.guppy57.propdeals.entity.PropertyAnalysis;

import java.time.Instant;
import java.util.UUID;

public record PropertyAnalysisRequest(
    UUID propertyId,
    DealType dealType,
    boolean isPrimary,
    AnalysisStatus status,
    int loanId,
    Long assumptionSetId,
    Long purchasePriceOverride,
    Long sellerCredits,
    boolean sellerPaysBuyersAgentFee,
    String notes
) {
    public static PropertyAnalysisRequest from(PropertyAnalysis e) {
        return new PropertyAnalysisRequest(
                e.propertyId(),
                e.dealType(),
                e.isPrimary(),
                e.status(),
                e.loanId(),
                e.assumptionSetId(),
                e.purchasePriceOverride(),
                e.sellerCredits(),
                e.sellerPaysBuyersAgentFee(),
                e.notes()
        );
    }
}