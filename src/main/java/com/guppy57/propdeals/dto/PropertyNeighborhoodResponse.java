package com.guppy57.propdeals.dto;

import com.guppy57.propdeals.entity.PropertyNeighborhood;

import java.time.Instant;
import java.util.UUID;

public record PropertyNeighborhoodResponse(
        Long id,
        UUID propertyId,
        Long neighborhoodId,
        Instant createdAt,
        Instant updatedAt
) {
    public static PropertyNeighborhoodResponse from(PropertyNeighborhood e) {
        return new PropertyNeighborhoodResponse(
                e.id(), e.propertyId(), e.neighborhoodId(),
                e.createdAt(), e.updatedAt()
        );
    }
}
