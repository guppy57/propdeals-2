package com.guppy57.propdeals.dto;

import com.guppy57.propdeals.entity.RentCompToUnit;

import java.time.Instant;
import java.util.UUID;

public record RentCompToUnitResponse(
        Long id,
        UUID rentCompId,
        Long unitId,
        Double distanceMiles,
        Double correlation,
        Instant createdAt
) {
    public static RentCompToUnitResponse from(RentCompToUnit e) {
        return new RentCompToUnitResponse(
                e.id(), e.rentCompId(), e.unitId(),
                e.distanceMiles(), e.correlation(), e.createdAt()
        );
    }
}
