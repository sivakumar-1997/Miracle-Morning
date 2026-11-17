-- ==============================================================================
-- Miracle Mornings — Supabase Database Schema & Row Level Security (RLS)
-- ==============================================================================
-- To set up your Supabase backend:
-- 1. Create a free project at https://supabase.com
-- 2. Navigate to SQL Editor in the Supabase Dashboard
-- 3. Paste and run this script
-- 4. Copy your Project URL & Anon Public Key from Project Settings > API
-- 5. Enter them into the "☁️ Cloud Sync" modal in the Miracle Mornings app
-- ==============================================================================

-- 1. PROFILES TABLE
create table if not exists public.profiles (
  id uuid primary key references auth.users(id) on delete cascade,
  email text,
  streak_days integer not null default 0,
  completed_dates jsonb not null default '[]'::jsonb,
  water_intent text default 'Abundance, peace, and vibrant physical vitality flow effortlessly into my life.',
  water_sips integer not null default 0,
  created_at timestamptz not null default timezone('utc'::text, now()),
  updated_at timestamptz not null default timezone('utc'::text, now())
);

-- 2. GRATITUDE ENTRIES TABLE (10 Daily Gratitudes)
create table if not exists public.gratitude_entries (
  id uuid primary key default gen_random_uuid(),
  user_id uuid not null references auth.users(id) on delete cascade,
  entry_date text not null, -- formatted as YYYY-MM-DD
  items jsonb not null default '[]'::jsonb,
  created_at timestamptz not null default timezone('utc'::text, now()),
  updated_at timestamptz not null default timezone('utc'::text, now()),
  constraint unique_user_gratitude_date unique (user_id, entry_date)
);

-- 3. WATER MANIFESTATION INTENTIONS & SIPS
create table if not exists public.water_intentions (
  id uuid primary key default gen_random_uuid(),
  user_id uuid not null references auth.users(id) on delete cascade,
  entry_date text not null, -- formatted as YYYY-MM-DD
  intention text not null default '',
  sips integer not null default 0,
  created_at timestamptz not null default timezone('utc'::text, now()),
  updated_at timestamptz not null default timezone('utc'::text, now()),
  constraint unique_user_water_date unique (user_id, entry_date)
);

-- 4. NIGHT REFLECTIONS TABLE
create table if not exists public.night_reflections (
  id uuid primary key default gen_random_uuid(),
  user_id uuid not null references auth.users(id) on delete cascade,
  entry_date text not null, -- formatted as YYYY-MM-DD
  reflection text not null default '',
  created_at timestamptz not null default timezone('utc'::text, now()),
  updated_at timestamptz not null default timezone('utc'::text, now()),
  constraint unique_user_night_date unique (user_id, entry_date)
);

-- 5. COMPLETED SESSIONS HISTORY LOG
create table if not exists public.completed_sessions (
  id uuid primary key default gen_random_uuid(),
  user_id uuid not null references auth.users(id) on delete cascade,
  session_type text not null, -- 'morning', 'night', 'chakra', 'hooponopono'
  entry_date text not null, -- formatted as YYYY-MM-DD
  completed_at timestamptz not null default timezone('utc'::text, now())
);

-- ==============================================================================
-- ROW LEVEL SECURITY (RLS) POLICIES
-- Ensures each user can strictly access and modify ONLY their own sacred data.
-- ==============================================================================

alter table public.profiles enable row level security;
alter table public.gratitude_entries enable row level security;
alter table public.water_intentions enable row level security;
alter table public.night_reflections enable row level security;
alter table public.completed_sessions enable row level security;

-- Profiles Policies
create policy "Users can view own profile"
  on public.profiles for select
  using (auth.uid() = id);

create policy "Users can update own profile"
  on public.profiles for update
  using (auth.uid() = id);

create policy "Users can insert own profile"
  on public.profiles for insert
  with check (auth.uid() = id);

-- Gratitude Entries Policies
create policy "Users can view own gratitude entries"
  on public.gratitude_entries for select
  using (auth.uid() = user_id);

create policy "Users can insert own gratitude entries"
  on public.gratitude_entries for insert
  with check (auth.uid() = user_id);

create policy "Users can update own gratitude entries"
  on public.gratitude_entries for update
  using (auth.uid() = user_id);

create policy "Users can delete own gratitude entries"
  on public.gratitude_entries for delete
  using (auth.uid() = user_id);

-- Water Intentions Policies
create policy "Users can view own water intentions"
  on public.water_intentions for select
  using (auth.uid() = user_id);

create policy "Users can insert own water intentions"
  on public.water_intentions for insert
  with check (auth.uid() = user_id);

create policy "Users can update own water intentions"
  on public.water_intentions for update
  using (auth.uid() = user_id);

-- Night Reflections Policies
create policy "Users can view own night reflections"
  on public.night_reflections for select
  using (auth.uid() = user_id);

create policy "Users can insert own night reflections"
  on public.night_reflections for insert
  with check (auth.uid() = user_id);

create policy "Users can update own night reflections"
  on public.night_reflections for update
  using (auth.uid() = user_id);

-- Completed Sessions Policies
create policy "Users can view own completed sessions"
  on public.completed_sessions for select
  using (auth.uid() = user_id);

create policy "Users can insert own completed sessions"
  on public.completed_sessions for insert
  with check (auth.uid() = user_id);

-- ==============================================================================
-- AUTOMATIC PROFILE CREATION TRIGGER
-- When a user registers through Supabase Auth, automatically create their profile.
-- ==============================================================================

create or replace function public.handle_new_user()
returns trigger as $$
begin
  insert into public.profiles (id, email, streak_days, completed_dates)
  values (new.id, new.email, 0, '[]'::jsonb)
  on conflict (id) do nothing;
  return new;
end;
$$ language plpgsql security definer;

-- Drop trigger if already exists to ensure idempotent re-runs
drop trigger if exists on_auth_user_created on auth.users;

create trigger on_auth_user_created
  after insert on auth.users
  for each row execute function public.handle_new_user();

-- Indexes for lightning fast lookups
create index if not exists idx_gratitude_user_date on public.gratitude_entries(user_id, entry_date);
create index if not exists idx_water_user_date on public.water_intentions(user_id, entry_date);
create index if not exists idx_night_user_date on public.night_reflections(user_id, entry_date);
create index if not exists idx_sessions_user_date on public.completed_sessions(user_id, entry_date);
