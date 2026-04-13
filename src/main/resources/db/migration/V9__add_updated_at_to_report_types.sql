-- V9: Add updated_at to report_types for consistency with other root entity tables

ALTER TABLE public.report_types
    ADD COLUMN updated_at timestamptz DEFAULT now();
