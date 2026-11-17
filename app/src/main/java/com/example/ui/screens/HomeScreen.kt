package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.DayStreak
import com.example.ui.components.AuraCard
import com.example.ui.components.SoftBlueButton
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
  val currentFormattedDate by viewModel.currentFormattedDate.collectAsState()
  val timeZoneDisplayName by viewModel.timeZoneDisplayName.collectAsState()
  val currentWeekDays by viewModel.currentWeekDays.collectAsState()

  val completedGratitudes = gratitudeList.count { it.isCompleted }
  val monthlyStreaks by viewModel.monthlyStreaks.collectAsState()
  val currentMonthYear by viewModel.currentMonthYear.collectAsState()
  var showStreakCalendarDialog by remember { mutableStateOf(false) }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 18.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    contentPadding = PaddingValues(top = 10.dp, bottom = 28.dp)
  ) {
    // Top User Greeting & Streak Pill (Matching Reference Screenshot)
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "Welcome back, ${user.nickname} ✨",
            style = MaterialTheme.typography.headlineMedium.copy(
              fontWeight = FontWeight.Bold,
              color = TextDeepForest
            )
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = currentFormattedDate,
            style = MaterialTheme.typography.bodyMedium.copy(
              color = ForestButtonBg,
              fontWeight = FontWeight.Bold
            )
          )
          Text(
            text = "Local Timezone: $timeZoneDisplayName",
            style = MaterialTheme.typography.labelSmall.copy(color = TextMediumSage)
          )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Streak Fire Pill (Tap to view interactive Monthly Calendar with completed days highlighted)
        Surface(
          color = SageCardElevated,
          shape = RoundedCornerShape(16.dp),
          border = androidx.compose.foundation.BorderStroke(
            1.5.dp,
            if (user.streakDays == 0) SageBorder else Color(0xFFEA580C).copy(alpha = 0.5f)
          ),
          modifier = Modifier
            .clickable { showStreakCalendarDialog = true }
            .testTag("home_streak_pill")
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.LocalFireDepartment,
              contentDescription = "Streak Fire - Tap to open calendar",
              tint = if (user.streakDays == 0) TextMutedSage else Color(0xFFEA580C),
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "${user.streakDays}d",
              style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                color = if (user.streakDays == 0) TextMediumSage else TextDeepForest
              )
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
              imageVector = Icons.Default.CalendarMonth,
              contentDescription = "View Calendar",
              tint = if (user.streakDays == 0) TextMutedSage else ForestButtonBg,
              modifier = Modifier.size(14.dp)
            )
          }
        }
      }
    }

    // First-Time User Welcome Banner if streak is 0
    if (user.streakDays == 0) {
      item {
        Surface(
          color = SageCardElevated,
          shape = RoundedCornerShape(14.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, LimePillAccent)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = null,
              tint = ForestButtonBg,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Day 1 Awakening (Streak: 0d)",
                style = MaterialTheme.typography.labelLarge.copy(
                  fontWeight = FontWeight.Bold,
                  color = TextDeepForest
                )
              )
              Text(
                text = "Begin your morning Brahma Muhurta routine below to ignite your first daily streak!",
                style = MaterialTheme.typography.bodySmall.copy(color = TextMediumSage)
              )
            }
          }
        }
      }
    }

    // Floating Dynamic Week Strip (Synchronized to User's Device Timezone & 100% Responsive)
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(18.dp))
          .background(SageCardElevated)
          .border(1.dp, SageBorder, RoundedCornerShape(18.dp))
          .clickable { showStreakCalendarDialog = true }
          .padding(vertical = 8.dp, horizontal = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        currentWeekDays.forEach { weekDay ->
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(12.dp))
              .background(if (weekDay.isToday) ForestButtonBg else Color.Transparent)
              .padding(vertical = 6.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(
                text = weekDay.dayName,
                style = MaterialTheme.typography.labelSmall.copy(
                  color = if (weekDay.isToday) ForestButtonText else TextMediumSage,
                  fontSize = 11.sp,
                  fontWeight = if (weekDay.isToday) FontWeight.Bold else FontWeight.Normal
                )
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "${weekDay.dayNumber}",
                style = MaterialTheme.typography.titleSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = if (weekDay.isToday) ForestButtonText else TextDeepForest
                )
              )
            }
          }
        }
      }
    }

    // "Today's Practice" Hero Card (Matching Reference Screenshot layout)
    item {
      Text(
        text = "Today's Practice",
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold,
          color = TextDeepForest
        )
      )

      Spacer(modifier = Modifier.height(8.dp))

      AuraCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = SageBorder
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Surface(
            color = SageCardElevated,
            shape = RoundedCornerShape(8.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, SageBorder)
          ) {
            Text(
              text = "Morning • 10-15 min",
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = TextDeepForest
              )
            )
          }

          Icon(
            imageVector = Icons.Default.WbSunny,
            contentDescription = "Morning Routine",
            tint = ForestButtonBg
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = "Brahma Muhurta & 7 Chakras Alignment",
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            color = TextDeepForest
          )
        )
        Text(
          text = "Aura Cleansing • Ho'oponopono 432 Hz • Water Alchemy • 10 Blessings",
          style = MaterialTheme.typography.bodySmall.copy(color = TextMediumSage)
        )

        Spacer(modifier = Modifier.height(14.dp))

        SoftBlueButton(
          onClick = { onNavigate(AppDestination.MORNING_ROUTINE) },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("home_start_morning_btn")
        ) {
          Icon(Icons.Default.PlayArrow, contentDescription = null, tint = ForestButtonText)
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Tap to start practice",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = ForestButtonText
            )
          )
        }
      }
    }

    // Water Manifestation Quick Alchemy
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
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(SageCardElevated),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.WaterDrop,
                contentDescription = null,
                tint = ForestButtonBg
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "Water Manifestation Alchemy",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = TextDeepForest
                )
              )
              Text(
                text = "Infuse intention • Sip by sip",
                style = MaterialTheme.typography.bodySmall.copy(color = TextMediumSage)
              )
            }
          }

          Text(
            text = "$waterSips / 8 Sips",
            style = MaterialTheme.typography.labelLarge.copy(
              fontWeight = FontWeight.Bold,
              color = TextDeepForest
            )
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Surface(
          color = SageCardElevated,
          shape = RoundedCornerShape(12.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, SageBorder)
        ) {
          Text(
            text = "“$waterIntent”",
            modifier = Modifier.padding(12.dp),
            style = MaterialTheme.typography.bodySmall.copy(
              fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
              color = TextDeepForest
            )
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          SoftBlueButton(
            onClick = { viewModel.takeWaterSip() },
            modifier = Modifier.weight(1f)
          ) {
            Icon(Icons.Default.Water, contentDescription = null, modifier = Modifier.size(16.dp), tint = ForestButtonText)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Take 1 Sip", fontWeight = FontWeight.Bold, color = ForestButtonText)
          }

          OutlinedButton(
            onClick = { onNavigate(AppDestination.MORNING_ROUTINE) },
            shape = RoundedCornerShape(26.dp),
            modifier = Modifier
              .height(52.dp)
              .minimumInteractiveComponentSize(),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, ForestButtonBg)
          ) {
            Text("Full Flow", color = ForestButtonBg, fontWeight = FontWeight.SemiBold)
          }
        }
      }
    }

    // 10 Gratitude Blessings Card
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
          Column {
            Text(
              text = "Count 10 Blessings (The Magic)",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextDeepForest
              )
            )
            Text(
              text = "$completedGratitudes of 10 completed today",
              style = MaterialTheme.typography.bodySmall.copy(color = TextMediumSage)
            )
          }

          IconButton(
            onClick = { onNavigate(AppDestination.MORNING_ROUTINE) },
            modifier = Modifier.minimumInteractiveComponentSize()
          ) {
            Icon(
              imageVector = Icons.Default.EditNote,
              contentDescription = "Edit Blessings",
              tint = ForestButtonBg
            )
          }
        }

        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
          progress = { (completedGratitudes / 10f).coerceIn(0f, 1f) },
          modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(RoundedCornerShape(3.dp)),
          color = ForestButtonBg,
          trackColor = SageCardElevated
        )
      }
    }

    // Night Routine Teaser Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .border(1.dp, SageBorder, RoundedCornerShape(20.dp))
          .clickable { onNavigate(AppDestination.NIGHT_ROUTINE) },
        colors = CardDefaults.cardColors(containerColor = SageCardElevated)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(CircleShape)
              .background(SageCardSurface),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.NightsStay,
              contentDescription = null,
              tint = ForestButtonBg,
              modifier = Modifier.size(24.dp)
            )
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Evening • The Magic Rock",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextDeepForest
              )
            )
            Text(
              text = "Preview tomorrow's practice & recall today's greatest blessing before sleep.",
              style = MaterialTheme.typography.bodySmall.copy(color = TextMediumSage)
            )
          }
          Icon(
            imageVector = Icons.Default.ArrowForwardIos,
            contentDescription = null,
            tint = ForestButtonBg,
            modifier = Modifier.size(16.dp)
          )
        }
      }
    }
  }

  // Monthly Calendar Modal (Shown when clicking streak or week strip)
  if (showStreakCalendarDialog) {
    StreakCalendarModal(
      streaks = monthlyStreaks,
      currentMonthYear = currentMonthYear,
      userStreakDays = user.streakDays,
      currentFormattedDate = currentFormattedDate,
      timeZoneDisplayName = timeZoneDisplayName,
      onNavigateToStreaks = {
        showStreakCalendarDialog = false
        onNavigate(AppDestination.STREAKS)
      },
      onDismiss = { showStreakCalendarDialog = false }
    )
  }
}

/**
 * Interactive Monthly Calendar Dialog displaying completed ritual days highlighted in Forest Green.
 */
@Composable
fun StreakCalendarModal(
  streaks: List<DayStreak>,
  currentMonthYear: String,
  userStreakDays: Int,
  currentFormattedDate: String,
  timeZoneDisplayName: String,
  onNavigateToStreaks: () -> Unit,
  onDismiss: () -> Unit
) {
  val completedDaysCount = streaks.count { it.isMorningCompleted || it.isNightCompleted }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.94f)
        .clip(RoundedCornerShape(24.dp))
        .testTag("streak_calendar_modal"),
      color = SageMistLight,
      border = androidx.compose.foundation.BorderStroke(1.5.dp, SageBorder)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Modal Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
          ) {
            Box(
              modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(ForestButtonBg),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.LocalFireDepartment,
                contentDescription = null,
                tint = if (userStreakDays == 0) TextMutedSage else Color(0xFFEA580C),
                modifier = Modifier.size(22.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "Ritual Streak Calendar",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = TextDeepForest
                )
              )
              Text(
                text = currentMonthYear,
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = ForestButtonBg
                )
              )
            }
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier
              .size(34.dp)
              .clip(CircleShape)
              .background(SageCardElevated)
          ) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextDeepForest, modifier = Modifier.size(18.dp))
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Streak Count Status Callout
        Surface(
          color = SageCardElevated,
          shape = RoundedCornerShape(14.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, SageBorder),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(
                text = if (userStreakDays == 0) "Day 1 (0 Days Streak)" else "$userStreakDays Consecutive Days 🔥",
                style = MaterialTheme.typography.labelLarge.copy(
                  fontWeight = FontWeight.Bold,
                  color = TextDeepForest
                )
              )
              Text(
                text = "$completedDaysCount completed ritual days in $currentMonthYear",
                style = MaterialTheme.typography.bodySmall.copy(
                  color = TextMediumSage,
                  fontSize = 11.5.sp
                )
              )
            }
            Surface(
              color = ForestButtonBg,
              shape = RoundedCornerShape(8.dp)
            ) {
              Text(
                text = "$completedDaysCount Days",
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelSmall.copy(
                  color = ForestButtonText,
                  fontWeight = FontWeight.Bold
                )
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Weekday column headers
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

        // 7-column calendar matrix
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

        // Legend row with high contrast badges
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceAround,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(12.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(ForestButtonBg)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Completed Date",
              style = MaterialTheme.typography.labelSmall.copy(color = TextDeepForest, fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
            )
          }
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(12.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(LimePillAccent.copy(alpha = 0.45f))
                .border(1.5.dp, ForestButtonBg, RoundedCornerShape(3.dp))
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Today",
              style = MaterialTheme.typography.labelSmall.copy(color = TextDeepForest, fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action button to full streaks screen
        SoftBlueButton(
          onClick = onNavigateToStreaks,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("modal_view_full_streaks_btn")
        ) {
          Icon(Icons.Default.DateRange, contentDescription = null, tint = ForestButtonText)
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Open Full Streaks & Milestones",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = ForestButtonText
            )
          )
        }
      }
    }
  }
}
