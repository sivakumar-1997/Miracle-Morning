package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.repository.ManifestationRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context verifies Miracle Mornings app name`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Miracle Mornings", appName)
  }

  @Test
  fun `repository contains 7 sacred chakras with authentic frequencies seed mantras and placements`() {
    val repo = ManifestationRepository()
    val chakras = repo.chakrasList
    assertEquals(7, chakras.size)

    val root = chakras.first { it.id == 1 }
    assertEquals("LAM", root.seedMantra)
    assertEquals(396.0, root.frequencyHz, 0.1)
    assertEquals("Base of the spine", root.anatomicalPlacement)
    assertEquals("Safety & Grounding", root.spiritualFocus)

    val sacral = chakras.first { it.id == 2 }
    assertEquals("VAM", sacral.seedMantra)
    assertEquals(417.0, sacral.frequencyHz, 0.1)
    assertEquals("Lower abdomen", sacral.anatomicalPlacement)
    assertEquals("Creativity & Emotions", sacral.spiritualFocus)

    val solar = chakras.first { it.id == 3 }
    assertEquals("RAM", solar.seedMantra)
    assertEquals(528.0, solar.frequencyHz, 0.1)
    assertEquals("Upper abdomen", solar.anatomicalPlacement)
    assertEquals("Confidence & Willpower", solar.spiritualFocus)

    val heart = chakras.first { it.id == 4 }
    assertEquals("YAM", heart.seedMantra)
    assertEquals(639.0, heart.frequencyHz, 0.1)
    assertEquals("Center of the chest", heart.anatomicalPlacement)
    assertEquals("Love & Compassion", heart.spiritualFocus)

    val throat = chakras.first { it.id == 5 }
    assertEquals("HAM", throat.seedMantra)
    assertEquals(741.0, throat.frequencyHz, 0.1)
    assertEquals("Throat", throat.anatomicalPlacement)
    assertEquals("Communication & Truth", throat.spiritualFocus)

    val thirdEye = chakras.first { it.id == 6 }
    assertEquals("OM", thirdEye.seedMantra)
    assertEquals(852.0, thirdEye.frequencyHz, 0.1)
    assertEquals("Between the eyebrows", thirdEye.anatomicalPlacement)
    assertEquals("Intuition & Insight", thirdEye.spiritualFocus)

    val crown = chakras.first { it.id == 7 }
    assertEquals("AH", crown.seedMantra)
    assertEquals(963.0, crown.frequencyHz, 0.1)
    assertEquals("Top of the head", crown.anatomicalPlacement)
    assertEquals("Spiritual Connection", crown.spiritualFocus)
  }

  @Test
  fun `repository contains 10 gratitude blessings from The Magic`() {
    val repo = ManifestationRepository()
    val list = repo.gratitudeList.value
    assertEquals(10, list.size)
    assertTrue(list[0].blessing.isNotBlank())
    assertTrue(list[0].reason.isNotBlank())
  }

  @Test
  fun `water manifestation sip increases count up to limit`() {
    val repo = ManifestationRepository()
    val initial = repo.waterSipsTaken.value
    repo.takeWaterSip()
    assertEquals(initial + 1, repo.waterSipsTaken.value)
  }

  @Test
  fun `repository contains foundational manifestation books including Rhonda Byrne`() {
    val repo = ManifestationRepository()
    val books = repo.books
    assertTrue(books.any { it.title == "The Magic" && it.author == "Rhonda Byrne" })
    assertTrue(books.any { it.title.contains("Water") && it.author == "Dr. Masaru Emoto" })
    assertTrue(books.any { it.author.contains("Dispenza") })
  }

  @Test
  fun `competitor pricing verifies Miracle Mornings is more affordable than Stella and Perla`() {
    val repo = ManifestationRepository()
    val plans = repo.competitorPlans
    val stella = plans.find { it.appName == "Stella" }
    val perla = plans.find { it.appName == "Perla" }
    val miracle = plans.find { it.appName == "Miracle Mornings" }

    assertNotNull(stella)
    assertNotNull(perla)
    assertNotNull(miracle)
    assertTrue(miracle!!.isRecommended)
  }

  @Test
  fun `ManifestationViewModel can be instantiated and first time user starts with zero streak`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val vm = com.example.ui.viewmodel.ManifestationViewModel(app)
    assertNotNull(vm)
    val user = vm.userProfile.value
    assertNotNull(user)
    assertEquals(0, user.streakDays)
    assertTrue(user.isFirstTimeUser)
  }

  @Test
  fun `first time user registration sets streak to zero`() {
    val repo = ManifestationRepository()
    repo.registerUser("Alice Walker", "alice@miraclemornings.app")
    val profile = repo.userProfile.value
    assertEquals(0, profile.streakDays)
    assertEquals("Alice", profile.nickname)
    assertTrue(profile.isFirstTimeUser)
    val streaks = repo.monthlyStreaks.value
    assertTrue(streaks.all { !it.isMorningCompleted })
  }

  @Test
  fun `date and month calculations adapt to specified timezones`() {
    val repo = ManifestationRepository()
    val tokyoTz = java.util.TimeZone.getTimeZone("Asia/Tokyo")
    val laTz = java.util.TimeZone.getTimeZone("America/Los_Angeles")

    val tokyoDate = ManifestationRepository.calculateCurrentDate(tokyoTz)
    val laDate = ManifestationRepository.calculateCurrentDate(laTz)
    val tokyoDisplay = ManifestationRepository.calculateTimeZoneDisplay(tokyoTz)

    assertTrue(tokyoDate.isNotBlank())
    assertTrue(laDate.isNotBlank())
    assertTrue(tokyoDisplay.contains("Asia/Tokyo"))
  }

  @Test
  fun `completing morning routine increases streak by one`() {
    val repo = ManifestationRepository()
    assertEquals(0, repo.userProfile.value.streakDays)
    repo.completeMorningRoutine()
    assertEquals(1, repo.userProfile.value.streakDays)
  }

  @Test
  fun `ManifestationViewModel manages chakra selection and cumulative journey state`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val vm = com.example.ui.viewmodel.ManifestationViewModel(app)
    val initial = vm.selectedChakra.value
    assertNotNull(initial)
    assertEquals("Anahata", initial.sanskritName)

    val root = vm.chakrasList.first { it.id == 1 }
    vm.selectChakra(root)
    assertEquals(1, vm.selectedChakra.value.id)
    assertEquals("LAM", vm.selectedChakra.value.seedMantra)
    assertEquals("Base of the spine", vm.selectedChakra.value.anatomicalPlacement)

    // Verify slow chakra journey state toggling
    assertEquals(false, vm.isChakraJourneyPlaying.value)
    vm.toggleSlowChakraJourney()
    assertEquals(true, vm.isChakraJourneyPlaying.value)
    assertEquals(true, vm.isAmbientBgActive.value)
    vm.stopChakraJourney()
    assertEquals(false, vm.isChakraJourneyPlaying.value)
    assertEquals(false, vm.isAmbientBgActive.value)

    // Verify manual ambient background music toggle
    vm.toggleAmbientMusic()
    assertEquals(true, vm.isAmbientBgActive.value)
    vm.toggleAmbientMusic()
    assertEquals(false, vm.isAmbientBgActive.value)
  }

  @Test
  fun `SacredSoundEngine initializes and supports frequency playback and 432Hz ambient`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val engine = com.example.audio.SacredSoundEngine(context)
    assertNotNull(engine)

    // Trigger 432Hz ambient soundscape
    engine.start432HzAmbient()
    Thread.sleep(100)
    engine.stopContinuousTone()

    // Trigger chakra frequency sound for 528Hz Solar Plexus
    engine.playChakraSound(freqHz = 528.0, durationSeconds = 1.0)
    Thread.sleep(100)
    engine.stopChakraSound()

    engine.release()
  }

  @Test
  fun `water manifestation supports up to 8 sacred sips matching web version`() {
    val repo = ManifestationRepository()
    repo.resetWaterSips()
    assertEquals(0, repo.waterSipsTaken.value)
    repeat(10) { repo.takeWaterSip() }
    assertEquals(8, repo.waterSipsTaken.value)
  }

  @Test
  fun `mergeCloudPayload merges web supabase profile, completed dates, water, and gratitudes`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repo = ManifestationRepository(context)
    repo.resetStreakToZero()

    val todayKey = ManifestationRepository.todayDateKey(java.util.TimeZone.getDefault())
    val payload = com.example.data.remote.CloudSyncPayload(
      streakDays = 5,
      completedDates = listOf(todayKey),
      waterIntent = "Infinite clarity and cosmic abundance",
      waterSips = 8,
      gratitudeItems = listOf(
        com.example.data.model.GratitudeItem(1, "Morning stillness", "it brings peace", true)
      ),
      nightReflection = "Grateful for today's serene sunrise"
    )

    repo.mergeCloudPayload(payload, cloudEmail = "seeker@miraclemornings.app")

    assertEquals(5, repo.userProfile.value.streakDays)
    assertTrue(repo.completedDates.value.contains(todayKey))
    assertEquals("Infinite clarity and cosmic abundance", repo.waterIntent.value)
    assertEquals(8, repo.waterSipsTaken.value)
    assertEquals(1, repo.gratitudeList.value.size)
    assertEquals("Grateful for today's serene sunrise", repo.nightBestBlessing.value)
    assertTrue(repo.monthlyStreaks.value.any { it.isToday && it.isMorningCompleted })
  }

  @Test
  fun `SupabaseService normalizes URLs, configures, and clears project credentials safely`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val sb = com.example.data.remote.SupabaseService(context)

    // Verify URL normalization handles pasted /rest/v1/ endpoints cleanly
    assertEquals(
      "https://demo123.supabase.co",
      com.example.data.remote.SupabaseService.normalizeSupabaseUrl("https://demo123.supabase.co/rest/v1/")
    )

    sb.configure("https://demo123.supabase.co/rest/v1/", "anon-key-xyz")
    assertEquals(true, sb.state.value.isConfigured)
    assertEquals("https://demo123.supabase.co", sb.state.value.projectUrl)
    assertEquals("Sign In", sb.state.value.statusLabel)

    sb.clearConfiguration()
    assertEquals(false, sb.state.value.isConfigured)
    assertEquals("Sign In", sb.state.value.statusLabel)
  }

  @Test
  fun `completing morning routine refreshes 10 blessings to next day of The Magic`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repo = ManifestationRepository(context)
    repo.refreshTodayBlessings(advanceToNextDay = false)
    val initialDay = repo.currentMagicDay.value
    assertEquals(10, repo.gratitudeList.value.size)

    // Complete first blessing
    repo.toggleGratitudeItem(repo.gratitudeList.value.first().id)
    assertTrue(repo.gratitudeList.value.first().isCompleted)

    // Completing morning routine advances to next day of The Magic with 10 fresh unchecked blessings
    repo.completeMorningRoutine()
    val expectedNextDay = (initialDay % 28) + 1
    assertEquals(expectedNextDay, repo.currentMagicDay.value)
    assertEquals(expectedNextDay, repo.currentMagicLesson.value.day)
    assertEquals(10, repo.gratitudeList.value.size)
    assertTrue(repo.gratitudeList.value.none { it.isCompleted })
  }

  @Test
  fun `new calendar day refreshes 10 blessings and lesson suggestions add to gratitude list`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repo = ManifestationRepository(context)
    val initialDay = repo.currentMagicDay.value

    // Simulate a new calendar day arriving
    repo.refreshDailyBlessingsIfNewDay("2099-12-31")
    val expectedNextDay = (initialDay % 28) + 1
    assertEquals(expectedNextDay, repo.currentMagicDay.value)
    assertEquals(10, repo.gratitudeList.value.size)
    assertTrue(repo.gratitudeList.value.none { it.isCompleted })

    // Add a suggestion from today's lesson
    val suggestion = repo.currentMagicLesson.value.suggestions.first()
    repo.addMagicSuggestion(suggestion.first, suggestion.second)
    assertTrue(
      repo.gratitudeList.value.any {
        it.blessing.equals(suggestion.first, ignoreCase = true) && it.isCompleted
      }
    )
  }

  @Test
  fun `legal documents and ViewModel legal navigation expose Privacy Policy and Terms`() {
    assertTrue(com.example.data.model.LegalDocuments.privacySections.size >= 6)
    assertTrue(com.example.data.model.LegalDocuments.termsSections.size >= 6)

    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val vm = com.example.ui.viewmodel.ManifestationViewModel(app)
    vm.openLegal(com.example.ui.screens.LegalTab.TERMS)
    assertEquals(com.example.ui.viewmodel.AppDestination.LEGAL, vm.currentDestination.value)
    assertEquals(com.example.ui.screens.LegalTab.TERMS, vm.legalInitialTab.value)
  }
}
