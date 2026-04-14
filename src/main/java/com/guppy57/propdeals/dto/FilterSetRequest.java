package com.guppy57.propdeals.dto;

import com.guppy57.propdeals.entity.DealType;

/**
 * Request body for creating or replacing a filter set.
 * {@code name} is required; {@code dealType} null means applies to all deal types.
 */
public record FilterSetRequest(
        String name,
        String description,
        DealType dealType,
        Boolean isDefault
) {}
