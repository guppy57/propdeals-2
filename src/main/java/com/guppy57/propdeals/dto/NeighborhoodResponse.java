package com.guppy57.propdeals.dto;

import com.guppy57.propdeals.entity.Neighborhood;

import java.time.Instant;

public record NeighborhoodResponse(
        Long id,
        String name,
        String nicheComMappedName,
        String letterGrade,
        String nicheComLetterGrade,
        String city,
        String state,
        String country,
        Instant createdAt,
        Instant updatedAt
) {
    public static NeighborhoodResponse from(Neighborhood e) {
        return new NeighborhoodResponse(
                e.id(),
                e.name(),
                e.nicheComMappedName(),
                e.letterGrade(),
                e.nicheComLetterGrade(),
                e.city(),
                e.state(),
                e.country(),
                e.createdAt(),
                e.updatedAt()
        );
    }
}
