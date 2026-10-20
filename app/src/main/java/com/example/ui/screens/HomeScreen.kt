package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.ui.components.AuraCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppDestination
import com.example.ui.viewmodel.ManifestationViewModel

@Composable
fun HomeScreen(
  viewModel: ManifestationViewModel,
  onNavigate: (AppDestination) -> Unit
) {
  val user by viewModel.userProfile.collectAsState()
  val gratitudeList by viewModel.gratitudeList.collectAsState()
  val waterSips by viewModel.waterSipsTaken.collectAsState()
  val waterIntent by viewModel.waterIntent.collectAsState()
  val isFrequencyPlaying by viewModel.isFrequencyPlaying.collectAsState()
  val selectedFrequency by viewModel.selectedFrequencyHz.collectAsState()

  val completedGratitudes = gratitudeList.count { it.isCompleted }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    contentPadding = PaddingValues(top = 10.dp, bottom = 28.dp)
  ) {
    // Top Greeting & Aura Badge Card
    item {
      AuraCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = RoseBlush
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Good Morning, ${user.nickname} ✨",
              style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = RoseDeep
              )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = "Vibrational State: ${user.currentMood}",
              style = MaterialTheme.typography.bodySmall.copy(color = BlueDeep)
            )
            Text(
              text = "Intention: ${user.primaryIntention}",
              style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
            )
          }

          Box(
            modifier = Modifier
              .size(54.dp)
              .clip(CircleShape)
              .background(
                Brush.radialGradient(
                  colors = listOf(RoseMist, BlueSky)
                )
              )
              .border(1.5.dp, AuraGold, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.SelfImprovement,
              contentDescription = "Aura Bloom",
              tint = RoseDeep,
              modifier = Modifier.size(28.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(color = CardBorder, thickness = 0.5.dp)
        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.LocalFireDepartment,
              contentDescription = "Streak",
              tint = RosePrimary,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "${user.streakDays} Day Streak",
              style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                color = TextDark
              )
            )
          }

          Surface(
            color = RoseMist,
            shape = RoundedCornerShape(8.dp)
          ) {
            Text(
              text = user.auraLevel,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = RoseDeep
              )
            )
          }
        }
      }
    }

    // Brahma Muhurta Sacred Hour Banner
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .border(1.dp, AuraGold.copy(alpha = 0.5f), RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = AuraGoldSoft.copy(alpha = 0.8f))
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(44.dp)
              .clip(CircleShape)
              .background(AuraGold),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.WbTwilight,
              contentDescription = null,
              tint = WhitePure,
              modifier = Modifier.size(24.dp)
            )
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Brahma Muhurta (4:30 AM - 5:00 AM)",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextDark
              )
            )
            Text(
              text = "The cosmic hour of creation. Pineal gland melatonin and alpha brainwaves are naturally harmonized.",
              style = MaterialTheme.typography.bodySmall.copy(color = TextDark.copy(alpha = 0.75f))
            )
          }
        }
      }
    }

    // Main Morning Routine Flow Card
    item {
      AuraCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = RosePrimary
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "Morning Manifestation Rituals",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = RoseDeep
              )
            )
            Text(
              text = "Aura Cleansing • Prayer • Water • Frequencies • 10 Blessings",
              style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
            )
          }
          Icon(
            imageVector = Icons.Default.WbSunny,
            contentDescription = null,
            tint = RosePrimary
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Progress bar (e.g. 50% completed)
        Column(modifier = Modifier.fillMaxWidth()) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = "Daily Routine Progress",
              style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
            )
            Text(
              text = "3 of 6 Steps Done",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = RoseDeep
              )
            )
          }
          Spacer(modifier = Modifier.height(6.dp))
          LinearProgressIndicator(
            progress = { 0.5f },
            modifier = Modifier
              .fillMaxWidth()
              .height(8.dp)
              .clip(RoundedCornerShape(4.dp)),
            color = RosePrimary,
            trackColor = RoseMist
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Button(
          onClick = { onNavigate(AppDestination.MORNING_ROUTINE) },
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("home_start_morning_btn"),
          shape = RoundedCornerShape(14.dp),
          colors = ButtonDefaults.buttonColors(containerColor = RosePrimary)
        ) {
          Icon(Icons.Default.PlayArrow, contentDescription = null, tint = WhitePure)
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Enter Morning Ritual Flow",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = WhitePure
            )
          )
        }
      }
    }

    // Water Manifestation Quick Widget
    item {
      AuraCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = BlueSky
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(BlueMist),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.WaterDrop,
                contentDescription = null,
                tint = BlueDeep
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "Water Manifestation Alchemy",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = BlueDeep
                )
              )
              Text(
                text = "Drink sip by sip with intention",
                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
              )
            }
          }

          Text(
            text = "$waterSips / 7 Sips",
            style = MaterialTheme.typography.labelLarge.copy(
              fontWeight = FontWeight.Bold,
              color = BlueDeep
            )
          )
        }

        Spacer(modifier = Modifier.height(10.dp))
        Surface(
          color = BlueSoft,
          shape = RoundedCornerShape(12.dp)
        ) {
          Text(
            text = "“$waterIntent”",
            modifier = Modifier.padding(10.dp),
            style = MaterialTheme.typography.bodySmall.copy(
              fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
              color = TextDark
            )
          )
        }

        Spacer(modifier = Modifier.height(10.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Button(
            onClick = { viewModel.takeWaterSip() },
            modifier = Modifier
              .weight(1f)
              .height(40.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BlueDeep)
          ) {
            Icon(Icons.Default.Water, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Take 1 Sip (+ Intention)")
          }

          OutlinedButton(
            onClick = { onNavigate(AppDestination.MORNING_ROUTINE) },
            modifier = Modifier.height(40.dp),
            shape = RoundedCornerShape(10.dp)
          ) {
            Text("Full Ritual")
          }
        }
      }
    }

    // 10 Gratitude Blessings (The Magic by Rhonda Byrne)
    item {
      AuraCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = RoseBlush
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Column {
            Text(
              text = "Count 10 Blessings (The Magic)",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = RoseDeep
              )
            )
            Text(
              text = "$completedGratitudes of 10 written today",
              style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
            )
          }

          IconButton(
            onClick = { onNavigate(AppDestination.MORNING_ROUTINE) }
          ) {
            Icon(
              imageVector = Icons.Default.EditNote,
              contentDescription = "Edit 10 Blessings",
              tint = RosePrimary
            )
          }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "“I am truly blessed to have [WHAT], because [WHY]. Thank you, thank you, thank you!”",
          style = MaterialTheme.typography.bodySmall.copy(
            color = RoseDeep,
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
          )
        )

        Spacer(modifier = Modifier.height(10.dp))
        LinearProgressIndicator(
          progress = { (completedGratitudes / 10f).coerceIn(0f, 1f) },
          modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(RoundedCornerShape(3.dp)),
          color = RosePrimary,
          trackColor = RoseMist
        )
      }
    }

    // Brain Frequency Tuning & Positive Book Wisdom
    item {
      AuraCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = BlueSky
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Headphones,
              contentDescription = null,
              tint = BlueDeep
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = "528 Hz Love & Miracle Tone",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = TextDark
                )
              )
              Text(
                text = "Solfeggio Frequency • Cellular DNA Repair",
                style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
              )
            }
          }

          FilledTonalIconButton(
            onClick = { viewModel.toggleFrequencyAudio() },
            colors = IconButtonDefaults.filledTonalIconButtonColors(
              containerColor = if (isFrequencyPlaying) RosePrimary else BlueMist
            )
          ) {
            Icon(
              imageVector = if (isFrequencyPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
              contentDescription = "Play Frequency",
              tint = if (isFrequencyPlaying) WhitePure else BlueDeep
            )
          }
        }

        if (isFrequencyPlaying) {
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "✨ Harmonizing brainwaves into Alpha coherence (8-12 Hz) with 528 Hz Solfeggio resonance...",
            style = MaterialTheme.typography.bodySmall.copy(color = BlueDeep)
          )
        }
      }
    }

    // Night Routine Teaser Card (Preview next day + Magic Rock)
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .border(1.dp, BlueNavy.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
          .clickable { onNavigate(AppDestination.NIGHT_ROUTINE) },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(44.dp)
              .clip(CircleShape)
              .background(Color(0xFF334155)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.NightsStay,
              contentDescription = null,
              tint = BlueSky,
              modifier = Modifier.size(24.dp)
            )
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Night Routine • Magic Rock",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = WhitePure
              )
            )
            Text(
              text = "Preview tomorrow's practice & recall today's greatest blessing before sleep.",
              style = MaterialTheme.typography.bodySmall.copy(color = BlueSky.copy(alpha = 0.8f))
            )
          }
          Icon(
            imageVector = Icons.Default.ArrowForwardIos,
            contentDescription = null,
            tint = BlueSky,
            modifier = Modifier.size(16.dp)
          )
        }
      }
    }

    // Competitive Advantage Card (vs Stella & Perla)
    item {
      AuraCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = AuraGold
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "AuraMagic vs Stella & Perla",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = RoseDeep
              )
            )
            Text(
              text = "$3.99/mo vs Stella ($9.99/mo) and Perla ($7.99/mo) — 60% more affordable with complete Brahma & Water rituals.",
              style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
            )
          }
          Button(
            onClick = { onNavigate(AppDestination.PRICING) },
            colors = ButtonDefaults.buttonColors(containerColor = AuraGold),
            shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
          ) {
            Text("Compare", color = TextDark, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
