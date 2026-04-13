-- V6: Restructure rent_comps and sale_comps to support many-to-many relationships.
--
-- Problems with current schema (post-V5):
--   - rent_comps.property_id and sale_comps.property_id create a direct many-to-one
--     relationship, meaning a comp is owned by exactly one property.
--   - A rent comp should be linkable to multiple properties AND multiple units.
--   - A sale comp should be linkable to multiple properties.
--
-- Changes:
--   1. Create junction tables: rent_comp_to_property, rent_comp_to_unit, sale_comp_to_property
--   2. Migrate existing property_id + distance_miles data into the junction tables
--   3. Remove property_id and distance_miles from rent_comps and sale_comps
--   4. Enrich comp tables with geographic/detail columns present in v1 schema


-- ================================================================
-- STEP 1: Create junction tables
-- ================================================================

CREATE TABLE public.rent_comp_to_property (
    id             bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    rent_comp_id   uuid             NOT NULL REFERENCES public.rent_comps(id)  ON DELETE CASCADE,
    property_id    uuid             NOT NULL REFERENCES public.properties(id)  ON DELETE CASCADE,
    distance_miles double precision,
    correlation    double precision,
    created_at     timestamptz      NOT NULL DEFAULT now(),
    CONSTRAINT rctp_rent_comp_property_uk UNIQUE (rent_comp_id, property_id)
);

CREATE TABLE public.rent_comp_to_unit (
    id             bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    rent_comp_id   uuid             NOT NULL REFERENCES public.rent_comps(id) ON DELETE CASCADE,
    unit_id        bigint           NOT NULL REFERENCES public.units(id)       ON DELETE CASCADE,
    distance_miles double precision,
    correlation    double precision,
    created_at     timestamptz      NOT NULL DEFAULT now(),
    CONSTRAINT rctu_rent_comp_unit_uk UNIQUE (rent_comp_id, unit_id)
);

CREATE TABLE public.sale_comp_to_property (
    id             bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    sale_comp_id   uuid             NOT NULL REFERENCES public.sale_comps(id) ON DELETE CASCADE,
    property_id    uuid             NOT NULL REFERENCES public.properties(id) ON DELETE CASCADE,
    distance_miles double precision,
    correlation    double precision,
    created_at     timestamptz      NOT NULL DEFAULT now(),
    CONSTRAINT sctp_sale_comp_property_uk UNIQUE (sale_comp_id, property_id)
);


-- ================================================================
-- STEP 2: Migrate existing link data into the junction tables
-- ================================================================

-- Existing rent_comps rows each have exactly one property_id; carry that
-- (and the distance) into the new junction table.
INSERT INTO public.rent_comp_to_property (rent_comp_id, property_id, distance_miles)
SELECT id, property_id, distance_miles
FROM   public.rent_comps
WHERE  property_id IS NOT NULL;

-- Existing sale_comps rows each have exactly one property_id.
INSERT INTO public.sale_comp_to_property (sale_comp_id, property_id, distance_miles)
SELECT id, property_id, distance_miles
FROM   public.sale_comps
WHERE  property_id IS NOT NULL;


-- ================================================================
-- STEP 3: Remove property_id and distance_miles from rent_comps
-- ================================================================

ALTER TABLE public.rent_comps DROP CONSTRAINT rent_comps_property_fkey;
DROP  INDEX IF EXISTS public.idx_rent_comps_property_id;
ALTER TABLE public.rent_comps DROP COLUMN property_id;
ALTER TABLE public.rent_comps DROP COLUMN distance_miles;


-- ================================================================
-- STEP 4: Enrich rent_comps with v1-equivalent detail columns
-- ================================================================

ALTER TABLE public.rent_comps
    ADD COLUMN county        text,
    ADD COLUMN latitude      double precision,
    ADD COLUMN longitude     double precision,
    ADD COLUMN property_type text,
    ADD COLUMN lot_size      integer,
    ADD COLUMN built_in      integer,
    ADD COLUMN status        text,
    ADD COLUMN days_old      integer;


-- ================================================================
-- STEP 5: Remove property_id and distance_miles from sale_comps
-- ================================================================

ALTER TABLE public.sale_comps DROP CONSTRAINT sale_comps_property_fkey;
DROP  INDEX IF EXISTS public.idx_sale_comps_property_id;
ALTER TABLE public.sale_comps DROP COLUMN property_id;
ALTER TABLE public.sale_comps DROP COLUMN distance_miles;


-- ================================================================
-- STEP 6: Enrich sale_comps with geographic detail columns
-- ================================================================

ALTER TABLE public.sale_comps
    ADD COLUMN county        text,
    ADD COLUMN latitude      double precision,
    ADD COLUMN longitude     double precision,
    ADD COLUMN property_type text;


-- ================================================================
-- STEP 7: Indexes on junction table FK columns
-- ================================================================

CREATE INDEX idx_rctp_rent_comp_id  ON public.rent_comp_to_property (rent_comp_id);
CREATE INDEX idx_rctp_property_id   ON public.rent_comp_to_property (property_id);

CREATE INDEX idx_rctu_rent_comp_id  ON public.rent_comp_to_unit (rent_comp_id);
CREATE INDEX idx_rctu_unit_id       ON public.rent_comp_to_unit (unit_id);

CREATE INDEX idx_sctp_sale_comp_id  ON public.sale_comp_to_property (sale_comp_id);
CREATE INDEX idx_sctp_property_id   ON public.sale_comp_to_property (property_id);
