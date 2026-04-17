package com.guppy57.propdeals.dto;

import com.guppy57.propdeals.entity.PropertyStatus;
import com.guppy57.propdeals.entity.SellerCircumstance;

import java.time.LocalDate;

/**
 * Request body for creating or replacing a property.
 * {@code address1} is required on create; all other fields are optional.
 */
public record PropertyRequest(

        String address1,
        String fullAddress,
        String city,
        String zipCode,
        String state,
        String country,
        String zillowLink,
        Long purchasePrice,
        Long beds,
        Float baths,
        Long squareFt,
        Long builtIn,
        Long units,
        Long walkScore,
        Long transitScore,
        Long bikeScore,
        Double lat,
        Double lon,
        Long annualElectricityCostEst,

        PropertyStatus status,
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
        String reasonForPassing
) {}
