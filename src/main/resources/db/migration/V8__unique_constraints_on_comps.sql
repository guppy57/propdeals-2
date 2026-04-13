-- V8: Add unique constraints on rent_comps and sale_comps to prevent
--     duplicate insertions during data migration and future imports.
--
-- NULLS NOT DISTINCT (Postgres 15+): NULL values in any key column are treated
-- as equal, so two rows with NULL beds/baths will still be considered duplicates
-- if all other key columns match.
--
-- Run cleanup_comps.py BEFORE applying this migration to remove existing dupes.

ALTER TABLE public.rent_comps
    ADD CONSTRAINT rent_comps_dedup_uk
    UNIQUE NULLS NOT DISTINCT (comp_address, beds, baths, sqft, rent_amount, source);

ALTER TABLE public.sale_comps
    ADD CONSTRAINT sale_comps_dedup_uk
    UNIQUE NULLS NOT DISTINCT (comp_address, source);
