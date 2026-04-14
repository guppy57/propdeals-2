package com.guppy57.propdeals.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * Maps to the public.property_assessment table.
 * property_id (UUID) was migrated from address1 text in V5.
 * Condition scores: 1=poor, 2=fair, 3=average, 4=good, 5=excellent.
 */
@Table("property_assessment")
public record PropertyAssessment(

        @Id Long id,

        UUID propertyId,              // NOT NULL FK → properties(id)

        // structural
        Integer foundationScore,
        Integer roofScore,
        Integer roofAgeYears,
        String roofNotes,
        Integer exteriorScore,

        // mechanical
        Integer hvacScore,
        Integer hvacAgeYears,
        Integer electricalScore,
        Integer plumbingScore,
        Integer waterHeaterScore,
        Integer waterHeaterAgeYears,

        // interior
        Integer interiorScore,
        Integer kitchenScore,
        Integer bathroomScore,
        Integer flooringScore,

        // overall
        Integer overallConditionScore,         // 1–5, drives score_condition in cache
        Long estimatedDeferredMaintenance,     // $ amount
        String assessmentNotes,
        String assessedBy,                     // self, inspector, agent

        Instant createdAt,
        Instant updatedAt
) {}
