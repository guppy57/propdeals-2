package com.guppy57.propdeals.dto;

import com.guppy57.propdeals.entity.RentComp;

import java.time.Instant;
import java.util.UUID;

public record RentCompResponse(
        UUID id,
        String compAddress,
        Integer beds,
        Float baths,
        Integer sqft,
        Integer rentAmount,
        String source,
        Instant fetchedAt,
        String county,
        Double latitude,
        Double longitude,
        String propertyType,
        Integer lotSize,
        Integer builtIn,
        String status,
        Integer daysOld
) {
    public static RentCompResponse from(RentComp e) {
        return new RentCompResponse(
                e.id(), e.compAddress(), e.beds(), e.baths(), e.sqft(),
                e.rentAmount(), e.source(), e.fetchedAt(),
                e.county(), e.latitude(), e.longitude(),
                e.propertyType(), e.lotSize(), e.builtIn(), e.status(), e.daysOld()
        );
    }
}
