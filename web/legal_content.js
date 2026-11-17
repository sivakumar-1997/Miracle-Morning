// Miracle Mornings — Privacy Policy & Terms and Conditions
// Mirrored 1:1 with Android's LegalDocuments.kt

(function () {
  const EFFECTIVE_DATE = "November 17, 2026";
  const CONTACT_EMAIL = "support@miraclemornings.app";

  const PRIVACY_SECTIONS = [
    {
      heading: "1. Overview",
      body: "Miracle Mornings ('we', 'our', or 'the App') respects your privacy and is designed as a peaceful daily mindfulness, sound-relaxation, and gratitude companion across Android and Web. This Privacy Policy explains what information is stored locally, what is synced if you opt into an account, and how your choices are protected."
    },
    {
      heading: "2. Data Stored Locally on Your Device",
      body: "By default, your daily morning and night progress—including your 28-day 'The Magic' lesson day, 10 daily gratitude blessings, 8-sip water intention & count, night reflections, streak calendar dates, and timezone formatting—is stored locally on your device using Android SharedPreferences (on Android) or browser localStorage (on Web)."
    },
    {
      heading: "3. Optional Account & Cloud Synchronization",
      body: "Signing in is optional. If you choose to create an account or sign in for multi-device synchronization, we collect and store your email address, encrypted authentication session tokens, and your manifestation records (streak count, completed dates, water intentions, gratitude blessings, night reflections, and session timestamps) in our Supabase cloud database solely to keep your Android and Web devices in sync."
    },
    {
      heading: "4. Permissions, Audio & Device Sensors",
      body: "Miracle Mornings follows a strict least-privilege approach. The app uses standard network access only for optional account sign-in and cloud sync. All 432 Hz ambient soundscapes, 7-chakra Solfeggio tones (396 Hz–963 Hz), and Ho'oponopono guidance are synthesized on-device or spoken via your browser/device Text-to-Speech engine. We do not access your camera, microphone, contacts, photo library, or precise GPS location."
    },
    {
      heading: "5. No Selling of Personal Data & No Third-Party Ads",
      body: "We never sell, rent, or trade your personal reflections, gratitude lists, or email address to advertisers or data brokers. The app contains no third-party behavioral ad trackers."
    },
    {
      heading: "6. Data Retention, Deletion & Your Rights",
      body: "You may use Miracle Mornings locally without creating an account, sign out at any time, or clear local app/browser storage to erase local records. If you have created a synced account and wish to export or permanently delete your account and cloud records, email us at " + CONTACT_EMAIL + "."
    },
    {
      heading: "7. Children's Privacy & Policy Updates",
      body: "Miracle Mornings is intended for users aged 13 and older and does not knowingly collect personal data from children under 13. We may update this Privacy Policy as features evolve; any changes will be reflected by updating the Effective Date above."
    }
  ];

  const TERMS_SECTIONS = [
    {
      heading: "1. Acceptance of Terms",
      body: "By downloading, accessing, or using the Miracle Mornings Android application or Web application, you agree to be bound by these Terms & Conditions and our Privacy Policy. If you do not agree, please discontinue use of the service."
    },
    {
      heading: "2. Wellness & Mindfulness Disclaimer",
      body: "Miracle Mornings offers guided mindfulness routines, gratitude journaling, water-intention reminders, and relaxing 432 Hz / Solfeggio sound frequencies for personal reflection and wellness support. Content is NOT medical, psychological, psychiatric, legal, or financial advice and is not intended to diagnose, treat, cure, or prevent any condition. Always consult a qualified professional for health concerns, and stop audio playback immediately if you feel any discomfort."
    },
    {
      heading: "3. User Accounts & Personal Entries",
      body: "If you register for optional cloud sync, you are responsible for keeping your login credentials confidential and for all activity under your account. You retain full ownership of the personal blessings, intentions, and night reflections you enter."
    },
    {
      heading: "4. Acceptable Use",
      body: "You agree to use the app strictly for lawful, personal mindfulness purposes and not to attempt unauthorized access, scraping, reverse engineering, disruption of backend sync services, or abuse of authentication endpoints."
    },
    {
      heading: "5. Intellectual Property & Book Attributions",
      body: "The Miracle Mornings interface, custom graphics, synthesized audio engine, and original software code are the property of Miracle Mornings. Educational summaries and lesson concepts referencing works such as 'The Magic', 'The Secret', 'The Hidden Messages in Water', and similar titles remain the intellectual property of their respective authors and publishers."
    },
    {
      heading: "6. Subscriptions & Billing",
      body: "Optional membership tiers or passes displayed within the app are subject to the pricing, renewal, and cancellation terms shown at checkout—including Google Play Billing terms when subscribed on Android. Trial or promotional availability may change over time."
    },
    {
      heading: "7. Limitation of Liability & Contact",
      body: "Miracle Mornings is provided on an 'as is' and 'as available' basis without warranties of any kind. To the fullest extent permitted by law, Miracle Mornings and its creators shall not be liable for indirect or consequential damages arising from app usage or service interruptions. Questions regarding these Terms may be sent to " + CONTACT_EMAIL + "."
    }
  ];

  window.MIRACLE_LEGAL = {
    effectiveDate: EFFECTIVE_DATE,
    contactEmail: CONTACT_EMAIL,
    privacySections: PRIVACY_SECTIONS,
    termsSections: TERMS_SECTIONS
  };

  window.renderLegalSectionsHtml = function (tab) {
    const isPrivacy = tab !== 'terms';
    const title = isPrivacy ? "Miracle Mornings Privacy Policy" : "Miracle Mornings Terms & Conditions";
    const sections = isPrivacy ? PRIVACY_SECTIONS : TERMS_SECTIONS;

    return `
      <div class="legal-section-card" style="border-color:var(--lime-accent);background:var(--sage-card-elevated);">
        <h4 style="font-size:15px;font-weight:800;color:var(--forest-dark);margin-bottom:4px;">${title}</h4>
        <p style="font-size:11.5px;color:var(--text-muted);">Effective Date: ${EFFECTIVE_DATE} • Contact: ${CONTACT_EMAIL}</p>
      </div>
      ${sections.map(s => `
        <div class="legal-section-card">
          <h4 style="font-size:13.5px;font-weight:800;color:var(--forest-dark);margin-bottom:5px;">${s.heading}</h4>
          <p style="font-size:12.5px;color:var(--text-muted);line-height:1.55;">${s.body}</p>
        </div>
      `).join('')}
    `;
  };
})();
