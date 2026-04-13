package com.guppy57.propdeals.dto;

import com.guppy57.propdeals.entity.ReportType;

import java.time.Instant;
import java.util.UUID;

public record ReportTypeResponse(
        UUID id,
        String researchType,
        String prompt,
        Instant createdAt,
        Instant updatedAt
) {
    public static ReportTypeResponse from(ReportType e) {
        return new ReportTypeResponse(
                e.id(),
                e.researchType(),
                e.prompt(),
                e.createdAt(),
                e.updatedAt()
        );
    }
}
