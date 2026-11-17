package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
fun OnboardingScreen(
  viewModel: ManifestationViewModel,
  onComplete: () -> Unit
) {
  val user by viewModel.userProfile.collectAsState()

  var nickname by remember { mutableStateOf(user.nickname) }
  var selectedMood by remember { mutableStateOf(user.currentMood) }
  var selectedIntention by remember { mutableStateOf(user.primaryIntention) }
  var wakeUpTime by remember { mutableStateOf("04:30 AM") }
  var currentStep by remember { mutableIntStateOf(0) } // 0 is Welcome intro screen like the reference!

  val scrollState = rememberScrollState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .statusBarsPadding()
      .navigationBarsPadding()
      .verticalScroll(scrollState)
      .padding(horizontal = 24.dp, vertical = 16.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    if (currentStep > 0) {
      // Stepper indicator
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Step $currentStep of 4",
          style = MaterialTheme.typography.labelLarge.copy(
            color = TextDeepForest,
            fontWeight = FontWeight.Bold
          )
        )
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          (1..4).forEach { step ->
            Box(
              modifier = Modifier
                .size(width = 28.dp, height = 6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(if (step <= currentStep) ForestButtonBg else SageCardElevated)
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    AnimatedContent(
      targetState = currentStep,
      label = "onboarding_step_anim"
    ) { step ->
      when (step) {
        0 -> {
          // Welcome Screen mirroring reference image: "Welcome to Miracle Mornings - Your personalized manifestation coach"
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
          ) {
            // Ethereal Flower / Lotus Pinwheel Graphic
            Box(
              modifier = Modifier
                .size(170.dp)
                .clip(CircleShape)
                .background(
                  Brush.radialGradient(
                    colors = listOf(ForestButtonBg.copy(alpha = 0.15f), LimePillAccent.copy(alpha = 0.25f), Color.Transparent)
                  )
                ),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Spa,
                contentDescription = "Miracle Organic Bloom",
                tint = ForestButtonBg,
                modifier = Modifier.size(94.dp)
              )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Surface(
              color = SageCardElevated,
              shape = RoundedCornerShape(12.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, SageBorder)
            ) {
              Text(
                text = "Nice to see you!",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                style = MaterialTheme.typography.labelMedium.copy(color = TextDeepForest, fontWeight = FontWeight.SemiBold)
              )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
              text = "Welcome to Miracle Mornings",
              style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = TextDeepForest,
                textAlign = TextAlign.Center
              )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = "Your personalized manifestation sanctuary & morning ritual guide",
              style = MaterialTheme.typography.bodyLarge.copy(
                color = TextMediumSage,
                textAlign = TextAlign.Center
              )
            )

            Spacer(modifier = Modifier.height(36.dp))

            SoftBlueButton(
              onClick = { currentStep = 1 },
              modifier = Modifier
                .fillMaxWidth()
                .testTag("onboarding_lets_begin_btn")
            ) {
              Text(
                text = "Let's begin",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = ForestButtonText
                )
              )
            }
          }
        }

        1 -> {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
          ) {
            Box(
              modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(SageCardElevated),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Face,
                contentDescription = null,
                tint = ForestButtonBg,
                modifier = Modifier.size(38.dp)
              )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
              text = "How May We Call You?",
              style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextDeepForest
              )
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "Every soul has a sacred name or warm nickname for your morning blessings.",
              style = MaterialTheme.typography.bodyMedium.copy(
                color = TextMediumSage,
                textAlign = TextAlign.Center
              )
            )

            Spacer(modifier = Modifier.height(24.dp))

            AuraCard(
              modifier = Modifier.fillMaxWidth(),
              cornerRadius = 20.dp,
              borderColor = SageBorder
            ) {
              Text(
                text = "Your Spiritual Nickname",
                style = MaterialTheme.typography.labelLarge.copy(color = TextDeepForest, fontWeight = FontWeight.Bold)
              )
              Spacer(modifier = Modifier.height(8.dp))
              OutlinedTextField(
                value = nickname,
                onValueChange = { nickname = it },
                placeholder = { Text("e.g. Siva, Starseeker, Joy") },
                singleLine = true,
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("onboarding_nickname_input"),
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

              Spacer(modifier = Modifier.height(12.dp))
              Text(
                text = "Quick suggestions:",
                style = MaterialTheme.typography.labelSmall.copy(color = TextMediumSage)
              )
              Spacer(modifier = Modifier.height(6.dp))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                listOf("Siva", "Aura", "Bliss", "Creator").forEach { tag ->
                  AssistChip(
                    onClick = { nickname = tag },
                    label = { Text(tag, color = TextDeepForest) },
                    modifier = Modifier.minimumInteractiveComponentSize()
                  )
                }
              }
            }
          }
        }

        2 -> {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
          ) {
            Box(
              modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(SageCardElevated),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = Color(0xFFDC2626),
                modifier = Modifier.size(38.dp)
              )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
              text = "How Are You Feeling Right Now?",
              style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextDeepForest,
                textAlign = TextAlign.Center
              )
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "Acknowledging your current state is the first step toward frequency tuning and aura alignment.",
              style = MaterialTheme.typography.bodyMedium.copy(
                color = TextMediumSage,
                textAlign = TextAlign.Center
              )
            )

            Spacer(modifier = Modifier.height(20.dp))

            val moods = listOf(
              Triple("Grateful & High Vibration", "Radiating appreciation and magnetic to miracles", Icons.Default.AutoAwesome),
              Triple("Seeking Serenity & Stillness", "Ready to release overthinking and calm the mind", Icons.Default.Spa),
              Triple("Desiring Energy & Focus", "Need clear direction, mental drive, and alignment", Icons.Default.Bolt),
              Triple("Heavy or Stressed (Need Reset)", "Ready to let go of resistance and cleanse my aura", Icons.Default.Air)
            )

            Column(
              verticalArrangement = Arrangement.spacedBy(10.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              moods.forEach { (moodTitle, moodDesc, icon) ->
                val isSelected = selectedMood == moodTitle
                Card(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                      width = if (isSelected) 2.dp else 1.dp,
                      color = if (isSelected) ForestButtonBg else SageBorder,
                      shape = RoundedCornerShape(16.dp)
                    )
                    .clickable { selectedMood = moodTitle }
                    .testTag("mood_option_${moodTitle.take(6).lowercase()}"),
                  colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) SageCardElevated else SageCardSurface
                  )
                ) {
                  Row(
                    modifier = Modifier
                      .fillMaxWidth()
                      .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Icon(
                      imageVector = icon,
                      contentDescription = null,
                      tint = if (isSelected) ForestButtonBg else TextMediumSage,
                      modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                      Text(
                        text = moodTitle,
                        style = MaterialTheme.typography.titleMedium.copy(
                          fontWeight = FontWeight.Bold,
                          color = TextDeepForest
                        )
                      )
                      Text(
                        text = moodDesc,
                        style = MaterialTheme.typography.bodySmall.copy(color = TextMediumSage)
                      )
                    }
                    if (isSelected) {
                      Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Selected",
                        tint = ForestButtonBg
                      )
                    }
                  }
                }
              }
            }
          }
        }

        3 -> {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
          ) {
            Box(
              modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(SageCardElevated),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = ForestButtonBg,
                modifier = Modifier.size(38.dp)
              )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
              text = "Primary Manifestation Intention",
              style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextDeepForest,
                textAlign = TextAlign.Center
              )
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "What seed would you like your morning thoughts and water alchemy to nurture?",
              style = MaterialTheme.typography.bodyMedium.copy(
                color = TextMediumSage,
                textAlign = TextAlign.Center
              )
            )

            Spacer(modifier = Modifier.height(20.dp))

            val intentions = listOf(
              "Financial Freedom & Abundance (The Magic Money)",
              "Radiant Vitality & Bodily Healing (Magical Health)",
              "Harmonious Relationships & Soulmate Love",
              "Creative Breakthrough & Purposeful Career",
              "Deep Inner Peace, Meditation & Divine Guidance"
            )

            Column(
              verticalArrangement = Arrangement.spacedBy(10.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              intentions.forEach { intent ->
                val isSelected = selectedIntention == intent
                Card(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                      width = if (isSelected) 2.dp else 1.dp,
                      color = if (isSelected) ForestButtonBg else SageBorder,
                      shape = RoundedCornerShape(16.dp)
                    )
                    .clickable { selectedIntention = intent },
                  colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) SageCardElevated else SageCardSurface
                  )
                ) {
                  Row(
                    modifier = Modifier
                      .fillMaxWidth()
                      .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    RadioButton(
                      selected = isSelected,
                      onClick = { selectedIntention = intent },
                      colors = RadioButtonDefaults.colors(
                        selectedColor = ForestButtonBg,
                        unselectedColor = TextMediumSage
                      ),
                      modifier = Modifier.minimumInteractiveComponentSize()
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                      text = intent,
                      style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = TextDeepForest
                      )
                    )
                  }
                }
              }
            }
          }
        }

        4 -> {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
          ) {
            Box(
              modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(SageCardElevated),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.WbSunny,
                contentDescription = null,
                tint = ForestButtonBg,
                modifier = Modifier.size(38.dp)
              )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
              text = "Brahma Muhurta Routine",
              style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextDeepForest,
                textAlign = TextAlign.Center
              )
            )
            Text(
              text = "The Cosmic Hour (4:30 AM – 5:00 AM)",
              style = MaterialTheme.typography.titleSmall.copy(
                color = TextMediumSage,
                fontWeight = FontWeight.SemiBold
              )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "In ancient traditions, 1 hour and 36 minutes before sunrise is known as Brahma Muhurta—the time of the Creator. Pineal gland melatonin is peaked and universal prana is purest.",
              style = MaterialTheme.typography.bodyMedium.copy(
                color = TextMediumSage,
                textAlign = TextAlign.Center
              )
            )

            Spacer(modifier = Modifier.height(20.dp))

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
                    text = "Recommended Awakening Time",
                    style = MaterialTheme.typography.labelMedium.copy(color = TextMediumSage)
                  )
                  Text(
                    text = wakeUpTime,
                    style = MaterialTheme.typography.headlineLarge.copy(
                      fontWeight = FontWeight.Bold,
                      color = TextDeepForest
                    )
                  )
                }

                Surface(
                  color = SageCardElevated,
                  shape = RoundedCornerShape(12.dp),
                  border = androidx.compose.foundation.BorderStroke(1.dp, SageBorder)
                ) {
                  Text(
                    text = "Cosmic Peak Hour",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                      fontWeight = FontWeight.Bold,
                      color = TextDeepForest
                    )
                  )
                }
              }

              Spacer(modifier = Modifier.height(12.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                listOf("04:30 AM", "04:45 AM", "05:00 AM", "05:30 AM").forEach { time ->
                  FilterChip(
                    selected = wakeUpTime == time,
                    onClick = { wakeUpTime = time },
                    label = { Text(time) },
                    colors = FilterChipDefaults.filterChipColors(
                      selectedContainerColor = ForestButtonBg,
                      selectedLabelColor = ForestButtonText,
                      containerColor = SageCardElevated,
                      labelColor = TextDeepForest
                    ),
                    modifier = Modifier.minimumInteractiveComponentSize()
                  )
                }
              }

              Spacer(modifier = Modifier.height(12.dp))
              Text(
                text = "Flow: 1. Awakening -> 2. 7 Chakras Aura Cleansing -> 3. Ho'oponopono 432Hz -> 4. Water Alchemy -> 5. Wisdom Notes -> 6. 10 Gratitude Blessings",
                style = MaterialTheme.typography.bodySmall.copy(
                  color = TextMediumSage,
                  fontWeight = FontWeight.Medium
                )
              )
            }
          }
        }
      }
    }

    if (currentStep > 0) {
      Spacer(modifier = Modifier.height(28.dp))

      // Navigation buttons (Back / Next with High Contrast Tones)
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        if (currentStep > 1) {
          OutlinedButton(
            onClick = { currentStep -= 1 },
            modifier = Modifier
              .weight(1f)
              .height(52.dp)
              .minimumInteractiveComponentSize(),
            shape = RoundedCornerShape(26.dp),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, ForestButtonBg)
          ) {
            Text("Previous", color = ForestButtonBg, fontWeight = FontWeight.SemiBold)
          }
        }

        SoftBlueButton(
          onClick = {
            if (currentStep < 4) {
              currentStep += 1
            } else {
              viewModel.updateNickname(nickname)
              viewModel.updateMood(selectedMood)
              viewModel.updateIntention(selectedIntention)
              viewModel.updateWakeUpTime(wakeUpTime)
              onComplete()
            }
          },
          modifier = Modifier
            .weight(if (currentStep > 1) 1.5f else 1f)
            .testTag("onboarding_next_button")
        ) {
          Text(
            text = if (currentStep == 4) "Complete Setup ✨" else "Next",
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
