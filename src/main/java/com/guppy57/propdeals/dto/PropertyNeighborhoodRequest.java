package com.guppy57.propdeals.dto;

import java.util.UUID;

/**
 * Request body for linking a property to a neighborhood.
 * Both {@code propertyId} and {@code neighborhoodId} are required.
 */
public record PropertyNeighborhoodRequest(
        UUID propertyId,
        Long neighborhoodId
) {}
