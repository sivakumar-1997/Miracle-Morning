// Miracle Mornings — Supabase Cloud Integration Client
// Provides cloud persistence, real-time sync, and authentication with offline fallback.

class SupabaseService {
  constructor() {
    this.client = null;
    this.currentUser = null;
    this.status = 'idle'; // 'idle' | 'connected' | 'authenticated' | 'syncing' | 'error'
    this.statusListeners = [];
    this.syncListeners = [];

    // Check saved or runtime-injected public credentials (never accept secret keys in browser)
    const runtimeCfg = (typeof window !== 'undefined' && window.MM_SUPABASE_CONFIG) ? window.MM_SUPABASE_CONFIG : {};
    const rawUrl = localStorage.getItem('mm_supabase_url') || runtimeCfg.url || '';
    const rawKey = (localStorage.getItem('mm_supabase_key') || runtimeCfg.anonKey || '').trim();

    this.url = this.normalizeUrl(rawUrl);
    this.anonKey = rawKey.startsWith('sb_secret_') ? '' : rawKey;

    this.init();
  }

  normalizeUrl(raw) {
    let clean = (raw || '').trim().replace(/\/+$/, '');
    clean = clean.replace(/\/(rest|auth)\/v1$/i, '').replace(/\/+$/, '');
    return clean;
  }

  init() {
    if (this.url && this.anonKey && window.supabase && window.supabase.createClient) {
      try {
        this.client = window.supabase.createClient(this.url, this.anonKey, {
          auth: {
            persistSession: true,
            autoRefreshToken: true,
            detectSessionInUrl: true
          }
        });

        // Listen for auth state changes
        this.client.auth.onAuthStateChange(async (event, session) => {
          this.currentUser = session ? session.user : null;
          this.notifyStatus(this.currentUser ? 'authenticated' : 'connected');
          if (this.currentUser) {
            await this.syncDown();
          }
        });

        // Check active session
        this.client.auth.getSession().then(({ data: { session } }) => {
          this.currentUser = session ? session.user : null;
          this.notifyStatus(this.currentUser ? 'authenticated' : 'connected');
        }).catch(err => {
          console.warn('[Supabase] getSession failed:', err);
          this.notifyStatus('error', err.message);
        });

      } catch (err) {
        console.error('[Supabase] Client init failed:', err);
        this.notifyStatus('error', err.message);
      }
    } else {
      this.notifyStatus('idle');
    }
  }

  isConfigured() {
    return Boolean(this.url && this.anonKey);
  }

  isAuthenticated() {
    return Boolean(this.client && this.currentUser);
  }

  getCurrentUser() {
    return this.currentUser;
  }

  configure(url, anonKey) {
    this.url = this.normalizeUrl(url);
    this.anonKey = (anonKey || '').trim();
    localStorage.setItem('mm_supabase_url', this.url);
    localStorage.setItem('mm_supabase_key', this.anonKey);
    this.init();
  }

  clearConfiguration() {
    this.url = '';
    this.anonKey = '';
    this.client = null;
    this.currentUser = null;
    localStorage.removeItem('mm_supabase_url');
    localStorage.removeItem('mm_supabase_key');
    this.notifyStatus('idle');
  }

  onStatusChange(listener) {
    this.statusListeners.push(listener);
    listener(this.status, this.currentUser);
  }

  notifyStatus(status, detail) {
    this.status = status;
    this.statusListeners.forEach(fn => {
      try { fn(status, this.currentUser, detail); } catch (e) { console.error(e); }
    });
  }

  // --- AUTH METHODS ---

  async signUp(email, password) {
    if (!this.client) throw new Error('Supabase client is not configured.');
    const { data, error } = await this.client.auth.signUp({
      email,
      password
    });
    if (error) throw error;
    this.currentUser = data.user;
    this.notifyStatus(this.currentUser ? 'authenticated' : 'connected');
    return data;
  }

  async signIn(email, password) {
    if (!this.client) throw new Error('Supabase client is not configured.');
    const { data, error } = await this.client.auth.signInWithPassword({
      email,
      password
    });
    if (error) throw error;
    this.currentUser = data.user;
    this.notifyStatus('authenticated');
    await this.syncDown();
    return data;
  }

  async signOut() {
    if (!this.client) return;
    await this.client.auth.signOut();
    this.currentUser = null;
    this.notifyStatus('connected');
  }

  // --- SYNC METHODS ---

  getTodayString() {
    const now = new Date();
    const y = now.getFullYear();
    const m = String(now.getMonth() + 1).padStart(2, '0');
    const d = String(now.getDate()).padStart(2, '0');
    return `${y}-${m}-${d}`;
  }

  // Pull latest cloud data into local state & storage
  async syncDown() {
    if (!this.isAuthenticated()) return null;

    try {
      this.notifyStatus('syncing');
      const userId = this.currentUser.id;
      const todayStr = this.getTodayString();

      // 1. Fetch Profile
      const { data: profile } = await this.client
        .from('profiles')
        .select('*')
        .eq('id', userId)
        .maybeSingle();

      if (profile) {
        if (typeof profile.streak_days === 'number') {
          const localStreak = parseInt(localStorage.getItem('mm_streak_days') || '0', 10);
          const maxStreak = Math.max(localStreak, profile.streak_days);
          localStorage.setItem('mm_streak_days', maxStreak);
        }
        if (Array.isArray(profile.completed_dates)) {
          const localDates = JSON.parse(localStorage.getItem('mm_completed_dates') || '[]');
          const combined = Array.from(new Set([...localDates, ...profile.completed_dates]));
          localStorage.setItem('mm_completed_dates', JSON.stringify(combined));
        }
        if (profile.water_intent) {
          localStorage.setItem('mm_water_intent', profile.water_intent);
        }
        if (typeof profile.water_sips === 'number') {
          localStorage.setItem('mm_water_sips', profile.water_sips);
        }
      }

      // 2. Fetch Today's Gratitudes
      const { data: gratitudeRow } = await this.client
        .from('gratitude_entries')
        .select('*')
        .eq('user_id', userId)
        .eq('entry_date', todayStr)
        .maybeSingle();

      if (gratitudeRow && Array.isArray(gratitudeRow.items) && gratitudeRow.items.length > 0) {
        localStorage.setItem('mm_gratitude_items', JSON.stringify(gratitudeRow.items));
      }

      // 3. Fetch Today's Night Reflection
      const { data: nightRow } = await this.client
        .from('night_reflections')
        .select('*')
        .eq('user_id', userId)
        .eq('entry_date', todayStr)
        .maybeSingle();

      if (nightRow && nightRow.reflection) {
        localStorage.setItem('mm_night_reflection', nightRow.reflection);
      }

      this.notifyStatus('authenticated', 'Sync complete');
      return { profile, gratitudeRow, nightRow };
    } catch (err) {
      console.warn('[Supabase] syncDown error:', err);
      this.notifyStatus('authenticated', 'Offline / sync failed');
      return null;
    }
  }

  // Push local updates to Supabase
  async syncUpProfile(streakDays, completedDates, waterIntent, waterSips) {
    if (!this.isAuthenticated()) return;
    try {
      const userId = this.currentUser.id;
      await this.client
        .from('profiles')
        .upsert({
          id: userId,
          email: this.currentUser.email,
          streak_days: streakDays,
          completed_dates: completedDates,
          water_intent: waterIntent,
          water_sips: waterSips,
          updated_at: new Date().toISOString()
        }, { onConflict: 'id' });
    } catch (err) {
      console.warn('[Supabase] syncUpProfile error:', err);
    }
  }

  async syncGratitude(items) {
    if (!this.isAuthenticated()) return;
    try {
      const userId = this.currentUser.id;
      const todayStr = this.getTodayString();
      await this.client
        .from('gratitude_entries')
        .upsert({
          user_id: userId,
          entry_date: todayStr,
          items: items,
          updated_at: new Date().toISOString()
        }, { onConflict: 'user_id,entry_date' });
    } catch (err) {
      console.warn('[Supabase] syncGratitude error:', err);
    }
  }

  async syncWater(intent, sips) {
    if (!this.isAuthenticated()) return;
    try {
      const userId = this.currentUser.id;
      const todayStr = this.getTodayString();
      await this.client
        .from('water_intentions')
        .upsert({
          user_id: userId,
          entry_date: todayStr,
          intention: intent,
          sips: sips,
          updated_at: new Date().toISOString()
        }, { onConflict: 'user_id,entry_date' });
    } catch (err) {
      console.warn('[Supabase] syncWater error:', err);
    }
  }

  async syncNightReflection(reflection) {
    if (!this.isAuthenticated()) return;
    try {
      const userId = this.currentUser.id;
      const todayStr = this.getTodayString();
      await this.client
        .from('night_reflections')
        .upsert({
          user_id: userId,
          entry_date: todayStr,
          reflection: reflection,
          updated_at: new Date().toISOString()
        }, { onConflict: 'user_id,entry_date' });
    } catch (err) {
      console.warn('[Supabase] syncNightReflection error:', err);
    }
  }

  async logCompletedSession(sessionType) {
    if (!this.isAuthenticated()) return;
    try {
      const userId = this.currentUser.id;
      const todayStr = this.getTodayString();
      await this.client
        .from('completed_sessions')
        .insert({
          user_id: userId,
          session_type: sessionType,
          entry_date: todayStr,
          completed_at: new Date().toISOString()
        });
    } catch (err) {
      console.warn('[Supabase] logCompletedSession error:', err);
    }
  }
}

// Global instance attached to window
window.supabaseService = new SupabaseService();
