CREATE TYPE public.house_hacking_unit_preference AS ENUM (
    'LEAST_RENT',
    'SMALLEST_SIZE',
    'MOST_RENT',
    'BIGGEST_SIZE'
);

CREATE TABLE public.user_settings (
    id                              uuid        PRIMARY KEY
                                                REFERENCES public.profiles(id)
                                                ON DELETE CASCADE,
    emergency_fund_monthly_amount   bigint      NOT NULL DEFAULT 1500,
    emergency_fund_months           integer     NOT NULL DEFAULT 3,
    house_hacking_unit_preference   public.house_hacking_unit_preference
                                                NOT NULL DEFAULT 'LEAST_RENT'
);

INSERT INTO public.user_settings (id)
SELECT id FROM public.profiles
ON CONFLICT DO NOTHING;

CREATE OR REPLACE FUNCTION public.handle_new_user()
RETURNS trigger
LANGUAGE plpgsql
SECURITY DEFINER SET search_path = ''
AS $$
BEGIN
    INSERT INTO public.profiles (id, email, full_name, avatar_url)
    VALUES (
        NEW.id,
        NEW.email,
        NEW.raw_user_meta_data ->> 'full_name',
        NEW.raw_user_meta_data ->> 'avatar_url'
    );

    INSERT INTO public.user_settings (id)
    VALUES (NEW.id);

    RETURN NEW;
END;
$$;
