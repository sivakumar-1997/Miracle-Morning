package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.data.model.*
import com.example.data.repository.ManifestationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class AppDestination(val label: String) {
  HOME("Home"),
  MORNING_ROUTINE("Morning"),
  NIGHT_ROUTINE("Night"),
  STREAKS("Streaks"),
  BOOKS("Books"),
  PRICING("Pricing"),
  WIREFRAME_VIEWER("2D Wireframe"),
  ONBOARDING("Onboarding"),
  AUTH("Auth")
}

enum class AuthSubMode {
  LOGIN,
  REGISTER,
  FORGOT_PASSWORD
}

class ManifestationViewModel(
  private val repository: ManifestationRepository = ManifestationRepository()
) : ViewModel() {

  val userProfile = repository.userProfile
  val gratitudeList = repository.gratitudeList
  val selectedFrequencyHz = repository.selectedFrequencyHz
  val isFrequencyPlaying = repository.isFrequencyPlaying
  val waterIntent = repository.waterIntent
  val waterSipsTaken = repository.waterSipsTaken
  val nightBestBlessing = repository.nightBestBlessing
  val nightCompleted = repository.nightCompleted
  val books = repository.books
  val monthlyStreaks = repository.monthlyStreaks
  val competitorPlans = repository.competitorPlans

  // Navigation State
  private val _currentDestination = MutableStateFlow(AppDestination.HOME)
  val currentDestination: StateFlow<AppDestination> = _currentDestination.asStateFlow()

  private val _navigationStack = MutableStateFlow(listOf(AppDestination.HOME))
  val navigationStack: StateFlow<List<AppDestination>> = _navigationStack.asStateFlow()

  private val _authMode = MutableStateFlow(AuthSubMode.LOGIN)
  val authMode: StateFlow<AuthSubMode> = _authMode.asStateFlow()

  // 2D Wireframe Blueprint Mode Overlay Toggle
  private val _isWireframeOverlay = MutableStateFlow(false)
  val isWireframeOverlay: StateFlow<Boolean> = _isWireframeOverlay.asStateFlow()

  // Morning routine stepped flow index (0..5)
  private val _activeMorningStepIndex = MutableStateFlow(0)
  val activeMorningStepIndex: StateFlow<Int> = _activeMorningStepIndex.asStateFlow()

  // Selected book for details modal
  private val _selectedBook = MutableStateFlow<ManifestationBook?>(null)
  val selectedBook: StateFlow<ManifestationBook?> = _selectedBook.asStateFlow()

  // Message toast banner
  private val _toastMessage = MutableStateFlow<String?>(null)
  val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

  fun navigateTo(dest: AppDestination) {
    _navigationStack.update { it + dest }
    _currentDestination.value = dest
  }

  fun navigateBack(): Boolean {
    val current = _navigationStack.value
    return if (current.size > 1) {
      val newStack = current.dropLast(1)
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

  fun toggleWireframeOverlay() {
    _isWireframeOverlay.update { !it }
  }

  fun setMorningStep(index: Int) {
    _activeMorningStepIndex.value = index.coerceIn(0, 5)
  }

  fun nextMorningStep() {
    _activeMorningStepIndex.update { (it + 1).coerceAtMost(5) }
  }

  fun prevMorningStep() {
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

  // Repository actions
  fun updateNickname(nickname: String) = repository.updateNickname(nickname)
  fun updateMood(mood: String) = repository.updateMood(mood)
  fun updateIntention(intent: String) = repository.updateIntention(intent)
  fun updateWakeUpTime(time: String) = repository.updateWakeUpTime(time)
  fun toggleGratitudeItem(id: Int) = repository.toggleGratitudeItem(id)
  fun updateGratitudeText(id: Int, blessing: String, reason: String) = repository.updateGratitudeText(id, blessing, reason)
  fun addGratitudeItem(blessing: String, reason: String) = repository.addGratitudeItem(blessing, reason)
  fun setFrequency(hz: Int) = repository.setFrequency(hz)
  fun toggleFrequencyAudio() = repository.toggleFrequencyAudio()
  fun setWaterIntent(intent: String) = repository.setWaterIntent(intent)
  fun takeWaterSip() = repository.takeWaterSip()
  fun resetWaterSips() = repository.resetWaterSips()
  fun saveNightReflection(bestBlessing: String) = repository.saveNightReflection(bestBlessing)
  fun loginUser(email: String) = repository.loginUser(email)
  fun logoutUser() = repository.logoutUser()
}
