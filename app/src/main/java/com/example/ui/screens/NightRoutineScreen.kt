package com.example.ui.screens

import androidx.compose.animation.*
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
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    contentPadding = PaddingValues(top = 10.dp, bottom = 28.dp)
  ) {
    // Night Sanctum Header
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .border(1.dp, BlueNavy.copy(alpha = 0.3f), RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
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
            Column {
              Text(
                text = "Night Twilight Sanctum 🌙",
                style = MaterialTheme.typography.headlineMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = WhitePure
                )
              )
              Text(
                text = "Hypnagogic Subconscious Programming & Reflection",
                style = MaterialTheme.typography.bodySmall.copy(color = BlueSky)
              )
            }

            Box(
              modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(Color(0xFF334155)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Bedtime,
                contentDescription = null,
                tint = AuraGold,
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
        borderColor = RosePrimary
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.MenuBook, contentDescription = null, tint = RosePrimary)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Step 1: Read Tomorrow's Practice Prior",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = RoseDeep
            )
          )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "“From Rhonda Byrne's 'The Magic': Always read through the next day's magical practice the night before you begin it, because some practices need you to collect something or do something before you wake up.”",
          style = MaterialTheme.typography.bodySmall.copy(
            color = TextDark,
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
            lineHeight = 18.sp
          )
        )

        Spacer(modifier = Modifier.height(12.dp))
        Surface(
          color = RoseMist.copy(alpha = 0.5f),
          shape = RoundedCornerShape(14.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, RoseBlush)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(
              text = "Tomorrow's Practice: Day 2 — The Magic Rock",
              style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = RoseDeep
              )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Find a smooth, small rock or pebble that fits pleasantly in the palm of your hand. Place it right by your bedside where you will see it as you go to sleep tomorrow. It will be your daily talisman of infinite abundance.",
              style = MaterialTheme.typography.bodySmall.copy(color = TextDark, lineHeight = 20.sp)
            )
          }
        }
      }
    }

    // Part 2: The Magic Rock & Evening Blessings Recall
    item {
      AuraCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = BlueDeep
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.Diamond, contentDescription = null, tint = BlueDeep)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Step 2: Recall the Day's Best Blessing",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = BlueDeep
            )
          )
        }

        Spacer(modifier = Modifier.height(10.dp))
        Text(
          text = "Before you sleep tonight, hold your Magic Rock (or cup your hands over your heart). Rewind your day from 4:30 AM Brahma Muhurta to this moment. What was the single best thing that happened?",
          style = MaterialTheme.typography.bodyMedium.copy(color = TextDark)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Glowing Magic Rock Visual
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
              Brush.horizontalGradient(
                colors = listOf(Color(0xFF0F172A), Color(0xFF1E293B), Color(0xFF334155))
              )
            ),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
              imageVector = Icons.Default.FilterVintage,
              contentDescription = "The Magic Rock",
              tint = AuraGold,
              modifier = Modifier.size(42.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "The Magic Rock • நன்றியுணர்வு மணி",
              style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                color = WhitePure
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
          value = inputBlessing,
          onValueChange = { inputBlessing = it },
          label = { Text("The Best Thing That Happened Today") },
          placeholder = { Text("e.g. Having deep peace during morning meditation & receiving unexpected help") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("night_best_blessing_input"),
          shape = RoundedCornerShape(14.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BlueDeep,
            unfocusedBorderColor = BlueSky
          )
        )

        Spacer(modifier = Modifier.height(14.dp))

        Button(
          onClick = {
            viewModel.saveNightReflection(inputBlessing)
            isSaved = true
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag("night_save_blessing_button"),
          shape = RoundedCornerShape(14.dp),
          colors = ButtonDefaults.buttonColors(containerColor = if (isSaved) RosePrimary else BlueDeep)
        ) {
          Icon(Icons.Default.Check, contentDescription = null, tint = WhitePure)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (isSaved) "Sealed into Subconscious with 'Thank You! ✨'" else "Seal with 'Thank You!' Before Sleep",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = WhitePure
            )
          )
        }

        if (isSaved) {
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = "✨ Your reticular activating system is calibrated for divine rest. Tomorrow awaits with miracles!",
            style = MaterialTheme.typography.bodySmall.copy(
              color = RoseDeep,
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
