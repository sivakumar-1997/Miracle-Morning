package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.HooponoponoAudioMode
import com.example.ui.theme.*

@Composable
fun HooponoponoPlayer(
  isPlaying: Boolean,
  timerSeconds: Int,
  audioMode: HooponoponoAudioMode,
  onTogglePlay: () -> Unit,
  onResetTimer: () -> Unit,
  onSelectMode: (HooponoponoAudioMode) -> Unit
) {
  val minutes = timerSeconds / 60
  val seconds = timerSeconds % 60
  val formattedTime = String.format("%02d:%02d", minutes, seconds)
  val progress = (300 - timerSeconds) / 300f

  val phrases = listOf(
    Pair("I am sorry.", "Cleanses past unintentional pain and unexpressed heavy memories."),
    Pair("Please forgive me.", "Releases subconscious guilt and grants unconditional mercy."),
    Pair("Thank you.", "Aligns your vibration with pure gratitude for life and lessons."),
    Pair("I love you.", "Returns your energy field to the zero state of Divine Source.")
  )

  // Subtle cyclic highlight of phrases every 4.5 seconds when playing
  val activePhraseIndex = if (isPlaying) ((300 - timerSeconds) / 4) % 4 else -1

  Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // 5-Minute Timer & Meditation Pacer
    AuraCard(
      modifier = Modifier.fillMaxWidth(),
      borderColor = SoftBlueLight.copy(alpha = 0.5f)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Ho'oponopono Prayer",
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Bold,
              color = TextPrimaryCozy
            )
          )
          Text(
            text = "Ancient Hawaiian Subconscious Cleansing",
            style = MaterialTheme.typography.labelSmall.copy(color = SoftBlueLight)
          )
        }

        // Circular Timer Pill
        Surface(
          color = CozyMidnightBg,
          shape = RoundedCornerShape(16.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, SoftBlueLight.copy(alpha = 0.4f))
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.HourglassEmpty,
              contentDescription = null,
              tint = SoftBluePrimary,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = formattedTime,
              style = MaterialTheme.typography.titleMedium.copy(
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = SoftBlueLight
              )
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier
          .fillMaxWidth()
          .height(6.dp)
          .clip(RoundedCornerShape(3.dp)),
        color = SoftBluePrimary,
        trackColor = CozySurfaceElevated
      )
    }

    // The Four Sacred Phrases prominently shown in UI
    Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      phrases.forEachIndexed { index, (phrase, meaning) ->
        val isHighlighted = activePhraseIndex == index
        val glowBorder by animateColorAsState(
          targetValue = if (isHighlighted) ForestButtonBg else SageBorder,
          label = "phraseBorder"
        )
        val containerBg by animateColorAsState(
          targetValue = if (isHighlighted) ForestButtonBg else SageCardSurface,
          label = "phraseBg"
        )

        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .border(
              width = if (isHighlighted) 2.dp else 1.dp,
              color = glowBorder,
              shape = RoundedCornerShape(18.dp)
            ),
          colors = CardDefaults.cardColors(containerColor = containerBg)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(if (isHighlighted) LimePillAccent else SageCardElevated),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "${index + 1}",
                style = MaterialTheme.typography.titleSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = if (isHighlighted) TextDeepForest else TextDeepForest
                )
              )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = phrase,
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = if (isHighlighted) WhitePure else TextDeepForest,
                  fontSize = 18.sp
                )
              )
              Text(
                text = meaning,
                style = MaterialTheme.typography.bodySmall.copy(
                  color = if (isHighlighted) SageMistLight else TextMediumSage
                )
              )
            }
          }
        }
      }
    }

    // Audio Mode Selection Options
    AuraCard(
      modifier = Modifier.fillMaxWidth(),
      borderColor = SageBorder
    ) {
      Text(
        text = "432 Hz Sound Engine Audio Options:",
        style = MaterialTheme.typography.titleSmall.copy(
          fontWeight = FontWeight.Bold,
          color = TextDeepForest
        )
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Option 1: Only 432 Hz BG Music
      val isMode1 = audioMode == HooponoponoAudioMode.BGM_ONLY_432HZ
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(14.dp))
          .background(if (isMode1) SageCardElevated else SageCardSurface)
          .border(
            width = if (isMode1) 2.dp else 1.dp,
            color = if (isMode1) ForestButtonBg else SageBorder,
            shape = RoundedCornerShape(14.dp)
          )
          .clickable { onSelectMode(HooponoponoAudioMode.BGM_ONLY_432HZ) }
          .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        RadioButton(
          selected = isMode1,
          onClick = { onSelectMode(HooponoponoAudioMode.BGM_ONLY_432HZ) },
          colors = RadioButtonDefaults.colors(
            selectedColor = ForestButtonBg,
            unselectedColor = TextMediumSage
          ),
          modifier = Modifier.minimumInteractiveComponentSize()
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
          Text(
            text = "Option 1: Only 432 Hz BG Music",
            style = MaterialTheme.typography.bodyMedium.copy(
              fontWeight = FontWeight.Bold,
              color = TextDeepForest
            )
          )
          Text(
            text = "Pure harmonic continuous 432 Hz Pythagorean frequency drone without voice",
            style = MaterialTheme.typography.bodySmall.copy(color = TextMediumSage)
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Option 2: 432 Hz BGM + Dictated Wordings in slow phase
      val isMode2 = audioMode == HooponoponoAudioMode.BGM_PLUS_VOICE_432HZ
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(14.dp))
          .background(if (isMode2) SageCardElevated else SageCardSurface)
          .border(
            width = if (isMode2) 2.dp else 1.dp,
            color = if (isMode2) ForestButtonBg else SageBorder,
            shape = RoundedCornerShape(14.dp)
          )
          .clickable { onSelectMode(HooponoponoAudioMode.BGM_PLUS_VOICE_432HZ) }
          .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        RadioButton(
          selected = isMode2,
          onClick = { onSelectMode(HooponoponoAudioMode.BGM_PLUS_VOICE_432HZ) },
          colors = RadioButtonDefaults.colors(
            selectedColor = ForestButtonBg,
            unselectedColor = TextMediumSage
          ),
          modifier = Modifier.minimumInteractiveComponentSize()
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
          Text(
            text = "Option 2: 432 Hz BGM + Voice Dictation",
            style = MaterialTheme.typography.bodyMedium.copy(
              fontWeight = FontWeight.Bold,
              color = TextDeepForest
            )
          )
          Text(
            text = "432 Hz ambient music with slow-paced soothing voice repeating the 4 phrases for 5 mins",
            style = MaterialTheme.typography.bodySmall.copy(color = TextMediumSage)
          )
        }
      }

      if (isPlaying) {
        Spacer(modifier = Modifier.height(10.dp))
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = LimePillAccent,
          border = androidx.compose.foundation.BorderStroke(1.dp, ForestButtonBg),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.GraphicEq,
              contentDescription = null,
              tint = ForestButtonBg,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (audioMode == HooponoponoAudioMode.BGM_ONLY_432HZ)
                "Playing: Continuous 432 Hz Meditative Background Music"
              else
                "Playing: 432 Hz Background Music + Mindful Female Voice",
              style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Bold,
                color = ForestButtonBg
              )
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Play / Pause & Reset Controls in High Contrast
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        SoftBlueButton(
          onClick = onTogglePlay,
          modifier = Modifier
            .weight(1.4f)
            .testTag("hooponopono_play_btn")
        ) {
          Icon(
            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
            contentDescription = if (isPlaying) "Pause Prayer" else "Play Prayer",
            tint = ForestButtonText
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (isPlaying) "Pause Prayer" else "Begin 5-Min Prayer",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = ForestButtonText
            )
          )
        }

        OutlinedButton(
          onClick = onResetTimer,
          modifier = Modifier
            .height(52.dp)
            .minimumInteractiveComponentSize(),
          shape = RoundedCornerShape(26.dp),
          border = androidx.compose.foundation.BorderStroke(1.5.dp, ForestButtonBg)
        ) {
          Icon(
            Icons.Default.Refresh,
            contentDescription = "Reset 5-Minute Timer",
            tint = ForestButtonBg
          )
        }
      }
    }
  }
}
