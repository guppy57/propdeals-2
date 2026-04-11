package com.guppy57.propdeals.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;

/**
 * Configures Spring Data JDBC to convert camelCase field names to snake_case
 * column names automatically, matching the PostgreSQL schema conventions.
 */
@Configuration
public class DataConfig {

    @Bean
    NamingStrategy namingStrategy() {
        return new NamingStrategy() {
            @Override
            public String getColumnName(RelationalPersistentProperty property) {
                return toSnakeCase(property.getName());
            }

            @Override
            public String getTableName(Class<?> type) {
                return toSnakeCase(type.getSimpleName());
            }
        };
    }

    private static String toSnakeCase(String s) {
        return s.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
}
