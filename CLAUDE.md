# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build
./mvnw clean package

# Run
./mvnw spring-boot:run

# Run tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=PropdealsApplicationTests

# Run Flyway migrations (requires flyway.properties)
./mvnw flyway:migrate

# Flyway info/validate
./mvnw flyway:info
./mvnw flyway:validate
```

## Environment Setup

The app requires a `DB_PASSWORD` environment variable for the application datasource:

```bash
export DB_PASSWORD=<your-password>
```

Flyway CLI migrations (via `./mvnw flyway:*`) use `flyway.properties` in the project root. This file is gitignored and must be created locally:

```properties
flyway.url=jdbc:postgresql://aws-1-us-west-2.pooler.supabase.com:5432/postgres
flyway.user=postgres.taypkqqcgjqnyserkwgy
flyway.password=<your-password>
```

## Architecture

**Stack:** Spring Boot 4.0.5, Java 26, Spring Data JDBC, Spring Security, Spring MVC, PostgreSQL (Supabase), Flyway.

The database is hosted on Supabase. Authentication integrates with Supabase's `auth.users` table — the `profiles` table (V2 migration) references `auth.users(id)` and is populated via a Postgres trigger `on_auth_user_created`.

**Database schema** is managed exclusively by Flyway. Hibernate DDL auto is set to `validate` only. Migrations live in `src/main/resources/db/migration/`.

### Domain Model (from V1 migration)

PropDeals 2.0 models real estate deal analysis for two deal types:
- **`LONG_TERM_RENTAL` (LTR)** — includes house hacking scenarios
- **`FIX_AND_FLIP`** — rehab/resale deals

Key enums: `deal_type`, `analysis_status` (ACTIVE → UNDER_CONTRACT → OWNED/PASSED/SOLD), `loan_type` (CONVENTIONAL, FHA, DSCR, HARD_MONEY, SELLER_FINANCE, CASH, SBA_7A), `seller_circumstance`, `contractor_type`, `filter_operator`.

The `properties` table uses `address1` (text) as its primary key for backward compatibility with existing Supabase data.
