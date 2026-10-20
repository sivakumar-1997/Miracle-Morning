package com.example.data.model

data class UserProfile(
  val name: String = "Sivakumar",
  val nickname: String = "Siva",
  val email: String = "user@auramagic.app",
  val currentMood: String = "Grateful & High Vibration",
  val primaryIntention: String = "Divine Abundance & Inner Peace",
  val wakeUpTime: String = "04:30 AM",
  val isBrahmaMuhurtaActive: Boolean = true,
  val streakDays: Int = 18,
  val auraLevel: String = "Luminous Bloom (Level 4)",
  val isLoggedIn: Boolean = true,
  val planTier: String = "Aura Magic Pro ($3.99/mo)"
)

data class GratitudeItem(
  val id: Int,
  val blessing: String,
  val reason: String,
  val isCompleted: Boolean = false
) {
  val fullMagicStatement: String
    get() = if (blessing.isNotBlank() && reason.isNotBlank()) {
      "I am truly blessed to have $blessing, because $reason. Thank you, thank you, thank you!"
    } else blessing
}

enum class MorningRoutineStep(
  val stepNumber: Int,
  val title: String,
  val tamilSubtitle: String,
  val durationMinutes: Int,
  val description: String,
  val iconName: String
) {
  BRAHMA_MUHURTA(
    1,
    "Brahma Muhurta Awakening",
    "பிரம்ம முகூர்த்த விழிப்பு",
    5,
    "Wake up between 4:30 AM - 5:00 AM during the cosmic hour of creation when the atmosphere is charged with pure sattva and pineal clarity.",
    "Alarm"
  ),
  AURA_CLEANSING(
    2,
    "Aura Cleansing Ritual",
    "ஆரா சுத்தி / பிராண சுத்தி",
    5,
    "Cleanse your etheric aura. Brush negative energy off your arms, chest, and head with gentle sweeping motions. Breathwork clears stagnant prana.",
    "AutoAwesome"
  ),
  SACRED_PRAYER(
    3,
    "Cosmic Prayer & Alignment",
    "பிரார்த்தனை மற்றும் சங்கல்பம்",
    5,
    "Surrender ego and connect with universal divine consciousness. Express reverent thanks for another day of life and abundance.",
    "SelfImprovement"
  ),
  WATER_MANIFESTATION(
    4,
    "Water Manifestation Alchemy",
    "நீர் சங்கல்பம் மற்றும் பிரசாதம்",
    7,
    "Place a clear glass of water before you. Cup it with both palms. Infuse your vibration of health, wealth, and gratitude into it, then sip slowly.",
    "WaterDrop"
  ),
  BRAIN_FREQUENCY_TUNING(
    5,
    "Frequency Tuning & Book Wisdom",
    "அதிர்வெண் இசைவு மற்றும் ஞானம்",
    8,
    "Harmonize brainwaves into Alpha/Theta state with 528Hz Solfeggio tones while absorbing positive notes and wisdom from manifestation masters.",
    "Headphones"
  ),
  TEN_GRATITUDE_PRACTICES(
    6,
    "Count 10 Blessings (The Magic)",
    "பத்து நன்றியுணர்வு சங்கல்பங்கள்",
    10,
    "Rhonda Byrne's cornerstone ritual from 'The Magic'. Write 10 things you are grateful for, say why, and repeat: 'Thank you, thank you, thank you!'",
    "EditNote"
  )
}

data class ManifestationBook(
  val id: String,
  val title: String,
  val author: String,
  val year: String,
  val category: String,
  val brainFrequencyConcept: String,
  val keyPractice: String,
  val quote: String,
  val fullSummary: String
)

data class DayStreak(
  val dayOfMonth: Int,
  val dayOfWeek: String,
  val isMorningCompleted: Boolean,
  val isNightCompleted: Boolean,
  val gratitudeCount: Int,
  val isToday: Boolean = false,
  val isFuture: Boolean = false
)

data class CompetitorPlan(
  val appName: String,
  val monthlyPrice: String,
  val yearlyPrice: String,
  val pros: List<String>,
  val cons: List<String>,
  val isRecommended: Boolean = false,
  val marketStatus: String
)
