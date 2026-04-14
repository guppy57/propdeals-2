package com.guppy57.propdeals.config;

import com.guppy57.propdeals.entity.PropertyStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;

import java.util.List;

/**
 * Configures Spring Data JDBC to convert camelCase field names to snake_case
 * column names automatically, matching the PostgreSQL schema conventions.
 *
 * Also registers custom converters for PropertyStatus, whose Postgres enum values
 * are lowercase and may contain spaces (e.g. 'pending sale', 'off market').
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

    @Bean
    JdbcCustomConversions jdbcCustomConversions() {
        return new JdbcCustomConversions(List.of(
                new PropertyStatusReadingConverter(),
                new PropertyStatusWritingConverter()
        ));
    }

    @ReadingConverter
    static class PropertyStatusReadingConverter implements Converter<String, PropertyStatus> {
        @Override
        public PropertyStatus convert(String source) {
            return PropertyStatus.fromDbValue(source);
        }
    }

    @WritingConverter
    static class PropertyStatusWritingConverter implements Converter<PropertyStatus, String> {
        @Override
        public String convert(PropertyStatus source) {
            return source.getDbValue();
        }
    }

    private static String toSnakeCase(String s) {
        return s.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
}
