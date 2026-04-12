-- V5: Replace address1 text PK on properties with a proper UUID id column.
--     All child tables that FK'd to properties(address1) are updated to
--     reference properties(id) via a new property_id uuid column.
--     address1 is kept on properties as a NOT NULL UNIQUE column.
--
-- Order of operations:
--   1. Drop ALL child FK constraints first (required before dropping the PK)
--   2. Add uuid id to properties, swap PK, keep address1 as UNIQUE
--   3. Per child table: add property_id, backfill, set NOT NULL, drop old
--      address1 column + indexes/constraints, add new FK + indexes


-- ================================================================
-- STEP 1: Drop all FK constraints on child tables
--         (Postgres refuses to drop the PK while any FK points at it)
-- ================================================================

ALTER TABLE public.units                  DROP CONSTRAINT units_address1_fkey;
ALTER TABLE public.property_neighborhood  DROP CONSTRAINT pn_address1_fkey;
ALTER TABLE public.neighborhood_assessment DROP CONSTRAINT na_address1_fkey;
ALTER TABLE public.property_assessment    DROP CONSTRAINT pa_address1_fkey;
ALTER TABLE public.research_reports       DROP CONSTRAINT research_reports_property_fkey;
ALTER TABLE public.rent_comps             DROP CONSTRAINT rent_comps_property_fkey;
ALTER TABLE public.sale_comps             DROP CONSTRAINT sale_comps_property_fkey;
ALTER TABLE public.property_analysis      DROP CONSTRAINT pa_address1_fkey;


-- ================================================================
-- STEP 2: Add UUID id to properties and promote it to PK
-- ================================================================

-- gen_random_uuid() back-fills every existing row automatically
ALTER TABLE public.properties
    ADD COLUMN id uuid NOT NULL DEFAULT gen_random_uuid();

-- Swap primary key: drop text PK, promote uuid id
ALTER TABLE public.properties
    DROP CONSTRAINT properties_pkey;

ALTER TABLE public.properties
    ADD CONSTRAINT properties_pkey PRIMARY KEY (id);

-- Keep address1 as a unique, human-readable identifier
ALTER TABLE public.properties
    ADD CONSTRAINT properties_address1_uk UNIQUE (address1);


-- ================================================================
-- STEP 3: units
--   address1 text → property_id uuid
--   unique (address1, unit_num) → (property_id, unit_num)
-- ================================================================

ALTER TABLE public.units ADD COLUMN property_id uuid;

UPDATE public.units u
SET property_id = p.id
FROM public.properties p
WHERE p.address1 = u.address1;

ALTER TABLE public.units ALTER COLUMN property_id SET NOT NULL;

DROP INDEX IF EXISTS public.idx_units_address1;
ALTER TABLE public.units DROP CONSTRAINT units_address1_unit_num_uk;
ALTER TABLE public.units DROP COLUMN address1;

ALTER TABLE public.units
    ADD CONSTRAINT units_property_fkey FOREIGN KEY (property_id)
        REFERENCES public.properties (id) ON DELETE CASCADE;

ALTER TABLE public.units
    ADD CONSTRAINT units_property_unit_num_uk UNIQUE (property_id, unit_num);

CREATE INDEX idx_units_property_id ON public.units (property_id);


-- ================================================================
-- STEP 4: property_neighborhood
--   address1 text → property_id uuid
-- ================================================================

ALTER TABLE public.property_neighborhood ADD COLUMN property_id uuid;

UPDATE public.property_neighborhood pn
SET property_id = p.id
FROM public.properties p
WHERE p.address1 = pn.address1;

ALTER TABLE public.property_neighborhood ALTER COLUMN property_id SET NOT NULL;

DROP INDEX IF EXISTS public.idx_pn_address1;
ALTER TABLE public.property_neighborhood DROP COLUMN address1;

ALTER TABLE public.property_neighborhood
    ADD CONSTRAINT pn_property_fkey FOREIGN KEY (property_id)
        REFERENCES public.properties (id) ON DELETE CASCADE;

CREATE INDEX idx_pn_property_id ON public.property_neighborhood (property_id);


-- ================================================================
-- STEP 5: neighborhood_assessment
--   address1 text → property_id uuid
-- ================================================================

ALTER TABLE public.neighborhood_assessment ADD COLUMN property_id uuid;

UPDATE public.neighborhood_assessment na
SET property_id = p.id
FROM public.properties p
WHERE p.address1 = na.address1;

ALTER TABLE public.neighborhood_assessment ALTER COLUMN property_id SET NOT NULL;

ALTER TABLE public.neighborhood_assessment DROP COLUMN address1;

ALTER TABLE public.neighborhood_assessment
    ADD CONSTRAINT na_property_fkey FOREIGN KEY (property_id)
        REFERENCES public.properties (id) ON DELETE CASCADE;


-- ================================================================
-- STEP 6: property_assessment
--   address1 text → property_id uuid
-- ================================================================

ALTER TABLE public.property_assessment ADD COLUMN property_id uuid;

UPDATE public.property_assessment pa
SET property_id = p.id
FROM public.properties p
WHERE p.address1 = pa.address1;

ALTER TABLE public.property_assessment ALTER COLUMN property_id SET NOT NULL;

ALTER TABLE public.property_assessment DROP COLUMN address1;

ALTER TABLE public.property_assessment
    ADD CONSTRAINT pa_property_fkey FOREIGN KEY (property_id)
        REFERENCES public.properties (id) ON DELETE CASCADE;


-- ================================================================
-- STEP 7: research_reports
--   property_id text → property_id uuid
--   (column already named property_id but was text referencing address1)
-- ================================================================

ALTER TABLE public.research_reports ADD COLUMN property_id_new uuid;

UPDATE public.research_reports r
SET property_id_new = p.id
FROM public.properties p
WHERE p.address1 = r.property_id;

ALTER TABLE public.research_reports ALTER COLUMN property_id_new SET NOT NULL;

DROP INDEX IF EXISTS public.idx_research_property_id;
ALTER TABLE public.research_reports DROP COLUMN property_id;
ALTER TABLE public.research_reports RENAME COLUMN property_id_new TO property_id;

ALTER TABLE public.research_reports
    ADD CONSTRAINT research_reports_property_fkey FOREIGN KEY (property_id)
        REFERENCES public.properties (id) ON DELETE CASCADE;

CREATE INDEX idx_research_property_id ON public.research_reports (property_id);


-- ================================================================
-- STEP 8: rent_comps
--   address1 text → property_id uuid
-- ================================================================

ALTER TABLE public.rent_comps ADD COLUMN property_id uuid;

UPDATE public.rent_comps rc
SET property_id = p.id
FROM public.properties p
WHERE p.address1 = rc.address1;

ALTER TABLE public.rent_comps ALTER COLUMN property_id SET NOT NULL;

DROP INDEX IF EXISTS public.idx_rent_comps_address1;
ALTER TABLE public.rent_comps DROP COLUMN address1;

ALTER TABLE public.rent_comps
    ADD CONSTRAINT rent_comps_property_fkey FOREIGN KEY (property_id)
        REFERENCES public.properties (id) ON DELETE CASCADE;

CREATE INDEX idx_rent_comps_property_id ON public.rent_comps (property_id);


-- ================================================================
-- STEP 9: sale_comps
--   address1 text → property_id uuid
-- ================================================================

ALTER TABLE public.sale_comps ADD COLUMN property_id uuid;

UPDATE public.sale_comps sc
SET property_id = p.id
FROM public.properties p
WHERE p.address1 = sc.address1;

ALTER TABLE public.sale_comps ALTER COLUMN property_id SET NOT NULL;

DROP INDEX IF EXISTS public.idx_sale_comps_address1;
ALTER TABLE public.sale_comps DROP COLUMN address1;

ALTER TABLE public.sale_comps
    ADD CONSTRAINT sale_comps_property_fkey FOREIGN KEY (property_id)
        REFERENCES public.properties (id) ON DELETE CASCADE;

CREATE INDEX idx_sale_comps_property_id ON public.sale_comps (property_id);


-- ================================================================
-- STEP 10: property_analysis
--   address1 text → property_id uuid
--   unique (address1, deal_type) → (property_id, deal_type)
-- ================================================================

ALTER TABLE public.property_analysis ADD COLUMN property_id uuid;

UPDATE public.property_analysis pa
SET property_id = p.id
FROM public.properties p
WHERE p.address1 = pa.address1;

ALTER TABLE public.property_analysis ALTER COLUMN property_id SET NOT NULL;

DROP INDEX IF EXISTS public.idx_pa_address1;
ALTER TABLE public.property_analysis DROP CONSTRAINT pa_address1_deal_type_uk;
ALTER TABLE public.property_analysis DROP COLUMN address1;

ALTER TABLE public.property_analysis
    ADD CONSTRAINT pa_property_fkey FOREIGN KEY (property_id)
        REFERENCES public.properties (id) ON DELETE CASCADE;

ALTER TABLE public.property_analysis
    ADD CONSTRAINT pa_property_deal_type_uk UNIQUE (property_id, deal_type);

CREATE INDEX idx_pa_property_id ON public.property_analysis (property_id);
