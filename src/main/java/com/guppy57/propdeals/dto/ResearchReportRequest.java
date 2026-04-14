package com.guppy57.propdeals.dto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Request body for creating a research report.
 * {@code propertyId}, {@code reportTypeId}, and {@code reportContent} are required.
 */
public record ResearchReportRequest(
        UUID propertyId,
        UUID reportTypeId,
        String reportContent,
        String status,
        BigDecimal apiCost
) {}
