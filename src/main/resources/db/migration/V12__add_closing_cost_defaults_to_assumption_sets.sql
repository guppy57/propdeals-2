ALTER TABLE assumption_sets
  ADD COLUMN broker_fees                     double precision,
  ADD COLUMN home_inspection_sfh_fee         double precision,
  ADD COLUMN home_inspection_mf_base_fee     double precision,
  ADD COLUMN home_inspection_mf_per_unit_fee double precision,
  ADD COLUMN property_survey_fee             double precision,
  ADD COLUMN pest_inspection_fee             double precision,
  ADD COLUMN structural_engineering_fee      double precision,
  ADD COLUMN sewer_scope_fee                 double precision;

ALTER TABLE calculation_cache
  RENAME COLUMN cc_keller_williams_fee TO cc_broker_fees;
