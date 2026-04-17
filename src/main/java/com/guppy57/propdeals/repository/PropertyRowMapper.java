package com.guppy57.propdeals.repository;

import com.guppy57.propdeals.entity.Property;
import com.guppy57.propdeals.entity.PropertyStatus;
import com.guppy57.propdeals.entity.SellerCircumstance;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/** Maps a {@code properties} result-set row to a {@link Property} record. */
public class PropertyRowMapper implements RowMapper<Property> {

    @Override
    public Property mapRow(ResultSet rs, int rowNum) throws SQLException {

        String statusStr = rs.getString("status");
        PropertyStatus status = statusStr != null
                ? PropertyStatus.fromDbValue(statusStr)
                : PropertyStatus.ACTIVE;

        String scStr = rs.getString("seller_circumstances");
        SellerCircumstance sellerCircumstances = scStr != null
                ? SellerCircumstance.valueOf(scStr)
                : null;

        Timestamp createdAtTs = rs.getTimestamp("created_at");
        Instant createdAt = createdAtTs != null ? createdAtTs.toInstant() : null;

        Timestamp updatedAtTs = rs.getTimestamp("updated_at");
        Instant updatedAt = updatedAtTs != null ? updatedAtTs.toInstant() : null;

        LocalDate listedDate = rs.getObject("listed_date", LocalDate.class);
        LocalDate lastPurchaseDate = rs.getObject("last_purchase_date", LocalDate.class);

        return new Property(
                rs.getObject("id", UUID.class),
                rs.getString("address1"),
                rs.getString("full_address"),
                rs.getString("city"),
                rs.getString("zip_code"),
                rs.getString("state"),
                rs.getString("country"),
                rs.getString("zillow_link"),
                rs.getObject("purchase_price", Long.class),
                rs.getObject("beds", Long.class),
                rs.getObject("baths", Float.class),
                rs.getObject("square_ft", Long.class),
                rs.getObject("built_in", Long.class),
                rs.getObject("units", Long.class),
                rs.getObject("walk_score", Long.class),
                rs.getObject("transit_score", Long.class),
                rs.getObject("bike_score", Long.class),
                rs.getObject("lat", Double.class),
                rs.getObject("lon", Double.class),
                rs.getObject("annual_electricity_cost_est", Long.class),
                status,
                listedDate,
                rs.getObject("has_tenants", Boolean.class),
                rs.getObject("has_reduced_price", Boolean.class),
                rs.getString("county"),
                rs.getObject("annual_tax_amount", Integer.class),
                rs.getObject("rent_dd_completed", Boolean.class),
                rs.getObject("gas_station_distance_miles", Double.class),
                rs.getObject("school_distance_miles", Double.class),
                rs.getObject("university_distance_miles", Double.class),
                rs.getObject("grocery_store_distance_miles", Double.class),
                rs.getObject("hospital_distance_miles", Double.class),
                rs.getObject("park_distance_miles", Double.class),
                rs.getObject("transit_station_distance_miles", Double.class),
                rs.getObject("gas_station_count_5mi", Integer.class),
                rs.getObject("school_count_5mi", Integer.class),
                rs.getObject("university_count_5mi", Integer.class),
                rs.getObject("grocery_store_count_5mi", Integer.class),
                rs.getObject("hospital_count_5mi", Integer.class),
                rs.getObject("park_count_5mi", Integer.class),
                rs.getObject("transit_station_count_5mi", Integer.class),
                sellerCircumstances,
                rs.getObject("obtained_county_records", Boolean.class),
                rs.getObject("historical_turnover_rate", Double.class),
                rs.getObject("has_deed_restrictions", Boolean.class),
                rs.getObject("has_hao", Boolean.class),
                rs.getObject("has_historic_preservation", Boolean.class),
                rs.getString("setbacks"),
                rs.getObject("has_easements", Boolean.class),
                rs.getString("easements"),
                rs.getObject("in_flood_zone", Boolean.class),
                rs.getObject("has_open_pulled_permits", Boolean.class),
                rs.getObject("has_work_done_wo_permits", Boolean.class),
                rs.getObject("last_purchase_price", Double.class),
                lastPurchaseDate,
                rs.getString("county_record_notes"),
                rs.getString("property_notes"),
                rs.getString("whitepages_notes"),
                rs.getObject("rent_estimate", Integer.class),
                rs.getObject("rent_estimate_low", Integer.class),
                rs.getObject("rent_estimate_high", Integer.class),
                rs.getObject("is_fsbo", Boolean.class),
                rs.getObject("average_ownership_duration", Double.class),
                rs.getObject("est_price", Integer.class),
                rs.getObject("est_price_low", Integer.class),
                rs.getObject("est_price_high", Integer.class),
                rs.getObject("has_market_research", Boolean.class),
                rs.getObject("property_condition_score", Integer.class),
                rs.getString("reason_for_passing"),
                rs.getObject("user_id", UUID.class),
                createdAt,
                updatedAt
        );
    }
}
