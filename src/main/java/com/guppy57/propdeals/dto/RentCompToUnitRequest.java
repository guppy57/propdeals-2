package com.guppy57.propdeals.dto;

import java.util.UUID;

/**
 * Request body for linking a rent comp to a unit.
 * {@code rentCompId} and {@code unitId} are required.
 */
public record RentCompToUnitRequest(
        UUID rentCompId,
        Long unitId,
        Double distanceMiles,
        Double correlation
) {}
