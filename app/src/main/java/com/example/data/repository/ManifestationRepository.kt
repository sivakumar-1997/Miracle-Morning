package com.example.data.repository

import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ManifestationRepository {

  private val _userProfile = MutableStateFlow(
    UserProfile(
      name = "Sivakumar",
      nickname = "Siva",
      email = "sivakumar@auramagic.app",
      currentMood = "Grateful & High Vibration",
      primaryIntention = "Financial Freedom & Serene Inner Peace",
      wakeUpTime = "04:30 AM",
      isBrahmaMuhurtaActive = true,
      streakDays = 18,
      auraLevel = "Luminous Bloom (Level 4)",
      isLoggedIn = true,
      planTier = "Aura Magic Pro ($3.99/mo)"
    )
  )
  val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

  // 10 Gratitude items initialized with Rhonda Byrne's format from "The Magic"
  private val _gratitudeList = MutableStateFlow(
    listOf(
      GratitudeItem(1, "my strong, healthy body and rhythmic breath", "it allows me to experience life with vitality and boundless energy", true),
      GratitudeItem(2, "the peaceful early morning Brahma Muhurta hours", "the silence connects me directly to divine creativity and stillness", true),
      GratitudeItem(3, "the pure glass of water placed in front of me", "it cleanses my cells and absorbs my highest thoughts of joy", true),
      GratitudeItem(4, "my supportive family and spiritual mentors", "their love grounds me and reminds me that I am always guided", true),
      GratitudeItem(5, "the abundance of money flowing into my life", "it gives me the freedom to create, give back, and live comfortably", true),
      GratitudeItem(6, "my sharp mind and intuition", "they guide every positive decision and open doors to synchronicities", false),
      GratitudeItem(7, "the books of Rhonda Byrne and Dr. Joe Dispenza", "they revealed the scientific and spiritual law of vibration to me", false),
      GratitudeItem(8, "the roof over my head and warm sanctuary", "it provides safety, serenity, and sacred rest every night", false),
      GratitudeItem(9, "all challenges I have overcome in the past", "they sculpted my inner resilience and made my aura radiant", false),
      GratitudeItem(10, "the limitless possibilities awaiting me today", "the universe is orchestrating unseen miracles on my behalf", false)
    )
  )
  val gratitudeList: StateFlow<List<GratitudeItem>> = _gratitudeList.asStateFlow()

  // Solfeggio frequency player state
  private val _selectedFrequencyHz = MutableStateFlow(528)
  val selectedFrequencyHz: StateFlow<Int> = _selectedFrequencyHz.asStateFlow()

  private val _isFrequencyPlaying = MutableStateFlow(false)
  val isFrequencyPlaying: StateFlow<Boolean> = _isFrequencyPlaying.asStateFlow()

  // Water manifestation interactive states
  private val _waterIntent = MutableStateFlow("Infinite Health, Radiant Energy, and Golden Abundance")
  val waterIntent: StateFlow<String> = _waterIntent.asStateFlow()

  private val _waterSipsTaken = MutableStateFlow(3)
  val waterSipsTaken: StateFlow<Int> = _waterSipsTaken.asStateFlow()

  // Night Routine State
  private val _nightBestBlessing = MutableStateFlow("Experiencing effortless deep focus and signing a dream creative partnership")
  val nightBestBlessing: StateFlow<String> = _nightBestBlessing.asStateFlow()

  private val _nightCompleted = MutableStateFlow(false)
  val nightCompleted: StateFlow<Boolean> = _nightCompleted.asStateFlow()

  // Books library
  val books: List<ManifestationBook> = listOf(
    ManifestationBook(
      id = "the-magic",
      title = "The Magic",
      author = "Rhonda Byrne",
      year = "2012",
      category = "Gratitude & Law of Attraction",
      brainFrequencyConcept = "Alpha Wave State (8-12 Hz) - Gratitude shifts emotional neuro-chemistry from cortisol stress to oxytocin & serotonin within 3 minutes.",
      keyPractice = "Write 10 Gratitude things every morning ('Count Your Blessings'), state why you are grateful, read each one, and say 'Thank you, thank you, thank you'. End each day with the Magic Rock practice.",
      quote = "Whoever has gratitude will be given more, and he or she will have an abundance. Whoever does not have gratitude, even what he or she has will be taken away.",
      fullSummary = "The Magic reveals the 28-day transformational gratitude journey. Byrne teaches that gratitude is a magnetic frequency. By structuring your morning with 10 deliberate blessings and previewing tomorrow's exercise the night before, your reticular activating system (RAS) locks into synchronicities throughout the day."
    ),
    ManifestationBook(
      id = "the-secret",
      title = "The Secret",
      author = "Rhonda Byrne",
      year = "2006",
      category = "Universal Laws & Frequency Tuning",
      brainFrequencyConcept = "Resonant Vibrational Frequency - Like attracts like. Thought is the electromagnetic electric charge, feeling is the magnetic pull.",
      keyPractice = "Ask, Believe, Receive. Visualize the end result as already fulfilled with intense sensory emotional richness.",
      quote = "See yourself living in abundance and you will attract it. It works every time, with every person.",
      fullSummary = "The foundational manifesto explaining that thoughts become things. The brain acts as a cosmic broadcasting tower transmitting thoughts on specific frequencies. By feeling joy, relief, and gratitude ahead of time, external reality reorganizes around that state."
    ),
    ManifestationBook(
      id = "breaking-habit",
      title = "Breaking the Habit of Being Yourself",
      author = "Dr. Joe Dispenza",
      year = "2012",
      category = "Neuroscience & Quantum Creation",
      brainFrequencyConcept = "Theta Wave (4-8 Hz) Subconscious Access during Brahma Muhurta & Twilight - Bypassing the analytical neocortex to recode the subconscious mind.",
      keyPractice = "Morning meditation during Brahma Muhurta (4:30 AM) when melatonin is highest, shifting from high-beta stress into alpha and theta quantum coherence.",
      quote = "To be empowered—to be free, to be unlimited, to be creative, to be genius, to be divine—that is who you are. Once you feel that way, memorize that feeling.",
      fullSummary = "Dr. Dispenza bridges quantum physics, brain neuroplasticity, and epigenetics. He explains that your personality creates your personal reality. When you align clear intention with elevated emotion in the early morning, you signal new genes in new ways."
    ),
    ManifestationBook(
      id = "ask-given",
      title = "Ask and It Is Given",
      author = "Esther and Jerry Hicks",
      year = "2004",
      category = "Vibrational Alignment & The Vortex",
      brainFrequencyConcept = "The Emotional Guidance Scale (22 levels) - Tuning from Fear/Grief up to Joy, Knowledge, Empowerment, and Love.",
      keyPractice = "The Morning Rampage of Appreciation: Tuning your vibrational setpoint before engaging with worldly distractions or smartphones.",
      quote = "You are the creator of your own experience. Everything you perceive is a vibrational match to your dominant frequency.",
      fullSummary = "A comprehensive manual on the vibrational universe. Teaches how to pivot thoughts gradually up the emotional scale so that manifestation occurs effortlessly without resistance."
    ),
    ManifestationBook(
      id = "hidden-messages-water",
      title = "The Hidden Messages in Water",
      author = "Dr. Masaru Emoto",
      year = "2004",
      category = "Water Alchemy & Cellular Consciousness",
      brainFrequencyConcept = "Hexagonal Crystallization - Human consciousness and spoken intention physically restructure water molecule geometry.",
      keyPractice = "Water Manifestation: Holding a glass of clean water with both palms, projecting 'Love and Gratitude', and drinking sip by sip to calibrate 70% of human bodily water.",
      quote = "Water is the mirror of our soul. Love and gratitude expressed to water creates the most symmetrical, radiant crystalline jewels.",
      fullSummary = "Pioneering photographic experiments demonstrating that water exposed to thoughts, prayers, and words of gratitude forms dazzling crystalline snowflakes, while water exposed to negative words forms discordant shapes. Direct inspiration for AuraMagic's morning water ritual."
    ),
    ManifestationBook(
      id = "power-subconscious",
      title = "The Power of Your Subconscious Mind",
      author = "Dr. Joseph Murphy",
      year = "1963",
      category = "Hypnagogic State & Mental Imprinting",
      brainFrequencyConcept = "Hypnagogic Twilight State - The golden threshold between wakefulness and deep sleep where subconscious impression is 100% receptive.",
      keyPractice = "Night lullaby prayer: Falling asleep repeating one or two power words such as 'Wealth, Success, Divine Health' like a lullaby.",
      quote = "Busy your mind with the concepts of harmony, health, peace, and good will, and wonders will happen in your life.",
      fullSummary = "Classic psychological and spiritual masterpiece detailing how the conscious mind plants seeds that the fertile subconscious garden reproduces without debate. Essential for our night review and morning Brahma Muhurta sequence."
    ),
    ManifestationBook(
      id = "feeling-is-secret",
      title = "Feeling is the Secret",
      author = "Neville Goddard",
      year = "1944",
      category = "SATS & The Law of Assumption",
      brainFrequencyConcept = "SATS (State Akin to Sleep) - Lowering sensory input so imagination feels real as physical reality.",
      keyPractice = "Night ritual: In the drowsiness before sleep, enact a short 5-second scene implying your wish is already completely fulfilled.",
      quote = "Assume the feeling of your wish fulfilled and continue therein until that which you feel is substantiated in your world.",
      fullSummary = "Goddard explains that feeling is the language of the subconscious. Never go to sleep in mood of discouragement or lack; always capture the feeling of natural relief and accomplishment."
    )
  )

  // 31-day streak dataset for October 2026
  val monthlyStreaks: List<DayStreak> = (1..31).map { day ->
    val isPast = day <= 18
    val isToday = day == 18
    val isFuture = day > 18
    val dayOfWeek = when (day % 7) {
      1 -> "Mon"; 2 -> "Tue"; 3 -> "Wed"; 4 -> "Thu"; 5 -> "Fri"; 6 -> "Sat"; else -> "Sun"
    }
    DayStreak(
      dayOfMonth = day,
      dayOfWeek = dayOfWeek,
      isMorningCompleted = isPast,
      isNightCompleted = isPast && !isToday,
      gratitudeCount = if (isPast) 10 else 0,
      isToday = isToday,
      isFuture = isFuture
    )
  }

  // Competitor Pricing Comparison
  val competitorPlans: List<CompetitorPlan> = listOf(
    CompetitorPlan(
      appName = "Stella",
      monthlyPrice = "$9.99/mo",
      yearlyPrice = "$59.99/yr",
      pros = listOf("Nice celestial visuals", "Daily horoscope"),
      cons = listOf("High monthly price", "No Brahma Muhurta routines", "No water manifestation ritual", "Declining community updates"),
      isRecommended = false,
      marketStatus = "Expensive / Stagnant"
    ),
    CompetitorPlan(
      appName = "Perla",
      monthlyPrice = "$7.99/mo",
      yearlyPrice = "$49.99/yr",
      pros = listOf("Simple affirmation cards", "Widget support"),
      cons = listOf("No book references or wisdom library", "No 10-gratitude Magic format", "No Solfeggio soundscapes", "Repetitive content"),
      isRecommended = false,
      marketStatus = "Basic / Limited Scope"
    ),
    CompetitorPlan(
      appName = "AuraMagic",
      monthlyPrice = "$3.99/mo",
      yearlyPrice = "$24.99/yr ($2.08/mo)",
      pros = listOf(
        "Complete Brahma Muhurta 4:30 AM flow",
        "Aura Cleansing & Prana breathing",
        "Sacred Water Manifestation with sip tracker",
        "10 Daily Gratitude practice from 'The Magic'",
        "Night Magic Rock reflection & day preview",
        "Curated Books library & Brain Frequency audio",
        "Monthly Streaks & Aura Bloom progression",
        "60% more affordable than Stella & Perla"
      ),
      cons = emptyList(),
      isRecommended = true,
      marketStatus = "Best Value & Complete System"
    )
  )

  // Actions
  fun updateNickname(nickname: String) {
    _userProfile.update { it.copy(nickname = nickname) }
  }

  fun updateMood(mood: String) {
    _userProfile.update { it.copy(currentMood = mood) }
  }

  fun updateIntention(intention: String) {
    _userProfile.update { it.copy(primaryIntention = intention) }
  }

  fun updateWakeUpTime(time: String) {
    _userProfile.update { it.copy(wakeUpTime = time) }
  }

  fun toggleGratitudeItem(id: Int) {
    _gratitudeList.update { list ->
      list.map { if (it.id == id) it.copy(isCompleted = !it.isCompleted) else it }
    }
  }

  fun updateGratitudeText(id: Int, blessing: String, reason: String) {
    _gratitudeList.update { list ->
      list.map { if (it.id == id) it.copy(blessing = blessing, reason = reason) else it }
    }
  }

  fun addGratitudeItem(blessing: String, reason: String) {
    _gratitudeList.update { list ->
      val newId = (list.maxOfOrNull { it.id } ?: 0) + 1
      list + GratitudeItem(newId, blessing, reason, false)
    }
  }

  fun setFrequency(hz: Int) {
    _selectedFrequencyHz.value = hz
  }

  fun toggleFrequencyAudio() {
    _isFrequencyPlaying.update { !it }
  }

  fun setWaterIntent(intent: String) {
    _waterIntent.value = intent
  }

  fun takeWaterSip() {
    _waterSipsTaken.update { (it + 1).coerceAtMost(7) }
  }

  fun resetWaterSips() {
    _waterSipsTaken.value = 0
  }

  fun saveNightReflection(bestBlessing: String) {
    _nightBestBlessing.value = bestBlessing
    _nightCompleted.value = true
    _userProfile.update { it.copy(streakDays = it.streakDays + 1) }
  }

  fun loginUser(email: String) {
    _userProfile.update { it.copy(email = email, isLoggedIn = true) }
  }

  fun logoutUser() {
    _userProfile.update { it.copy(isLoggedIn = false) }
  }
}
