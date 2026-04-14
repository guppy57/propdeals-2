package com.guppy57.propdeals.dto;

import com.guppy57.propdeals.entity.SaleComp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record SaleCompResponse(
        UUID id,
        String compAddress,
        Integer beds,
        Float baths,
        Integer sqft,
        Long salePrice,
        LocalDate saleDate,
        Double pricePerSqft,
        String source,
        Instant fetchedAt,
        String county,
        Double latitude,
        Double longitude,
        String propertyType
) {
    public static SaleCompResponse from(SaleComp e) {
        return new SaleCompResponse(
                e.id(), e.compAddress(), e.beds(), e.baths(), e.sqft(),
                e.salePrice(), e.saleDate(), e.pricePerSqft(), e.source(), e.fetchedAt(),
                e.county(), e.latitude(), e.longitude(), e.propertyType()
        );
    }
}
