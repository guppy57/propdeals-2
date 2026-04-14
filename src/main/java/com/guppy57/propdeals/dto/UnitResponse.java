package com.guppy57.propdeals.dto;

import com.guppy57.propdeals.entity.Unit;

import java.time.Instant;
import java.util.UUID;

public record UnitResponse(
        Long id,
        UUID propertyId,
        Long unitNum,
        Long beds,
        Float baths,
        Long estimatedSqrft,
        Long rentEstimate,
        Long rentEstimateLow,
        Long rentEstimateHigh,
        Instant createdAt,
        Instant updatedAt
) {
    public static UnitResponse from(Unit e) {
        return new UnitResponse(
                e.id(), e.propertyId(), e.unitNum(),
                e.beds(), e.baths(), e.estimatedSqrft(),
                e.rentEstimate(), e.rentEstimateLow(), e.rentEstimateHigh(),
                e.createdAt(), e.updatedAt()
        );
    }
}
