package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.data.model.DayStreak
import com.example.ui.components.AuraCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.ManifestationViewModel

@Composable
fun StreaksScreen(
  viewModel: ManifestationViewModel
) {
  val user by viewModel.userProfile.collectAsState()
  val streaks by viewModel.monthlyStreaks.collectAsState()
  val currentMonthYear by viewModel.currentMonthYear.collectAsState()
  val timeZoneDisplayName by viewModel.timeZoneDisplayName.collectAsState()
  val currentFormattedDate by viewModel.currentFormattedDate.collectAsState()

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 18.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    contentPadding = PaddingValues(top = 10.dp, bottom = 28.dp)
  ) {
    // Top Streak Banner
    item {
      AuraCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = SageBorder
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = if (user.streakDays == 0) "0 Days Streak" else "${user.streakDays} Consecutive Days",
              style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = TextDeepForest
              )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = if (user.streakDays == 0)
                "Welcome to your Day 1 journey! Complete your morning rituals to ignite your flame."
              else
                "Unbroken Brahma Muhurta & Gratitude Alignment",
              style = MaterialTheme.typography.bodySmall.copy(color = TextMediumSage)
            )
          }

          Spacer(modifier = Modifier.width(8.dp))

          Box(
            modifier = Modifier
              .size(52.dp)
              .clip(CircleShape)
              .background(SageCardElevated)
              .border(1.5.dp, if (user.streakDays == 0) SageBorder else Color(0xFFEA580C).copy(alpha = 0.5f), CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.LocalFireDepartment,
              contentDescription = "Streak Fire",
              tint = if (user.streakDays == 0) TextMutedSage else Color(0xFFEA580C),
              modifier = Modifier.size(32.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))
        HorizontalDivider(color = SageBorder, thickness = 1.dp)
        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceAround
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = if (user.streakDays == 0) "0%" else "96%",
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = if (user.streakDays == 0) TextMutedSage else ForestButtonBg
              )
            )
            Text("Morning Brahma", style = MaterialTheme.typography.labelSmall.copy(color = TextMediumSage))
          }
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = if (user.streakDays == 0) "0%" else "90%",
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = if (user.streakDays == 0) TextMutedSage else ForestButtonBg
              )
            )
            Text("Night Magic Rock", style = MaterialTheme.typography.labelSmall.copy(color = TextMediumSage))
          }
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = if (user.streakDays == 0) "0" else "${user.streakDays * 10}+",
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = if (user.streakDays == 0) TextMutedSage else TextDeepForest
              )
            )
            Text("Blessings Logged", style = MaterialTheme.typography.labelSmall.copy(color = TextMediumSage))
          }
        }
      }
    }

    // Interactive Streak Switcher / Test Buttons (Test 0-Streak vs Active Streak)
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        OutlinedButton(
          onClick = { viewModel.resetStreakToZero() },
          modifier = Modifier
            .weight(1f)
            .heightIn(min = 44.dp),
          shape = RoundedCornerShape(12.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, SageBorder)
        ) {
          Text(
            text = "Reset to 0d (New User)",
            style = MaterialTheme.typography.labelSmall.copy(
              color = TextDeepForest,
              fontWeight = FontWeight.SemiBold
            )
          )
        }

        Button(
          onClick = { viewModel.completeMorningRoutine() },
          modifier = Modifier
            .weight(1f)
            .heightIn(min = 44.dp),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(containerColor = ForestButtonBg)
        ) {
          Icon(Icons.Default.Add, contentDescription = null, tint = ForestButtonText, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "Practice Done (+1d)",
            style = MaterialTheme.typography.labelSmall.copy(
              color = ForestButtonText,
              fontWeight = FontWeight.Bold
            )
          )
        }
      }
    }

    // Monthly Calendar Grid (Dynamically Computed to User's Timezone)
    item {
      AuraCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = SageBorder
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Monthly Streak Matrix",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextDeepForest
              )
            )
            Text(
              text = "Syncs with user's local timezone",
              style = MaterialTheme.typography.labelSmall.copy(color = TextMediumSage)
            )
          }

          Surface(
            color = SageCardElevated,
            shape = RoundedCornerShape(8.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, SageBorder)
          ) {
            Text(
              text = currentMonthYear,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = TextDeepForest
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Timezone Details Pill
        Surface(
          color = SageCardElevated,
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.fillMaxWidth(),
          border = androidx.compose.foundation.BorderStroke(1.dp, SageBorder)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Today: $currentFormattedDate",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = TextDeepForest)
              )
              Text(
                text = "Timezone: $timeZoneDisplayName",
                style = MaterialTheme.typography.labelSmall.copy(color = TextMediumSage)
              )
            }
            IconButton(
              onClick = { viewModel.refreshTimeZoneAndDate() },
              modifier = Modifier.size(36.dp)
            ) {
              Icon(Icons.Default.Refresh, contentDescription = "Refresh Timezone", tint = ForestButtonBg, modifier = Modifier.size(18.dp))
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Weekday headers (100% Responsive with weight(1f))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          listOf("S", "M", "T", "W", "T", "F", "S").forEach { day ->
            Text(
              text = day,
              style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextMutedSage
              ),
              modifier = Modifier.weight(1f),
              textAlign = TextAlign.Center
            )
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Calendar chips in 7-column rows (Responsive without clipping)
        val chunkedDays = streaks.chunked(7)
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
          chunkedDays.forEach { week ->
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              week.forEach { dayStreak ->
                Box(
                  modifier = Modifier.weight(1f),
                  contentAlignment = Alignment.Center
                ) {
                  DayCalendarCell(dayStreak = dayStreak)
                }
              }
              if (week.size < 7) {
                repeat(7 - week.size) {
                  Spacer(modifier = Modifier.weight(1f))
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(ForestButtonBg))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Morning Done", style = MaterialTheme.typography.labelSmall.copy(color = TextMediumSage))
          }
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(LimePillAccent))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Night Reflection", style = MaterialTheme.typography.labelSmall.copy(color = TextMediumSage))
          }
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(10.dp).clip(RoundedCornerShape(2.dp)).border(1.dp, ForestButtonBg))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Today", style = MaterialTheme.typography.labelSmall.copy(color = TextMediumSage))
          }
        }
      }
    }

    // Aura Progression
    item {
      AuraCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = SageBorder
      ) {
        Text(
          text = "Aura Radiance Evolution",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = TextDeepForest
          )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Current: ${user.auraLevel}",
          style = MaterialTheme.typography.bodySmall.copy(color = TextMediumSage, fontWeight = FontWeight.SemiBold)
        )
        Spacer(modifier = Modifier.height(8.dp))
        val progress = when {
          user.streakDays >= 28 -> 1.0f
          user.streakDays >= 14 -> 0.75f
          user.streakDays >= 7 -> 0.50f
          user.streakDays >= 3 -> 0.25f
          user.streakDays >= 1 -> 0.10f
          else -> 0.03f
        }
        LinearProgressIndicator(
          progress = { progress },
          modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .clip(RoundedCornerShape(4.dp)),
          color = ForestButtonBg,
          trackColor = SageCardElevated
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = if (user.streakDays == 0)
            "Complete your first morning Brahma Muhurta routine to start Level 1 ✨"
          else
            "Keep your sacred consistency to unlock the next Aura tier ✨",
          style = MaterialTheme.typography.labelSmall.copy(color = TextMediumSage)
        )
      }
    }
  }
}

@Composable
fun DayCalendarCell(dayStreak: DayStreak) {
  val isCompleted = dayStreak.isMorningCompleted
  val isToday = dayStreak.isToday
  val isFuture = dayStreak.isFuture

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .aspectRatio(1f)
      .clip(RoundedCornerShape(10.dp))
      .background(
        when {
          isToday && isCompleted -> ForestButtonBg
          isCompleted -> ForestButtonBg
          isToday -> LimePillAccent.copy(alpha = 0.45f)
          else -> SageCardSurface
        }
      )
      .border(
        width = if (isToday) 2.dp else if (isCompleted) 1.5.dp else 1.dp,
        color = when {
          isToday && isCompleted -> LimePillAccent
          isToday -> ForestButtonBg
          isCompleted -> LimePillAccent.copy(alpha = 0.8f)
          else -> SageBorder
        },
        shape = RoundedCornerShape(10.dp)
      ),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Text(
        text = "${dayStreak.dayOfMonth}",
        style = MaterialTheme.typography.labelSmall.copy(
          fontWeight = if (isToday || isCompleted) FontWeight.ExtraBold else FontWeight.Normal,
          color = when {
            isCompleted -> ForestButtonText
            isToday -> ForestButtonBg
            isFuture -> TextMutedSage
            else -> TextDeepForest
          }
        )
      )
      if (isCompleted && !isFuture) {
        Row(
          horizontalArrangement = Arrangement.spacedBy(2.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Completed",
            tint = LimePillAccent,
            modifier = Modifier.size(9.dp)
          )
          if (dayStreak.isNightCompleted) {
            Box(
              modifier = Modifier
                .size(4.dp)
                .clip(CircleShape)
                .background(LimePillAccent)
            )
          }
        }
      } else if (isToday) {
        Box(
          modifier = Modifier
            .size(4.dp)
            .clip(CircleShape)
            .background(ForestButtonBg)
        )
      }
    }
  }
}
