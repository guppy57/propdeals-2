package com.guppy57.propdeals.entity;

/**
 * Maps to the public.property_status Postgres enum.
 * DB values are lowercase and may contain spaces, so a custom converter is
 * registered in DataConfig to bridge between Java enum names and DB strings.
 */
public enum PropertyStatus {
    ACTIVE("active"),
    ACCEPTED("accepted"),
    PASSED("passed"),
    SOLD("sold"),
    PENDING_SALE("pending sale"),
    OFF_MARKET("off market");

    private final String dbValue;

    PropertyStatus(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }

    public static PropertyStatus fromDbValue(String value) {
        for (PropertyStatus s : values()) {
            if (s.dbValue.equals(value)) return s;
        }
        throw new IllegalArgumentException("Unknown property_status value: " + value);
    }
}
