package com.guppy57.propdeals.dto;

import com.guppy57.propdeals.entity.AnalysisStatus;
import com.guppy57.propdeals.entity.DealType;
import com.guppy57.propdeals.entity.PropertyAnalysis;

import java.time.Instant;
import java.util.UUID;

public record PropertyAnalysisResponse (
    UUID id,
    UUID propertyId,
    DealType dealType,
    boolean isPrimary,
    AnalysisStatus status,
    int loanId,
    Long assumptionSetId,
    Long purchasePriceOverride,
    Long sellerCredits,
    boolean sellerPaysBuyersAgentFee,
    String notes,
    Instant createdAt,
    Instant updatedAt
) {
    public static PropertyAnalysisResponse from(PropertyAnalysis e) {
        return new PropertyAnalysisResponse(
                e.id(),
                e.propertyId(),
                e.dealType(),
                e.isPrimary(),
                e.status(),
                e.loanId(),
                e.assumptionSetId(),
                e.purchasePriceOverride(),
                e.sellerCredits(),
                e.sellerPaysBuyersAgentFee(),
                e.notes(),
                e.createdAt(),
                e.updatedAt()
        );
    }
}