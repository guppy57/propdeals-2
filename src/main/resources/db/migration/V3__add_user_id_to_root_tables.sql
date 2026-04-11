-- V3: Add user_id (owner) to all root entity tables
-- NOT NULL + FK to auth.users(id) ON DELETE CASCADE
-- Safe to run: all tables are currently empty

ALTER TABLE public.properties
    ADD COLUMN user_id uuid NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE;

ALTER TABLE public.loans
    ADD COLUMN user_id uuid NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE;

ALTER TABLE public.assumption_sets
    ADD COLUMN user_id uuid NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE;

ALTER TABLE public.neighborhoods
    ADD COLUMN user_id uuid NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE;

ALTER TABLE public.report_types
    ADD COLUMN user_id uuid NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE;

ALTER TABLE public.filter_sets
    ADD COLUMN user_id uuid NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE;

-- Indexes for efficient per-user queries and RLS policies
CREATE INDEX ON public.properties      (user_id);
CREATE INDEX ON public.loans           (user_id);
CREATE INDEX ON public.assumption_sets (user_id);
CREATE INDEX ON public.neighborhoods   (user_id);
CREATE INDEX ON public.report_types    (user_id);
CREATE INDEX ON public.filter_sets     (user_id);
