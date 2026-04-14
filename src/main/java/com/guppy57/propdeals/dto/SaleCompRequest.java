package com.guppy57.propdeals.dto;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Request body for creating or updating a sale comp.
 * {@code compAddress} and {@code salePrice} are required.
 */
public record SaleCompRequest(
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
) {}
