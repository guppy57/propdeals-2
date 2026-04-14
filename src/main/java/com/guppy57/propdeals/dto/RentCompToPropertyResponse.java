package com.guppy57.propdeals.dto;

import com.guppy57.propdeals.entity.RentCompToProperty;

import java.time.Instant;
import java.util.UUID;

public record RentCompToPropertyResponse(
        Long id,
        UUID rentCompId,
        UUID propertyId,
        Double distanceMiles,
        Double correlation,
        Instant createdAt
) {
    public static RentCompToPropertyResponse from(RentCompToProperty e) {
        return new RentCompToPropertyResponse(
                e.id(), e.rentCompId(), e.propertyId(),
                e.distanceMiles(), e.correlation(), e.createdAt()
        );
    }
}
