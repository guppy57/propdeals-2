package com.guppy57.propdeals.dto;

import com.guppy57.propdeals.entity.ResearchReport;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ResearchReportResponse(
        UUID id,
        UUID propertyId,
        UUID reportTypeId,
        String reportContent,
        String status,
        BigDecimal apiCost,
        Instant createdAt
) {
    public static ResearchReportResponse from(ResearchReport e) {
        return new ResearchReportResponse(
                e.id(), e.propertyId(), e.reportTypeId(),
                e.reportContent(), e.status(), e.apiCost(), e.createdAt()
        );
    }
}
