package com.guppy57.propdeals.dto;

import com.guppy57.propdeals.entity.FilterOperator;

/**
 * Request body for creating or updating a filter rule.
 * {@code filterSetId}, {@code metric}, and {@code valueA} are required.
 */
public record FilterRuleRequest(
        Long filterSetId,
        String metric,
        FilterOperator operator,
        Double valueA,
        Double valueB
) {}
