package com.guppy57.propdeals.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Maps to the public.properties table.
 * PK is UUID id (promoted in V5). address1 is kept as UNIQUE NOT NULL.
 */
@Table("properties")
public record Property(

        @Id UUID id,

        String address1,              // NOT NULL UNIQUE
        String fullAddress,
        String zillowLink,
        Long purchasePrice,
        Long beds,
        Float baths,
        Long squareFt,
        Long builtIn,
        Long units,                   // 0 = SFH, >0 = multifamily unit count
        Long walkScore,
        Long transitScore,
        Long bikeScore,
        Double lat,
        Double lon,
        Long annualElectricityCostEst,

        PropertyStatus status,        // NOT NULL DEFAULT 'active'
        LocalDate listedDate,
        Boolean hasTenants,
        Boolean hasReducedPrice,
        String county,
        Integer annualTaxAmount,
        Boolean rentDdCompleted,

        // amenity proximity
        Double gasStationDistanceMiles,
        Double schoolDistanceMiles,
        Double universityDistanceMiles,
        Double groceryStoreDistanceMiles,
        Double hospitalDistanceMiles,
        Double parkDistanceMiles,
        Double transitStationDistanceMiles,
        Integer gasStationCount5mi,
        Integer schoolCount5mi,
        Integer universityCount5mi,
        Integer groceryStoreCount5mi,
        Integer hospitalCount5mi,
        Integer parkCount5mi,
        Integer transitStationCount5mi,

        // seller context
        SellerCircumstance sellerCircumstances,

        // county due diligence
        Boolean obtainedCountyRecords,
        Double historicalTurnoverRate,
        Boolean hasDeedRestrictions,
        Boolean hasHao,
        Boolean hasHistoricPreservation,
        String setbacks,
        Boolean hasEasements,
        String easements,
        Boolean inFloodZone,
        Boolean hasOpenPulledPermits,
        Boolean hasWorkDoneWoPermits,
        Double lastPurchasePrice,
        LocalDate lastPurchaseDate,
        String countyRecordNotes,
        String propertyNotes,
        String whitepagesNotes,

        // SFH whole-property rent estimate
        Integer rentEstimate,
        Integer rentEstimateLow,
        Integer rentEstimateHigh,

        Boolean isFsbo,
        Double averageOwnershipDuration,

        // estimated property value
        Integer estPrice,
        Integer estPriceLow,
        Integer estPriceHigh,

        // research & condition
        Boolean hasMarketResearch,
        Integer propertyConditionScore,
        String reasonForPassing,

        UUID userId,                  // NOT NULL — set from JWT, never from client

        Instant createdAt,
        Instant updatedAt
) {}
