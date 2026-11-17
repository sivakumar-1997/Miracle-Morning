# Miracle Mornings — Vercel Deployment & Supabase Integration Guide

This guide details how to deploy the **Miracle Mornings** web application to **Vercel** and connect it with **Supabase** for persistent cloud storage and multi-device authentication.

---

## 🚀 1. Deploying to Vercel

The application is pre-configured with a root `vercel.json` file specifying `"outputDirectory": "web"`.

### Option A: Via GitHub (Recommended)
1. Push this repository to your GitHub account.
2. Go to [vercel.com](https://vercel.com) and click **"Add New" > "Project"**.
3. Import your GitHub repository.
4. In the Project Configuration:
   - **Framework Preset:** `Other`
   - **Root Directory:** `./` (default)
   - **Output Directory:** `web` (automatically detected from `vercel.json`)
5. Click **Deploy**. Vercel will build and deploy the app instantly with an SSL certificate.

### Option B: Via Vercel CLI
```bash
npm install -g vercel
vercel
```

---

## ⚡ 2. Setting up Supabase

1. Go to [supabase.com](https://supabase.com) and create a new project.
2. In the Supabase Dashboard, click on **SQL Editor** in the left sidebar.
3. Open `supabase_schema.sql` from this repository, copy its entire contents, paste it into the SQL Editor, and click **Run**.
   - This sets up all tables: `profiles`, `gratitude_entries`, `water_intentions`, `night_reflections`, `completed_sessions`.
   - It automatically configures **Row Level Security (RLS)** policies so each user can strictly access only their own data.
   - It sets up automatic user profile creation on signup.
4. In Supabase, navigate to **Project Settings** > **API**:
   - Copy your **Project URL** (e.g. `https://xyzabc.supabase.co`).
   - Copy your **anon / public** API Key.

---

## ☁️ 3. Connecting Supabase in the App

You can connect Supabase in either of two seamless ways:

### Method 1: Directly in the Web App (Zero Configuration)
1. Open the deployed Vercel web app.
2. In the top right header, click **☁️ Cloud Sync**.
3. Select **⚙️ Project Settings**.
4. Paste your **Project URL** and **Anon Public Key**, then click **💾 Save & Connect**.
5. Switch to the **🔐 Sign In / Up** tab:
   - Enter your email and a password.
   - Click **Create Free Account** or **Sign In**.
6. Your 10 daily gratitudes, streaks, water intentions, and bedtime reflections are now securely synced to your private Supabase database across all devices!

### Method 2: Via Environment Variables (Optional)
If embedding Supabase credentials at build time in Vercel:
- In your Vercel Project Dashboard > **Settings** > **Environment Variables**:
  - `SUPABASE_URL`: `https://your-project.supabase.co`
  - `SUPABASE_ANON_KEY`: `your-anon-key`

---

## 🔒 Security & Offline Resilience
- **Offline-First:** All user interactions (gratitude checkboxes, water sips, bedtime notes) save instantly to `localStorage`. When Supabase is configured and online, it synchronizes in the background.
- **Row Level Security:** Only authenticated users can access their own rows (`auth.uid() = user_id`).
