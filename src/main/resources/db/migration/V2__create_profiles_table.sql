CREATE TABLE public.profiles (
                                 id         uuid PRIMARY KEY REFERENCES auth.users(id) ON DELETE CASCADE,
                                 email      text,
                                 full_name  text,
                                 avatar_url text,
                                 created_at timestamptz DEFAULT now()
);

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
RETURN NEW;
END;
$$;

CREATE TRIGGER on_auth_user_created
    AFTER INSERT ON auth.users
    FOR EACH ROW EXECUTE FUNCTION public.handle_new_user();