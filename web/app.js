// Miracle Mornings Web Application Controller

const CHAKRAS = [
  {
    id: 1,
    name: "Root",
    sanskrit: "Muladhara",
    frequencyHz: 396,
    color: "#E53935",
    placement: "Base of the spine",
    focus: "Safety & Grounding",
    seedMantra: "LAM",
    pronunciation: "luhm or lung",
    element: "Earth",
    affirmation: "I am safe, grounded, and deeply protected by Mother Earth.",
    yFraction: 0.92
  },
  {
    id: 2,
    name: "Sacral",
    sanskrit: "Svadhisthana",
    frequencyHz: 417,
    color: "#FB8C00",
    placement: "Lower abdomen",
    focus: "Creativity & Emotions",
    seedMantra: "VAM",
    pronunciation: "vuhm or vung",
    element: "Water",
    affirmation: "I welcome pure creativity, passion, and emotional healing.",
    yFraction: 0.77
  },
  {
    id: 3,
    name: "Solar Plexus",
    sanskrit: "Manipura",
    frequencyHz: 528,
    color: "#FDD835",
    placement: "Upper abdomen",
    focus: "Confidence & Willpower",
    seedMantra: "RAM",
    pronunciation: "raaam",
    element: "Fire",
    affirmation: "I am radiant, powerful, and completely confident in my path.",
    yFraction: 0.62
  },
  {
    id: 4,
    name: "Heart",
    sanskrit: "Anahata",
    frequencyHz: 639,
    color: "#43A047",
    placement: "Center of the chest",
    focus: "Love & Compassion",
    seedMantra: "YAM",
    pronunciation: "yaaam",
    element: "Air",
    affirmation: "My heart is open to give and receive unconditional divine love.",
    yFraction: 0.46
  },
  {
    id: 5,
    name: "Throat",
    sanskrit: "Vishuddha",
    frequencyHz: 741,
    color: "#00ACC1",
    placement: "Throat",
    focus: "Communication & Truth",
    seedMantra: "HAM",
    pronunciation: "haaam",
    element: "Ether",
    affirmation: "I speak my highest sacred truth with clarity, love, and courage.",
    yFraction: 0.31
  },
  {
    id: 6,
    name: "Third Eye",
    sanskrit: "Ajna",
    frequencyHz: 852,
    color: "#3949AB",
    placement: "Between the eyebrows",
    focus: "Intuition & Insight",
    seedMantra: "OM",
    pronunciation: "aum",
    element: "Light",
    affirmation: "I trust my intuition and see through the lens of divine wisdom.",
    yFraction: 0.19
  },
  {
    id: 7,
    name: "Crown",
    sanskrit: "Sahasrara",
    frequencyHz: 963,
    color: "#8E24AA",
    placement: "Top of the head",
    focus: "Spiritual Connection",
    seedMantra: "AH",
    pronunciation: "silent OM / aaaah",
    element: "Cosmic Consciousness",
    affirmation: "I am one with Source Consciousness, eternally blessed and whole.",
    yFraction: 0.08
  }
];

const BOOKS = [
  {
    title: "The Secret",
    author: "Rhonda Byrne",
    tag: "Law of Attraction",
    quote: "Thoughts become things. What you focus on with intense feeling, you draw into your physical reality.",
    summary: "Explains how subconscious thoughts act as vibrational transmitters that attract congruent circumstances into reality."
  },
  {
    title: "Think and Grow Rich",
    author: "Napoleon Hill",
    tag: "Definiteness of Purpose",
    quote: "Whatever the mind can conceive and believe, it can achieve.",
    summary: "13 principles of personal achievement distilled from studying 500 self-made masters, anchored in burning desire and autosuggestion."
  },
  {
    title: "The Power of Now",
    author: "Eckhart Tolle",
    tag: "Pure Presence",
    quote: "Realize deeply that the present moment is all you have. Make the NOW the primary focus of your life.",
    summary: "Guide to spiritual enlightenment through transcendence of the egoic mind and anchoring into pure unconditioned awareness."
  },
  {
    title: "Ask and It Is Given",
    author: "Esther & Jerry Hicks",
    tag: "Vibrational Alignment",
    quote: "You are the creator of your own experience. Nothing enters your experience unless you invite it through thought.",
    summary: "Step-by-step emotional guidance scale techniques to elevate your vibration from fear/doubt into joy, love, and eager expectation."
  }
];

class MiracleMorningsApp {
  constructor() {
    this.activeTab = 'morning';
    this.morningStep = 0; // 0..4
    this.selectedChakraId = 4; // Heart
    this.activePlayingChakraId = null;
    this.isJourneyPlaying = false;
    this.journeyTimer = null;
    this.isBgmActive = false;

    // Ho'oponopono
    this.hooponoponoPlaying = false;
    this.hooponoponoSeconds = 300;
    this.hooponoponoInterval = null;
    this.hooponoponoMode = 'bgm_plus_voice'; // 'bgm_only' | 'bgm_plus_voice'
    this.activePhraseIndex = 0;

    // Water
    this.waterSips = parseInt(localStorage.getItem('mm_water_sips') || '0', 10);
    this.waterIntent = localStorage.getItem('mm_water_intent') || 'Abundance, peace, and vibrant physical vitality flow effortlessly into my life.';

    // Streaks & User
    this.streakDays = parseInt(localStorage.getItem('mm_streak_days') || '0', 10);
    this.completedDates = JSON.parse(localStorage.getItem('mm_completed_dates') || '[]');

    // Gratitude list
    this.gratitudeItems = JSON.parse(localStorage.getItem('mm_gratitude_items') || 'null') || [
      { id: 1, blessing: "The miracle of waking up healthy and breathing fresh prana.", done: true },
      { id: 2, blessing: "A warm and peaceful home that protects and comforts me.", done: true },
      { id: 3, blessing: "Clean pure drinking water that energizes every cell.", done: false },
      { id: 4, blessing: "Unlimited opportunities to learn, expand, and serve.", done: false },
      { id: 5, blessing: "Family, loving friends, and uplifting souls in my circle.", done: false },
      { id: 6, blessing: "Financial abundance flowing from expected & unexpected channels.", done: false },
      { id: 7, blessing: "The quiet pre-dawn hours for meditation and self-discovery.", done: false },
      { id: 8, blessing: "Forgiveness, divine mercy, and emotional lightness.", done: false },
      { id: 9, blessing: "The vibrant wisdom of books and ancient masters.", done: false },
      { id: 10, blessing: "Knowing that the Universe is orchestrating everything in my favor.", done: false }
    ];

    this.init();
  }

  init() {
    this.renderHeader();
    this.bindEvents();
    this.renderCurrentView();

    if (window.supabaseService) {
      window.supabaseService.onStatusChange((status, user) => {
        this.updateCloudBadge(status, user);
        if (status === 'authenticated' && user) {
          this.refreshFromStorage();
        }
      });
    }
  }

  refreshFromStorage() {
    this.streakDays = parseInt(localStorage.getItem('mm_streak_days') || '0', 10);
    this.completedDates = JSON.parse(localStorage.getItem('mm_completed_dates') || '[]');
    this.waterSips = parseInt(localStorage.getItem('mm_water_sips') || '0', 10);
    this.waterIntent = localStorage.getItem('mm_water_intent') || this.waterIntent;
    const savedGratitude = localStorage.getItem('mm_gratitude_items');
    if (savedGratitude) {
      try { this.gratitudeItems = JSON.parse(savedGratitude); } catch(e) {}
    }
    this.renderHeader();
    this.renderCurrentView();
  }

  updateCloudBadge(status, user) {
    const badge = document.getElementById('cloud-sync-badge');
    if (!badge) return;
    if (user && user.email) {
      const shortEmail = user.email.split('@')[0];
      badge.innerHTML = `🟢 ${shortEmail}`;
      badge.classList.add('connected');
      badge.title = `Supabase Sync Active (${user.email}). Tap to manage account.`;
    } else if (window.supabaseService && window.supabaseService.isConfigured()) {
      badge.innerHTML = `☁️ Sign In`;
      badge.classList.remove('connected');
      badge.title = `Supabase configured. Tap to sign in or create account.`;
    } else {
      badge.innerHTML = `☁️ Cloud Sync`;
      badge.classList.remove('connected');
      badge.title = `Connect Supabase for multi-device sync and cloud backup.`;
    }
  }

  renderHeader() {
    // Current timezone & date
    try {
      const now = new Date();
      const tz = Intl.DateTimeFormat().resolvedOptions().timeZone || 'Local';
      const dateStr = now.toLocaleDateString(undefined, { weekday: 'short', month: 'short', day: 'numeric' });
      document.getElementById('current-date-badge').innerHTML = `📅 ${dateStr}`;
      document.getElementById('tz-badge').innerHTML = `🌐 ${tz}`;
    } catch (e) {}

    document.getElementById('streak-count-badge').innerHTML = `🔥 ${this.streakDays} Day${this.streakDays === 1 ? '' : 's'}`;
  }

  bindEvents() {
    // Tabs
    document.querySelectorAll('.tab-btn').forEach(btn => {
      btn.addEventListener('click', (e) => {
        document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
        btn.classList.add('active');
        this.activeTab = btn.dataset.tab;
        this.stopAllAudios();
        this.renderCurrentView();
      });
    });

    // Streak badge click -> Open Streak Calendar
    document.getElementById('streak-count-badge').addEventListener('click', () => {
      this.openCalendarModal();
    });

    // Cloud Sync Badge click -> Open Supabase Modal
    const cloudBadge = document.getElementById('cloud-sync-badge');
    if (cloudBadge) {
      cloudBadge.addEventListener('click', () => {
        this.openSupabaseModal();
      });
    }

    const closeSyncBtn = document.getElementById('btn-close-sync-modal');
    if (closeSyncBtn) {
      closeSyncBtn.addEventListener('click', () => {
        this.closeSupabaseModal();
      });
    }

    const backdrop = document.getElementById('supabase-modal-backdrop');
    if (backdrop) {
      backdrop.addEventListener('click', (e) => {
        if (e.target === backdrop) this.closeSupabaseModal();
      });
    }
  }

  renderCurrentView() {
    const mainArea = document.getElementById('main-content-area');
    if (this.activeTab === 'morning') {
      this.renderMorningView(mainArea);
    } else if (this.activeTab === 'night') {
      this.renderNightView(mainArea);
    } else if (this.activeTab === 'streaks') {
      this.renderStreaksView(mainArea);
    } else if (this.activeTab === 'books') {
      this.renderBooksView(mainArea);
    } else if (this.activeTab === 'pricing') {
      this.renderPricingView(mainArea);
    }
  }

  // --- MORNING STEPPED ROUTINE ---
  renderMorningView(container) {
    const stepNames = [
      "1. Brahma Muhurta",
      "2. 7 Chakras Cleansing",
      "3. Ho'oponopono Prayer",
      "4. Water Alchemy",
      "5. 10 Gratitude Practices"
    ];

    let html = `
      <div class="stepper-row">
        ${stepNames.map((name, idx) => `
          <div class="step-pill ${this.morningStep === idx ? 'active' : ''} ${this.morningStep > idx ? 'completed' : ''}" data-step="${idx}">
            ${idx + 1}
          </div>
        `).join('')}
      </div>
      <div id="morning-step-container"></div>
    `;

    container.innerHTML = html;

    // Attach step pill click handlers
    container.querySelectorAll('.step-pill').forEach(pill => {
      pill.addEventListener('click', () => {
        this.morningStep = parseInt(pill.dataset.step, 10);
        this.stopAllAudios();
        this.renderMorningView(container);
      });
    });

    const stepContainer = document.getElementById('morning-step-container');
    if (this.morningStep === 0) {
      this.renderBrahmaMuhurtaStep(stepContainer);
    } else if (this.morningStep === 1) {
      this.renderChakrasStep(stepContainer);
    } else if (this.morningStep === 2) {
      this.renderHooponoponoStep(stepContainer);
    } else if (this.morningStep === 3) {
      this.renderWaterStep(stepContainer);
    } else if (this.morningStep === 4) {
      this.renderGratitudeStep(stepContainer);
    }
  }

  // STEP 0: BRAHMA MUHURTA
  renderBrahmaMuhurtaStep(container) {
    container.innerHTML = `
      <div class="aura-card">
        <div class="card-header-row">
          <div>
            <h2 style="font-size:18px;font-weight:800;color:var(--forest-dark);">1. Brahma Muhurta Awakening</h2>
            <p style="font-size:12px;color:var(--text-muted);">The Creator's Sacred Pre-Dawn Window (~3:30 AM – 5:30 AM)</p>
          </div>
          <span class="step-meta-badge">~2 min</span>
        </div>
        <p style="font-size:13.5px;line-height:1.55;color:var(--text-forest);margin-bottom:12px;">
          Brahma Muhurta begins 1 hour and 36 minutes before sunrise. At this sacred hour, cosmic electromagnetic activity is tranquil, sattvic prana fills the atmosphere, and the pineal gland naturally releases melatonin and serotonin, opening your subconscious channel to effortless manifestation.
        </p>
        <div style="background:var(--sage-mist);padding:14px;border-radius:14px;border:1px solid var(--sage-border);margin-bottom:14px;">
          <h4 style="font-size:13px;font-weight:700;color:var(--forest-dark);margin-bottom:4px;">✨ Sacred Morning Wake-up Affirmation</h4>
          <p style="font-size:13px;font-style:italic;color:var(--text-muted);">"I awake with pure clarity, divine protection, and deep joy. Today is my masterpiece."</p>
        </div>
        <button class="btn-forest" id="btn-next-step">Continue to Aura Cleansing ➔</button>
      </div>
    `;

    container.querySelector('#btn-next-step').addEventListener('click', () => {
      this.morningStep = 1;
      this.renderMorningView(document.getElementById('main-content-area'));
    });
  }

  // STEP 1: 7 CHAKRAS AURA CLEANSING
  renderChakrasStep(container) {
    const current = CHAKRAS.find(c => c.id === this.selectedChakraId) || CHAKRAS[3];

    let html = `
      <!-- Placement Guide link -->
      <div class="anatomy-link-banner" id="btn-open-anatomy-guide">
        <div style="display:flex;align-items:center;gap:10px;">
          <div style="width:32px;height:32px;border-radius:50%;background:var(--forest-dark);color:#fff;display:flex;align-items:center;justify-content:center;font-size:16px;">☸</div>
          <div>
            <h4 style="font-size:13.5px;font-weight:700;color:var(--forest-dark);">Where are the Chakras placed?</h4>
            <p style="font-size:11.5px;color:var(--text-muted);">Tap to view full Anatomy, Placement & Seed Mantra Guide</p>
          </div>
        </div>
        <span style="font-weight:700;font-size:18px;color:var(--forest-dark);">›</span>
      </div>

      <!-- Meditating Body Visualizer Card -->
      <div class="aura-card" style="border-color:${current.color};">
        <div class="card-header-row">
          <div style="display:flex;align-items:center;gap:8px;">
            <div style="width:10px;height:10px;border-radius:50%;background:${current.color};"></div>
            <span style="font-size:13px;font-weight:700;color:var(--forest-dark);" id="chakra-status-label">
              ${this.isJourneyPlaying ? `Slow Journey: ${current.name} (${current.frequencyHz} Hz)` : this.activePlayingChakraId ? `Vibrating ${current.frequencyHz} Hz Frequency` : 'Meditating Body • 7 Chakras'}
            </span>
          </div>
          <button class="pill-badge" id="btn-toggle-bgm" style="background:${this.isBgmActive ? 'var(--lime-accent)' : 'var(--sage-mist)'};">
            🎵 BG Music: ${this.isBgmActive ? 'ON' : 'OFF'}
          </button>
        </div>

        <!-- Silhouette Container with Interactive Glowing Dots -->
        <div class="meditating-canvas-box" id="silhouette-container">
          <!-- Scaled SVG Silhouette -->
          <svg class="body-svg" viewBox="0 0 200 260" xmlns="http://www.w3.org/2000/svg">
            <path fill="rgba(20, 43, 30, 0.08)" d="M 100, 10 A 80, 80 0 0, 0 20, 90 C 20, 160 50, 240 100, 250 C 150, 240 180, 160 180, 90 A 80, 80 0 0, 0 100, 10 Z" />
            <path stroke="rgba(20, 43, 30, 0.25)" stroke-width="2" stroke-linecap="round" d="M 100, 28 L 100, 235" />
            <path fill="rgba(20, 43, 30, 0.22)" d="M 100, 26 A 16, 18 0 1, 0 100, 62 A 16, 18 0 1, 0 100, 26 Z" />
            <path fill="rgba(20, 43, 30, 0.22)" d="M 94, 60 C 94, 66 84, 72 65, 80 C 52, 85 48, 95 48, 110 L 46, 140 C 46, 155 42, 175 36, 195 C 33, 205 40, 212 50, 212 C 58, 212 65, 204 70, 192 L 78, 165 C 80, 160 84, 158 88, 162 C 94, 168 106, 168 112, 162 C 116, 158 120, 160 122, 165 L 130, 192 C 135, 204 142, 212 150, 212 C 160, 212 167, 205 164, 195 C 158, 175 154, 155 154, 140 L 152, 110 C 152, 95 148, 85 135, 80 C 116, 72 106, 66 106, 60 Z" />
            <path fill="rgba(20, 43, 30, 0.24)" d="M 72, 82 C 70, 100 74, 125 76, 145 C 78, 165 82, 182 86, 195 L 114, 195 C 118, 182 122, 165 124, 145 C 126, 125 130, 100 128, 82 Z" />
            <path fill="rgba(20, 43, 30, 0.22)" d="M 86, 192 C 70, 194 45, 198 28, 208 C 20, 214 20, 226 28, 230 C 42, 236 70, 238 100, 238 C 130, 238 158, 236 172, 230 C 180, 226 180, 214 172, 208 C 155, 198 130, 194 114, 192 Z" />
          </svg>

          <!-- 7 Glowing Interactive Chakra Nodes Along Spine -->
          ${CHAKRAS.map(c => `
            <div class="chakra-node-dot ${this.selectedChakraId === c.id ? 'active' : ''}" 
                 style="top:${c.yFraction * 100}%; left:50%; background:${c.color}; color:${c.color};"
                 data-id="${c.id}"
                 title="${c.name} Chakra (${c.frequencyHz} Hz)">
            </div>
          `).join('')}
        </div>

        <!-- Dynamic Selected Chakra Card -->
        <div style="background:var(--sage-mist);padding:14px;border-radius:14px;border:1px solid var(--sage-border);margin-bottom:12px;">
          <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:6px;">
            <div style="display:flex;align-items:center;gap:8px;">
              <span style="font-weight:800;font-size:16px;color:var(--forest-dark);">${current.id}. ${current.name} Chakra (${current.sanskrit})</span>
            </div>
            <span style="background:${current.color};color:#fff;padding:3px 9px;border-radius:8px;font-size:12px;font-weight:700;">${current.frequencyHz} Hz</span>
          </div>
          <p style="font-size:12px;color:var(--text-muted);margin-bottom:4px;"><strong>Placement:</strong> ${current.placement} • <strong>Spiritual Focus:</strong> ${current.focus}</p>
          <p style="font-size:12px;color:var(--text-muted);margin-bottom:8px;"><strong>Seed Mantra (Bija):</strong> ${current.seedMantra} (pronounced "${current.pronunciation}")</p>
          <p style="font-size:13px;font-style:italic;color:var(--forest-dark);">"${current.affirmation}"</p>
        </div>

        <!-- Play Individual Tone Button -->
        <button class="btn-forest" id="btn-play-selected-tone" style="margin-bottom:14px;">
          ${this.activePlayingChakraId === current.id ? `🔊 Vibrating ${current.frequencyHz} Hz Frequency...` : `▶ Play ${current.frequencyHz} Hz Frequency (${current.name})`}
        </button>

        <!-- Cumulative Slow Journey Banner -->
        <div class="journey-banner ${this.isJourneyPlaying ? 'playing' : ''}" id="btn-toggle-journey">
          <div style="display:flex;justify-content:space-between;align-items:center;">
            <div>
              <h4 style="font-size:14px;font-weight:800;">${this.isJourneyPlaying ? 'Slow Journey: Playing All 7 Chakras' : 'Play All 7 Chakra Frequencies (Slow + 432Hz BG)'}</h4>
              <p style="font-size:11.5px;opacity:0.85;">Ascends Root (396Hz) to Crown (963Hz) with continuous 432Hz meditative background</p>
            </div>
            <button class="btn-pill-play" style="pointer-events:none;">${this.isJourneyPlaying ? 'Pause' : 'Start'}</button>
          </div>
          <div class="journey-step-bar">
            ${[1,2,3,4,5,6,7].map(s => `
              <div class="journey-step-segment ${this.isJourneyPlaying && this.selectedChakraId === s ? 'current' : this.isJourneyPlaying && this.selectedChakraId > s ? 'past' : ''}"></div>
            `).join('')}
          </div>
        </div>
      </div>

      <!-- 7 Chakra Frequency Audios List -->
      <div class="aura-card">
        <div class="card-header-row">
          <div>
            <h3 style="font-size:16px;font-weight:800;color:var(--forest-dark);">7 Chakra Frequency Audios</h3>
            <p style="font-size:11.5px;color:var(--text-muted);">Generic Solfeggio frequency sounds (396Hz to 963Hz)</p>
          </div>
          <button class="pill-badge" id="btn-play-all-sequence">
            ▶ Play All
          </button>
        </div>

        <div class="chakra-audios-list">
          ${CHAKRAS.map(c => `
            <div class="chakra-audio-item ${this.selectedChakraId === c.id ? 'active' : ''}" data-id="${c.id}">
              <div style="display:flex;align-items:center;gap:12px;">
                <div style="width:30px;height:30px;border-radius:50%;background:${c.color};color:#fff;display:flex;align-items:center;justify-content:center;font-weight:800;font-size:13px;">${c.id}</div>
                <div>
                  <div style="display:flex;align-items:center;gap:6px;">
                    <span style="font-weight:700;font-size:13.5px;color:var(--forest-dark);">${c.name} Chakra</span>
                    <span style="font-size:11px;font-weight:700;background:${c.color}20;color:${c.color};padding:2px 6px;border-radius:6px;">${c.frequencyHz} Hz</span>
                  </div>
                  <span style="font-size:11.5px;color:var(--text-muted);">${c.placement} • ${c.focus}</span>
                </div>
              </div>
              <button class="btn-pill-play ${this.activePlayingChakraId === c.id ? 'playing' : ''}" data-id="${c.id}">
                ${this.activePlayingChakraId === c.id ? 'Playing' : 'Play'}
              </button>
            </div>
          `).join('')}
        </div>
      </div>

      <div class="nav-bottom-bar">
        <button class="btn-outline" id="btn-prev-step">← Previous</button>
        <button class="btn-forest" id="btn-next-step">Next: Ho'oponopono Prayer ➔</button>
      </div>
    `;

    container.innerHTML = html;

    // Attach listeners
    container.querySelector('#btn-open-anatomy-guide').addEventListener('click', () => {
      this.openAnatomyGuideModal();
    });

    container.querySelectorAll('.chakra-node-dot').forEach(dot => {
      dot.addEventListener('click', () => {
        this.selectedChakraId = parseInt(dot.dataset.id, 10);
        this.playChakra(this.selectedChakraId);
        this.renderChakrasStep(container);
      });
    });

    container.querySelectorAll('.chakra-audio-item').forEach(item => {
      item.addEventListener('click', (e) => {
        const id = parseInt(item.dataset.id, 10);
        this.selectedChakraId = id;
        this.playChakra(id);
        this.renderChakrasStep(container);
      });
    });

    container.querySelector('#btn-play-selected-tone').addEventListener('click', () => {
      this.playChakra(this.selectedChakraId);
      this.renderChakrasStep(container);
    });

    container.querySelector('#btn-toggle-bgm').addEventListener('click', () => {
      if (this.isBgmActive) {
        window.soundEngine.stop432HzAmbient();
        this.isBgmActive = false;
      } else {
        window.soundEngine.start432HzAmbient();
        this.isBgmActive = true;
      }
      this.renderChakrasStep(container);
    });

    container.querySelector('#btn-toggle-journey').addEventListener('click', () => {
      this.toggleSlowJourney();
      this.renderChakrasStep(container);
    });

    container.querySelector('#btn-play-all-sequence').addEventListener('click', () => {
      this.toggleAllSequence();
      this.renderChakrasStep(container);
    });

    container.querySelector('#btn-prev-step').addEventListener('click', () => {
      this.morningStep = 0;
      this.stopAllAudios();
      this.renderMorningView(document.getElementById('main-content-area'));
    });

    container.querySelector('#btn-next-step').addEventListener('click', () => {
      this.morningStep = 2;
      this.stopAllAudios();
      this.renderMorningView(document.getElementById('main-content-area'));
    });
  }

  playChakra(id) {
    const chakra = CHAKRAS.find(c => c.id === id);
    if (!chakra) return;
    this.activePlayingChakraId = id;
    window.soundEngine.playChakraSound(chakra.frequencyHz, 6.0, () => {
      if (this.activePlayingChakraId === id) {
        this.activePlayingChakraId = null;
        if (this.activeTab === 'morning' && this.morningStep === 1) {
          const stepContainer = document.getElementById('morning-step-container');
          if (stepContainer) this.renderChakrasStep(stepContainer);
        }
      }
    });
  }

  toggleSlowJourney() {
    if (this.isJourneyPlaying) {
      clearInterval(this.journeyTimer);
      this.isJourneyPlaying = false;
      this.activePlayingChakraId = null;
      window.soundEngine.stopAll();
      this.isBgmActive = false;
      return;
    }

    this.isJourneyPlaying = true;
    this.isBgmActive = true;
    window.soundEngine.start432HzAmbient();

    let step = 1;
    this.selectedChakraId = step;
    this.playChakra(step);

    this.journeyTimer = setInterval(() => {
      step++;
      if (step > 7) {
        clearInterval(this.journeyTimer);
        this.isJourneyPlaying = false;
        this.activePlayingChakraId = null;
        window.soundEngine.stopAll();
        this.isBgmActive = false;
        this.showToast("Chakra Journey Complete! Your aura is cleansed ✨");
      } else {
        this.selectedChakraId = step;
        this.playChakra(step);
      }
      if (this.activeTab === 'morning' && this.morningStep === 1) {
        const stepContainer = document.getElementById('morning-step-container');
        if (stepContainer) this.renderChakrasStep(stepContainer);
      }
    }, 7000);
  }

  toggleAllSequence() {
    this.toggleSlowJourney();
  }

  // STEP 2: HO'OPONOPONO PRAYER
  renderHooponoponoStep(container) {
    const phrases = [
      { text: "I am sorry.", meaning: "Cleanses past unintentional pain and unexpressed heavy memories." },
      { text: "Please forgive me.", meaning: "Releases subconscious guilt and grants unconditional mercy." },
      { text: "Thank you.", meaning: "Aligns your vibration with pure gratitude for life and lessons." },
      { text: "I love you.", meaning: "Returns your energy field to the zero state of Divine Source." }
    ];

    const mins = Math.floor(this.hooponoponoSeconds / 60);
    const secs = this.hooponoponoSeconds % 60;
    const timeStr = `${String(mins).padStart(2, '0')}:${String(secs).padStart(2, '0')}`;

    container.innerHTML = `
      <div class="aura-card">
        <div class="card-header-row">
          <div>
            <h2 style="font-size:18px;font-weight:800;color:var(--forest-dark);">3. Ho'oponopono Sacred Prayer</h2>
            <p style="font-size:12px;color:var(--text-muted);">Ancient Hawaiian Subconscious Cleansing & Inner Child Healing</p>
          </div>
          <span class="step-meta-badge">5 min</span>
        </div>

        <div class="timer-dial-card">
          <p style="font-size:12px;text-transform:uppercase;letter-spacing:1px;opacity:0.85;">Mindful Meditation Timer</p>
          <div class="timer-countdown" id="hooponopono-timer-display">${timeStr}</div>
          <div style="display:flex;justify-content:center;gap:10px;">
            <button class="btn-forest" id="btn-toggle-hooponopono" style="background:var(--lime-accent);color:var(--forest-dark);width:auto;padding:10px 24px;">
              ${this.hooponoponoPlaying ? '⏸ Pause Prayer' : '▶ Begin 5-Min Prayer'}
            </button>
            <button class="btn-outline" id="btn-reset-hooponopono" style="color:#fff;border-color:rgba(255,255,255,0.4);padding:10px 16px;">
              ↺ Reset
            </button>
          </div>
        </div>

        <!-- 4 Sacred Phrases -->
        <div class="phrases-list">
          ${phrases.map((p, idx) => `
            <div class="phrase-card ${this.hooponoponoPlaying && this.activePhraseIndex === idx ? 'active' : ''}">
              <div class="phrase-number">${idx + 1}</div>
              <div>
                <h4 style="font-size:16px;font-weight:800;margin-bottom:2px;">${p.text}</h4>
                <p class="phrase-meaning" style="font-size:12px;color:var(--text-muted);">${p.meaning}</p>
              </div>
            </div>
          `).join('')}
        </div>

        <!-- Audio Mode Selector -->
        <div class="audio-mode-selector">
          <h4 style="font-size:13px;font-weight:700;color:var(--forest-dark);">432 Hz Sound Engine Audio Options:</h4>
          <div class="radio-mode-row ${this.hooponoponoMode === 'bgm_only' ? 'selected' : ''}" data-mode="bgm_only">
            <input type="radio" name="hooponopono-audio" ${this.hooponoponoMode === 'bgm_only' ? 'checked' : ''}>
            <div>
              <strong style="font-size:13px;color:var(--forest-dark);">Option 1: Only 432 Hz BG Music</strong>
              <p style="font-size:11.5px;color:var(--text-muted);">Pure continuous Pythagorean harmonic drone without voice</p>
            </div>
          </div>
          <div class="radio-mode-row ${this.hooponoponoMode === 'bgm_plus_voice' ? 'selected' : ''}" data-mode="bgm_plus_voice">
            <input type="radio" name="hooponopono-audio" ${this.hooponoponoMode === 'bgm_plus_voice' ? 'checked' : ''}>
            <div>
              <strong style="font-size:13px;color:var(--forest-dark);">Option 2: 432 Hz BGM + Mindful Voice Recitation</strong>
              <p style="font-size:11.5px;color:var(--text-muted);">Ambient soundscape with gentle female human voice repeating the 4 phrases</p>
            </div>
          </div>
        </div>
      </div>

      <div class="nav-bottom-bar">
        <button class="btn-outline" id="btn-prev-step">← Previous</button>
        <button class="btn-forest" id="btn-next-step">Next: Water Manifestation ➔</button>
      </div>
    `;

    // Listeners
    container.querySelector('#btn-toggle-hooponopono').addEventListener('click', () => {
      this.toggleHooponopono();
      this.renderHooponoponoStep(container);
    });

    container.querySelector('#btn-reset-hooponopono').addEventListener('click', () => {
      this.resetHooponopono();
      this.renderHooponoponoStep(container);
    });

    container.querySelectorAll('.radio-mode-row').forEach(row => {
      row.addEventListener('click', () => {
        this.hooponoponoMode = row.dataset.mode;
        if (this.hooponoponoPlaying) {
          this.applyHooponoponoAudio();
        }
        this.renderHooponoponoStep(container);
      });
    });

    container.querySelector('#btn-prev-step').addEventListener('click', () => {
      this.morningStep = 1;
      this.stopAllAudios();
      this.renderMorningView(document.getElementById('main-content-area'));
    });

    container.querySelector('#btn-next-step').addEventListener('click', () => {
      this.morningStep = 3;
      this.stopAllAudios();
      this.renderMorningView(document.getElementById('main-content-area'));
    });
  }

  toggleHooponopono() {
    if (this.hooponoponoPlaying) {
      this.pauseHooponopono();
    } else {
      this.startHooponopono();
    }
  }

  startHooponopono() {
    this.hooponoponoPlaying = true;
    this.applyHooponoponoAudio();

    this.hooponoponoInterval = setInterval(() => {
      if (this.hooponoponoSeconds > 0) {
        this.hooponoponoSeconds--;
        const display = document.getElementById('hooponopono-timer-display');
        if (display) {
          const mins = Math.floor(this.hooponoponoSeconds / 60);
          const secs = this.hooponoponoSeconds % 60;
          display.textContent = `${String(mins).padStart(2, '0')}:${String(secs).padStart(2, '0')}`;
        }
      } else {
        this.pauseHooponopono();
        this.showToast("Ho'oponopono prayer complete. Peace be with you ✨");
      }
    }, 1000);
  }

  applyHooponoponoAudio() {
    window.soundEngine.start432HzAmbient();
    if (this.hooponoponoMode === 'bgm_plus_voice') {
      window.soundEngine.startHooponoponoVoice((idx) => {
        this.activePhraseIndex = idx;
        const cards = document.querySelectorAll('.phrase-card');
        cards.forEach((c, i) => {
          if (i === idx) c.classList.add('active');
          else c.classList.remove('active');
        });
      });
    } else {
      window.soundEngine.stopHooponoponoVoice();
    }
  }

  pauseHooponopono() {
    this.hooponoponoPlaying = false;
    clearInterval(this.hooponoponoInterval);
    window.soundEngine.stopAll();
  }

  resetHooponopono() {
    this.pauseHooponopono();
    this.hooponoponoSeconds = 300;
  }

  // STEP 3: WATER MANIFESTATION
  renderWaterStep(container) {
    container.innerHTML = `
      <div class="aura-card">
        <div class="card-header-row">
          <div>
            <h2 style="font-size:18px;font-weight:800;color:var(--forest-dark);">4. Water Alchemy & Intention</h2>
            <p style="font-size:12px;color:var(--text-muted);">Dr. Masaru Emoto Water Crystal Programming</p>
          </div>
          <span class="step-meta-badge">~3 min</span>
        </div>

        <div class="water-cup-box">
          <div style="font-size:42px;margin-bottom:8px;">🥛</div>
          <h3 style="font-size:16px;font-weight:800;color:var(--forest-dark);margin-bottom:4px;">Sacred Sips Taken: ${this.waterSips} / 8</h3>
          <p style="font-size:12px;color:var(--text-muted);">Hold your glass with both hands. Infuse each sip with gratitude.</p>

          <div class="sip-dots-row">
            ${[1,2,3,4,5,6,7,8].map(sip => `
              <div class="sip-dot ${this.waterSips >= sip ? 'filled' : ''}">${sip}</div>
            `).join('')}
          </div>

          <div style="display:flex;justify-content:center;gap:10px;">
            <button class="btn-forest" id="btn-take-sip" style="background:var(--soft-blue);width:auto;padding:10px 20px;">
              💧 Take Sacred Sip
            </button>
            <button class="btn-outline" id="btn-reset-sips" style="padding:10px 14px;">↺ Reset</button>
          </div>
        </div>

        <div style="margin-top:14px;">
          <label style="font-size:13px;font-weight:700;color:var(--forest-dark);display:block;margin-bottom:6px;">Your Water Intention Script:</label>
          <textarea id="water-intent-input" style="width:100%;height:75px;border-radius:12px;border:1.5px solid var(--sage-border);padding:10px;font-size:13px;background:var(--sage-mist);color:var(--forest-dark);">${this.waterIntent}</textarea>
        </div>
      </div>

      <div class="nav-bottom-bar">
        <button class="btn-outline" id="btn-prev-step">← Previous</button>
        <button class="btn-forest" id="btn-next-step">Next: 10 Gratitude Practices ➔</button>
      </div>
    `;

    container.querySelector('#btn-take-sip').addEventListener('click', () => {
      if (this.waterSips < 8) this.waterSips++;
      localStorage.setItem('mm_water_sips', this.waterSips);
      if (window.supabaseService) {
        window.supabaseService.syncWater(this.waterIntent, this.waterSips);
        window.supabaseService.syncUpProfile(this.streakDays, this.completedDates, this.waterIntent, this.waterSips);
      }
      this.renderWaterStep(container);
    });

    container.querySelector('#btn-reset-sips').addEventListener('click', () => {
      this.waterSips = 0;
      localStorage.setItem('mm_water_sips', 0);
      if (window.supabaseService) {
        window.supabaseService.syncWater(this.waterIntent, this.waterSips);
        window.supabaseService.syncUpProfile(this.streakDays, this.completedDates, this.waterIntent, this.waterSips);
      }
      this.renderWaterStep(container);
    });

    container.querySelector('#water-intent-input').addEventListener('change', (e) => {
      this.waterIntent = e.target.value;
      localStorage.setItem('mm_water_intent', this.waterIntent);
      if (window.supabaseService) {
        window.supabaseService.syncWater(this.waterIntent, this.waterSips);
        window.supabaseService.syncUpProfile(this.streakDays, this.completedDates, this.waterIntent, this.waterSips);
      }
    });

    container.querySelector('#btn-prev-step').addEventListener('click', () => {
      this.morningStep = 2;
      this.renderMorningView(document.getElementById('main-content-area'));
    });

    container.querySelector('#btn-next-step').addEventListener('click', () => {
      this.morningStep = 4;
      this.renderMorningView(document.getElementById('main-content-area'));
    });
  }

  // STEP 4: 10 GRATITUDE PRACTICES
  renderGratitudeStep(container) {
    const completedCount = this.gratitudeItems.filter(g => g.done).length;

    container.innerHTML = `
      <div class="aura-card">
        <div class="card-header-row">
          <div>
            <h2 style="font-size:18px;font-weight:800;color:var(--forest-dark);">5. 10 Sacred Gratitudes</h2>
            <p style="font-size:12px;color:var(--text-muted);">${completedCount} of 10 Blessings Counted</p>
          </div>
          <button class="pill-badge" id="btn-add-blessing">+ Add Blessing</button>
        </div>

        <div class="gratitude-list">
          ${this.gratitudeItems.map(item => `
            <div class="gratitude-item ${item.done ? 'completed' : ''}" data-id="${item.id}">
              <div class="gratitude-checkbox">${item.done ? '✓' : ''}</div>
              <p style="font-size:13.5px;color:var(--forest-dark);flex:1;">${item.blessing}</p>
            </div>
          `).join('')}
        </div>
      </div>

      <div class="nav-bottom-bar">
        <button class="btn-outline" id="btn-prev-step">← Previous</button>
        <button class="btn-forest" id="btn-complete-routine" style="background:#2E7D32;">
          ✨ Complete Routine (+1 Streak)
        </button>
      </div>
    `;

    container.querySelectorAll('.gratitude-item').forEach(item => {
      item.addEventListener('click', () => {
        const id = parseInt(item.dataset.id, 10);
        const target = this.gratitudeItems.find(g => g.id === id);
        if (target) {
          target.done = !target.done;
          localStorage.setItem('mm_gratitude_items', JSON.stringify(this.gratitudeItems));
          if (window.supabaseService) {
            window.supabaseService.syncGratitude(this.gratitudeItems);
          }
          this.renderGratitudeStep(container);
        }
      });
    });

    container.querySelector('#btn-add-blessing').addEventListener('click', () => {
      this.openAddBlessingModal();
    });

    container.querySelector('#btn-prev-step').addEventListener('click', () => {
      this.morningStep = 3;
      this.renderMorningView(document.getElementById('main-content-area'));
    });

    container.querySelector('#btn-complete-routine').addEventListener('click', () => {
      this.completeMorningRoutine();
    });
  }

  completeMorningRoutine() {
    this.streakDays++;
    const todayStr = new Date().toISOString().split('T')[0];
    if (!this.completedDates.includes(todayStr)) {
      this.completedDates.push(todayStr);
    }
    localStorage.setItem('mm_streak_days', this.streakDays);
    localStorage.setItem('mm_completed_dates', JSON.stringify(this.completedDates));

    if (window.supabaseService) {
      window.supabaseService.syncUpProfile(this.streakDays, this.completedDates, this.waterIntent, this.waterSips);
      window.supabaseService.logCompletedSession('morning');
    }

    this.renderHeader();
    this.showToast("Miracle Morning complete! Sacred streak updated ✨");
    setTimeout(() => {
      this.activeTab = 'streaks';
      document.querySelectorAll('.tab-btn').forEach(b => b.classList.toggle('active', b.dataset.tab === 'streaks'));
      this.renderCurrentView();
    }, 1200);
  }

  // --- NIGHT ROUTINE VIEW ---
  renderNightView(container) {
    const savedReflection = localStorage.getItem('mm_night_reflection') || '';
    container.innerHTML = `
      <div class="aura-card" style="background:#142B1E;color:#fff;border-color:var(--lime-accent);">
        <div class="card-header-row">
          <div>
            <h2 style="font-size:18px;font-weight:800;color:#fff;">Night Manifestation & Subconscious Imprinting</h2>
            <p style="font-size:12px;color:var(--lime-accent);">Theta Brainwave Bedtime Alignment</p>
          </div>
          <span class="step-meta-badge" style="background:rgba(255,255,255,0.15);color:#fff;">~5 min</span>
        </div>
        <p style="font-size:13.5px;line-height:1.6;opacity:0.9;margin-bottom:14px;">
          As you drift toward sleep, your brain transitions from Alpha to Theta frequencies. The subconscious mind accepts your final thoughts as absolute truth and works during deep delta sleep to materialize them.
        </p>

        <div style="background:rgba(255,255,255,0.08);padding:14px;border-radius:14px;border:1px solid rgba(255,255,255,0.2);margin-bottom:14px;">
          <label style="font-size:13px;font-weight:700;display:block;margin-bottom:6px;color:var(--lime-accent);">
            🌟 What was the single best blessing that happened today?
          </label>
          <textarea id="night-reflection-input" placeholder="Recall the moment that filled you with deepest gratitude..." style="width:100%;height:85px;border-radius:10px;padding:10px;background:rgba(255,255,255,0.12);color:#fff;border:1px solid rgba(255,255,255,0.3);font-size:13px;">${savedReflection}</textarea>
        </div>

        <button class="btn-forest" id="btn-save-night" style="background:var(--lime-accent);color:var(--forest-dark);">
          🌙 Save Night Reflection & Sleep Peacefully
        </button>
      </div>
    `;

    container.querySelector('#btn-save-night').addEventListener('click', () => {
      const val = container.querySelector('#night-reflection-input').value;
      localStorage.setItem('mm_night_reflection', val);
      if (window.supabaseService) {
        window.supabaseService.syncNightReflection(val);
        window.supabaseService.logCompletedSession('night');
      }
      this.showToast("Night reflection saved & synced to Supabase ✨");
    });
  }

  // --- STREAKS & CALENDAR VIEW ---
  renderStreaksView(container) {
    const now = new Date();
    const monthName = now.toLocaleString('default', { month: 'long', year: 'numeric' });
    const daysInMonth = new Date(now.getFullYear(), now.getMonth() + 1, 0).getDate();
    const firstDayIndex = new Date(now.getFullYear(), now.getMonth(), 1).getDay();

    let gridHtml = '';
    const dayLabels = ['S', 'M', 'T', 'W', 'T', 'F', 'S'];
    dayLabels.forEach(d => gridHtml += `<div class="calendar-day-header">${d}</div>`);

    for (let i = 0; i < firstDayIndex; i++) {
      gridHtml += `<div></div>`;
    }

    for (let day = 1; day <= daysInMonth; day++) {
      const dateKey = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
      const isDone = this.completedDates.includes(dateKey) || (day <= this.streakDays && this.streakDays > 0);
      gridHtml += `
        <div class="calendar-cell ${isDone ? 'completed' : ''}">
          <span>${day}</span>
          ${isDone ? '<span style="font-size:9px;">✓</span>' : ''}
        </div>
      `;
    }

    container.innerHTML = `
      <div class="aura-card">
        <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:12px;">
          <div>
            <h2 style="font-size:18px;font-weight:800;color:var(--forest-dark);">Your Manifestation Streaks</h2>
            <p style="font-size:12px;color:var(--text-muted);">Consistency shapes neuroplasticity and reality</p>
          </div>
          <span style="font-size:22px;font-weight:800;color:#E65100;">🔥 ${this.streakDays} Days</span>
        </div>

        <div style="display:flex;gap:10px;margin-bottom:16px;">
          <div style="flex:1;background:var(--sage-mist);padding:12px;border-radius:14px;text-align:center;">
            <div style="font-size:11px;color:var(--text-muted);">Current Streak</div>
            <div style="font-size:20px;font-weight:800;color:var(--forest-dark);">${this.streakDays} Days</div>
          </div>
          <div style="flex:1;background:var(--sage-mist);padding:12px;border-radius:14px;text-align:center;">
            <div style="font-size:11px;color:var(--text-muted);">Total Sessions</div>
            <div style="font-size:20px;font-weight:800;color:var(--forest-dark);">${this.completedDates.length || this.streakDays}</div>
          </div>
        </div>

        <h4 style="font-size:14px;font-weight:800;color:var(--forest-dark);">${monthName}</h4>
        <div class="calendar-grid">${gridHtml}</div>
      </div>
    `;
  }

  // --- BOOKS VIEW ---
  renderBooksView(container) {
    container.innerHTML = `
      <div class="aura-card">
        <h2 style="font-size:18px;font-weight:800;color:var(--forest-dark);margin-bottom:4px;">Sacred Wisdom & Manifestation Books</h2>
        <p style="font-size:12px;color:var(--text-muted);margin-bottom:16px;">Essential literature on the Law of Attraction, Vibration, and Subconscious Alchemy</p>

        <div style="display:flex;flex-direction:column;gap:12px;">
          ${BOOKS.map((b, idx) => `
            <div style="background:var(--sage-card-elevated);border:1px solid var(--sage-border);border-radius:16px;padding:14px;display:flex;flex-direction:column;gap:6px;">
              <div style="display:flex;justify-content:space-between;align-items:center;">
                <h3 style="font-size:15px;font-weight:800;color:var(--forest-dark);">${b.title}</h3>
                <span class="step-meta-badge">${b.tag}</span>
              </div>
              <p style="font-size:12px;color:var(--text-muted);">By ${b.author}</p>
              <p style="font-size:13px;font-style:italic;color:var(--forest-dark);background:var(--sage-mist);padding:8px 12px;border-radius:8px;">"${b.quote}"</p>
              <p style="font-size:12px;color:var(--text-forest);">${b.summary}</p>
            </div>
          `).join('')}
        </div>
      </div>
    `;
  }

  // --- PRICING VIEW ---
  renderPricingView(container) {
    container.innerHTML = `
      <div class="aura-card">
        <h2 style="font-size:18px;font-weight:800;color:var(--forest-dark);margin-bottom:4px;">Miracle Mornings Membership</h2>
        <p style="font-size:12px;color:var(--text-muted);margin-bottom:16px;">Transform your mornings and align with infinite abundance</p>

        <div style="display:flex;flex-direction:column;gap:12px;">
          <div style="border:2px solid var(--forest-dark);background:var(--sage-mist);border-radius:18px;padding:16px;">
            <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:6px;">
              <span style="font-weight:800;font-size:16px;color:var(--forest-dark);">Annual Sacred Pass (Recommended)</span>
              <span style="background:var(--forest-dark);color:#fff;padding:4px 8px;border-radius:8px;font-size:11px;font-weight:700;">SAVE 60%</span>
            </div>
            <div style="font-size:24px;font-weight:800;color:var(--forest-dark);margin-bottom:6px;">$49.99 <span style="font-size:13px;font-weight:400;">/ year ($4.16/mo)</span></div>
            <ul style="font-size:12.5px;color:var(--text-forest);margin-left:18px;line-height:1.6;margin-bottom:12px;">
              <li>Unlimited 432 Hz Pythagorean audio engine</li>
              <li>All 7 Chakra Solfeggio soundscapes & singing bowls</li>
              <li>Ho'oponopono 5-min guided vocal meditation</li>
              <li>Full streaks tracking & cloud progress sync</li>
            </ul>
            <button class="btn-forest">Choose Annual Pass</button>
          </div>

          <div style="border:1px solid var(--sage-border);background:var(--sage-card-elevated);border-radius:18px;padding:16px;">
            <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:6px;">
              <span style="font-weight:700;font-size:15px;color:var(--forest-dark);">Monthly Explorer</span>
              <span style="font-size:18px;font-weight:800;color:var(--forest-dark);">$9.99 <span style="font-size:12px;font-weight:400;">/ mo</span></span>
            </div>
            <button class="btn-outline" style="width:100%;margin-top:8px;">Choose Monthly</button>
          </div>
        </div>
      </div>
    `;
  }

  // --- MODALS ---
  openAnatomyGuideModal() {
    const modal = document.createElement('div');
    modal.className = 'modal-overlay';
    modal.innerHTML = `
      <div class="modal-sheet">
        <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:12px;">
          <h2 style="font-size:18px;font-weight:800;color:var(--forest-dark);">The 7 Chakras Anatomy Guide</h2>
          <button style="border:none;background:var(--sage-mist);width:32px;height:32px;border-radius:50%;font-size:16px;cursor:pointer;" id="modal-close-btn">✕</button>
        </div>
        <p style="font-size:12.5px;color:var(--text-muted);margin-bottom:14px;">Body Placements, Seed Mantras & Solfeggio Frequencies</p>
        <div style="display:flex;flex-direction:column;gap:10px;">
          ${CHAKRAS.map(c => `
            <div style="background:var(--sage-mist);border-left:4px solid ${c.color};padding:12px;border-radius:10px;">
              <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:4px;">
                <span style="font-weight:800;font-size:14px;color:var(--forest-dark);">${c.id}. ${c.name} (${c.sanskrit})</span>
                <span style="background:${c.color};color:#fff;padding:2px 6px;border-radius:6px;font-size:11px;font-weight:700;">${c.frequencyHz} Hz</span>
              </div>
              <p style="font-size:12px;color:var(--text-muted);"><strong>Placement:</strong> ${c.placement}</p>
              <p style="font-size:12px;color:var(--text-muted);"><strong>Seed Mantra (Bija):</strong> ${c.seedMantra} (pronounced "${c.pronunciation}")</p>
              <p style="font-size:12px;color:var(--text-forest);margin-top:4px;"><em>"${c.affirmation}"</em></p>
            </div>
          `).join('')}
        </div>
      </div>
    `;

    document.body.appendChild(modal);
    modal.querySelector('#modal-close-btn').addEventListener('click', () => modal.remove());
    modal.addEventListener('click', (e) => { if (e.target === modal) modal.remove(); });
  }

  openCalendarModal() {
    const modal = document.createElement('div');
    modal.className = 'modal-overlay';
    const now = new Date();
    const monthName = now.toLocaleString('default', { month: 'long', year: 'numeric' });
    const daysInMonth = new Date(now.getFullYear(), now.getMonth() + 1, 0).getDate();
    const firstDayIndex = new Date(now.getFullYear(), now.getMonth(), 1).getDay();

    let gridHtml = '';
    const dayLabels = ['S', 'M', 'T', 'W', 'T', 'F', 'S'];
    dayLabels.forEach(d => gridHtml += `<div class="calendar-day-header">${d}</div>`);
    for (let i = 0; i < firstDayIndex; i++) gridHtml += `<div></div>`;
    for (let day = 1; day <= daysInMonth; day++) {
      const dateKey = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
      const isDone = this.completedDates.includes(dateKey) || (day <= this.streakDays && this.streakDays > 0);
      gridHtml += `
        <div class="calendar-cell ${isDone ? 'completed' : ''}">
          <span>${day}</span>
          ${isDone ? '<span style="font-size:9px;">✓</span>' : ''}
        </div>
      `;
    }

    modal.innerHTML = `
      <div class="modal-sheet">
        <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:12px;">
          <div>
            <h2 style="font-size:18px;font-weight:800;color:var(--forest-dark);">${monthName} Streak Calendar</h2>
            <p style="font-size:12px;color:var(--text-muted);">Current Streak: 🔥 ${this.streakDays} Day${this.streakDays === 1 ? '' : 's'}</p>
          </div>
          <button style="border:none;background:var(--sage-mist);width:32px;height:32px;border-radius:50%;font-size:16px;cursor:pointer;" id="modal-close-btn">✕</button>
        </div>
        <div class="calendar-grid">${gridHtml}</div>
      </div>
    `;

    document.body.appendChild(modal);
    modal.querySelector('#modal-close-btn').addEventListener('click', () => modal.remove());
    modal.addEventListener('click', (e) => { if (e.target === modal) modal.remove(); });
  }

  openAddBlessingModal() {
    const modal = document.createElement('div');
    modal.className = 'modal-overlay';
    modal.innerHTML = `
      <div class="modal-sheet">
        <h3 style="font-size:16px;font-weight:800;color:var(--forest-dark);margin-bottom:10px;">Add New Blessing</h3>
        <input type="text" id="new-blessing-input" placeholder="What are you grateful for today?" style="width:100%;padding:12px;border-radius:12px;border:1.5px solid var(--sage-border);margin-bottom:12px;font-size:14px;">
        <div style="display:flex;gap:10px;">
          <button class="btn-outline" id="modal-cancel-btn" style="flex:1;">Cancel</button>
          <button class="btn-forest" id="modal-save-blessing-btn" style="flex:1;">Save Blessing</button>
        </div>
      </div>
    `;

    document.body.appendChild(modal);
    modal.querySelector('#modal-cancel-btn').addEventListener('click', () => modal.remove());
    modal.querySelector('#modal-save-blessing-btn').addEventListener('click', () => {
      const text = modal.querySelector('#new-blessing-input').value.trim();
      if (text) {
        this.gratitudeItems.push({ id: Date.now(), blessing: text, done: true });
        localStorage.setItem('mm_gratitude_items', JSON.stringify(this.gratitudeItems));
        modal.remove();
        if (this.activeTab === 'morning' && this.morningStep === 4) {
          this.renderGratitudeStep(document.getElementById('morning-step-container'));
        }
      }
    });
  }

  // --- SUPABASE CLOUD SYNC MODAL ---
  openSupabaseModal() {
    const backdrop = document.getElementById('supabase-modal-backdrop');
    if (!backdrop) return;
    this.supabaseModalTab = this.supabaseModalTab || 'auth'; // 'auth' | 'settings'
    this.supabaseAuthMode = this.supabaseAuthMode || 'login'; // 'login' | 'signup'
    backdrop.style.display = 'flex';
    this.renderSupabaseModalContent();
  }

  closeSupabaseModal() {
    const backdrop = document.getElementById('supabase-modal-backdrop');
    if (backdrop) backdrop.style.display = 'none';
  }

  renderSupabaseModalContent() {
    const container = document.getElementById('supabase-modal-content');
    if (!container) return;

    const sb = window.supabaseService;
    const isConfigured = sb && sb.isConfigured();
    const user = sb && sb.getCurrentUser();

    // If not configured, default tab to settings
    const activeTab = isConfigured ? this.supabaseModalTab : 'settings';

    let html = `
      <div class="auth-tabs-row">
        <button class="auth-tab-btn ${activeTab === 'auth' ? 'active' : ''}" id="modal-tab-auth">
          ${user ? '👤 Account' : '🔐 Sign In / Up'}
        </button>
        <button class="auth-tab-btn ${activeTab === 'settings' ? 'active' : ''}" id="modal-tab-settings">
          ⚙️ Project Settings
        </button>
      </div>
    `;

    if (activeTab === 'settings') {
      html += `
        <div style="font-size:13px;color:var(--text-muted);margin-bottom:12px;line-height:1.5;">
          Connect your free <a href="https://supabase.com" target="_blank" style="color:var(--forest-dark);font-weight:700;">Supabase</a> project for zero-loss cloud persistence across devices.
        </div>

        <div class="auth-input-group">
          <label>Project URL</label>
          <input type="url" id="sb-url-input" class="auth-input" placeholder="https://xyzcompany.supabase.co" value="${sb ? sb.url : ''}">
        </div>

        <div class="auth-input-group">
          <label>Anon Public Key</label>
          <input type="text" id="sb-key-input" class="auth-input" placeholder="eyJhbGciOiJIUzI1NiIsInR5c..." value="${sb ? sb.anonKey : ''}">
        </div>

        <div id="sb-settings-error" style="color:#D32F2F;font-size:12px;margin-bottom:10px;display:none;"></div>

        <div style="display:flex;gap:10px;margin-top:14px;">
          <button class="btn-forest" id="btn-save-sb-settings" style="flex:1;">
            💾 Save & Connect
          </button>
          ${isConfigured ? '<button class="btn-outline" id="btn-disconnect-sb" style="color:#D32F2F;border-color:#FFCDD2;">Disconnect</button>' : ''}
        </div>

        <div style="margin-top:14px;padding:10px;border-radius:10px;background:var(--sage-mist);font-size:11.5px;color:var(--text-muted);line-height:1.5;">
          💡 Run the schema from <code>supabase_schema.sql</code> in your Supabase SQL Editor to enable tables, indexes, and Row Level Security.
        </div>
      `;
    } else {
      // Auth / Profile Tab
      if (user) {
        // User logged in
        html += `
          <div style="background:var(--sage-card-elevated);border:1px solid var(--sage-border);border-radius:14px;padding:14px;margin-bottom:14px;">
            <div style="font-size:12px;color:var(--text-muted);margin-bottom:2px;">Logged in as</div>
            <div style="font-size:15px;font-weight:800;color:var(--forest-dark);word-break:break-all;">${user.email}</div>
            <div style="display:flex;gap:14px;margin-top:10px;font-size:12.5px;">
              <span>🔥 Streak: <strong>${this.streakDays} Days</strong></span>
              <span>📅 Completed: <strong>${this.completedDates.length} Days</strong></span>
            </div>
          </div>

          <div style="display:flex;gap:10px;">
            <button class="btn-forest" id="btn-sb-sync-now" style="flex:1;">
              🔄 Sync Now
            </button>
            <button class="btn-outline" id="btn-sb-signout" style="color:#D32F2F;">
              Sign Out
            </button>
          </div>
        `;
      } else {
        // User NOT logged in
        const isLogin = this.supabaseAuthMode === 'login';
        html += `
          <div style="display:flex;gap:8px;margin-bottom:14px;">
            <button class="pill-badge ${isLogin ? 'active' : ''}" id="btn-toggle-login" style="${isLogin ? 'background:var(--forest-dark);color:#fff;' : ''}">Sign In</button>
            <button class="pill-badge ${!isLogin ? 'active' : ''}" id="btn-toggle-signup" style="${!isLogin ? 'background:var(--forest-dark);color:#fff;' : ''}">Create Account</button>
          </div>

          <div class="auth-input-group">
            <label>Email Address</label>
            <input type="email" id="sb-auth-email" class="auth-input" placeholder="you@example.com">
          </div>

          <div class="auth-input-group">
            <label>Password</label>
            <input type="password" id="sb-auth-password" class="auth-input" placeholder="••••••••">
          </div>

          <div id="sb-auth-error" style="color:#D32F2F;font-size:12px;margin-bottom:10px;display:none;"></div>

          <button class="btn-forest" id="btn-sb-submit-auth">
            ${isLogin ? '🔐 Sign In & Sync' : '✨ Create Free Account'}
          </button>
        `;
      }
    }

    container.innerHTML = html;

    // Attach tab listeners
    const tabAuthBtn = container.querySelector('#modal-tab-auth');
    if (tabAuthBtn) {
      tabAuthBtn.addEventListener('click', () => {
        this.supabaseModalTab = 'auth';
        this.renderSupabaseModalContent();
      });
    }

    const tabSettingsBtn = container.querySelector('#modal-tab-settings');
    if (tabSettingsBtn) {
      tabSettingsBtn.addEventListener('click', () => {
        this.supabaseModalTab = 'settings';
        this.renderSupabaseModalContent();
      });
    }

    // Attach settings handlers
    const saveSettingsBtn = container.querySelector('#btn-save-sb-settings');
    if (saveSettingsBtn) {
      saveSettingsBtn.addEventListener('click', () => {
        const url = container.querySelector('#sb-url-input').value.trim();
        const key = container.querySelector('#sb-key-input').value.trim();
        const errEl = container.querySelector('#sb-settings-error');

        if (!url || !key) {
          errEl.textContent = 'Please enter both the Supabase URL and Anon Public Key.';
          errEl.style.display = 'block';
          return;
        }

        sb.configure(url, key);
        this.showToast('Supabase credentials saved ✨');
        this.supabaseModalTab = 'auth';
        this.renderSupabaseModalContent();
      });
    }

    const disconnectBtn = container.querySelector('#btn-disconnect-sb');
    if (disconnectBtn) {
      disconnectBtn.addEventListener('click', () => {
        if (confirm('Disconnect from Supabase? Your local data will remain safe.')) {
          sb.clearConfiguration();
          this.showToast('Disconnected from Supabase');
          this.supabaseModalTab = 'settings';
          this.renderSupabaseModalContent();
        }
      });
    }

    // Attach auth handlers
    const toggleLoginBtn = container.querySelector('#btn-toggle-login');
    if (toggleLoginBtn) {
      toggleLoginBtn.addEventListener('click', () => {
        this.supabaseAuthMode = 'login';
        this.renderSupabaseModalContent();
      });
    }

    const toggleSignupBtn = container.querySelector('#btn-toggle-signup');
    if (toggleSignupBtn) {
      toggleSignupBtn.addEventListener('click', () => {
        this.supabaseAuthMode = 'signup';
        this.renderSupabaseModalContent();
      });
    }

    const submitAuthBtn = container.querySelector('#btn-sb-submit-auth');
    if (submitAuthBtn) {
      submitAuthBtn.addEventListener('click', async () => {
        const email = container.querySelector('#sb-auth-email').value.trim();
        const pass = container.querySelector('#sb-auth-password').value.trim();
        const errEl = container.querySelector('#sb-auth-error');

        if (!email || !pass) {
          errEl.textContent = 'Please provide both email and password.';
          errEl.style.display = 'block';
          return;
        }

        errEl.style.display = 'none';
        submitAuthBtn.disabled = true;
        submitAuthBtn.textContent = 'Connecting...';

        try {
          if (this.supabaseAuthMode === 'signup') {
            await sb.signUp(email, pass);
            this.showToast('Account created! Please check your email or sign in ✨');
          } else {
            await sb.signIn(email, pass);
            this.showToast('Signed in successfully! Cloud synced ✨');
          }
          this.renderSupabaseModalContent();
        } catch (err) {
          errEl.textContent = err.message || 'Authentication failed. Please check credentials.';
          errEl.style.display = 'block';
          submitAuthBtn.disabled = false;
          submitAuthBtn.textContent = this.supabaseAuthMode === 'login' ? '🔐 Sign In & Sync' : '✨ Create Free Account';
        }
      });
    }

    const syncNowBtn = container.querySelector('#btn-sb-sync-now');
    if (syncNowBtn) {
      syncNowBtn.addEventListener('click', async () => {
        syncNowBtn.disabled = true;
        syncNowBtn.textContent = 'Syncing...';
        await sb.syncDown();
        await sb.syncUpProfile(this.streakDays, this.completedDates, this.waterIntent, this.waterSips);
        await sb.syncGratitude(this.gratitudeItems);
        this.refreshFromStorage();
        this.showToast('All sacred data synchronized with Supabase ☁️');
        syncNowBtn.disabled = false;
        syncNowBtn.textContent = '🔄 Sync Now';
        this.renderSupabaseModalContent();
      });
    }

    const signoutBtn = container.querySelector('#btn-sb-signout');
    if (signoutBtn) {
      signoutBtn.addEventListener('click', async () => {
        await sb.signOut();
        this.showToast('Signed out of Supabase');
        this.renderSupabaseModalContent();
      });
    }
  }

  showToast(msg) {
    const existing = document.querySelector('.toast-banner');
    if (existing) existing.remove();

    const toast = document.createElement('div');
    toast.className = 'toast-banner';
    toast.textContent = msg;
    document.body.appendChild(toast);
    setTimeout(() => toast.remove(), 3200);
  }

  stopAllAudios() {
    window.soundEngine.stopAll();
    this.activePlayingChakraId = null;
    this.isJourneyPlaying = false;
    this.isBgmActive = false;
    this.hooponoponoPlaying = false;
    clearInterval(this.journeyTimer);
    clearInterval(this.hooponoponoInterval);
  }
}

document.addEventListener('DOMContentLoaded', () => {
  window.app = new MiracleMorningsApp();
});
