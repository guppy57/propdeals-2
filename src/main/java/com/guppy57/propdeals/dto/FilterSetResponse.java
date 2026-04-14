package com.guppy57.propdeals.dto;

import com.guppy57.propdeals.entity.DealType;
import com.guppy57.propdeals.entity.FilterSet;

import java.time.Instant;

/** Outbound representation — excludes the internal {@code userId} field. */
public record FilterSetResponse(
        Long id,
        String name,
        String description,
        DealType dealType,
        boolean isDefault,
        Instant createdAt
) {
    public static FilterSetResponse from(FilterSet e) {
        return new FilterSetResponse(
                e.id(), e.name(), e.description(), e.dealType(), e.isDefault(), e.createdAt()
        );
    }
}
