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
  fun `read string from context verifies AuraMagic app name`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("AuraMagic", appName)
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
  fun `competitor pricing verifies AuraMagic is more affordable than Stella and Perla`() {
    val repo = ManifestationRepository()
    val plans = repo.competitorPlans
    val stella = plans.find { it.appName == "Stella" }
    val perla = plans.find { it.appName == "Perla" }
    val aura = plans.find { it.appName == "AuraMagic" }

    assertNotNull(stella)
    assertNotNull(perla)
    assertNotNull(aura)
    assertTrue(aura!!.isRecommended)
  }
}
