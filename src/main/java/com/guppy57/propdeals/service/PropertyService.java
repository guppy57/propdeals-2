package com.guppy57.propdeals.service;

import com.guppy57.propdeals.dto.PropertyRequest;
import com.guppy57.propdeals.dto.PropertyResponse;
import com.guppy57.propdeals.entity.Property;
import com.guppy57.propdeals.entity.PropertyStatus;
import com.guppy57.propdeals.repository.PropertyRepository;
import com.guppy57.propdeals.repository.PropertyRowMapper;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PropertyService {

    private static final Map<String, String> SORT_COLUMN_MAP = Map.ofEntries(
            Map.entry("address1",      "address1"),
            Map.entry("city",          "city"),
            Map.entry("zipCode",       "zip_code"),
            Map.entry("state",         "state"),
            Map.entry("beds",          "beds"),
            Map.entry("baths",         "baths"),
            Map.entry("squareFt",      "square_ft"),
            Map.entry("purchasePrice", "purchase_price"),
            Map.entry("status",        "status"),
            Map.entry("createdAt",     "created_at")
    );

    private static final PropertyRowMapper ROW_MAPPER = new PropertyRowMapper();

    private final PropertyRepository repository;
    private final NamedParameterJdbcTemplate jdbc;

    public PropertyService(PropertyRepository repository, NamedParameterJdbcTemplate jdbc) {
        this.repository = repository;
        this.jdbc = jdbc;
    }

    public List<PropertyResponse> findAll(UUID userId, int page, int size,
                                          String search, String sortBy, String sortDir) {
        String col = SORT_COLUMN_MAP.getOrDefault(sortBy, "created_at");
        String dir = "ASC".equalsIgnoreCase(sortDir) ? "ASC" : "DESC";

        String sql = """
                SELECT * FROM properties
                WHERE user_id = :userId
                  AND (:search = '' OR address1 ILIKE '%%' || :search || '%%')
                ORDER BY %s %s
                LIMIT :limit OFFSET :offset
                """.formatted(col, dir);

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("search", search != null ? search : "")
                .addValue("limit", size)
                .addValue("offset", (long) page * size);

        return jdbc.query(sql, params, ROW_MAPPER)
                .stream()
                .map(PropertyResponse::from)
                .toList();
    }

    public PropertyResponse findById(UUID id, UUID userId) {
        return repository.findByIdAndUserId(id, userId)
                .map(PropertyResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public PropertyResponse create(PropertyRequest req, UUID userId) {
        if (req.address1() == null || req.address1().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "address1 is required");
        }
        Instant now = Instant.now();
        Property entity = toEntity(null, req, userId, now, now);
        return PropertyResponse.from(repository.save(entity));
    }

    public PropertyResponse update(UUID id, PropertyRequest req, UUID userId) {
        Property existing = repository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Property updated = toEntity(existing.id(), req, userId, existing.createdAt(), Instant.now());
        return PropertyResponse.from(repository.save(updated));
    }

    public void delete(UUID id, UUID userId) {
        if (repository.findByIdAndUserId(id, userId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        repository.deleteById(id);
    }

    private Property toEntity(UUID id, PropertyRequest req, UUID userId,
                               Instant createdAt, Instant updatedAt) {
        return new Property(
                id,
                req.address1(),
                req.fullAddress(),
                req.city(),
                req.zipCode(),
                req.state(),
                req.country(),
                req.zillowLink(),
                req.purchasePrice(),
                req.beds(),
                req.baths(),
                req.squareFt(),
                req.builtIn(),
                req.units(),
                req.walkScore(),
                req.transitScore(),
                req.bikeScore(),
                req.lat(),
                req.lon(),
                req.annualElectricityCostEst(),
                req.status() != null ? req.status() : PropertyStatus.ACTIVE,
                req.listedDate(),
                req.hasTenants() != null ? req.hasTenants() : false,
                req.hasReducedPrice() != null ? req.hasReducedPrice() : false,
                req.county(),
                req.annualTaxAmount(),
                req.rentDdCompleted() != null ? req.rentDdCompleted() : false,
                req.gasStationDistanceMiles(),
                req.schoolDistanceMiles(),
                req.universityDistanceMiles(),
                req.groceryStoreDistanceMiles(),
                req.hospitalDistanceMiles(),
                req.parkDistanceMiles(),
                req.transitStationDistanceMiles(),
                req.gasStationCount5mi(),
                req.schoolCount5mi(),
                req.universityCount5mi(),
                req.groceryStoreCount5mi(),
                req.hospitalCount5mi(),
                req.parkCount5mi(),
                req.transitStationCount5mi(),
                req.sellerCircumstances(),
                req.obtainedCountyRecords(),
                req.historicalTurnoverRate(),
                req.hasDeedRestrictions(),
                req.hasHao(),
                req.hasHistoricPreservation(),
                req.setbacks(),
                req.hasEasements(),
                req.easements(),
                req.inFloodZone(),
                req.hasOpenPulledPermits(),
                req.hasWorkDoneWoPermits(),
                req.lastPurchasePrice(),
                req.lastPurchaseDate(),
                req.countyRecordNotes(),
                req.propertyNotes(),
                req.whitepagesNotes(),
                req.rentEstimate(),
                req.rentEstimateLow(),
                req.rentEstimateHigh(),
                req.isFsbo() != null ? req.isFsbo() : false,
                req.averageOwnershipDuration(),
                req.estPrice(),
                req.estPriceLow(),
                req.estPriceHigh(),
                req.hasMarketResearch() != null ? req.hasMarketResearch() : false,
                req.propertyConditionScore() != null ? req.propertyConditionScore() : 3,
                req.reasonForPassing(),
                userId,
                createdAt,
                updatedAt
        );
    }
}
