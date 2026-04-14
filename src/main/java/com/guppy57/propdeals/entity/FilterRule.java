package com.guppy57.propdeals.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

/**
 * Maps to the public.filter_rules table.
 * metric is a column name in calculation_cache. valueB is only used for BETWEEN operator.
 */
@Table("filter_rules")
public record FilterRule(

        @Id Long id,

        Long filterSetId,             // NOT NULL FK → filter_sets(id)
        String metric,                // NOT NULL — column name in calculation_cache
        FilterOperator operator,      // NOT NULL DEFAULT BETWEEN
        Double valueA,                // NOT NULL
        Double valueB,                // nullable — only for BETWEEN

        Instant createdAt
) {}
