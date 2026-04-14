package com.guppy57.propdeals.dto;

import java.time.Instant;

/**
 * Request body for creating or updating a rent comp.
 * {@code compAddress} and {@code rentAmount} are required.
 */
public record RentCompRequest(
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
) {}
