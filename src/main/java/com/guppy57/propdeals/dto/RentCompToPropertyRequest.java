package com.guppy57.propdeals.dto;

import java.util.UUID;

/**
 * Request body for linking a rent comp to a property.
 * {@code rentCompId} and {@code propertyId} are required.
 */
public record RentCompToPropertyRequest(
        UUID rentCompId,
        UUID propertyId,
        Double distanceMiles,
        Double correlation
) {}
