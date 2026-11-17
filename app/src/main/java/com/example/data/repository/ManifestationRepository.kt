package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.*
import com.example.data.remote.CloudSyncPayload
import com.example.ui.theme.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.json.JSONArray
import org.json.JSONObject

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class ManifestationRepository(context: Context? = null) {

  private val prefs: SharedPreferences? =
    context?.getSharedPreferences("miracle_mornings_local_prefs", Context.MODE_PRIVATE)

  private val _currentTimeZone = MutableStateFlow(TimeZone.getDefault())
  val currentTimeZone: StateFlow<TimeZone> = _currentTimeZone.asStateFlow()

  private val _currentFormattedDate = MutableStateFlow(calculateCurrentDate(TimeZone.getDefault()))
  val currentFormattedDate: StateFlow<String> = _currentFormattedDate.asStateFlow()

  private val _currentMonthYear = MutableStateFlow(calculateMonthYear(TimeZone.getDefault()))
  val currentMonthYear: StateFlow<String> = _currentMonthYear.asStateFlow()

  private val _timeZoneDisplayName = MutableStateFlow(calculateTimeZoneDisplay(TimeZone.getDefault()))
  val timeZoneDisplayName: StateFlow<String> = _timeZoneDisplayName.asStateFlow()

  private val _currentWeekDays = MutableStateFlow(calculateWeekDays(TimeZone.getDefault()))
  val currentWeekDays: StateFlow<List<WeekDayItem>> = _currentWeekDays.asStateFlow()

  // Initial user starts with 0 streakDays for first-time login (or loads saved local progress)
  private val _userProfile = MutableStateFlow(
    UserProfile(
      name = prefs?.getString("mm_user_name", null) ?: "New Practitioner",
      nickname = prefs?.getString("mm_user_nickname", null) ?: "Seeker",
      email = prefs?.getString("mm_user_email", null) ?: "seeker@miraclemornings.app",
      currentMood = "Grateful & High Vibration",
      primaryIntention = "Financial Freedom & Serene Inner Peace",
      wakeUpTime = "04:30 AM",
      isBrahmaMuhurtaActive = true,
      streakDays = prefs?.getInt("mm_streak_days", 0) ?: 0,
      auraLevel = auraLevelForStreak(prefs?.getInt("mm_streak_days", 0) ?: 0),
      isLoggedIn = true,
      isFirstTimeUser = (prefs?.getInt("mm_streak_days", 0) ?: 0) == 0,
      planTier = "Miracle Mornings Pass ($3.99/mo)"
    )
  )
  val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

  private val _completedDates = MutableStateFlow(loadSavedCompletedDates())
  val completedDates: StateFlow<Set<String>> = _completedDates.asStateFlow()

  private val _monthlyStreaks = MutableStateFlow(
    calculateMonthlyStreaks(
      streakDays = _userProfile.value.streakDays,
      tz = _currentTimeZone.value,
      completedDates = _completedDates.value
    )
  )
  val monthlyStreaks: StateFlow<List<DayStreak>> = _monthlyStreaks.asStateFlow()

  // 7 Chakras dataset with authentic frequencies, seed mantras, and anatomical placements
  val chakrasList: List<ChakraInfo> = listOf(
    ChakraInfo(
      id = 1,
      name = "Root Chakra",
      sanskritName = "Muladhara",
      frequencyHz = 396.0,
      color = ChakraRootRed,
      location = "Base of spine / Coccyx",
      anatomicalPlacement = "Base of the spine",
      spiritualFocus = "Safety & Grounding",
      seedMantra = "LAM",
      pronunciation = "pronounced \"luhm\" or \"lung\"",
      affirmation = "I am safe, grounded, and supported by the Universe.",
      element = "Earth",
      physicalOrgans = "Spinal column, kidneys, legs, feet, immune system",
      description = "Located at the perineum and base of the spine. Anchors our connection to mother earth, providing deep stability, physical survival vitality, and releasing irrational fear."
    ),
    ChakraInfo(
      id = 2,
      name = "Sacral Chakra",
      sanskritName = "Svadhisthana",
      frequencyHz = 417.0,
      color = ChakraSacralOrange,
      location = "Lower abdomen, below navel",
      anatomicalPlacement = "Lower abdomen",
      spiritualFocus = "Creativity & Emotions",
      seedMantra = "VAM",
      pronunciation = "pronounced \"vuhm\" or \"vung\"",
      affirmation = "I embrace creative passion, joy, and emotional flow.",
      element = "Water",
      physicalOrgans = "Reproductive organs, bladder, hips, sacrum",
      description = "Located in the pelvis and lower abdomen. Drives passion, joyful creative manifestations, fluid emotional wellness, and dissolving past trauma."
    ),
    ChakraInfo(
      id = 3,
      name = "Solar Plexus",
      sanskritName = "Manipura",
      frequencyHz = 528.0,
      color = ChakraSolarYellow,
      location = "Upper abdomen / Solar plexus",
      anatomicalPlacement = "Upper abdomen",
      spiritualFocus = "Confidence & Willpower",
      seedMantra = "RAM",
      pronunciation = "RAM",
      affirmation = "I stand in my authentic power and divine confidence.",
      element = "Fire",
      physicalOrgans = "Stomach, liver, digestive system, adrenal glands",
      description = "Located just above the navel in the solar plexus. The furnace of personal transformation, courage, inner authority, and DNA transformation at 528 Hz."
    ),
    ChakraInfo(
      id = 4,
      name = "Heart Chakra",
      sanskritName = "Anahata",
      frequencyHz = 639.0,
      color = ChakraHeartGreen,
      location = "Center of chest / Cardiac plexus",
      anatomicalPlacement = "Center of the chest",
      spiritualFocus = "Love & Compassion",
      seedMantra = "YAM",
      pronunciation = "YAM",
      affirmation = "I give and receive unconditional love with an open heart.",
      element = "Air",
      physicalOrgans = "Heart, lungs, thymus gland, circulatory system",
      description = "Located at the energetic core of the chest. Unites the physical and spiritual realms, opening the gateway to unconditional self-love, empathy, and deep interpersonal forgiveness."
    ),
    ChakraInfo(
      id = 5,
      name = "Throat Chakra",
      sanskritName = "Vishuddha",
      frequencyHz = 741.0,
      color = ChakraThroatBlue,
      location = "Throat / Vocal cords",
      anatomicalPlacement = "Throat",
      spiritualFocus = "Communication & Truth",
      seedMantra = "HAM",
      pronunciation = "HAM",
      affirmation = "I speak my divine truth clearly, lovingly, and fearlessly.",
      element = "Ether",
      physicalOrgans = "Throat, thyroid, vocal cords, jaw, neck",
      description = "Located at the center of the throat. The center of truthful expression, clear intention articulation, creative manifestation speech, and authentic living."
    ),
    ChakraInfo(
      id = 6,
      name = "Third Eye",
      sanskritName = "Ajna",
      frequencyHz = 852.0,
      color = ChakraThirdEyeIndigo,
      location = "Between eyebrows / Pineal gland",
      anatomicalPlacement = "Between the eyebrows",
      spiritualFocus = "Intuition & Insight",
      seedMantra = "OM",
      pronunciation = "OM (or AUM)",
      affirmation = "I trust my intuition, deep vision, and spiritual guidance.",
      element = "Light",
      physicalOrgans = "Pineal gland, pituitary gland, eyes, brain center",
      description = "Located between the eyebrows just above the nose bridge. Unlocks spiritual sight, vivid manifestation visualization, lucid intuition, and cosmic inner knowing."
    ),
    ChakraInfo(
      id = 7,
      name = "Crown Chakra",
      sanskritName = "Sahasrara",
      frequencyHz = 963.0,
      color = ChakraCrownViolet,
      location = "Top of the head / Crown",
      anatomicalPlacement = "Top of the head",
      spiritualFocus = "Spiritual Connection",
      seedMantra = "AH",
      pronunciation = "AH (or silent OM)",
      affirmation = "I am one with Infinite Divine Intelligence.",
      element = "Cosmic Consciousness",
      physicalOrgans = "Cerebral cortex, central nervous system, biofield",
      description = "Located at the top of the skull. The thousand-petaled lotus connecting human consciousness directly to the universal source, divine bliss, and infinite unity."
    )
  )

  // 10 Gratitude items initialized with Rhonda Byrne's format from "The Magic"
  private val _gratitudeList = MutableStateFlow(
    listOf(
      GratitudeItem(1, "my strong, healthy body and rhythmic breath", "it allows me to experience life with vitality and boundless energy", true),
      GratitudeItem(2, "the peaceful early morning Brahma Muhurta hours", "the silence connects me directly to divine stillness and clarity", true),
      GratitudeItem(3, "the pure glass of water placed in front of me", "it cleanses my cells and absorbs my highest thoughts of joy", true),
      GratitudeItem(4, "my supportive family and mentors", "their love grounds me and reminds me that I am always guided", true),
      GratitudeItem(5, "the abundance of money flowing into my life", "it gives me the freedom to create, share, and live comfortably", true),
      GratitudeItem(6, "my sharp intuition and inner guidance", "they guide positive decisions and open doors to synchronicities", false),
      GratitudeItem(7, "the sacred books of Rhonda Byrne and Dr. Joe Dispenza", "they revealed the scientific and spiritual law of vibration to me", false),
      GratitudeItem(8, "the sanctuary of my home", "it provides safety, serenity, and sacred rest every night", false),
      GratitudeItem(9, "all challenges I have overcome in the past", "they cultivated my inner resilience and made my spirit strong", false),
      GratitudeItem(10, "the limitless possibilities awaiting me today", "the universe is orchestrating miracles on my behalf", false)
    )
  )
  val gratitudeList: StateFlow<List<GratitudeItem>> = _gratitudeList.asStateFlow()

  // Water manifestation interactive states (8 Sacred Sips matching Web)
  private val _waterIntent = MutableStateFlow(
    prefs?.getString("mm_water_intent", null)
      ?: "Infinite Health, Radiant Energy, and Golden Abundance"
  )
  val waterIntent: StateFlow<String> = _waterIntent.asStateFlow()

  private val _waterSipsTaken = MutableStateFlow(
    (prefs?.getInt("mm_water_sips", 3) ?: 3).coerceIn(0, 8)
  )
  val waterSipsTaken: StateFlow<Int> = _waterSipsTaken.asStateFlow()

  // Night Routine State
  private val _nightBestBlessing = MutableStateFlow(
    prefs?.getString("mm_night_reflection", null)
      ?: "Experiencing effortless deep peace and completing my sacred morning rituals"
  )
  val nightBestBlessing: StateFlow<String> = _nightBestBlessing.asStateFlow()

  private val _nightCompleted = MutableStateFlow(false)
  val nightCompleted: StateFlow<Boolean> = _nightCompleted.asStateFlow()

  init {
    loadSavedGratitude()?.let { saved ->
      if (saved.isNotEmpty()) {
        _gratitudeList.value = saved
      }
    }
  }

  // Books library
  val books: List<ManifestationBook> = listOf(
    ManifestationBook(
      id = "the-magic",
      title = "The Magic",
      author = "Rhonda Byrne",
      year = "2012",
      category = "Gratitude & Law of Attraction",
      brainFrequencyConcept = "Alpha Wave State (8-12 Hz) - Gratitude shifts emotional chemistry from cortisol to serotonin within 3 minutes.",
      keyPractice = "Write 10 Gratitude things every morning ('Count Your Blessings'), state why you are grateful, read each one, and say 'Thank you, thank you, thank you'. End each day with the Magic Rock practice.",
      quote = "Whoever has gratitude will be given more, and he or she will have an abundance. Whoever does not have gratitude, even what he or she has will be taken away.",
      fullSummary = "The Magic reveals the 28-day transformational gratitude journey. Byrne teaches that gratitude is an electromagnetic frequency. By structuring your morning with 10 deliberate blessings and previewing tomorrow's exercise the night before, your reticular activating system locks onto synchronicities."
    ),
    ManifestationBook(
      id = "the-secret",
      title = "The Secret",
      author = "Rhonda Byrne",
      year = "2006",
      category = "Universal Laws & Frequency Tuning",
      brainFrequencyConcept = "Resonant Vibrational Frequency - Like attracts like. Thought is the electric impulse; feeling is the magnetic draw.",
      keyPractice = "Ask, Believe, Receive. Visualize the end result as already fulfilled with intense sensory emotion.",
      quote = "See yourself living in abundance and you will attract it. It works every time, with every person.",
      fullSummary = "The foundational manifesto explaining that thoughts become things. The brain acts as a broadcasting tower transmitting on specific frequencies. By cultivating joy and appreciation ahead of time, circumstances reorganize around that state."
    ),
    ManifestationBook(
      id = "breaking-habit",
      title = "Breaking the Habit of Being Yourself",
      author = "Dr. Joe Dispenza",
      year = "2012",
      category = "Neuroscience & Quantum Creation",
      brainFrequencyConcept = "Theta Wave (4-8 Hz) Subconscious Access during Brahma Muhurta & Twilight - Bypassing analytical filters to recode the subconscious mind.",
      keyPractice = "Morning meditation during Brahma Muhurta (4:30 AM) when melatonin is peaked, shifting from stress into alpha and theta quantum coherence.",
      quote = "To be empowered—to be free, to be unlimited, to be creative, to be divine—that is who you are. Once you feel that way, memorize that feeling.",
      fullSummary = "Dr. Dispenza bridges quantum physics, brain neuroplasticity, and epigenetics. He explains that your personality creates your personal reality. When you combine clear intention with elevated emotion in the early morning, you signal new genes in new ways."
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
      fullSummary = "Pioneering photographic experiments demonstrating that water exposed to prayers and words of gratitude forms dazzling crystalline snowflakes, while negative words produce disordered shapes. Direct inspiration for Miracle Mornings' water ritual."
    ),
    ManifestationBook(
      id = "power-subconscious",
      title = "The Power of Your Subconscious Mind",
      author = "Dr. Joseph Murphy",
      year = "1963",
      category = "Hypnagogic State & Mental Imprinting",
      brainFrequencyConcept = "Hypnagogic Twilight State - The threshold between wakefulness and deep sleep where subconscious impression is 100% receptive.",
      keyPractice = "Night lullaby prayer: Falling asleep repeating power words such as 'Wealth, Success, Divine Health' like a peaceful lullaby.",
      quote = "Busy your mind with the concepts of harmony, health, peace, and good will, and wonders will happen in your life.",
      fullSummary = "Classic psychological and spiritual masterpiece detailing how the conscious mind plants seeds that the fertile subconscious garden reproduces without debate. Essential for our night review and morning sequence."
    ),
    ManifestationBook(
      id = "feeling-is-secret",
      title = "Feeling is the Secret",
      author = "Neville Goddard",
      year = "1944",
      category = "SATS & The Law of Assumption",
      brainFrequencyConcept = "SATS (State Akin to Sleep) - Lowering sensory input so imagination feels as real as physical reality.",
      keyPractice = "Night ritual: In the drowsiness before sleep, enact a short 5-second scene implying your wish is already completely fulfilled.",
      quote = "Assume the feeling of your wish fulfilled and continue therein until that which you feel is substantiated in your world.",
      fullSummary = "Goddard explains that feeling is the language of the subconscious. Never go to sleep in a mood of discouragement or lack; always capture the feeling of natural relief and accomplishment."
    )
  )

  // Competitor Pricing Comparison
  val competitorPlans: List<CompetitorPlan> = listOf(
    CompetitorPlan(
      appName = "Stella",
      monthlyPrice = "$9.99/mo",
      yearlyPrice = "$59.99/yr",
      pros = listOf("Celestial visuals", "Daily horoscope"),
      cons = listOf("High monthly price", "No Brahma Muhurta routines", "No 7 chakras sound baths", "Declining community updates"),
      isRecommended = false,
      marketStatus = "Expensive / Stagnant"
    ),
    CompetitorPlan(
      appName = "Perla",
      monthlyPrice = "$7.99/mo",
      yearlyPrice = "$49.99/yr",
      pros = listOf("Simple affirmation cards", "Clean aesthetic"),
      cons = listOf("No Ho'oponopono 432Hz player", "No structured water manifestation", "No book wisdom library", "Repetitive content"),
      isRecommended = false,
      marketStatus = "Basic / Limited Scope"
    ),
    CompetitorPlan(
      appName = "Miracle Mornings",
      monthlyPrice = "$3.99/mo",
      yearlyPrice = "$24.99/yr ($2.08/mo)",
      pros = listOf(
        "Complete Brahma Muhurta 4:30 AM flow",
        "7 Chakras Aura Cleansing with authentic singing bowls",
        "Ho'oponopono Prayer with 432Hz BGM + Voice narration",
        "Sacred Water Manifestation with sip tracker",
        "10 Daily Gratitude practice from 'The Magic'",
        "Night Magic Rock reflection & day preview",
        "Curated Books library & Subconscious tuning",
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
    prefs?.edit()?.putString("mm_user_nickname", nickname)?.apply()
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
    saveGratitudeLocally(_gratitudeList.value)
  }

  fun addGratitudeItem(blessing: String, reason: String) {
    _gratitudeList.update { list ->
      val newId = (list.maxOfOrNull { it.id } ?: 0) + 1
      list + GratitudeItem(newId, blessing, reason, false)
    }
    saveGratitudeLocally(_gratitudeList.value)
  }

  fun setWaterIntent(intent: String) {
    _waterIntent.value = intent
    prefs?.edit()?.putString("mm_water_intent", intent)?.apply()
  }

  fun takeWaterSip() {
    _waterSipsTaken.update { (it + 1).coerceAtMost(8) }
    prefs?.edit()?.putInt("mm_water_sips", _waterSipsTaken.value)?.apply()
  }

  fun resetWaterSips() {
    _waterSipsTaken.value = 0
    prefs?.edit()?.putInt("mm_water_sips", 0)?.apply()
  }

  fun saveNightReflection(bestBlessing: String) {
    _nightBestBlessing.value = bestBlessing
    _nightCompleted.value = true
    prefs?.edit()?.putString("mm_night_reflection", bestBlessing)?.apply()

    val todayKey = todayDateKey(_currentTimeZone.value)
    val updatedDates = _completedDates.value + todayKey
    _completedDates.value = updatedDates
    saveCompletedDatesLocally(updatedDates)

    val current = _userProfile.value
    val newStreak = current.streakDays + 1
    val newLevel = auraLevelForStreak(newStreak)
    _userProfile.update { it.copy(streakDays = newStreak, auraLevel = newLevel, isFirstTimeUser = false) }
    prefs?.edit()?.putInt("mm_streak_days", newStreak)?.apply()
    refreshStreaks()
  }

  fun completeMorningRoutine() {
    val todayKey = todayDateKey(_currentTimeZone.value)
    val updatedDates = _completedDates.value + todayKey
    _completedDates.value = updatedDates
    saveCompletedDatesLocally(updatedDates)

    val current = _userProfile.value
    val newStreak = current.streakDays + 1
    val newLevel = auraLevelForStreak(newStreak)
    _userProfile.update { it.copy(streakDays = newStreak, auraLevel = newLevel, isFirstTimeUser = false) }
    prefs?.edit()?.putInt("mm_streak_days", newStreak)?.apply()
    refreshStreaks()
  }

  fun mergeCloudPayload(payload: CloudSyncPayload, cloudEmail: String? = null) {
    val mergedDates = _completedDates.value + payload.completedDates
    _completedDates.value = mergedDates
    saveCompletedDatesLocally(mergedDates)

    val localStreak = _userProfile.value.streakDays
    val cloudStreak = payload.streakDays ?: 0
    val mergedStreak = maxOf(localStreak, cloudStreak)
    val nicknameFromEmail = cloudEmail?.substringBefore("@")
      ?.replace(".", " ")
      ?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

    _userProfile.update { current ->
      current.copy(
        email = cloudEmail ?: current.email,
        nickname = if (current.nickname == "Seeker" && !nicknameFromEmail.isNullOrBlank()) nicknameFromEmail else current.nickname,
        streakDays = mergedStreak,
        auraLevel = auraLevelForStreak(mergedStreak),
        isFirstTimeUser = mergedStreak == 0,
        isLoggedIn = true
      )
    }
    prefs?.edit()
      ?.putInt("mm_streak_days", mergedStreak)
      ?.apply {
        if (!cloudEmail.isNullOrBlank()) putString("mm_user_email", cloudEmail)
        if (!nicknameFromEmail.isNullOrBlank() && _userProfile.value.nickname == nicknameFromEmail) {
          putString("mm_user_nickname", nicknameFromEmail)
        }
      }
      ?.apply()

    payload.waterIntent?.takeIf { it.isNotBlank() }?.let { intent ->
      _waterIntent.value = intent
      prefs?.edit()?.putString("mm_water_intent", intent)?.apply()
    }
    payload.waterSips?.let { sips ->
      val clamped = sips.coerceIn(0, 8)
      _waterSipsTaken.value = clamped
      prefs?.edit()?.putInt("mm_water_sips", clamped)?.apply()
    }
    payload.gratitudeItems?.takeIf { it.isNotEmpty() }?.let { items ->
      _gratitudeList.value = items
      saveGratitudeLocally(items)
    }
    payload.nightReflection?.takeIf { it.isNotBlank() }?.let { reflection ->
      _nightBestBlessing.value = reflection
      prefs?.edit()?.putString("mm_night_reflection", reflection)?.apply()
    }

    refreshStreaks()
  }

  fun registerUser(name: String, email: String) {
    val nickname = name.trim().split(" ").firstOrNull() ?: name.trim()
    _userProfile.value = UserProfile(
      name = name.trim(),
      nickname = if (nickname.isNotBlank()) nickname else "Seeker",
      email = email.trim(),
      currentMood = "Grateful & Ready to Transform",
      primaryIntention = "Inner Peace, Wealth & Serenity",
      wakeUpTime = "04:30 AM",
      isBrahmaMuhurtaActive = true,
      streakDays = 0, // Brand new account starts strictly at 0!
      auraLevel = "Seed of Intention (Level 1)",
      isLoggedIn = true,
      isFirstTimeUser = true,
      planTier = "Miracle Mornings Pass ($3.99/mo)"
    )
    refreshStreaks()
  }

  fun loginUser(email: String, isFirstTime: Boolean = false) {
    if (isFirstTime || email.contains("new", ignoreCase = true) || email.contains("fresh", ignoreCase = true)) {
      _userProfile.value = UserProfile(
        name = "New Practitioner",
        nickname = email.substringBefore("@").replace(".", " ").replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
        email = email,
        streakDays = 0, // First-time login starts strictly with zero
        auraLevel = "Seed of Intention (Level 1)",
        isLoggedIn = true,
        isFirstTimeUser = true
      )
    } else if (email == "siva@miraclemornings.app") {
      // Demo practitioner with 18-day streak for testing
      _userProfile.value = UserProfile(
        name = "Sivakumar",
        nickname = "Siva",
        email = email,
        streakDays = 18,
        auraLevel = "Luminous Bloom (Level 4)",
        isLoggedIn = true,
        isFirstTimeUser = false
      )
    } else {
      // Regular login default for new users
      _userProfile.value = UserProfile(
        name = email.substringBefore("@"),
        nickname = email.substringBefore("@").replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
        email = email,
        streakDays = 0, // Starts at 0
        auraLevel = "Seed of Intention (Level 1)",
        isLoggedIn = true,
        isFirstTimeUser = true
      )
    }
    refreshStreaks()
  }

  fun resetStreakToZero() {
    _completedDates.value = emptySet()
    saveCompletedDatesLocally(emptySet())
    _userProfile.update { it.copy(streakDays = 0, auraLevel = "Seed of Intention (Level 1)", isFirstTimeUser = true) }
    prefs?.edit()?.putInt("mm_streak_days", 0)?.apply()
    refreshStreaks()
  }

  fun logoutUser() {
    _userProfile.update { it.copy(isLoggedIn = false) }
  }

  fun refreshTimeZoneAndDate() {
    val tz = TimeZone.getDefault()
    _currentTimeZone.value = tz
    _currentFormattedDate.value = calculateCurrentDate(tz)
    _currentMonthYear.value = calculateMonthYear(tz)
    _timeZoneDisplayName.value = calculateTimeZoneDisplay(tz)
    _currentWeekDays.value = calculateWeekDays(tz)
    refreshStreaks()
  }

  private fun refreshStreaks() {
    _monthlyStreaks.value = calculateMonthlyStreaks(
      streakDays = _userProfile.value.streakDays,
      tz = _currentTimeZone.value,
      completedDates = _completedDates.value
    )
  }

  private fun loadSavedCompletedDates(): Set<String> {
    val raw = prefs?.getString("mm_completed_dates", null) ?: return emptySet()
    return runCatching {
      val arr = JSONArray(raw)
      buildSet {
        for (i in 0 until arr.length()) {
          val s = arr.optString(i).trim()
          if (s.isNotBlank()) add(s)
        }
      }
    }.getOrDefault(emptySet())
  }

  private fun saveCompletedDatesLocally(dates: Set<String>) {
    val arr = JSONArray().apply { dates.forEach { put(it) } }
    prefs?.edit()?.putString("mm_completed_dates", arr.toString())?.apply()
  }

  private fun loadSavedGratitude(): List<GratitudeItem>? {
    val raw = prefs?.getString("mm_gratitude_items", null) ?: return null
    return runCatching {
      val arr = JSONArray(raw)
      buildList {
        for (i in 0 until arr.length()) {
          val obj = arr.optJSONObject(i) ?: continue
          val id = obj.optInt("id", i + 1)
          val blessing = obj.optString("blessing").trim()
          val reason = obj.optString("reason").ifBlank { "it brings joy and abundance into my day" }
          val done = if (obj.has("done")) obj.optBoolean("done") else obj.optBoolean("isCompleted")
          if (blessing.isNotBlank()) add(GratitudeItem(id, blessing, reason, done))
        }
      }
    }.getOrNull()
  }

  private fun saveGratitudeLocally(items: List<GratitudeItem>) {
    val arr = JSONArray().apply {
      items.forEach { item ->
        put(JSONObject().apply {
          put("id", item.id)
          put("blessing", item.blessing)
          put("reason", item.reason)
          put("done", item.isCompleted)
        })
      }
    }
    prefs?.edit()?.putString("mm_gratitude_items", arr.toString())?.apply()
  }

  private fun auraLevelForStreak(streak: Int): String {
    return when {
      streak >= 28 -> "Cosmic Master (Level 5)"
      streak >= 14 -> "Luminous Bloom (Level 4)"
      streak >= 7 -> "Radiant Awakening (Level 3)"
      streak >= 3 -> "Sprouting Prana (Level 2)"
      else -> "Seed of Intention (Level 1)"
    }
  }

  companion object {
    fun calculateCurrentDate(tz: TimeZone): String {
      val cal = Calendar.getInstance(tz, Locale.getDefault())
      val sdf = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
      sdf.timeZone = tz
      return sdf.format(cal.time)
    }

    fun calculateMonthYear(tz: TimeZone): String {
      val cal = Calendar.getInstance(tz, Locale.getDefault())
      val sdf = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
      sdf.timeZone = tz
      return sdf.format(cal.time)
    }

    fun calculateTimeZoneDisplay(tz: TimeZone): String {
      val cal = Calendar.getInstance(tz, Locale.getDefault())
      val isDaylight = tz.inDaylightTime(cal.time)
      val shortName = tz.getDisplayName(isDaylight, TimeZone.SHORT, Locale.getDefault())
      val id = tz.id
      val offsetMillis = tz.getOffset(System.currentTimeMillis())
      val offsetHours = offsetMillis / 3600000.0
      val sign = if (offsetHours >= 0) "+" else ""
      val offsetFormatted = String.format(Locale.US, "%.1f", offsetHours).replace(".0", "")
      return "$id ($shortName, GMT$sign$offsetFormatted)"
    }

    fun calculateWeekDays(tz: TimeZone): List<WeekDayItem> {
      val cal = Calendar.getInstance(tz, Locale.getDefault())
      val todayDayOfYear = cal.get(Calendar.DAY_OF_YEAR)
      val todayYear = cal.get(Calendar.YEAR)

      // Find Monday of the current week
      cal.firstDayOfWeek = Calendar.MONDAY
      cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

      val dayNames = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
      return (0..6).map { idx ->
        val dayCal = cal.clone() as Calendar
        dayCal.add(Calendar.DAY_OF_MONTH, idx)
        val dayNum = dayCal.get(Calendar.DAY_OF_MONTH)
        val dayName = dayNames[idx]
        val isToday = dayCal.get(Calendar.DAY_OF_YEAR) == todayDayOfYear && dayCal.get(Calendar.YEAR) == todayYear
        val isPast = dayCal.get(Calendar.YEAR) < todayYear || (dayCal.get(Calendar.YEAR) == todayYear && dayCal.get(Calendar.DAY_OF_YEAR) < todayDayOfYear)
        WeekDayItem(dayName, dayNum, isToday, isPast)
      }
    }

    fun todayDateKey(tz: TimeZone): String {
      val cal = Calendar.getInstance(tz, Locale.getDefault())
      val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
      sdf.timeZone = tz
      return sdf.format(cal.time)
    }

    fun calculateMonthlyStreaks(
      streakDays: Int,
      tz: TimeZone,
      completedDates: Set<String> = emptySet()
    ): List<DayStreak> {
      val cal = Calendar.getInstance(tz, Locale.getDefault())
      val todayDayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
      val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
      val year = cal.get(Calendar.YEAR)
      val month = cal.get(Calendar.MONTH)

      val dayNames = arrayOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

      return (1..daysInMonth).map { day ->
        val dayCal = Calendar.getInstance(tz, Locale.getDefault()).apply {
          set(Calendar.YEAR, year)
          set(Calendar.MONTH, month)
          set(Calendar.DAY_OF_MONTH, day)
        }
        val dowIndex = dayCal.get(Calendar.DAY_OF_WEEK) - 1
        val dowName = dayNames.getOrElse(dowIndex) { "Mon" }
        val dateKey = String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, day)

        val isToday = (day == todayDayOfMonth)
        val isFuture = (day > todayDayOfMonth)

        // Check both explicit cloud/local completed dates and consecutive streakDays fallback
        val isExplicitDone = completedDates.contains(dateKey)
        val isStreakDone = if (streakDays <= 0) {
          false
        } else {
          day in (todayDayOfMonth - streakDays + 1)..todayDayOfMonth
        }
        val isCompleted = isExplicitDone || isStreakDone

        DayStreak(
          dayOfMonth = day,
          dayOfWeek = dowName,
          isMorningCompleted = isCompleted,
          isNightCompleted = isCompleted && !isToday,
          gratitudeCount = if (isCompleted) 10 else 0,
          isToday = isToday,
          isFuture = isFuture
        )
      }
    }
  }
}
