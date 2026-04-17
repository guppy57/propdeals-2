package com.guppy57.propdeals.dto;

import com.guppy57.propdeals.entity.Property;
import com.guppy57.propdeals.entity.PropertyStatus;
import com.guppy57.propdeals.entity.SellerCircumstance;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/** Outbound representation — excludes the internal {@code userId} field. */
public record PropertyResponse(

        UUID id,
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
        String reasonForPassing,

        Instant createdAt,
        Instant updatedAt
) {
    public static PropertyResponse from(Property e) {
        return new PropertyResponse(
                e.id(), e.address1(), e.fullAddress(), e.city(), e.zipCode(), e.state(), e.country(), e.zillowLink(),
                e.purchasePrice(), e.beds(), e.baths(), e.squareFt(), e.builtIn(), e.units(),
                e.walkScore(), e.transitScore(), e.bikeScore(), e.lat(), e.lon(),
                e.annualElectricityCostEst(),
                e.status(), e.listedDate(), e.hasTenants(), e.hasReducedPrice(),
                e.county(), e.annualTaxAmount(), e.rentDdCompleted(),
                e.gasStationDistanceMiles(), e.schoolDistanceMiles(), e.universityDistanceMiles(),
                e.groceryStoreDistanceMiles(), e.hospitalDistanceMiles(), e.parkDistanceMiles(),
                e.transitStationDistanceMiles(),
                e.gasStationCount5mi(), e.schoolCount5mi(), e.universityCount5mi(),
                e.groceryStoreCount5mi(), e.hospitalCount5mi(), e.parkCount5mi(),
                e.transitStationCount5mi(),
                e.sellerCircumstances(),
                e.obtainedCountyRecords(), e.historicalTurnoverRate(), e.hasDeedRestrictions(),
                e.hasHao(), e.hasHistoricPreservation(), e.setbacks(), e.hasEasements(),
                e.easements(), e.inFloodZone(), e.hasOpenPulledPermits(), e.hasWorkDoneWoPermits(),
                e.lastPurchasePrice(), e.lastPurchaseDate(), e.countyRecordNotes(),
                e.propertyNotes(), e.whitepagesNotes(),
                e.rentEstimate(), e.rentEstimateLow(), e.rentEstimateHigh(),
                e.isFsbo(), e.averageOwnershipDuration(),
                e.estPrice(), e.estPriceLow(), e.estPriceHigh(),
                e.hasMarketResearch(), e.propertyConditionScore(), e.reasonForPassing(),
                e.createdAt(), e.updatedAt()
        );
    }
}
