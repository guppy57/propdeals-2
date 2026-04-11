-- ================================================================
-- PropDeals 2.0 — Complete Database Schema
-- Flyway Migration: V1__propdeals_schema.sql
--
-- Covers: LTR (incl. house hacking) + Fix & Flip only
-- All metrics from dataframe_helpers.py are present in
-- calculation_cache. Nullable sections are separated by deal type.
-- ================================================================


-- ================================================================
-- ENUMS
-- ================================================================

CREATE TYPE public.deal_type AS ENUM (
  'LONG_TERM_RENTAL',
  'FIX_AND_FLIP'
);

CREATE TYPE public.analysis_status AS ENUM (
  'ACTIVE',
  'UNDER_CONTRACT',
  'OWNED',
  'PASSED',
  'SOLD'
);

CREATE TYPE public.assumption_segment AS ENUM (
  'LTR',
  'FLIP'
);

CREATE TYPE public.contractor_type AS ENUM (
  'SELF',
  'GC',
  'MIXED'
);

CREATE TYPE public.filter_operator AS ENUM (
  'GT',
  'GTE',
  'LT',
  'LTE',
  'EQ',
  'NEQ',
  'BETWEEN'
);

-- Expanded from PropDeals 1 to cover flip financing
CREATE TYPE public.loan_type AS ENUM (
  'CONVENTIONAL',
  'FHA',
  'DSCR',
  'SBA_7A',
  'HARD_MONEY',
  'SELLER_FINANCE',
  'CASH'
);

-- Kept from PropDeals 1 (unchanged)
CREATE TYPE public.property_status AS ENUM (
  'active',
  'accepted',
  'passed',
  'sold'
);

-- Kept from PropDeals 1 (add values as needed)
CREATE TYPE public.seller_circumstance AS ENUM (
  'DIVORCE',
  'ESTATE_SALE',
  'FORECLOSURE',
  'RELOCATION',
  'FINANCIAL_DISTRESS',
  'TIRED_LANDLORD',
  'UNKNOWN'
);


-- ================================================================
-- CORE TABLES
-- ================================================================

-- properties: the physical asset. address1 kept as PK for
-- backward compat with existing Supabase data.
CREATE TABLE public.properties (
  address1                      text          NOT NULL,
  full_address                  text,
  zillow_link                   text,
  purchase_price                bigint,
  beds                          bigint,
  baths                         real,
  square_ft                     bigint,
  built_in                      bigint,
  units                         bigint,           -- 0 = SFH, >0 = multifamily unit count
  walk_score                    bigint,
  transit_score                 bigint,
  bike_score                    bigint,
  lat                           double precision,
  lon                           double precision,
  annual_electricity_cost_est   bigint,
  status                        public.property_status NOT NULL DEFAULT 'active',
  listed_date                   date,
  has_tenants                   boolean        DEFAULT false,
  has_reduced_price             boolean        DEFAULT false,
  county                        text,
  annual_tax_amount             integer,
  rent_dd_completed             boolean        DEFAULT false,

  -- amenity proximity (from PropDeals 1)
  gas_station_distance_miles    double precision,
  school_distance_miles         double precision,
  university_distance_miles     double precision,
  grocery_store_distance_miles  double precision,
  hospital_distance_miles       double precision,
  park_distance_miles           double precision,
  transit_station_distance_miles double precision,
  gas_station_count_5mi         integer,
  school_count_5mi              integer,
  university_count_5mi          integer,
  grocery_store_count_5mi       integer,
  hospital_count_5mi            integer,
  park_count_5mi                integer,
  transit_station_count_5mi     integer,

  -- seller context
  seller_circumstances          public.seller_circumstance,

  -- county due diligence (from PropDeals 1)
  obtained_county_records       boolean,
  historical_turnover_rate      double precision,
  has_deed_restrictions         boolean,
  has_hao                       boolean,
  has_historic_preservation     boolean,
  setbacks                      text,
  has_easements                 boolean,
  easements                     text,
  in_flood_zone                 boolean,
  has_open_pulled_permits       boolean,
  has_work_done_wo_permits      boolean,
  last_purchase_price           double precision,
  last_purchase_date            date,
  county_record_notes           text,
  property_notes                text,
  whitepages_notes              text,

  -- SFH whole-property rent estimate (kept for SFH quick screen;
  -- multi-family rent lives on units table)
  rent_estimate                 integer,
  rent_estimate_low             integer,
  rent_estimate_high            integer,

  is_fsbo                       boolean        DEFAULT false,
  average_ownership_duration    double precision,

  -- estimated property value
  est_price                     integer,
  est_price_low                 integer,
  est_price_high                integer,

  -- research & condition
  has_market_research           boolean        DEFAULT false,
  property_condition_score      integer        DEFAULT 3,
  reason_for_passing            text,

  created_at                    timestamptz    DEFAULT now(),
  updated_at                    timestamptz    DEFAULT now(),

  CONSTRAINT properties_pkey PRIMARY KEY (address1)
);


-- units: one row per rentable unit. Applies to both SFH (1 unit row)
-- and multifamily (N unit rows). Renamed from rent_estimates in v1.
CREATE TABLE public.units (
  id                  bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  address1            text          NOT NULL,
  unit_num            bigint        NOT NULL,
  beds                bigint,
  baths               real,
  estimated_sqrft     bigint,
  rent_estimate       bigint,
  rent_estimate_low   bigint,
  rent_estimate_high  bigint,
  created_at          timestamptz   DEFAULT now(),
  updated_at          timestamptz   DEFAULT now(),

  CONSTRAINT units_address1_fkey FOREIGN KEY (address1)
    REFERENCES public.properties (address1) ON DELETE CASCADE,
  CONSTRAINT units_address1_unit_num_uk UNIQUE (address1, unit_num)
);


-- ================================================================
-- LOANS
-- ================================================================

-- loans: templates for financing scenarios. asset_type controls
-- which deal type's dropdown shows this loan.
CREATE TABLE public.loans (
  id                    smallint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name                  text          NOT NULL,
  loan_type             public.loan_type NOT NULL DEFAULT 'CONVENTIONAL',
  interest_rate         double precision,
  apr_rate              double precision,
  down_payment_rate     double precision,
  years                 bigint,
  mip_upfront_rate      double precision,    -- FHA only
  mip_annual_rate       double precision,    -- FHA only
  upfront_discounts     double precision     NOT NULL DEFAULT 0,
  lender_fees           real             DEFAULT 0,
  pmi_amount_override            bigint,              -- monthly PMI if flat amount
  -- hard money specific
  points                double precision,    -- origination points (% of loan)
  is_default            boolean          NOT NULL DEFAULT false,
  created_at            timestamptz      DEFAULT now()
);


-- ================================================================
-- ASSUMPTION SETS
-- ================================================================

-- assumption_sets: named parameter bundles used in calculations.
-- segment discriminates LTR vs FLIP. Nullable columns are
-- segment-specific and documented below.
CREATE TABLE public.assumption_sets (
  id                              bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  segment                         public.assumption_segment NOT NULL,
  description                     text          NOT NULL,
  is_default                      boolean       NOT NULL DEFAULT false,

  -- ── Shared fields ────────────────────────────────────────────
  appreciation_rate               real,          -- annual property appreciation
  closing_costs_rate              real,
  federal_tax_rate                double precision  DEFAULT 0.22,
  state_tax_code                  text,          -- e.g. 'IA', 'IL'
  land_value_prcnt                real,          -- for depreciation calc

  -- ── LTR-only ─────────────────────────────────────────────────
  rent_appreciation_rate          real,
  property_tax_rate               real,
  home_insurance_rate             real,
  vacancy_rate                    real,
  repair_savings_rate             real,
  capex_reserve_rate              double precision  DEFAULT 0.0,
  discount_rate                   real,
  selling_costs_rate              double precision  DEFAULT 0.07,
  longterm_capital_gains_tax_rate double precision  DEFAULT 0.15,
  residential_depreciation_period_yrs double precision DEFAULT 27.5,
  default_property_condition_score integer       DEFAULT 3,
  gross_annual_income             integer,
  -- utility baselines (scaled by sqft at calc time)
  utility_electric_base           double precision  DEFAULT 136.00,
  utility_gas_base                double precision  DEFAULT 106.40,
  utility_water_base              double precision  DEFAULT 49.00,
  utility_trash_base              double precision  DEFAULT 18.00,
  utility_internet_base           double precision  DEFAULT 60.00,
  utility_baseline_sqft           integer           DEFAULT 1500,
  -- multifamily uses a slightly lower appreciation rate (set at calc time
  -- as appreciation_rate - 0.01; stored explicitly here for transparency)
  mf_appreciation_rate_override   real,          -- null = use appreciation_rate - 0.01

  -- ── FLIP-only ─────────────────────────────────────────────────
  rehab_contingency_pct           double precision, -- default buffer on rehab estimate
  holding_cost_rate_monthly       double precision, -- % of purchase price per month
  flip_selling_costs_rate         double precision, -- agent + transfer tax ~7%
  shortterm_capital_gains_rate    double precision, -- flips <1yr = ordinary income
  min_roi_pct                     double precision, -- target threshold
  min_profit_amt                  double precision, -- target threshold

  created_at                      timestamptz   DEFAULT now(),
  updated_at                      timestamptz   DEFAULT now()
);


-- ================================================================
-- NEIGHBORHOODS
-- ================================================================

CREATE TABLE public.neighborhoods (
  id                    bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name                  text NOT NULL,
  niche_com_mapped_name text,
  letter_grade          text,
  niche_com_letter_grade text,
  created_at            timestamptz DEFAULT now(),
  updated_at            timestamptz DEFAULT now()
);

CREATE TABLE public.property_neighborhood (
  id              bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  address1        text   NOT NULL,
  neighborhood_id bigint NOT NULL,
  created_at      timestamptz DEFAULT now(),
  updated_at      timestamptz DEFAULT now(),

  CONSTRAINT pn_address1_fkey FOREIGN KEY (address1)
    REFERENCES public.properties (address1) ON DELETE CASCADE,
  CONSTRAINT pn_neighborhood_fkey FOREIGN KEY (neighborhood_id)
    REFERENCES public.neighborhoods (id) ON DELETE CASCADE
);

CREATE TABLE public.neighborhood_assessment (
  id                          bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  address1                    text   NOT NULL,
  school_district_name        text,
  num_sex_off_2m              integer,
  neighborhood_stakeout_notes text,
  talking_neighbor_notes      text,
  elementary_school_rating    double precision,
  middle_school_rating        double precision,
  high_school_rating          double precision,
  created_at                  timestamptz DEFAULT now(),
  updated_at                  timestamptz DEFAULT now(),

  CONSTRAINT na_address1_fkey FOREIGN KEY (address1)
    REFERENCES public.properties (address1) ON DELETE CASCADE
);


-- ================================================================
-- PROPERTY ASSESSMENT
-- ================================================================

-- Physical condition assessment — ties to deal diligence.
-- condition scores: 1=poor, 2=fair, 3=average, 4=good, 5=excellent
CREATE TABLE public.property_assessment (
  id                        bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  address1                  text NOT NULL,

  -- structural
  foundation_score          integer,
  roof_score                integer,
  roof_age_years            integer,
  roof_notes                text,
  exterior_score            integer,

  -- mechanical
  hvac_score                integer,
  hvac_age_years            integer,
  electrical_score          integer,
  plumbing_score            integer,
  water_heater_score        integer,
  water_heater_age_years    integer,

  -- interior
  interior_score            integer,
  kitchen_score             integer,
  bathroom_score            integer,
  flooring_score            integer,

  -- overall
  overall_condition_score   integer,     -- 1–5, drives score_condition in cache
  estimated_deferred_maintenance bigint, -- $ amount
  assessment_notes          text,
  assessed_by               text,        -- self, inspector, agent

  created_at                timestamptz DEFAULT now(),
  updated_at                timestamptz DEFAULT now(),

  CONSTRAINT pa_address1_fkey FOREIGN KEY (address1)
    REFERENCES public.properties (address1) ON DELETE CASCADE
);


-- ================================================================
-- RESEARCH REPORTS
-- ================================================================

-- From PropDeals 1 — kept as-is with minor additions
CREATE TABLE public.report_types (
  id uuid NOT NULL DEFAULT gen_random_uuid(),
  research_type text NOT NULL,
  prompt NOT NULL,
  created_at timestamptz DEFAULT now(),
)

CREATE TABLE public.research_reports (
  id            uuid         NOT NULL DEFAULT gen_random_uuid(),
  property_id   text         NOT NULL,
  report_type_id text NOT NULL,
  report_content text        NOT NULL,
  status        text         DEFAULT 'pending',
  api_cost      numeric(10,4) DEFAULT 0.0,
  created_at    timestamptz  DEFAULT now(),

  CONSTRAINT research_reports_pkey PRIMARY KEY (id),
  CONSTRAINT research_reports_property_fkey FOREIGN KEY (property_id)
    REFERENCES public.properties (address1) ON DELETE CASCADE,
  CONSTRAINT research_reports_status_check CHECK (
    status IN ('pending','processing','completed','failed')
  ),
  CONSTRAINT research_reports_report_type_fkey FOREIGN KEY (report_type_id)
    REFERENCES public.report_types (id) ON DELETE CASCADE
);

-- ================================================================
-- COMPARABLES
-- ================================================================

-- Rent comps from Rentcast or manual entry
CREATE TABLE public.rent_comps (
  id              uuid         NOT NULL DEFAULT gen_random_uuid(),
  address1        text         NOT NULL,   -- subject property
  comp_address    text         NOT NULL,
  beds            integer,
  baths           real,
  sqft            integer,
  rent_amount     integer      NOT NULL,
  distance_miles  double precision,
  source          text         DEFAULT 'RENTCAST',
  fetched_at      timestamptz  DEFAULT now(),

  CONSTRAINT rent_comps_pkey PRIMARY KEY (id),
  CONSTRAINT rent_comps_property_fkey FOREIGN KEY (address1)
    REFERENCES public.properties (address1) ON DELETE CASCADE
);

-- Sale comps for ARV (used heavily in flip analysis)
CREATE TABLE public.sale_comps (
  id              uuid         NOT NULL DEFAULT gen_random_uuid(),
  address1        text         NOT NULL,   -- subject property
  comp_address    text         NOT NULL,
  beds            integer,
  baths           real,
  sqft            integer,
  sale_price      bigint       NOT NULL,
  sale_date       date,
  price_per_sqft  double precision,
  distance_miles  double precision,
  source          text         DEFAULT 'MANUAL',
  fetched_at      timestamptz  DEFAULT now(),

  CONSTRAINT sale_comps_pkey PRIMARY KEY (id),
  CONSTRAINT sale_comps_property_fkey FOREIGN KEY (address1)
    REFERENCES public.properties (address1) ON DELETE CASCADE
);


-- ================================================================
-- FILTER SETS
-- ================================================================

-- Dynamic replacement for hard-coded PHASE0_CRITERIA / PHASE1_CRITERIA.
-- Rules evaluate against fields in calculation_cache.
CREATE TABLE public.filter_sets (
  id          bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name        text   NOT NULL,
  description text,
  deal_type   public.deal_type,  -- null = applies to all
  is_default  boolean NOT NULL DEFAULT false,
  created_at  timestamptz DEFAULT now()
);

CREATE TABLE public.filter_rules (
  id            bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  filter_set_id bigint     NOT NULL,
  metric        text       NOT NULL,  -- column name in calculation_cache
  operator      public.filter_operator NOT NULL,
  value_a       double precision NOT NULL,
  value_b       double precision,     -- only for BETWEEN
  created_at    timestamptz DEFAULT now(),

  CONSTRAINT fr_filter_set_fkey FOREIGN KEY (filter_set_id)
    REFERENCES public.filter_sets (id) ON DELETE CASCADE
);


-- ================================================================
-- PROPERTY ANALYSIS (the input configuration layer)
-- ================================================================

-- property_analysis: ties a property to a deal type, loan, and
-- assumption set. One property can have one row per deal_type.
-- is_primary controls which analysis shows in list/compare views.
CREATE TABLE public.property_analysis (
  id                    uuid    NOT NULL DEFAULT gen_random_uuid(),
  address1              text    NOT NULL,
  deal_type             public.deal_type  NOT NULL,
  is_primary            boolean NOT NULL DEFAULT false,
  status                public.analysis_status NOT NULL DEFAULT 'ACTIVE',
  loan_id               smallint NOT NULL,
  assumption_set_id     bigint   NOT NULL,

  -- price override: null = use properties.purchase_price
  -- set when modeling a negotiated offer or price reduction scenario
  purchase_price_override bigint,
  seller_credits bigint NOT NULL DEFAULT 0,
  seller_pays_buyers_agent_fee boolean NOT NULL DEFAULT false,

  notes                 text,
  created_at            timestamptz DEFAULT now(),
  updated_at            timestamptz DEFAULT now(),

  CONSTRAINT property_analysis_pkey PRIMARY KEY (id),
  CONSTRAINT pa_address1_fkey FOREIGN KEY (address1)
    REFERENCES public.properties (address1) ON DELETE CASCADE,
  CONSTRAINT pa_loan_fkey FOREIGN KEY (loan_id)
    REFERENCES public.loans (id),
  CONSTRAINT pa_assumption_set_fkey FOREIGN KEY (assumption_set_id)
    REFERENCES public.assumption_sets (id),
  -- one analysis per deal type per property
  CONSTRAINT pa_address1_deal_type_uk UNIQUE (address1, deal_type)
);


-- ltr_analysis_inputs: behavioral overrides for LTR calculations.
-- Rent estimates live in the units table; these are flags only.
CREATE TABLE public.ltr_analysis_inputs (
  id                          bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  property_analysis_id        uuid NOT NULL,

  -- house hacking flags
  live_in_unit                boolean NOT NULL DEFAULT false,
  include_room_rental         boolean NOT NULL DEFAULT false,
  -- null = calculate from min_rent unit's bed count (v1 behavior)
  room_rental_income_override decimal(10,2),

  -- management
  mgmt_self_managed           boolean NOT NULL DEFAULT true,

  -- rent override: null = roll up from units table
  rent_override_total         decimal(10,2),

  created_at                  timestamptz DEFAULT now(),

  CONSTRAINT ltr_inputs_pkey UNIQUE (property_analysis_id),
  CONSTRAINT ltr_inputs_pa_fkey FOREIGN KEY (property_analysis_id)
    REFERENCES public.property_analysis (id) ON DELETE CASCADE
);


-- flip_analysis_inputs: deal-specific inputs for fix & flip.
CREATE TABLE public.flip_analysis_inputs (
  id                            bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  property_analysis_id          uuid NOT NULL,

  arv_estimate                  bigint,          -- after-repair value
  rehab_estimate                bigint,          -- base rehab cost estimate
  holding_period_months         integer,

  -- null = use arv_estimate as expected sale price
  sale_price_override           bigint,

  -- null = use assumption_set.rehab_contingency_pct
  rehab_contingency_override    double precision,

  contractor_type               public.contractor_type NOT NULL DEFAULT 'GC',

  -- hard money financing detail
  uses_hard_money               boolean NOT NULL DEFAULT false,
  -- null if uses_hard_money = false
  hard_money_points             double precision,

  created_at                    timestamptz DEFAULT now(),

  CONSTRAINT flip_inputs_pkey UNIQUE (property_analysis_id),
  CONSTRAINT flip_inputs_pa_fkey FOREIGN KEY (property_analysis_id)
    REFERENCES public.property_analysis (id) ON DELETE CASCADE
);


-- ================================================================
-- CALCULATION CACHE
-- ================================================================
--
-- One row per property_analysis. All computed outputs live here.
-- Controllers read this table only — they never call the calculator.
-- The CalculationEngine (Spring service) writes this; controllers don't.
--
-- Layout:
--   Section 1: Cache control + input snapshots
--   Section 2: Shared acquisition costs (LTR + flip)
--   Section 3: LTR results — NULL for flip rows
--   Section 4: Flip results — NULL for LTR rows
--   Section 5: Deal scores (both deal types, different sub-scores)
--
-- is_stale = true means the CalculationEngine hasn't run yet or
-- an input changed (price, loan, assumption) since last run.
-- ================================================================

CREATE TABLE public.calculation_cache (
  id                      uuid    NOT NULL DEFAULT gen_random_uuid(),
  property_analysis_id    uuid    NOT NULL,

  -- ──────────────────────────────────────────────────────────────
  -- SECTION 1: Cache control
  -- ──────────────────────────────────────────────────────────────
  is_stale                boolean NOT NULL DEFAULT true,
  stale_reason            text,
  calculated_at           timestamptz,
  -- Bump this integer in Spring config when calc logic changes.
  -- All rows with lower version are auto-marked stale + re-queued.
  calculation_version     integer NOT NULL DEFAULT 1,

  -- Snapshots of inputs used — lets you audit what produced a result
  -- even if loan/assumptions have since been edited.
  purchase_price_used     bigint,
  loan_snapshot           jsonb,       -- rate, term, down%, type at calc time
  assumption_snapshot     jsonb,       -- full assumption_set at calc time

  -- ──────────────────────────────────────────────────────────────
  -- SECTION 2: Shared acquisition costs (LTR + flip)
  -- ──────────────────────────────────────────────────────────────
  down_payment            double precision,
  loan_amount             double precision,
  monthly_mortgage        double precision,
  monthly_mip             double precision,     -- FHA only, else 0
  monthly_taxes           double precision,
  monthly_insurance       double precision,
  piti                    double precision,     -- principal+interest+tax+insurance
  cash_needed             double precision,     -- down + closing - discounts
  amortization_estimate   double precision,     -- monthly principal paydown estimate

  -- Closing costs total + pct
  closing_costs           double precision,
  closing_costs_prcnt     double precision,

  -- Closing cost line items (stored so detail page requires no recalc)
  -- Lender costs
  cc_loan_origination_fee    double precision,
  cc_processing_fee          double precision,
  cc_underwriting_fee        double precision,
  cc_credit_reporting_fee    double precision,
  cc_total_lender_costs      double precision,
  -- Title costs
  cc_tax_service_fee         double precision,
  cc_flood_certification_fee double precision,
  cc_appraisal_fee           double precision,
  cc_abstract_update_fee     double precision,
  cc_title_examination_fee   double precision,
  cc_title_guaranty_cert     double precision,
  cc_owners_title_insurance  double precision,
  cc_settlement_fee          double precision,
  cc_total_title_costs       double precision,
  -- Government costs
  cc_deed_recording_fee      double precision,
  cc_mortgage_recording_fee  double precision,
  cc_total_government_costs  double precision,
  -- Prepaids
  cc_prepaid_home_insurance  double precision,
  cc_property_tax_proration  double precision,
  cc_prepaid_interest        double precision,
  cc_total_prepaid_costs     double precision,
  -- Escrow
  cc_insurance_reserve       double precision,
  cc_tax_reserve             double precision,
  cc_aggregate_adjustment    double precision,
  cc_total_escrow_costs      double precision,
  -- Optional / miscellaneous
  cc_home_inspection_fee     double precision,
  cc_property_survey_fee     double precision,
  cc_pest_inspection_fee     double precision,
  cc_structural_engineer_fee double precision,
  cc_sewer_inspection_fee    double precision,
  cc_keller_williams_fee     double precision,
  cc_courier_fees            double precision,
  cc_notary_fees             double precision,
  cc_total_optional_costs    double precision,

  -- IFA / second loan info (Iowa-specific)
  second_loan_type           text,       -- 'reduced_dp' | 'reduced_loan' | null

  -- ──────────────────────────────────────────────────────────────
  -- SECTION 3: LTR results (NULL for flip rows)
  -- ──────────────────────────────────────────────────────────────

  -- Quick estimate section (uses rule-of-thumb rent, not Rentcast data)
  quick_monthly_rent_estimate    double precision,
  qe_total_rent                  double precision,
  qe_monthly_vacancy_costs       double precision,
  qe_monthly_repair_costs        double precision,
  qe_monthly_capex_costs         double precision,
  qe_operating_expenses          double precision,
  qe_total_monthly_cost          double precision,
  qe_monthly_cash_flow           double precision,
  qe_annual_cash_flow            double precision,

  -- Utility breakdown (shared across quick + market rent sections)
  monthly_utility_electric       double precision,
  monthly_utility_gas            double precision,
  monthly_utility_water          double precision,
  monthly_utility_trash          double precision,
  monthly_utility_internet       double precision,
  monthly_utility_total          double precision,
  -- House hack utility splits
  roommate_utilities             double precision,   -- portion covered by roommate(s)
  owner_utilities                double precision,   -- owner's out-of-pocket utilities
  emergency_fund_3m              double precision,   -- 3-month PITI + utilities reserve

  -- Market rent section (uses actual rent_estimates from units table)
  -- y1 = owner lives in one unit (house hack Y1), y2 = fully rented
  gross_rent_monthly             double precision,   -- total from units table
  y1_opex_rent_base              double precision,   -- rent basis for Y1 expense calc
  y2_rent_base                   double precision,   -- rent basis for Y2+ (full occupancy)
  y2_rent_base_source            text,               -- 'whole_property' | 'room_sum'

  -- House hack income splits
  mr_net_rent_y1                 double precision,   -- rent net of owner unit
  mr_net_rent_y2                 double precision,   -- rent fully occupied
  mr_annual_rent_y1              double precision,
  mr_annual_rent_y2              double precision,

  -- Operating expenses (market rent basis, monthly)
  mr_monthly_vacancy_costs       double precision,
  mr_monthly_repair_costs        double precision,
  mr_monthly_capex_costs         double precision,
  mr_expense_property_tax        double precision,   -- same as monthly_taxes
  mr_expense_insurance           double precision,   -- same as monthly_insurance
  mr_operating_expenses          double precision,   -- sum of above 5 lines
  mr_total_monthly_cost          double precision,   -- opex + debt service + utilities

  -- NOI
  mr_monthly_noi_y1              double precision,
  mr_monthly_noi_y2              double precision,
  mr_annual_noi_y1               double precision,
  mr_annual_noi_y2               double precision,

  -- Cashflow projections
  mr_monthly_cash_flow_y1        double precision,
  mr_monthly_cash_flow_y2        double precision,
  mr_annual_cash_flow_y1         double precision,
  mr_annual_cash_flow_y2         double precision,
  -- House hack utility detail by year
  roommate_utilities_y1          double precision,
  roommate_utilities_y2          double precision,
  owner_utilities_y1             double precision,
  owner_utilities_y2             double precision,
  -- House hack net cost (what the owner actually pays out of pocket)
  owner_monthly_cost             double precision,

  -- Core investment ratios
  cap_rate_y1                    double precision,
  cap_rate_y2                    double precision,
  coc_y1                         double precision,   -- cash-on-cash Y1
  coc_y2                         double precision,   -- cash-on-cash Y2
  grm_y1                         double precision,   -- gross rent multiplier Y1
  grm_y2                         double precision,
  mgr_pp                         double precision,   -- monthly gross rent / purchase price (1% rule metric)
  opex_rent                      double precision,   -- operating expenses / rent (50% rule metric)
  dscr                           double precision,   -- debt service coverage ratio
  ltv_ratio                      double precision,
  price_per_door                 double precision,
  rent_per_sqft                  double precision,
  break_even_occupancy           double precision,   -- occupancy needed to cover all costs
  break_even_vacancy             double precision,   -- max vacancy before going negative
  oer                            double precision,   -- operating expense ratio
  egi                            double precision,   -- effective gross income (monthly)
  debt_yield                     double precision,   -- annual NOI / loan amount
  fha_self_sufficiency_ratio     double precision,   -- (y2_rent * 0.75) / piti — FHA MF test
  passes_one_pct                 boolean,            -- mgr_pp >= 0.01
  passes_fifty_pct               boolean,            -- opex_rent <= 0.50

  -- Mobility
  mobility_score                 double precision,   -- walk*0.6 + transit*0.3 + bike*0.1
  costs_to_income                double precision,   -- piti / after_tax_monthly_income

  -- Tax & depreciation
  monthly_depreciation           double precision,
  tax_savings_monthly            double precision,
  after_tax_cash_flow_y1         double precision,
  after_tax_cash_flow_y2         double precision,

  -- Long-term value projections
  future_value_5yr               double precision,
  future_value_10yr              double precision,
  future_value_20yr              double precision,
  net_proceeds_5yr               double precision,   -- after selling costs + cap gains
  net_proceeds_10yr              double precision,
  net_proceeds_20yr              double precision,

  -- Total return forecasts (cash flow + equity + appreciation)
  forecast_5yr                   double precision,
  forecast_10yr                  double precision,
  forecast_20yr                  double precision,

  -- Return metrics
  equity_multiple_5yr            double precision,
  equity_multiple_10yr           double precision,
  equity_multiple_20yr           double precision,
  avg_annual_return_5yr          double precision,
  avg_annual_return_10yr         double precision,
  avg_annual_return_20yr         double precision,
  roe_y2                         double precision,   -- return on equity year 2
  leverage_benefit               double precision,   -- CoC_y2 - cap_rate_y2
  payback_period_years           double precision,

  -- IRR
  irr_5yr                        double precision,
  irr_10yr                       double precision,
  irr_20yr                       double precision,

  -- NPV (discount rate from assumption_set)
  npv_5yr                        double precision,
  npv_10yr                       double precision,
  npv_20yr                       double precision,

  -- Fair value (purchase_price + NPV)
  fair_value_5yr                 double precision,
  fair_value_10yr                double precision,
  fair_value_20yr                double precision,

  -- Value gap % (NPV / cash_needed * 100)
  value_gap_pct_5yr              double precision,
  value_gap_pct_10yr             double precision,
  value_gap_pct_20yr             double precision,

  beats_market                   boolean,            -- npv_10yr > 0

  -- Downside scenario (10% rent reduction stress test)
  cash_flow_y1_downside_10pct    double precision,
  cash_flow_y2_downside_10pct    double precision,

  -- ──────────────────────────────────────────────────────────────
  -- SECTION 4: Flip results (NULL for LTR rows)
  -- ──────────────────────────────────────────────────────────────

  -- Cost basis build-up
  flip_purchase_closing_costs    double precision,
  flip_rehab_base                double precision,
  flip_rehab_contingency_amt     double precision,
  flip_rehab_total               double precision,   -- base + contingency
  flip_holding_costs_monthly     double precision,
  flip_holding_costs_total       double precision,   -- monthly * holding_period
  flip_hard_money_points_amt     double precision,   -- null if conventional
  flip_selling_costs_amt         double precision,   -- agent fees + transfer tax
  flip_total_cost_basis          double precision,   -- all costs combined

  -- Returns
  flip_arv_used                  double precision,
  flip_expected_sale_price       double precision,
  flip_gross_profit              double precision,   -- sale - total_cost_basis
  flip_tax_estimate              double precision,   -- short-term cap gains
  flip_net_profit                double precision,   -- gross - taxes
  flip_roi_pct                   double precision,
  flip_annualized_roi_pct        double precision,   -- accounts for hold period
  flip_equity_multiple           double precision,
  flip_profit_per_month          double precision,

  -- Deal rules
  flip_mao                       double precision,   -- max allowable offer = ARV*70% - rehab
  flip_passes_70_pct_rule        boolean,
  flip_arv_to_price_ratio        double precision,
  flip_ltv_ratio                 double precision,   -- loan / arv (lender underwrites on ARV)

  -- ──────────────────────────────────────────────────────────────
  -- SECTION 5: Deal scores (0–100 integers)
  -- LTR sub-scores: cashflow, location, condition, rent_upside, market
  -- Flip sub-scores: margin, rehab_risk, market_velocity
  -- score_overall applies to both
  -- ──────────────────────────────────────────────────────────────
  score_overall            integer,
  -- LTR scores
  score_cashflow           integer,
  score_location           integer,
  score_condition          integer,
  score_rent_upside        integer,
  score_market             integer,
  -- Flip scores
  score_margin             integer,
  score_rehab_risk         integer,
  score_market_velocity    integer,

  CONSTRAINT calculation_cache_pkey PRIMARY KEY (id),
  CONSTRAINT cc_property_analysis_fkey FOREIGN KEY (property_analysis_id)
    REFERENCES public.property_analysis (id) ON DELETE CASCADE,
  CONSTRAINT cc_property_analysis_uk UNIQUE (property_analysis_id)
);


-- ================================================================
-- INDEXES
-- ================================================================

-- properties
CREATE INDEX idx_properties_status        ON public.properties (status);
CREATE INDEX idx_properties_purchase_price ON public.properties (purchase_price);
CREATE INDEX idx_properties_units         ON public.properties (units);

-- units
CREATE INDEX idx_units_address1           ON public.units (address1);

-- research_reports
CREATE INDEX idx_research_property_id     ON public.research_reports (property_id);
CREATE INDEX idx_research_created_at      ON public.research_reports (created_at DESC);
CREATE INDEX idx_research_status          ON public.research_reports (status);

-- property_analysis
CREATE INDEX idx_pa_address1              ON public.property_analysis (address1);
CREATE INDEX idx_pa_deal_type             ON public.property_analysis (deal_type);
CREATE INDEX idx_pa_status                ON public.property_analysis (status);
CREATE INDEX idx_pa_is_primary            ON public.property_analysis (is_primary);

-- calculation_cache — the most heavily queried table
CREATE INDEX idx_cc_is_stale              ON public.calculation_cache (is_stale);
CREATE INDEX idx_cc_score_overall         ON public.calculation_cache (score_overall DESC);
CREATE INDEX idx_cc_cap_rate_y2           ON public.calculation_cache (cap_rate_y2 DESC);
CREATE INDEX idx_cc_cash_flow_y2          ON public.calculation_cache (mr_monthly_cash_flow_y2 DESC);
CREATE INDEX idx_cc_dscr                  ON public.calculation_cache (dscr DESC);
CREATE INDEX idx_cc_cash_needed           ON public.calculation_cache (cash_needed ASC);
CREATE INDEX idx_cc_coc_y2                ON public.calculation_cache (coc_y2 DESC);
CREATE INDEX idx_cc_flip_net_profit       ON public.calculation_cache (flip_net_profit DESC);
CREATE INDEX idx_cc_flip_roi              ON public.calculation_cache (flip_roi_pct DESC);

-- rent/sale comps
CREATE INDEX idx_rent_comps_address1      ON public.rent_comps (address1);
CREATE INDEX idx_sale_comps_address1      ON public.sale_comps (address1);

-- filter rules
CREATE INDEX idx_filter_rules_set_id      ON public.filter_rules (filter_set_id);

-- neighborhood
CREATE INDEX idx_pn_address1              ON public.property_neighborhood (address1);
CREATE INDEX idx_pn_neighborhood_id       ON public.property_neighborhood (neighborhood_id);


-- ================================================================
-- SEED DATA: Default assumption sets and filter sets
-- ================================================================

INSERT INTO public.assumption_sets (
  segment, description, is_default,
  appreciation_rate, closing_costs_rate, federal_tax_rate, state_tax_code,
  land_value_prcnt, rent_appreciation_rate, property_tax_rate,
  home_insurance_rate, vacancy_rate, repair_savings_rate, capex_reserve_rate,
  discount_rate, selling_costs_rate, longterm_capital_gains_tax_rate,
  residential_depreciation_period_yrs, default_property_condition_score,
  gross_annual_income,
  utility_electric_base, utility_gas_base, utility_water_base,
  utility_trash_base, utility_internet_base, utility_baseline_sqft
) VALUES (
  'LTR', 'Default LTR — Des Moines', true,
  0.03, 0.03, 0.22, 'IA',
  0.20, 0.02, 0.0175,
  0.006, 0.08, 0.05, 0.05,
  0.07, 0.07, 0.15,
  27.5, 3,
  85000,
  136.00, 106.40, 49.00,
  18.00, 60.00, 1500
);

INSERT INTO public.assumption_sets (
  segment, description, is_default,
  appreciation_rate, closing_costs_rate, federal_tax_rate, state_tax_code,
  land_value_prcnt,
  rehab_contingency_pct, holding_cost_rate_monthly,
  flip_selling_costs_rate, shortterm_capital_gains_rate,
  min_roi_pct, min_profit_amt
) VALUES (
  'FLIP', 'Default Flip — Des Moines', true,
  0.03, 0.03, 0.22, 'IA',
  0.20,
  0.15, 0.01,
  0.07, 0.22,
  0.20, 25000
);

-- Default filter sets (replacing hard-coded PHASE0 / PHASE1 criteria)
INSERT INTO public.filter_sets (name, description, deal_type, is_default)
VALUES
  ('Quick Screen', 'Phase 0 equivalent — fast pass/fail on basic numbers', 'LONG_TERM_RENTAL', true),
  ('Full Qualification', 'Phase 1 equivalent — requires market rent data', 'LONG_TERM_RENTAL', false),
  ('Flip Quick Screen', 'Basic flip viability check', 'FIX_AND_FLIP', true);

-- Quick screen rules (mirrors PHASE0_CRITERIA from v1)
INSERT INTO public.filter_rules (filter_set_id, metric, operator, value_a)
SELECT id, 'cash_needed',          'LTE', 40000 FROM public.filter_sets WHERE name = 'Quick Screen'
UNION ALL
SELECT id, 'qe_monthly_cash_flow', 'GTE', -600  FROM public.filter_sets WHERE name = 'Quick Screen';

-- Full qualification rules (mirrors PHASE1_CRITERIA from v1)
INSERT INTO public.filter_rules (filter_set_id, metric, operator, value_a)
SELECT id, 'mgr_pp',                    'GT',  0.01  FROM public.filter_sets WHERE name = 'Full Qualification'
UNION ALL
SELECT id, 'opex_rent',                 'LT',  0.5   FROM public.filter_sets WHERE name = 'Full Qualification'
UNION ALL
SELECT id, 'dscr',                      'GT',  1.25  FROM public.filter_sets WHERE name = 'Full Qualification'
UNION ALL
SELECT id, 'mr_monthly_cash_flow_y1',   'GTE', -700  FROM public.filter_sets WHERE name = 'Full Qualification'
UNION ALL
SELECT id, 'npv_10yr',                  'GT',  0     FROM public.filter_sets WHERE name = 'Full Qualification';

-- Flip quick screen rules
INSERT INTO public.filter_rules (filter_set_id, metric, operator, value_a)
SELECT id, 'flip_net_profit',      'GTE', 25000 FROM public.filter_sets WHERE name = 'Flip Quick Screen'
UNION ALL
SELECT id, 'flip_roi_pct',         'GTE', 0.20  FROM public.filter_sets WHERE name = 'Flip Quick Screen'
UNION ALL
SELECT id, 'flip_passes_70_pct_rule', 'EQ', 1   FROM public.filter_sets WHERE name = 'Flip Quick Screen';