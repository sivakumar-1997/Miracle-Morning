package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DayStreak
import com.example.ui.components.AuraCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.ManifestationViewModel

@Composable
fun StreaksScreen(
  viewModel: ManifestationViewModel
) {
  val user by viewModel.userProfile.collectAsState()
  val streaks = viewModel.monthlyStreaks

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    contentPadding = PaddingValues(top = 10.dp, bottom = 28.dp)
  ) {
    // Top Streak Banner
    item {
      AuraCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = RosePrimary
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Column {
            Text(
              text = "${user.streakDays} Consecutive Days",
              style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = RoseDeep
              )
            )
            Text(
              text = "Unbroken Brahma Muhurta & Gratitude Alignment",
              style = MaterialTheme.typography.bodySmall.copy(color = BlueDeep)
            )
          }

          Box(
            modifier = Modifier
              .size(52.dp)
              .clip(CircleShape)
              .background(RoseMist)
              .border(1.5.dp, RosePrimary, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.LocalFireDepartment,
              contentDescription = "Streak Fire",
              tint = RosePrimary,
              modifier = Modifier.size(32.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))
        HorizontalDivider(color = CardBorder, thickness = 0.5.dp)
        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceAround
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("94%", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = BlueDeep))
            Text("Morning Brahma", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
          }
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("88%", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = RoseDeep))
            Text("Night Magic Rock", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
          }
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("180+", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = AuraGold))
            Text("Blessings Written", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
          }
        }
      }
    }

    // Monthly Calendar Grid (October 2026)
    item {
      AuraCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = BlueSky
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "October 2026 Streak Calendar",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = TextDark
            )
          )
          Surface(
            color = BlueMist,
            shape = RoundedCornerShape(8.dp)
          ) {
            Text(
              text = "Day 18 of 31",
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = BlueDeep
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Weekday headers
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceAround
        ) {
          listOf("M", "T", "W", "T", "F", "S", "S").forEach { day ->
            Text(
              text = day,
              style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextMuted
              ),
              modifier = Modifier.width(32.dp),
              textAlign = TextAlign.Center
            )
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 31-day calendar chips rendered in 7-column layout
        val chunkedDays = streaks.chunked(7)
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
          chunkedDays.forEach { week ->
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceAround
            ) {
              week.forEach { dayStreak ->
                DayCalendarCell(dayStreak = dayStreak)
              }
              // Fill remaining week slots if last row has less than 7 days
              if (week.size < 7) {
                repeat(7 - week.size) {
                  Spacer(modifier = Modifier.width(36.dp))
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
            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(RosePrimary))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Morning Done", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
          }
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(BlueDeep))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Night Magic Rock", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
          }
        }
      }
    }

    // Aura Radiance Evolution
    item {
      AuraCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = AuraGold
      ) {
        Text(
          text = "Aura Bloom Progression",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = RoseDeep
          )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Current: ${user.auraLevel}",
          style = MaterialTheme.typography.bodySmall.copy(color = TextDark, fontWeight = FontWeight.SemiBold)
        )
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
          progress = { 0.75f },
          modifier = Modifier
            .fillMaxWidth()
            .height(10.dp)
            .clip(RoundedCornerShape(5.dp)),
          color = AuraGold,
          trackColor = AuraGoldSoft
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "3 more consecutive days to unlock Level 5: Cosmic Luminary ✨",
          style = MaterialTheme.typography.labelSmall.copy(color = BlueDeep)
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
      .size(36.dp)
      .clip(RoundedCornerShape(10.dp))
      .background(
        when {
          isToday -> RoseMist
          isCompleted -> BlueMist.copy(alpha = 0.6f)
          else -> Color(0xFFF1F5F9)
        }
      )
      .border(
        width = if (isToday) 1.5.dp else 0.5.dp,
        color = if (isToday) RosePrimary else if (isCompleted) BlueSky else Color.Transparent,
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
          fontWeight = if (isToday || isCompleted) FontWeight.Bold else FontWeight.Normal,
          color = if (isToday) RoseDeep else if (isFuture) TextMuted.copy(alpha = 0.5f) else TextDark
        )
      )
      if (isCompleted && !isFuture) {
        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
          Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(RosePrimary))
          if (dayStreak.isNightCompleted) {
            Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(BlueDeep))
          }
        }
      }
    }
  }
}
