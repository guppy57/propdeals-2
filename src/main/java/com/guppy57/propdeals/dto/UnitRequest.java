package com.guppy57.propdeals.dto;

import java.util.UUID;

/**
 * Request body for creating or updating a unit.
 * {@code propertyId} and {@code unitNum} are required on create.
 */
public record UnitRequest(
        UUID propertyId,
        Long unitNum,
        Long beds,
        Float baths,
        Long estimatedSqrft,
        Long rentEstimate,
        Long rentEstimateLow,
        Long rentEstimateHigh
) {}
