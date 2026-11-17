package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.data.model.ChakraInfo
import com.example.ui.theme.*

@Composable
fun ChakraVisualizer(
  chakras: List<ChakraInfo>,
  selectedChakra: ChakraInfo? = null,
  activeChakraId: Int?,
  isSequencePlaying: Boolean = false,
  isJourneyPlaying: Boolean = false,
  isAmbientBgActive: Boolean = false,
  journeyStep: Int = 0,
  onSelectChakra: (ChakraInfo) -> Unit = {},
  onPlayChakra: (ChakraInfo) -> Unit,
  onToggleJourney: () -> Unit = {},
  onToggleAmbient: () -> Unit = {},
  onPlayAllSequence: () -> Unit = {},
  onChantMantra: (ChakraInfo) -> Unit = {}
) {
  var localSelectedChakra by remember { mutableStateOf(chakras.firstOrNull { it.id == 4 } ?: chakras.first()) }
  val currentChakra = selectedChakra ?: localSelectedChakra
  var showAnatomyGuide by remember { mutableStateOf(false) }

  val infiniteTransition = rememberInfiniteTransition(label = "chakraPulse")
  val pulseRing by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "chakraRingAnim"
  )

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("chakra_visualizer_container"),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // 1. Where are the Chakras placed? Link
    Surface(
      onClick = { showAnatomyGuide = true },
      shape = RoundedCornerShape(16.dp),
      color = SageMistLight,
      border = BorderStroke(1.5.dp, ForestButtonBg.copy(alpha = 0.35f)),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("chakra_anatomy_guide_link")
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.weight(1f)
        ) {
          Box(
            modifier = Modifier
              .size(32.dp)
              .clip(CircleShape)
              .background(ForestButtonBg),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.HelpOutline,
              contentDescription = "Chakra placement guide",
              tint = WhitePure,
              modifier = Modifier.size(18.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "Where are the Chakras placed?",
              style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                color = TextDeepForest
              )
            )
            Text(
              text = "Tap to view full Anatomy, Placement & Mantra guide",
              style = MaterialTheme.typography.bodySmall.copy(
                color = TextMediumSage,
                fontSize = 11.5.sp
              )
            )
          }
        }
        Icon(
          imageVector = Icons.Default.ChevronRight,
          contentDescription = null,
          tint = TextDeepForest,
          modifier = Modifier.size(20.dp)
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // 2. Meditating Silhouette Body Image with 7 Glowing Chakras
    AuraCard(
      modifier = Modifier
        .fillMaxWidth()
        .testTag("meditating_body_visualizer_card"),
      borderColor = currentChakra.color.copy(alpha = 0.6f)
    ) {
      // Header Status
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(10.dp)
              .clip(CircleShape)
              .background(if (activeChakraId != null || isJourneyPlaying) currentChakra.color else ChakraSolarYellow)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (isJourneyPlaying) {
              "Slow Journey: ${currentChakra.name} (${currentChakra.frequencyHz.toInt()} Hz)"
            } else if (activeChakraId != null) {
              "Vibrating ${currentChakra.frequencyHz.toInt()} Hz Frequency"
            } else {
              "Meditating Body • 7 Chakras"
            },
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold,
              color = TextDeepForest
            )
          )
        }

        Surface(
          onClick = onToggleAmbient,
          shape = RoundedCornerShape(10.dp),
          color = if (isAmbientBgActive || isJourneyPlaying) LimePillAccent else SageMistLight,
          border = BorderStroke(1.dp, if (isAmbientBgActive || isJourneyPlaying) ForestButtonBg else SageBorder),
          modifier = Modifier.testTag("toggle_ambient_music_btn")
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = if (isAmbientBgActive || isJourneyPlaying) Icons.Default.MusicNote else Icons.Default.MusicOff,
              contentDescription = "Toggle background music",
              tint = if (isAmbientBgActive || isJourneyPlaying) ForestButtonBg else TextMediumSage,
              modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = if (isAmbientBgActive || isJourneyPlaying) "BG Music: ON" else "BG Music: OFF",
              style = MaterialTheme.typography.labelSmall.copy(
                color = if (isAmbientBgActive || isJourneyPlaying) ForestButtonBg else TextMediumSage,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Meditating Body Container with Exact Proportional Chakra Orbs
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(310.dp)
          .clip(RoundedCornerShape(20.dp))
          .background(
            Brush.radialGradient(
              colors = listOf(
                currentChakra.color.copy(alpha = if (activeChakraId == currentChakra.id || isJourneyPlaying) 0.28f else 0.12f),
                SageMistLight,
                SageCreamBg
              )
            )
          )
          .border(1.dp, SageBorder, RoundedCornerShape(20.dp)),
        contentAlignment = Alignment.Center
      ) {
        // Meditating Figure Illustration from drawable
        Image(
          painter = painterResource(id = R.drawable.ic_meditating_body),
          contentDescription = "Meditating figure silhouette in lotus posture",
          modifier = Modifier
            .fillMaxHeight(0.92f)
            .padding(vertical = 8.dp),
          contentScale = ContentScale.Fit
        )

        // Canvas for exact luminous chakra orbs and radiating soundwaves
        Canvas(modifier = Modifier.fillMaxSize()) {
          val centerX = size.width / 2f
          val topY = size.height * 0.12f
          val bottomY = size.height * 0.88f
          val totalHeight = bottomY - topY

          // Chakra heights along the spine from Crown (top) to Root (bottom)
          // ID 7: Crown = 0.00
          // ID 6: Third Eye = 0.12
          // ID 5: Throat = 0.25
          // ID 4: Heart = 0.42
          // ID 3: Solar Plexus = 0.58
          // ID 2: Sacral = 0.74
          // ID 1: Root = 1.00
          val fractionMap = mapOf(
            7 to 0.00f,
            6 to 0.13f,
            5 to 0.26f,
            4 to 0.43f,
            3 to 0.59f,
            2 to 0.76f,
            1 to 1.00f
          )

          chakras.forEach { chakra ->
            val fraction = fractionMap[chakra.id] ?: 0.5f
            val nodeY = topY + (fraction * totalHeight)
            val isCurrentActive = activeChakraId == chakra.id
            val isCurrentSelected = currentChakra.id == chakra.id

            // Radiating pulse ring when actively chanting / playing
            if (isCurrentActive) {
              val ringRadius = 14.dp.toPx() + (pulseRing * 26.dp.toPx())
              val ringAlpha = (1f - pulseRing).coerceIn(0f, 1f)
              drawCircle(
                color = chakra.color.copy(alpha = ringAlpha * 0.8f),
                radius = ringRadius,
                center = Offset(centerX, nodeY),
                style = Stroke(width = 3.dp.toPx())
              )
              // Second outer harmonic ring
              drawCircle(
                color = chakra.color.copy(alpha = ringAlpha * 0.4f),
                radius = ringRadius * 1.35f,
                center = Offset(centerX, nodeY),
                style = Stroke(width = 1.5.dp.toPx())
              )
            }

            // Glowing halo
            drawCircle(
              color = chakra.color.copy(alpha = if (isCurrentActive) 0.55f else if (isCurrentSelected) 0.35f else 0.18f),
              radius = if (isCurrentActive) 22.dp.toPx() else if (isCurrentSelected) 16.dp.toPx() else 11.dp.toPx(),
              center = Offset(centerX, nodeY)
            )

            // Inner solid luminous orb
            drawCircle(
              color = chakra.color,
              radius = if (isCurrentActive) 10.dp.toPx() else if (isCurrentSelected) 8.dp.toPx() else 6.5.dp.toPx(),
              center = Offset(centerX, nodeY)
            )

            // White highlight core
            drawCircle(
              color = WhitePure.copy(alpha = 0.75f),
              radius = if (isCurrentActive) 4.5.dp.toPx() else 2.5.dp.toPx(),
              center = Offset(centerX, nodeY)
            )
          }
        }

        // Interactive Touch Nodes & Seed Mantra Badges arranged along the body
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
          val totalHeight = maxHeight * (0.88f - 0.12f)
          val topOffset = maxHeight * 0.12f

          val fractionMap = mapOf(
            7 to 0.00f,
            6 to 0.13f,
            5 to 0.26f,
            4 to 0.43f,
            3 to 0.59f,
            2 to 0.76f,
            1 to 1.00f
          )

          chakras.forEach { chakra ->
            val fraction = fractionMap[chakra.id] ?: 0.5f
            val nodeY = topOffset + (totalHeight * fraction) - 18.dp
            val isCurrentActive = activeChakraId == chakra.id
            val isCurrentSelected = currentChakra.id == chakra.id
            val isLeftSide = chakra.id % 2 != 0 // Alternate pill tags for clean spacing

            Box(
              modifier = Modifier
                .fillMaxWidth()
                .offset(y = nodeY)
                .padding(horizontal = 14.dp),
              contentAlignment = if (isLeftSide) Alignment.CenterStart else Alignment.CenterEnd
            ) {
              Surface(
                onClick = {
                  localSelectedChakra = chakra
                  onSelectChakra(chakra)
                  onPlayChakra(chakra)
                },
                shape = RoundedCornerShape(12.dp),
                color = if (isCurrentActive) chakra.color else if (isCurrentSelected) SageCardElevated else SageCardSurface.copy(alpha = 0.85f),
                border = BorderStroke(
                  width = if (isCurrentActive || isCurrentSelected) 1.5.dp else 1.dp,
                  color = if (isCurrentActive) WhitePure else if (isCurrentSelected) chakra.color else SageBorder
                ),
                shadowElevation = if (isCurrentActive) 4.dp else 1.dp,
                modifier = Modifier.testTag("chakra_body_tag_${chakra.id}")
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Box(
                    modifier = Modifier
                      .size(8.dp)
                      .clip(CircleShape)
                      .background(if (isCurrentActive) WhitePure else chakra.color)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "${chakra.frequencyHz.toInt()}Hz • ${chakra.name}",
                    style = MaterialTheme.typography.labelSmall.copy(
                      fontWeight = FontWeight.Bold,
                      color = if (isCurrentActive) WhitePure else TextDeepForest,
                      fontSize = 10.sp
                    ),
                    maxLines = 1
                  )
                  if (isCurrentActive) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                      imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                      contentDescription = null,
                      tint = WhitePure,
                      modifier = Modifier.size(12.dp)
                    )
                  }
                }
              }
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // 3. CUMULATIVE BUTTON: Play All 7 Sounds in Slow Meditative Manner with 432 Hz BG Music
    Surface(
      onClick = onToggleJourney,
      shape = RoundedCornerShape(20.dp),
      color = if (isJourneyPlaying) ForestButtonBg else SageCardElevated,
      border = BorderStroke(
        width = 2.dp,
        color = if (isJourneyPlaying) LimePillAccent else ForestButtonBg.copy(alpha = 0.4f)
      ),
      shadowElevation = if (isJourneyPlaying) 6.dp else 2.dp,
      modifier = Modifier
        .fillMaxWidth()
        .testTag("cumulative_chakra_journey_btn")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
          ) {
            Box(
              modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(if (isJourneyPlaying) LimePillAccent else ForestButtonBg),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = if (isJourneyPlaying) Icons.Default.GraphicEq else Icons.Default.AllInclusive,
                contentDescription = null,
                tint = if (isJourneyPlaying) ForestButtonBg else WhitePure,
                modifier = Modifier.size(22.dp)
              )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = if (isJourneyPlaying) {
                  "Step $journeyStep of 7: ${currentChakra.name} (${currentChakra.frequencyHz.toInt()} Hz)"
                } else {
                  "Play All 7 Chakra Frequencies (Slow + 432Hz BG)"
                },
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = if (isJourneyPlaying) WhitePure else TextDeepForest
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
              Text(
                text = if (isJourneyPlaying) {
                  "Vibrating ${currentChakra.frequencyHz.toInt()} Hz • 432 Hz Soundscape"
                } else {
                  "Ascends Root to Crown • 396Hz to 963Hz with 432Hz ambient"
                },
                style = MaterialTheme.typography.bodySmall.copy(
                  color = if (isJourneyPlaying) SageMistLight else TextMediumSage,
                  fontSize = 11.5.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
            }
          }

          Spacer(modifier = Modifier.width(8.dp))

          Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (isJourneyPlaying) LimePillAccent else ForestButtonBg,
            contentColor = if (isJourneyPlaying) ForestButtonBg else WhitePure
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = if (isJourneyPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = if (isJourneyPlaying) "Pause" else "Start",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
              )
            }
          }
        }

        // Active Journey Step Indicator Bar
        if (isJourneyPlaying) {
          Spacer(modifier = Modifier.height(12.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            (1..7).forEach { stepIndex ->
              val isPast = stepIndex < journeyStep
              val isCurrent = stepIndex == journeyStep
              val stepChakra = chakras.firstOrNull { it.id == stepIndex }
              Box(
                modifier = Modifier
                  .weight(1f)
                  .height(5.dp)
                  .clip(RoundedCornerShape(3.dp))
                  .background(
                    if (isCurrent) (stepChakra?.color ?: LimePillAccent)
                    else if (isPast) LimePillAccent.copy(alpha = 0.8f)
                    else WhitePure.copy(alpha = 0.25f)
                  )
              )
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // 4. Dynamically Changing Description & Tuning Card for the Current Chakra
    AuraCard(
      modifier = Modifier
        .fillMaxWidth()
        .testTag("selected_chakra_card"),
      borderColor = currentChakra.color.copy(alpha = 0.7f)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          modifier = Modifier.weight(1f),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(28.dp)
              .clip(CircleShape)
              .background(currentChakra.color)
              .border(1.5.dp, WhitePure, CircleShape)
          )
          Spacer(modifier = Modifier.width(10.dp))
          Column(modifier = Modifier.weight(1f, fill = false)) {
            Text(
              text = "${currentChakra.id}. ${currentChakra.name}",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextDeepForest
              ),
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
            Text(
              text = "${currentChakra.sanskritName} • ${currentChakra.anatomicalPlacement}",
              style = MaterialTheme.typography.labelSmall.copy(
                color = TextMediumSage,
                fontWeight = FontWeight.SemiBold
              ),
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Surface(
          color = SageCardElevated,
          shape = RoundedCornerShape(12.dp),
          border = BorderStroke(1.5.dp, currentChakra.color)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(currentChakra.color)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "${currentChakra.frequencyHz.toInt()} Hz",
              style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextDeepForest
              )
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Authentic Seed Mantra & Spiritual Focus Box
      Surface(
        color = currentChakra.color.copy(alpha = 0.12f),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, currentChakra.color.copy(alpha = 0.35f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(10.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "Seed Mantra (Bija): ",
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = TextDeepForest
                )
              )
              Text(
                text = currentChakra.seedMantra,
                style = MaterialTheme.typography.labelLarge.copy(
                  fontWeight = FontWeight.ExtraBold,
                  color = currentChakra.color
                )
              )
            }

            Text(
              text = currentChakra.pronunciation,
              style = MaterialTheme.typography.labelSmall.copy(
                color = TextMediumSage,
                fontStyle = FontStyle.Italic
              )
            )
          }

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = "Focus: ${currentChakra.spiritualFocus} • Element: ${currentChakra.element}",
            style = MaterialTheme.typography.bodySmall.copy(
              color = TextDeepForest,
              fontSize = 11.5.sp
            )
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      if (currentChakra.description.isNotBlank()) {
        Text(
          text = currentChakra.description,
          style = MaterialTheme.typography.bodySmall.copy(
            color = TextDeepForest,
            fontSize = 12.sp,
            lineHeight = 16.sp
          )
        )
        Spacer(modifier = Modifier.height(8.dp))
      }

      Text(
        text = "Affirmation: “${currentChakra.affirmation}”",
        style = MaterialTheme.typography.bodyMedium.copy(
          fontStyle = FontStyle.Italic,
          color = TextMediumSage
        )
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Audio Control Button (Plays pure Solfeggio frequency sound without voice chant)
      SoftBlueButton(
        onClick = { onPlayChakra(currentChakra) },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("play_chakra_tone_btn")
      ) {
        Icon(
          imageVector = if (activeChakraId == currentChakra.id) Icons.Default.GraphicEq else Icons.AutoMirrored.Filled.VolumeUp,
          contentDescription = null,
          tint = ForestButtonText,
          modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = if (activeChakraId == currentChakra.id)
            "Vibrating ${currentChakra.frequencyHz.toInt()} Hz Pure Frequency..."
          else
            "Play ${currentChakra.frequencyHz.toInt()} Hz Frequency (${currentChakra.name})",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = ForestButtonText
          ),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }
    }

    Spacer(modifier = Modifier.height(18.dp))

    // 5. Explicit Audio Frequency Players for Every Chakra
    AuraCard(
      modifier = Modifier
        .fillMaxWidth()
        .testTag("all_chakra_frequency_audios_card"),
      borderColor = SageBorder
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "7 Chakra Frequency Audios",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = TextDeepForest
            )
          )
          Text(
            text = "Generic Solfeggio frequency sounds (396Hz to 963Hz)",
            style = MaterialTheme.typography.labelSmall.copy(color = TextMediumSage)
          )
        }

        Surface(
          onClick = onPlayAllSequence,
          shape = RoundedCornerShape(12.dp),
          color = if (isSequencePlaying) LimePillAccent else ForestButtonBg,
          contentColor = if (isSequencePlaying) ForestButtonBg else WhitePure,
          modifier = Modifier.testTag("play_all_sequence_btn")
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = if (isSequencePlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
              contentDescription = null,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = if (isSequencePlaying) "Pause All" else "Play All",
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        chakras.sortedBy { it.id }.forEach { chakra ->
          val isPlayingThis = activeChakraId == chakra.id
          val isSelectedThis = currentChakra.id == chakra.id

          Surface(
            onClick = {
              localSelectedChakra = chakra
              onSelectChakra(chakra)
              onPlayChakra(chakra)
            },
            shape = RoundedCornerShape(14.dp),
            color = if (isPlayingThis) chakra.color.copy(alpha = 0.18f) else if (isSelectedThis) SageCardElevated else SageCardSurface,
            border = BorderStroke(
              width = if (isPlayingThis || isSelectedThis) 1.5.dp else 1.dp,
              color = if (isPlayingThis) chakra.color else if (isSelectedThis) ForestButtonBg else SageBorder
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("chakra_audio_row_${chakra.id}")
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
              ) {
                // Colored Chakra Orb indicator
                Box(
                  modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(chakra.color),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = "${chakra.id}",
                    style = MaterialTheme.typography.labelMedium.copy(
                      fontWeight = FontWeight.Bold,
                      color = WhitePure
                    )
                  )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                      text = "${chakra.name} Chakra",
                      style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextDeepForest
                      )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                      color = chakra.color.copy(alpha = 0.2f),
                      shape = RoundedCornerShape(6.dp)
                    ) {
                      Text(
                        text = "${chakra.frequencyHz.toInt()} Hz",
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                          fontWeight = FontWeight.Bold,
                          color = chakra.color,
                          fontSize = 10.5.sp
                        )
                      )
                    }
                  }
                  Text(
                    text = "${chakra.anatomicalPlacement} • ${chakra.spiritualFocus}",
                    style = MaterialTheme.typography.bodySmall.copy(
                      color = TextMediumSage,
                      fontSize = 11.5.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                  )
                }
              }

              Spacer(modifier = Modifier.width(8.dp))

              // Play / Stop Audio Button for this specific frequency
              FilledTonalButton(
                onClick = {
                  localSelectedChakra = chakra
                  onSelectChakra(chakra)
                  onPlayChakra(chakra)
                },
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                  containerColor = if (isPlayingThis) chakra.color else ForestButtonBg,
                  contentColor = WhitePure
                ),
                modifier = Modifier.testTag("play_frequency_btn_${chakra.id}")
              ) {
                Icon(
                  imageVector = if (isPlayingThis) Icons.Default.GraphicEq else Icons.AutoMirrored.Filled.VolumeUp,
                  contentDescription = "Play ${chakra.frequencyHz.toInt()} Hz",
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = if (isPlayingThis) "Playing" else "Play",
                  style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
              }
            }
          }
        }
      }
    }
  }

  // 6. Comprehensive Chakra Anatomy & Alignment Guide Dialog
  if (showAnatomyGuide) {
    ChakraAnatomyGuideDialog(
      chakras = chakras,
      activeChakraId = activeChakraId,
      onPlayChakra = onPlayChakra,
      onDismiss = { showAnatomyGuide = false }
    )
  }
}

/**
 * Full Info Dialog displaying complete anatomical placements, seed mantras,
 * Solfeggio frequencies, spiritual focus, and organ systems for all 7 Chakras.
 */
@Composable
fun ChakraAnatomyGuideDialog(
  chakras: List<ChakraInfo>,
  activeChakraId: Int?,
  onPlayChakra: (ChakraInfo) -> Unit,
  onDismiss: () -> Unit
) {
  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.94f)
        .fillMaxHeight(0.90f)
        .clip(RoundedCornerShape(24.dp))
        .testTag("chakra_anatomy_guide_dialog"),
      color = SageMistLight,
      border = BorderStroke(1.5.dp, SageBorder)
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(18.dp)
      ) {
        // Dialog Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "The 7 Chakras Anatomy Guide",
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = TextDeepForest
              )
            )
            Text(
              text = "Body Placements, Seed Mantras & Solfeggio Frequencies",
              style = MaterialTheme.typography.bodySmall.copy(color = TextMediumSage)
            )
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(SageCardElevated)
          ) {
            Icon(Icons.Default.Close, contentDescription = "Close guide", tint = TextDeepForest)
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Anatomical Body Explanation Note Card
        Surface(
          color = SageCardSurface,
          shape = RoundedCornerShape(14.dp),
          border = BorderStroke(1.dp, SageBorder),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Spa,
              contentDescription = null,
              tint = ForestButtonBg,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = "Chakras are vortexes of prana located along the Sushumna Nadi (spinal axis). Each connects to an anatomical nerve plexus, endocrine gland, and Solfeggio sound vibration.",
              style = MaterialTheme.typography.bodySmall.copy(
                color = TextDeepForest,
                fontSize = 12.sp,
                lineHeight = 16.sp
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Scrollable List of All 7 Chakras with full details
        LazyColumn(
          modifier = Modifier
            .weight(1f)
            .fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          items(chakras) { chakra ->
            val isPlaying = activeChakraId == chakra.id

            Surface(
              shape = RoundedCornerShape(16.dp),
              color = SageCardElevated,
              border = BorderStroke(
                1.5.dp,
                if (isPlaying) chakra.color else chakra.color.copy(alpha = 0.45f)
              ),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(14.dp)) {
                // Title Row
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
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(chakra.color),
                      contentAlignment = Alignment.Center
                    ) {
                      Text(
                        text = "${chakra.id}",
                        style = MaterialTheme.typography.labelSmall.copy(
                          color = WhitePure,
                          fontWeight = FontWeight.Bold
                        )
                      )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                      Text(
                        text = "${chakra.name} (${chakra.sanskritName})",
                        style = MaterialTheme.typography.titleMedium.copy(
                          fontWeight = FontWeight.Bold,
                          color = TextDeepForest
                        )
                      )
                      Text(
                        text = "Element: ${chakra.element}",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextMediumSage)
                      )
                    }
                  }

                  // Sound Button
                  FilledTonalButton(
                    onClick = { onPlayChakra(chakra) },
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                      containerColor = if (isPlaying) chakra.color else chakra.color.copy(alpha = 0.2f),
                      contentColor = if (isPlaying) WhitePure else TextDeepForest
                    ),
                    shape = RoundedCornerShape(10.dp)
                  ) {
                    Icon(
                      imageVector = if (isPlaying) Icons.AutoMirrored.Filled.VolumeUp else Icons.Default.PlayArrow,
                      contentDescription = null,
                      modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                      text = "${chakra.frequencyHz.toInt()}Hz",
                      style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                  }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                  shape = RoundedCornerShape(10.dp),
                  color = SageMistLight,
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Column(
                    modifier = Modifier.padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                  ) {
                    Text(
                      text = "📍 Anatomical Placement: ${chakra.anatomicalPlacement}",
                      style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = TextDeepForest
                      )
                    )

                    Text(
                      text = "✨ Spiritual Focus: ${chakra.spiritualFocus}",
                      style = MaterialTheme.typography.bodySmall.copy(
                        color = TextDeepForest
                      )
                    )

                    Text(
                      text = "🕉️ Seed Mantra (Bija): ${chakra.seedMantra} (${chakra.pronunciation}) • Solfeggio: ${chakra.frequencyHz.toInt()} Hz",
                      style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = chakra.color
                      )
                    )

                    if (chakra.physicalOrgans.isNotBlank()) {
                      Text(
                        text = "🩺 Associated Organs: ${chakra.physicalOrgans}",
                        style = MaterialTheme.typography.bodySmall.copy(
                          color = TextMediumSage,
                          fontSize = 11.sp
                        )
                      )
                    }
                  }
                }

                if (chakra.description.isNotBlank()) {
                  Spacer(modifier = Modifier.height(8.dp))
                  Text(
                    text = chakra.description,
                    style = MaterialTheme.typography.bodySmall.copy(
                      color = TextDeepForest,
                      fontSize = 11.5.sp,
                      lineHeight = 16.sp
                    )
                  )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                  text = "Affirmation: “${chakra.affirmation}”",
                  style = MaterialTheme.typography.bodySmall.copy(
                    fontStyle = FontStyle.Italic,
                    color = TextMediumSage,
                    fontSize = 11.sp
                  )
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
          onClick = onDismiss,
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
          shape = RoundedCornerShape(24.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = ForestButtonBg,
            contentColor = WhitePure
          )
        ) {
          Text(
            text = "Done & Return to Cleansing",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
          )
        }
      }
    }
  }
}
