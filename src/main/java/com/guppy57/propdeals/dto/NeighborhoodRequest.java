package com.guppy57.propdeals.dto;

public record NeighborhoodRequest(
        String name,
        String nicheComMappedName,
        String letterGrade,
        String nicheComLetterGrade,
        String city,
        String state,
        String country
) {}
