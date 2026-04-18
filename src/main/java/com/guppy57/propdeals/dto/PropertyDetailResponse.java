package com.guppy57.propdeals.dto;

import java.util.List;

/** Single-property response that includes the property's units. */
public record PropertyDetailResponse(PropertyResponse property, List<UnitResponse> units) {}
