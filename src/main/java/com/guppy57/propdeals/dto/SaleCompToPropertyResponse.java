package com.guppy57.propdeals.dto;

import com.guppy57.propdeals.entity.SaleCompToProperty;

import java.time.Instant;
import java.util.UUID;

public record SaleCompToPropertyResponse(
        Long id,
        UUID saleCompId,
        UUID propertyId,
        Double distanceMiles,
        Double correlation,
        Instant createdAt
) {
    public static SaleCompToPropertyResponse from(SaleCompToProperty e) {
        return new SaleCompToPropertyResponse(
                e.id(), e.saleCompId(), e.propertyId(),
                e.distanceMiles(), e.correlation(), e.createdAt()
        );
    }
}
