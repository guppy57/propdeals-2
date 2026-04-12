-- Seed: Des Moines neighborhoods
-- Run via psql against your Supabase database.
-- user_id is pulled from the first row in auth.users at execution time.
-- If you need a specific user, replace the subquery with a literal UUID:
--   '00000000-0000-0000-0000-000000000000'::uuid

DO $$
DECLARE
  v_user_id uuid;
BEGIN
  SELECT id INTO v_user_id FROM auth.users LIMIT 1;

  IF v_user_id IS NULL THEN
    RAISE EXCEPTION 'No users found in auth.users — log in via the app first, then re-run this seed.';
  END IF;

  INSERT INTO public.neighborhoods
    (id, name, letter_grade, niche_com_letter_grade, niche_com_mapped_name, city, state, country, created_at, updated_at, user_id)
  VALUES
    (1,  'drake',                          'B',  'A-',  'drake',                          'Des Moines', 'IA', 'USA', '2025-11-24 23:21:49.616083+00', '2025-11-24 23:21:49.616083+00', v_user_id),
    (2,  'greater south side',             null, 'B+',  'greater south side',             'Des Moines', 'IA', 'USA', '2025-11-24 23:21:49.966936+00', '2025-11-24 23:21:49.966936+00', v_user_id),
    (3,  'indianola hills',                'D',  'B-',  'indianola hills',                'Des Moines', 'IA', 'USA', '2025-11-24 23:21:50.230153+00', '2025-11-24 23:21:50.230153+00', v_user_id),
    (4,  'oak park',                       'B',  'B-',  'oak park',                       'Des Moines', 'IA', 'USA', '2025-11-24 23:21:50.583075+00', '2025-11-24 23:21:50.583075+00', v_user_id),
    (5,  'drake park',                     'C',  'B',   'drake park',                     'Des Moines', 'IA', 'USA', '2025-11-24 23:21:51.077195+00', '2025-11-24 23:21:51.077195+00', v_user_id),
    (6,  'grays woods',                    'B',  'C+',  'grays woods',                    'Des Moines', 'IA', 'USA', '2025-11-24 23:21:52.158836+00', '2025-11-24 23:21:52.158836+00', v_user_id),
    (7,  'river bend',                     'D',  'C+',  'river bend',                     'Des Moines', 'IA', 'USA', '2025-11-24 23:21:52.889184+00', '2025-11-24 23:21:52.889184+00', v_user_id),
    (8,  'martin luther king jr. park',    'C',  'B-',  'martin luther king jr. park',    'Des Moines', 'IA', 'USA', '2025-11-24 23:21:53.563739+00', '2025-11-24 23:21:53.563739+00', v_user_id),
    (9,  'carpenter',                      'C',  'C',   'carpenter',                      'Des Moines', 'IA', 'USA', '2025-11-24 23:21:53.901469+00', '2025-11-24 23:21:53.901469+00', v_user_id),
    (10, 'evelyn davis park',              'C',  'C+',  'evelyn davis park',              'Des Moines', 'IA', 'USA', '2025-11-24 23:21:55.027663+00', '2025-11-24 23:21:55.027663+00', v_user_id),
    (11, 'capitol park',                   'D',  'B-',  'capitol park',                   'Des Moines', 'IA', 'USA', '2025-11-24 23:21:55.388269+00', '2025-11-24 23:21:55.388269+00', v_user_id),
    (12, 'union park',                     'B',  'C+',  'union park',                     'Des Moines', 'IA', 'USA', '2025-11-24 23:21:55.769798+00', '2025-11-24 23:21:55.769798+00', v_user_id),
    (13, 'merle hay',                      'C',  'B+',  'merle hay',                      'Des Moines', 'IA', 'USA', '2025-11-24 23:21:57.439088+00', '2025-11-24 23:21:57.439088+00', v_user_id),
    (14, 'fairmont park',                  'D',  'B-',  'fairmont park',                  'Des Moines', 'IA', 'USA', '2025-11-24 23:21:58.361399+00', '2025-11-24 23:21:58.361399+00', v_user_id),
    (15, 'mondamin presidential',          'C',  'C+',  'mondamin presidential',          'Des Moines', 'IA', 'USA', '2025-11-24 23:21:58.589327+00', '2025-11-24 23:21:58.589327+00', v_user_id),
    (16, 'capitol east',                   'D',  'B',   'capitol east',                   'Des Moines', 'IA', 'USA', '2025-11-24 23:21:58.904871+00', '2025-11-24 23:21:58.904871+00', v_user_id),
    (17, 'sherman hill',                   null, 'B+',  'sherman hill',                   'Des Moines', 'IA', 'USA', '2025-11-24 23:21:59.436246+00', '2025-11-24 23:21:59.436246+00', v_user_id),
    (18, 'accent',                         'C',  'C+',  'accent',                         'Des Moines', 'IA', 'USA', '2025-11-24 23:22:00.16625+00',  '2025-11-24 23:22:00.16625+00',  v_user_id),
    (19, 'bloomfield/allen',               null, 'B-',  'bloomfield/allen',               'Des Moines', 'IA', 'USA', '2025-11-24 23:22:01.153545+00', '2025-11-24 23:22:01.153545+00', v_user_id),
    (20, 'fairground',                     'C',  'C+',  'fairground',                     'Des Moines', 'IA', 'USA', '2025-11-24 23:22:02.150089+00', '2025-11-24 23:22:02.150089+00', v_user_id),
    (21, 'magnolia park',                  null, 'C',   'magnolia park',                  'Des Moines', 'IA', 'USA', '2025-11-24 23:22:02.505715+00', '2025-11-24 23:22:02.505715+00', v_user_id),
    (22, 'beaverdale',                     'B',  'A-',  'beaverdale',                     'Des Moines', 'IA', 'USA', '2025-11-24 23:22:03.076041+00', '2025-11-24 23:22:03.076041+00', v_user_id),
    (23, 'chautauqua park',                null, 'A-',  'chautauqua park',                'Des Moines', 'IA', 'USA', '2025-11-24 23:22:03.823898+00', '2025-11-24 23:22:03.823898+00', v_user_id),
    (24, 'chesterfield',                   'B',  'C-',  'chesterfield',                   'Des Moines', 'IA', 'USA', '2025-11-24 23:22:05.879862+00', '2025-11-24 23:22:05.879862+00', v_user_id),
    (25, 'lower beaver',                   'B',  'B',   'lower beaver',                   'Des Moines', 'IA', 'USA', '2025-11-24 23:22:06.574411+00', '2025-11-24 23:22:06.574411+00', v_user_id),
    (26, 'woodland heights',               'B',  'A',   'woodland heights',               'Des Moines', 'IA', 'USA', '2025-11-24 23:22:07.329902+00', '2025-11-24 23:22:07.329902+00', v_user_id),
    (27, 'douglas acres',                  null, 'C',   'douglas acres',                  'Des Moines', 'IA', 'USA', '2025-11-24 23:22:07.859293+00', '2025-11-24 23:22:07.859293+00', v_user_id),
    (28, 'highland park',                  'D',  'B-',  'highland park',                  'Des Moines', 'IA', 'USA', '2025-11-24 23:22:09.405119+00', '2025-11-24 23:22:09.405119+00', v_user_id),
    (29, 'southwestern hills',             'B',  'B+',  'southwestern hills',             'Des Moines', 'IA', 'USA', '2025-11-24 23:22:10.168443+00', '2025-11-24 23:22:10.168443+00', v_user_id),
    (30, 'greenwood',                      null, 'A',   'greenwood',                      'Des Moines', 'IA', 'USA', '2025-11-24 23:22:10.505258+00', '2025-11-24 23:22:10.505258+00', v_user_id),
    (31, 'south park',                     null, 'C+',  'south park',                     'Des Moines', 'IA', 'USA', '2025-11-24 23:22:13.184095+00', '2025-11-24 23:22:13.184095+00', v_user_id),
    (32, 'meredith',                       null, 'B+',  'meredith',                       'Des Moines', 'IA', 'USA', '2025-11-24 23:22:16.184444+00', '2025-11-24 23:22:16.184444+00', v_user_id),
    (33, 'valley high manor',              null, 'B-',  'valley high manor',              'Des Moines', 'IA', 'USA', '2025-11-24 23:22:17.13934+00',  '2025-11-24 23:22:17.13934+00',  v_user_id),
    (34, 'north of grand',                 'C',  'A-',  'north of grand',                 'Des Moines', 'IA', 'USA', '2025-11-24 23:22:18.660176+00', '2025-11-24 23:22:18.660176+00', v_user_id),
    (35, 'capitol view south',             null, 'B+',  'capitol view south',             'Des Moines', 'IA', 'USA', '2025-11-24 23:22:19.006406+00', '2025-11-24 23:22:19.006406+00', v_user_id),
    (36, 'waveland woods',                 'B',  'A',   'waveland woods',                 'Des Moines', 'IA', 'USA', '2025-11-24 23:22:19.580308+00', '2025-11-24 23:22:19.580308+00', v_user_id),
    (37, 'jordan park',                    null, null,  null,                             'Des Moines', 'IA', 'USA', '2025-11-24 23:22:20.648791+00', '2025-11-24 23:22:20.648791+00', v_user_id),
    (38, 'little italy',                   'C',  null,  null,                             'Des Moines', 'IA', 'USA', '2025-11-24 23:22:21.642011+00', '2025-11-24 23:22:21.642011+00', v_user_id),
    (39, 'john barrow',                    null, null,  null,                             'Des Moines', 'IA', 'USA', '2025-11-25 02:22:49.603364+00', '2025-11-25 02:22:49.603364+00', v_user_id),
    (40, 'sheridan gardens',               'B',  null,  null,                             'Des Moines', 'IA', 'USA', '2025-11-25 02:38:35.609002+00', '2025-11-25 02:38:35.609002+00', v_user_id),
    (41, 'king irving',                    'C',  null,  null,                             'Des Moines', 'IA', 'USA', '2025-11-25 14:36:04.294342+00', '2025-11-25 14:36:04.294342+00', v_user_id),
    (43, 'historic east village',          'D',  'B+',  'east village',                   'Des Moines', 'IA', 'USA', '2025-11-25 18:16:01.980006+00', '2025-11-25 18:16:01.980006+00', v_user_id),
    (44, 'highland park / oak park',       'C',  'B-',  'highland park / oak park',       'Des Moines', 'IA', 'USA', '2025-11-25 18:20:58.92516+00',  '2025-11-25 18:20:58.92516+00',  v_user_id),
    (45, 'good park',                      'C',  null,  null,                             'Des Moines', 'IA', 'USA', '2025-11-25 18:21:59.465509+00', '2025-11-25 18:21:59.465509+00', v_user_id),
    (46, 'prospect park',                  'C',  null,  null,                             'Des Moines', 'IA', 'USA', '2025-11-25 18:22:52.839412+00', '2025-11-25 18:22:52.839412+00', v_user_id),
    (47, 'watrous heights',                'B',  null,  null,                             'Des Moines', 'IA', 'USA', '2025-11-25 18:23:15.330718+00', '2025-11-25 18:23:15.330718+00', v_user_id),
    (48, 'cheatom park',                   'D',  null,  null,                             'Des Moines', 'IA', 'USA', '2025-11-25 18:24:03.769104+00', '2025-11-25 18:24:03.769104+00', v_user_id),
    (49, 'east village',                   'D',  'B+',  'east village',                   'Des Moines', 'IA', 'USA', '2025-11-26 20:18:10.248573+00', '2025-11-26 20:18:10.248573+00', v_user_id),
    (50, 'grays lake',                     'B',  null,  null,                             'Des Moines', 'IA', 'USA', '2025-12-01 20:43:54.08719+00',  '2025-12-01 20:43:54.08719+00',  v_user_id),
    (52, 'mckinley school / columbus park','C',  'B-',  'mckinley school / columbus park','Des Moines', 'IA', 'USA', '2025-12-01 20:54:21.884274+00', '2025-12-01 20:54:21.884274+00', v_user_id),
    (53, 'watrous south',                  'C',  null,  null,                             'Des Moines', 'IA', 'USA', '2025-12-01 20:55:32.24975+00',  '2025-12-01 20:55:32.24975+00',  v_user_id),
    (55, 'easter lake area',               'C',  null,  null,                             'Des Moines', 'IA', 'USA', '2025-12-08 02:43:16.55338+00',  '2025-12-08 02:43:16.55338+00',  v_user_id),
    (56, 'salisbury oaks',                 'C',  'A',   'salisbury oaks',                 'Des Moines', 'IA', 'USA', '2025-12-15 21:55:52.019661+00', '2025-12-15 21:55:52.019661+00', v_user_id),
    (58, 'south central dsm',              'B',  null,  null,                             'Des Moines', 'IA', 'USA', '2025-12-17 17:17:43.116055+00', '2025-12-17 17:17:43.116055+00', v_user_id),
    (59, 'waterbury',                      'C',  'A+',  'waterbury',                      'Des Moines', 'IA', 'USA', '2025-12-17 17:28:59.922761+00', '2025-12-17 17:28:59.922761+00', v_user_id),
    (60, 'brook run',                      null, null,  null,                             'Des Moines', 'IA', 'USA', '2025-12-25 19:42:54.168939+00', '2025-12-25 19:42:54.168939+00', v_user_id),
    (61, 'westwood',                       null, null,  null,                             'Des Moines', 'IA', 'USA', '2026-01-22 00:47:27.386746+00', '2026-01-22 00:47:27.386746+00', v_user_id),
    (62, 'river woods',                    null, null,  null,                             'Des Moines', 'IA', 'USA', '2026-02-06 00:10:46.941137+00', '2026-02-06 00:10:46.941137+00', v_user_id),
    (63, 'waveland park',                  null, null,  null,                             'Des Moines', 'IA', 'USA', '2026-02-06 00:18:51.498076+00', '2026-02-06 00:18:51.498076+00', v_user_id)
  ON CONFLICT (id) DO NOTHING;

  -- Advance the identity sequence past the highest seeded id
  PERFORM setval(
    pg_get_serial_sequence('public.neighborhoods', 'id'),
    (SELECT MAX(id) FROM public.neighborhoods)
  );

END $$;
