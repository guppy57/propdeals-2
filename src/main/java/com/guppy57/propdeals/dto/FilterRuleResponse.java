package com.guppy57.propdeals.dto;

import com.guppy57.propdeals.entity.FilterOperator;
import com.guppy57.propdeals.entity.FilterRule;

import java.time.Instant;

public record FilterRuleResponse(
        Long id,
        Long filterSetId,
        String metric,
        FilterOperator operator,
        Double valueA,
        Double valueB,
        Instant createdAt
) {
    public static FilterRuleResponse from(FilterRule e) {
        return new FilterRuleResponse(
                e.id(), e.filterSetId(), e.metric(), e.operator(),
                e.valueA(), e.valueB(), e.createdAt()
        );
    }
}
