package com.guppy57.propdeals.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * Maps to the public.neighborhood_assessment table.
 * property_id (UUID) was migrated from address1 text in V5.
 */
@Table("neighborhood_assessment")
public record NeighborhoodAssessment(

        @Id Long id,

        UUID propertyId,              // NOT NULL FK → properties(id)
        String schoolDistrictName,
        Integer numSexOff2m,
        String neighborhoodStakeoutNotes,
        String talkingNeighborNotes,
        Double elementarySchoolRating,
        Double middleSchoolRating,
        Double highSchoolRating,

        Instant createdAt,
        Instant updatedAt
) {}
