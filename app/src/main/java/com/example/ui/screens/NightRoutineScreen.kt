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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AuraCard
import com.example.ui.components.SoftBlueButton
import com.example.ui.theme.*
import com.example.ui.viewmodel.ManifestationViewModel

@Composable
fun NightRoutineScreen(
  viewModel: ManifestationViewModel
) {
  val bestBlessing by viewModel.nightBestBlessing.collectAsState()
  val isCompleted by viewModel.nightCompleted.collectAsState()

  var inputBlessing by remember { mutableStateOf(bestBlessing) }
  var isSaved by remember { mutableStateOf(isCompleted) }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 18.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    contentPadding = PaddingValues(top = 10.dp, bottom = 28.dp)
  ) {
    // Night Sanctum Header
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(22.dp))
          .border(1.dp, SageBorder, RoundedCornerShape(22.dp)),
        colors = CardDefaults.cardColors(containerColor = SageCardElevated)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Evening Twilight Sanctum 🌙",
                style = MaterialTheme.typography.headlineMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = TextDeepForest
                )
              )
              Text(
                text = "Hypnagogic Subconscious Programming & Reflection",
                style = MaterialTheme.typography.bodySmall.copy(color = TextMediumSage)
              )
            }

            Box(
              modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(SageCardSurface),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Bedtime,
                contentDescription = null,
                tint = ForestButtonBg,
                modifier = Modifier.size(26.dp)
              )
            }
          }
        }
      }
    }

    // Part 1: Preview Tomorrow's Gratitude Practice (The Magic Rule)
    item {
      AuraCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = SageBorder
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.MenuBook, contentDescription = null, tint = ForestButtonBg)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Step 1: Read Tomorrow's Practice Prior",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = TextDeepForest
            )
          )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "“From Rhonda Byrne's 'The Magic': Always read through the next day's magical practice the night before you begin it, so your subconscious mind processes it overnight.”",
          style = MaterialTheme.typography.bodySmall.copy(
            color = TextMediumSage,
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
            lineHeight = 18.sp
          )
        )

        Spacer(modifier = Modifier.height(12.dp))
        Surface(
          color = SageCardElevated,
          shape = RoundedCornerShape(14.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, SageBorder)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(
              text = "Tomorrow's Practice: Day 2 — The Magic Rock",
              style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = TextDeepForest
              )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Place a smooth pebble or crystal by your bedside. Holding it as you fall asleep focuses your subconscious on the single best occurrence of the day, dissolving worry.",
              style = MaterialTheme.typography.bodySmall.copy(color = TextMediumSage, lineHeight = 20.sp)
            )
          }
        }
      }
    }

    // Part 2: The Magic Rock & Evening Blessings Recall
    item {
      AuraCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = SageBorder
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.Diamond, contentDescription = null, tint = ForestButtonBg)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Step 2: Recall the Day's Best Blessing",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = TextDeepForest
            )
          )
        }

        Spacer(modifier = Modifier.height(10.dp))
        Text(
          text = "Rewind your day from 4:30 AM Brahma Muhurta to this moment. What was the single best blessing that occurred today?",
          style = MaterialTheme.typography.bodyMedium.copy(color = TextMediumSage)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Glowing Magic Rock Visual
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(SageCardElevated)
            .border(1.dp, SageBorder, RoundedCornerShape(18.dp)),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
              imageVector = Icons.Default.FilterVintage,
              contentDescription = "The Magic Rock",
              tint = ForestButtonBg,
              modifier = Modifier.size(42.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "The Magic Rock • Bedside Talisman",
              style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextDeepForest
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
          value = inputBlessing,
          onValueChange = { inputBlessing = it },
          label = { Text("The Best Occurrence Today") },
          placeholder = { Text("e.g. Deep stillness in morning meditation and kind words from a friend") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("night_best_blessing_input"),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ForestButtonBg,
            unfocusedBorderColor = SageBorder,
            focusedTextColor = TextDeepForest,
            unfocusedTextColor = TextDeepForest,
            focusedLabelColor = ForestButtonBg,
            unfocusedLabelColor = TextMediumSage
          ),
          shape = RoundedCornerShape(14.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        SoftBlueButton(
          onClick = {
            viewModel.saveNightReflection(inputBlessing)
            isSaved = true
          },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("night_save_blessing_button")
        ) {
          Icon(Icons.Default.Check, contentDescription = null, tint = ForestButtonText)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (isSaved) "Sealed with 'Thank You! ✨'" else "Seal with 'Thank You!' Before Sleep",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = ForestButtonText
            )
          )
        }

        if (isSaved) {
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = "✨ Your mind is calibrated for peaceful rest. Tomorrow awaits with miracles!",
            style = MaterialTheme.typography.bodySmall.copy(
              color = TextDeepForest,
              fontWeight = FontWeight.SemiBold,
              textAlign = TextAlign.Center
            ),
            modifier = Modifier.fillMaxWidth()
          )
        }
      }
    }
  }
}
