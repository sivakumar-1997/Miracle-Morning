package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.SacredSoundEngine
import com.example.data.model.*
import com.example.data.remote.SupabaseCloudState
import com.example.data.remote.SupabaseService
import com.example.data.repository.ManifestationRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AppDestination(val label: String) {
  HOME("Home"),
  MORNING_ROUTINE("Morning"),
  NIGHT_ROUTINE("Night"),
  STREAKS("Streaks"),
  BOOKS("Books"),
  PRICING("Pricing"),
  ONBOARDING("Onboarding"),
  AUTH("Auth"),
  LEGAL("Privacy & Terms")
}

enum class AuthSubMode {
  LOGIN,
  REGISTER,
  FORGOT_PASSWORD
}

class ManifestationViewModel(
  application: Application
) : AndroidViewModel(application) {

  private val repository: ManifestationRepository = ManifestationRepository(application.applicationContext)
  private val supabaseService: SupabaseService = SupabaseService(application.applicationContext)

  val soundEngine = SacredSoundEngine(application.applicationContext)

  val cloudState: StateFlow<SupabaseCloudState> = supabaseService.state
  val userProfile = repository.userProfile
  val completedDates = repository.completedDates
  val currentMagicDay = repository.currentMagicDay
  val currentMagicLesson = repository.currentMagicLesson
  val gratitudeList = repository.gratitudeList
  val chakrasList = repository.chakrasList
  val waterIntent = repository.waterIntent
  val waterSipsTaken = repository.waterSipsTaken
  val nightBestBlessing = repository.nightBestBlessing
  val nightCompleted = repository.nightCompleted
  val books = repository.books
  val monthlyStreaks = repository.monthlyStreaks
  val competitorPlans = repository.competitorPlans

  init {
    if (supabaseService.state.value.isAuthenticated) {
      viewModelScope.launch {
        syncDownFromCloud()
      }
    }
  }

  // Dynamic Timezone and Date StateFlows
  val currentFormattedDate = repository.currentFormattedDate
  val currentMonthYear = repository.currentMonthYear
  val timeZoneDisplayName = repository.timeZoneDisplayName
  val currentWeekDays = repository.currentWeekDays

  // Navigation State
  private val _currentDestination = MutableStateFlow(AppDestination.HOME)
  val currentDestination: StateFlow<AppDestination> = _currentDestination.asStateFlow()

  private val _navigationStack = MutableStateFlow(listOf(AppDestination.HOME))
  val navigationStack: StateFlow<List<AppDestination>> = _navigationStack.asStateFlow()

  private val _authMode = MutableStateFlow(AuthSubMode.LOGIN)
  val authMode: StateFlow<AuthSubMode> = _authMode.asStateFlow()

  private val _legalInitialTab = MutableStateFlow(com.example.ui.screens.LegalTab.PRIVACY)
  val legalInitialTab: StateFlow<com.example.ui.screens.LegalTab> = _legalInitialTab.asStateFlow()

  // Morning routine stepped flow index (0..5)
  private val _activeMorningStepIndex = MutableStateFlow(0)
  val activeMorningStepIndex: StateFlow<Int> = _activeMorningStepIndex.asStateFlow()

  // Chakra Soundscape State
  private val _selectedChakra = MutableStateFlow(chakrasList.firstOrNull { it.id == 4 } ?: chakrasList.first())
  val selectedChakra: StateFlow<ChakraInfo> = _selectedChakra.asStateFlow()

  private val _playingChakraId = MutableStateFlow<Int?>(null)
  val playingChakraId: StateFlow<Int?> = _playingChakraId.asStateFlow()

  private val _isChakraSequencePlaying = MutableStateFlow(false)
  val isChakraSequencePlaying: StateFlow<Boolean> = _isChakraSequencePlaying.asStateFlow()

  private val _isChakraJourneyPlaying = MutableStateFlow(false)
  val isChakraJourneyPlaying: StateFlow<Boolean> = _isChakraJourneyPlaying.asStateFlow()

  private val _chakraJourneyStep = MutableStateFlow(0)
  val chakraJourneyStep: StateFlow<Int> = _chakraJourneyStep.asStateFlow()

  private val _isAmbientBgActive = MutableStateFlow(false)
  val isAmbientBgActive: StateFlow<Boolean> = _isAmbientBgActive.asStateFlow()

  // Ho'oponopono Prayer State (5 min = 300 sec timer)
  private val _hooponoponoAudioMode = MutableStateFlow(HooponoponoAudioMode.BGM_PLUS_VOICE_432HZ)
  val hooponoponoAudioMode: StateFlow<HooponoponoAudioMode> = _hooponoponoAudioMode.asStateFlow()

  private val _hooponoponoTimerSeconds = MutableStateFlow(300) // 5 minutes
  val hooponoponoTimerSeconds: StateFlow<Int> = _hooponoponoTimerSeconds.asStateFlow()

  private val _isHooponoponoPlaying = MutableStateFlow(false)
  val isHooponoponoPlaying: StateFlow<Boolean> = _isHooponoponoPlaying.asStateFlow()

  private var timerJob: Job? = null
  private var chakraJourneyJob: Job? = null
  private var chakraSequenceJob: Job? = null
  private var chakraToneJob: Job? = null

  // Selected book for details modal
  private val _selectedBook = MutableStateFlow<ManifestationBook?>(null)
  val selectedBook: StateFlow<ManifestationBook?> = _selectedBook.asStateFlow()

  // Message toast banner
  private val _toastMessage = MutableStateFlow<String?>(null)
  val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

  fun navigateTo(dest: AppDestination) {
    if (_currentDestination.value != dest) {
      if (_currentDestination.value == AppDestination.MORNING_ROUTINE && dest != AppDestination.MORNING_ROUTINE) {
        stopAllAudio()
      }
      _navigationStack.update { it + dest }
      _currentDestination.value = dest
    }
  }

  fun navigateBack(): Boolean {
    val current = _navigationStack.value
    return if (current.size > 1) {
      val newStack = current.dropLast(1)
      if (_currentDestination.value == AppDestination.MORNING_ROUTINE && newStack.last() != AppDestination.MORNING_ROUTINE) {
        stopAllAudio()
      }
      _navigationStack.value = newStack
      _currentDestination.value = newStack.last()
      true
    } else {
      false
    }
  }

  fun setAuthMode(mode: AuthSubMode) {
    _authMode.value = mode
  }

  fun openLegal(tab: com.example.ui.screens.LegalTab = com.example.ui.screens.LegalTab.PRIVACY) {
    _legalInitialTab.value = tab
    navigateTo(AppDestination.LEGAL)
  }

  fun setMorningStep(index: Int) {
    if (_activeMorningStepIndex.value != index) {
      stopChakraJourney()
      stopChakraSequence()
      pauseHooponopono()
      _activeMorningStepIndex.value = index.coerceIn(0, 5)
    }
  }

  fun nextMorningStep() {
    stopChakraJourney()
    stopChakraSequence()
    pauseHooponopono()
    _activeMorningStepIndex.update { (it + 1).coerceAtMost(5) }
  }

  fun prevMorningStep() {
    stopChakraJourney()
    stopChakraSequence()
    pauseHooponopono()
    _activeMorningStepIndex.update { (it - 1).coerceAtLeast(0) }
  }

  fun selectBook(book: ManifestationBook?) {
    _selectedBook.value = book
  }

  fun showToast(msg: String) {
    _toastMessage.value = msg
  }

  fun clearToast() {
    _toastMessage.value = null
  }

  // --- 7 CHAKRAS SOUND METHODS (Pure Solfeggio Frequencies) ---
  fun selectChakra(chakra: ChakraInfo) {
    _selectedChakra.value = chakra
  }

  fun playChakraTone(chakra: ChakraInfo) {
    chakraToneJob?.cancel()
    _selectedChakra.value = chakra
    _playingChakraId.value = chakra.id
    soundEngine.playChakraSound(
      freqHz = chakra.frequencyHz,
      seedMantra = chakra.seedMantra,
      chakraName = chakra.name,
      durationSeconds = 5.0,
      includeMantraChant = false
    )
    chakraToneJob = viewModelScope.launch {
      delay(5000)
      if (_playingChakraId.value == chakra.id && !_isChakraJourneyPlaying.value && !_isChakraSequencePlaying.value) {
        _playingChakraId.value = null
      }
    }
  }

  fun chantMantraSolo(chakra: ChakraInfo) {
    // Plays the pure singing bowl Solfeggio frequency for this chakra
    playChakraTone(chakra)
  }

  fun playAllChakrasSequence() {
    if (_isChakraSequencePlaying.value) {
      stopChakraSequence()
      return
    }

    stopChakraJourney()
    stopChakraSequence()

    _isChakraSequencePlaying.value = true
    chakraSequenceJob = viewModelScope.launch {
      try {
        for (chakra in chakrasList) {
          if (!_isChakraSequencePlaying.value) break
          _selectedChakra.value = chakra
          _playingChakraId.value = chakra.id
          soundEngine.playChakraSound(
            freqHz = chakra.frequencyHz,
            seedMantra = chakra.seedMantra,
            chakraName = chakra.name,
            durationSeconds = 5.0,
            includeMantraChant = false
          )
          delay(5000)
        }
      } finally {
        _isChakraSequencePlaying.value = false
        _playingChakraId.value = null
      }
    }
  }

  fun stopChakraSequence() {
    _isChakraSequencePlaying.value = false
    chakraSequenceJob?.cancel()
    chakraSequenceJob = null
    _playingChakraId.value = null
    soundEngine.stopChakraSound()
  }

  /**
   * Cumulative Slow Chakra Journey with 432 Hz Meditative Ambient Background Music.
   * Plays each of the 7 chakra frequencies sequentially from Root (396Hz) to Crown (963Hz) slowly.
   * In each step, the meditating body image highlights that chakra, and the description
   * card dynamically changes to display the active chakra, frequency, and affirmation.
   */
  fun toggleSlowChakraJourney() {
    if (_isChakraJourneyPlaying.value) {
      stopChakraJourney()
      return
    }

    stopChakraJourney()
    stopChakraSequence()

    _isChakraJourneyPlaying.value = true
    _isChakraSequencePlaying.value = false
    _isAmbientBgActive.value = true

    // Start 432 Hz continuous meditative ambient background music
    soundEngine.start432HzAmbient {
      _isAmbientBgActive.value = true
    }

    chakraJourneyJob = viewModelScope.launch {
      try {
        val ascendingChakras = chakrasList.sortedBy { it.id }
        for (chakra in ascendingChakras) {
          if (!_isChakraJourneyPlaying.value) break

          // Dynamically highlight chakra on image & update description
          _selectedChakra.value = chakra
          _playingChakraId.value = chakra.id
          _chakraJourneyStep.value = chakra.id

          // Play authentic pure Solfeggio frequency sound over 432Hz BG
          soundEngine.playChakraSound(
            freqHz = chakra.frequencyHz,
            seedMantra = chakra.seedMantra,
            chakraName = chakra.name,
            durationSeconds = 7.0,
            includeMantraChant = false
          )

          // Meditative slow pacing: 7.5 seconds per chakra center
          delay(7500)
        }
      } finally {
        stopChakraJourney()
      }
    }
  }

  fun stopChakraJourney() {
    _isChakraJourneyPlaying.value = false
    chakraJourneyJob?.cancel()
    chakraJourneyJob = null
    _playingChakraId.value = null
    _chakraJourneyStep.value = 0
    _isAmbientBgActive.value = false
    soundEngine.stopChakraSound()
    soundEngine.stopContinuousTone()
  }

  fun toggleAmbientMusic() {
    if (_isAmbientBgActive.value) {
      soundEngine.stopContinuousTone()
      _isAmbientBgActive.value = false
    } else {
      _isAmbientBgActive.value = true
      soundEngine.start432HzAmbient {
        _isAmbientBgActive.value = true
      }
    }
  }

  fun stopAllAudio() {
    stopChakraJourney()
    stopChakraSequence()
    pauseHooponopono()
    _isAmbientBgActive.value = false
    soundEngine.stopAll()
  }

  // --- HO'OPONOPONO PRAYER & 432 HZ ENGINE ---
  fun setHooponoponoAudioMode(mode: HooponoponoAudioMode) {
    _hooponoponoAudioMode.value = mode
    if (_isHooponoponoPlaying.value) {
      // Re-trigger current audio mode
      when (mode) {
        HooponoponoAudioMode.BGM_ONLY_432HZ -> {
          soundEngine.stopVoiceRecitation()
          soundEngine.start432HzAmbient()
        }
        HooponoponoAudioMode.BGM_PLUS_VOICE_432HZ -> {
          soundEngine.startHooponoponoVoiceLoop(includeVoice = true)
        }
        HooponoponoAudioMode.OFF -> {
          soundEngine.stopAll()
        }
      }
    }
  }

  fun toggleHooponoponoPlayback() {
    if (_isHooponoponoPlaying.value) {
      pauseHooponopono()
    } else {
      startHooponopono()
    }
  }

  private fun startHooponopono() {
    _isHooponoponoPlaying.value = true
    when (_hooponoponoAudioMode.value) {
      HooponoponoAudioMode.BGM_ONLY_432HZ -> soundEngine.start432HzAmbient()
      HooponoponoAudioMode.BGM_PLUS_VOICE_432HZ -> soundEngine.startHooponoponoVoiceLoop(includeVoice = true)
      HooponoponoAudioMode.OFF -> {}
    }

    timerJob?.cancel()
    timerJob = viewModelScope.launch {
      while (_hooponoponoTimerSeconds.value > 0 && _isHooponoponoPlaying.value) {
        delay(1000)
        _hooponoponoTimerSeconds.update { (it - 1).coerceAtLeast(0) }
      }
      if (_hooponoponoTimerSeconds.value == 0) {
        pauseHooponopono()
        showToast("Ho'oponopono sacred prayer complete. Peace be with you ✨")
      }
    }
  }

  fun pauseHooponopono() {
    _isHooponoponoPlaying.value = false
    timerJob?.cancel()
    soundEngine.stopAll()
  }

  fun resetHooponoponoTimer() {
    pauseHooponopono()
    _hooponoponoTimerSeconds.value = 300
  }

  // Repository actions with automatic background Supabase sync
  fun updateNickname(nickname: String) = repository.updateNickname(nickname)
  fun updateMood(mood: String) = repository.updateMood(mood)
  fun updateIntention(intent: String) = repository.updateIntention(intent)
  fun updateWakeUpTime(time: String) = repository.updateWakeUpTime(time)

  fun toggleGratitudeItem(id: Int) {
    repository.toggleGratitudeItem(id)
    pushGratitudeInBackground()
  }

  fun addGratitudeItem(blessing: String, reason: String) {
    repository.addGratitudeItem(blessing, reason)
    pushGratitudeInBackground()
  }

  fun addMagicSuggestion(blessing: String, reason: String) {
    repository.addMagicSuggestion(blessing, reason)
    pushGratitudeInBackground()
    showToast("Added blessing from today's 'The Magic' lesson ✨")
  }

  fun refreshTodayBlessings(advanceToNextDay: Boolean = false) {
    repository.refreshTodayBlessings(advanceToNextDay)
    pushGratitudeInBackground()
    showToast("Blessings refreshed for Day ${repository.currentMagicDay.value} of 'The Magic' ✨")
  }

  fun setWaterIntent(intent: String) {
    repository.setWaterIntent(intent)
    pushWaterInBackground()
  }

  fun takeWaterSip() {
    repository.takeWaterSip()
    pushWaterInBackground()
  }

  fun resetWaterSips() {
    repository.resetWaterSips()
    pushWaterInBackground()
  }

  fun saveNightReflection(bestBlessing: String) {
    repository.saveNightReflection(bestBlessing)
    if (supabaseService.state.value.isAuthenticated) {
      viewModelScope.launch {
        supabaseService.syncNightReflection(bestBlessing)
        supabaseService.syncUpProfile(
          streakDays = repository.userProfile.value.streakDays,
          completedDates = repository.completedDates.value,
          waterIntent = repository.waterIntent.value,
          waterSips = repository.waterSipsTaken.value
        )
        supabaseService.logCompletedSession("night")
      }
    }
  }

  fun completeMorningRoutine() {
    repository.completeMorningRoutine()
    if (supabaseService.state.value.isAuthenticated) {
      viewModelScope.launch {
        supabaseService.syncUpProfile(
          streakDays = repository.userProfile.value.streakDays,
          completedDates = repository.completedDates.value,
          waterIntent = repository.waterIntent.value,
          waterSips = repository.waterSipsTaken.value
        )
        supabaseService.syncGratitude(repository.gratitudeList.value)
        supabaseService.logCompletedSession("morning")
      }
    }
  }

  fun registerUser(name: String, email: String) = repository.registerUser(name, email)
  fun loginUser(email: String, isFirstTime: Boolean = false) = repository.loginUser(email, isFirstTime)
  fun resetStreakToZero() = repository.resetStreakToZero()
  fun refreshTimeZoneAndDate() = repository.refreshTimeZoneAndDate()
  fun logoutUser() = repository.logoutUser()

  // --- SUPABASE CLOUD SYNC ACTIONS ---

  fun configureSupabase(url: String, anonKey: String) {
    supabaseService.configure(url, anonKey)
    showToast("Supabase credentials saved ✨")
  }

  fun disconnectSupabase() {
    supabaseService.clearConfiguration()
    showToast("Disconnected from Supabase")
  }

  fun signInSupabase(
    email: String,
    password: String,
    onSuccess: () -> Unit = {},
    onError: (String) -> Unit = {}
  ) {
    viewModelScope.launch {
      val result = supabaseService.signIn(email, password)
      result.onSuccess { user ->
        syncDownFromCloud()
        showToast("Signed in & synced with Supabase ✨")
        onSuccess()
      }.onFailure { err ->
        onError(err.message ?: "Sign in failed. Please check your credentials.")
      }
    }
  }

  fun signUpSupabase(
    email: String,
    password: String,
    onSuccess: () -> Unit = {},
    onError: (String) -> Unit = {}
  ) {
    viewModelScope.launch {
      val result = supabaseService.signUp(email, password)
      result.onSuccess { user ->
        if (supabaseService.state.value.isAuthenticated) {
          pushAllToCloud()
          showToast("Account created & synced ✨")
        } else {
          showToast("Account created! Check your email to confirm ✨")
        }
        onSuccess()
      }.onFailure { err ->
        onError(err.message ?: "Sign up failed. Please check your details.")
      }
    }
  }

  fun recoverPasswordSupabase(email: String, onResult: (String) -> Unit) {
    viewModelScope.launch {
      val result = supabaseService.recoverPassword(email)
      result.onSuccess {
        onResult("Reset link sent to $email! Check your inbox.")
      }.onFailure { err ->
        onResult(err.message ?: "Could not send reset email.")
      }
    }
  }

  fun signOutSupabase() {
    viewModelScope.launch {
      supabaseService.signOut()
      showToast("Signed out of Supabase")
    }
  }

  fun syncNowWithCloud(onComplete: () -> Unit = {}) {
    viewModelScope.launch {
      syncDownFromCloud()
      pushAllToCloud()
      showToast("All sacred data synchronized with Supabase ☁️")
      onComplete()
    }
  }

  private suspend fun syncDownFromCloud() {
    val payload = supabaseService.syncDown() ?: return
    val cloudEmail = supabaseService.state.value.user?.email
    repository.mergeCloudPayload(payload, cloudEmail)
  }

  private suspend fun pushAllToCloud() {
    if (!supabaseService.state.value.isAuthenticated) return
    supabaseService.syncUpProfile(
      streakDays = repository.userProfile.value.streakDays,
      completedDates = repository.completedDates.value,
      waterIntent = repository.waterIntent.value,
      waterSips = repository.waterSipsTaken.value
    )
    supabaseService.syncGratitude(repository.gratitudeList.value)
    supabaseService.syncWater(repository.waterIntent.value, repository.waterSipsTaken.value)
  }

  private fun pushGratitudeInBackground() {
    if (!supabaseService.state.value.isAuthenticated) return
    viewModelScope.launch {
      supabaseService.syncGratitude(repository.gratitudeList.value)
    }
  }

  private fun pushWaterInBackground() {
    if (!supabaseService.state.value.isAuthenticated) return
    viewModelScope.launch {
      supabaseService.syncWater(repository.waterIntent.value, repository.waterSipsTaken.value)
      supabaseService.syncUpProfile(
        streakDays = repository.userProfile.value.streakDays,
        completedDates = repository.completedDates.value,
        waterIntent = repository.waterIntent.value,
        waterSips = repository.waterSipsTaken.value
      )
    }
  }

  override fun onCleared() {
    super.onCleared()
    chakraJourneyJob?.cancel()
    chakraSequenceJob?.cancel()
    chakraToneJob?.cancel()
    timerJob?.cancel()
    soundEngine.release()
  }
}
