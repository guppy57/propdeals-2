-- V7: Add missing property_status enum values needed for v1 → v2 data migration.
--
-- Existing values: active, accepted, passed, sold
-- Adding: 'pending sale', 'off market'
--
-- ALTER TYPE ... ADD VALUE cannot run inside a transaction block.
ALTER TYPE public.property_status ADD VALUE IF NOT EXISTS 'pending sale';
ALTER TYPE public.property_status ADD VALUE IF NOT EXISTS 'off market';
