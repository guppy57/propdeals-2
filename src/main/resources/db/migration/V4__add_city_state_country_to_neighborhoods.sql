-- V4: Add city, state, country to neighborhoods
ALTER TABLE public.neighborhoods
    ADD COLUMN city    text,
    ADD COLUMN state   text,
    ADD COLUMN country text;
