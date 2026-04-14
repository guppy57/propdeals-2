package com.guppy57.propdeals.dto;

import java.util.UUID;

/**
 * Request body for linking a sale comp to a property.
 * {@code saleCompId} and {@code propertyId} are required.
 */
public record SaleCompToPropertyRequest(
        UUID saleCompId,
        UUID propertyId,
        Double distanceMiles,
        Double correlation
) {}
