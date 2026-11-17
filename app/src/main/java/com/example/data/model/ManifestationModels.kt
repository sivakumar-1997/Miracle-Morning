package com.example.data.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.*

data class UserProfile(
  val name: String = "Practitioner",
  val nickname: String = "Seeker",
  val email: String = "seeker@miraclemornings.app",
  val currentMood: String = "Grateful & High Vibration",
  val primaryIntention: String = "Financial Freedom & Serene Inner Peace",
  val wakeUpTime: String = "04:30 AM",
  val isBrahmaMuhurtaActive: Boolean = true,
  val streakDays: Int = 0,
  val auraLevel: String = "Seed of Intention (Level 1)",
  val isLoggedIn: Boolean = true,
  val isFirstTimeUser: Boolean = true,
  val planTier: String = "Miracle Mornings Pass ($3.99/mo)"
)

data class WeekDayItem(
  val dayName: String,
  val dayNumber: Int,
  val isToday: Boolean,
  val isPast: Boolean
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
  val subtitle: String,
  val durationMinutes: Int,
  val description: String
) {
  BRAHMA_MUHURTA(
    1,
    "Brahma Muhurta Awakening",
    "4:30 AM – 5:00 AM Cosmic Hour",
    5,
    "Awaken during the Creator's hour when natural sattva prana is highest and the pineal gland is attuned to stillness."
  ),
  AURA_CLEANSING(
    2,
    "7 Chakras Aura Cleansing",
    "Energetic Alignment & Sound Baths",
    7,
    "Cleanse your etheric aura using the 7 chakra singing bowl sounds (396Hz to 963Hz) and align your body's energy centers."
  ),
  HOOPONOPONO_PRAYER(
    3,
    "Ho'oponopono Sacred Prayer",
    "Ancient Hawaiian Reconciliation & Peace",
    5,
    "Recite the four sacred phrases to clear subconscious memory and emotional resistance with 432 Hz healing background music."
  ),
  WATER_MANIFESTATION(
    4,
    "Water Manifestation Alchemy",
    "Intention Imprint & Mindful Sips",
    7,
    "Hold a clear glass of water in front of you, charge it with love and gratitude, and drink it sip by sip to calibrate your cells."
  ),
  POSITIVE_WISDOM(
    5,
    "Positive Notes & Mind Wisdom",
    "Subconscious Tuning from Masters",
    5,
    "Absorb uplifting notes and wisdom from classic manifestation literature to tune your brain into positive receptivity."
  ),
  TEN_GRATITUDE_PRACTICES(
    6,
    "Count 10 Blessings",
    "From Rhonda Byrne's 'The Magic'",
    10,
    "Write 10 things you are grateful for, explain why, and feel the emotion repeating: 'Thank you, thank you, thank you!'"
  )
}

data class ChakraInfo(
  val id: Int,
  val name: String,
  val sanskritName: String,
  val frequencyHz: Double,
  val color: Color,
  val location: String,
  val anatomicalPlacement: String,
  val spiritualFocus: String,
  val seedMantra: String,
  val pronunciation: String,
  val affirmation: String,
  val element: String,
  val physicalOrgans: String = "",
  val description: String = ""
)

enum class HooponoponoAudioMode {
  BGM_ONLY_432HZ,
  BGM_PLUS_VOICE_432HZ,
  OFF
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
