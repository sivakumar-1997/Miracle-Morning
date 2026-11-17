package com.example.ui.screens

import androidx.compose.animation.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GratitudeItem
import com.example.data.model.MorningRoutineStep
import com.example.ui.components.AuraCard
import com.example.ui.components.ChakraVisualizer
import com.example.ui.components.HooponoponoPlayer
import com.example.ui.components.SoftBlueButton
import com.example.ui.theme.*
import com.example.ui.viewmodel.ManifestationViewModel
import kotlinx.coroutines.delay

@Composable
fun MorningRoutineScreen(
  viewModel: ManifestationViewModel,
  onNavigateToHome: () -> Unit = {}
) {
  val currentStepIndex by viewModel.activeMorningStepIndex.collectAsState()
  val steps = MorningRoutineStep.values()
  val currentStep = steps[currentStepIndex]

  val gratitudeList by viewModel.gratitudeList.collectAsState()
  val chakrasList = viewModel.chakrasList
  val activeChakraId by viewModel.playingChakraId.collectAsState()
  val isSequencePlaying by viewModel.isChakraSequencePlaying.collectAsState()
  val selectedChakra by viewModel.selectedChakra.collectAsState()
  val isJourneyPlaying by viewModel.isChakraJourneyPlaying.collectAsState()
  val chakraJourneyStep by viewModel.chakraJourneyStep.collectAsState()
  val isAmbientBgActive by viewModel.isAmbientBgActive.collectAsState()

  val hooponoponoPlaying by viewModel.isHooponoponoPlaying.collectAsState()
  val hooponoponoTimer by viewModel.hooponoponoTimerSeconds.collectAsState()
  val hooponoponoAudioMode by viewModel.hooponoponoAudioMode.collectAsState()

  val waterSips by viewModel.waterSipsTaken.collectAsState()
  val waterIntent by viewModel.waterIntent.collectAsState()

  var newBlessingWhat by remember { mutableStateOf("") }
  var newBlessingWhy by remember { mutableStateOf("") }
  var showAddDialog by remember { mutableStateOf(false) }
  var showCompletionGreeting by remember { mutableStateOf(false) }

  // Stop any playing audio when user navigates away from Morning Routine
  DisposableEffect(Unit) {
    onDispose {
      viewModel.stopAllAudio()
    }
  }

  // Auto return to home after greeting
  LaunchedEffect(showCompletionGreeting) {
    if (showCompletionGreeting) {
      delay(2800)
      viewModel.setMorningStep(0)
      onNavigateToHome()
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 18.dp)
  ) {
    // Stepper Tab Selector (Horizontal pill tabs)
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 10.dp),
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      steps.forEachIndexed { index, step ->
        val isSelected = index == currentStepIndex
        val isDone = index < currentStepIndex
        Box(
          modifier = Modifier
            .weight(1f)
            .height(38.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
              when {
                isSelected -> ForestButtonBg
                isDone -> LimePillAccent.copy(alpha = 0.5f)
                else -> SageCardElevated
              }
            )
            .border(
              width = if (isSelected) 1.dp else 0.dp,
              color = if (isSelected) LimePillAccent else Color.Transparent,
              shape = RoundedCornerShape(12.dp)
            )
            .clickable { viewModel.setMorningStep(index) }
            .testTag("morning_step_tab_$index"),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "${index + 1}",
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold,
              color = if (isSelected) ForestButtonText else if (isDone) ForestButtonBg else TextMutedSage
            )
          )
        }
      }
    }

    // Step Header Card
    AuraCard(
      modifier = Modifier.fillMaxWidth(),
      borderColor = SageBorder
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(ForestButtonBg),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "${currentStep.stepNumber}",
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Bold,
              color = ForestButtonText
            )
          )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = currentStep.title,
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = TextDeepForest
            )
          )
          Text(
            text = currentStep.subtitle,
            style = MaterialTheme.typography.labelSmall.copy(
              color = TextMediumSage
            )
          )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Surface(
          color = SageCardElevated,
          shape = RoundedCornerShape(10.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, SageBorder)
        ) {
          Text(
            text = "~${currentStep.durationMinutes} min",
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall.copy(
              color = TextDeepForest,
              fontWeight = FontWeight.Bold
            )
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Interactive Step Body Content
    Box(modifier = Modifier.weight(1f)) {
      when (currentStep) {
        MorningRoutineStep.BRAHMA_MUHURTA -> {
          BrahmaMuhurtaAwakeningContent()
        }
        MorningRoutineStep.AURA_CLEANSING -> {
          LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            item {
              ChakraVisualizer(
                chakras = chakrasList,
                selectedChakra = selectedChakra,
                activeChakraId = activeChakraId,
                isSequencePlaying = isSequencePlaying,
                isJourneyPlaying = isJourneyPlaying,
                isAmbientBgActive = isAmbientBgActive,
                journeyStep = chakraJourneyStep,
                onSelectChakra = { chakra -> viewModel.selectChakra(chakra) },
                onPlayChakra = { chakra -> viewModel.playChakraTone(chakra) },
                onToggleJourney = { viewModel.toggleSlowChakraJourney() },
                onToggleAmbient = { viewModel.toggleAmbientMusic() },
                onPlayAllSequence = { viewModel.playAllChakrasSequence() },
                onChantMantra = { chakra -> viewModel.chantMantraSolo(chakra) }
              )
            }
          }
        }
        MorningRoutineStep.HOOPONOPONO_PRAYER -> {
          LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            item {
              HooponoponoPlayer(
                isPlaying = hooponoponoPlaying,
                timerSeconds = hooponoponoTimer,
                audioMode = hooponoponoAudioMode,
                onTogglePlay = { viewModel.toggleHooponoponoPlayback() },
                onResetTimer = { viewModel.resetHooponoponoTimer() },
                onSelectMode = { mode -> viewModel.setHooponoponoAudioMode(mode) }
              )
            }
          }
        }
        MorningRoutineStep.WATER_MANIFESTATION -> {
          WaterManifestationContent(
            waterIntent = waterIntent,
            waterSips = waterSips,
            onSip = { viewModel.takeWaterSip() },
            onReset = { viewModel.resetWaterSips() },
            onUpdateIntent = { viewModel.setWaterIntent(it) }
          )
        }
        MorningRoutineStep.POSITIVE_WISDOM -> {
          PositiveWisdomContent()
        }
        MorningRoutineStep.TEN_GRATITUDE_PRACTICES -> {
          TenGratitudeContent(
            gratitudeList = gratitudeList,
            onToggle = { viewModel.toggleGratitudeItem(it) },
            onOpenAdd = { showAddDialog = true }
          )
        }
      }
    }

    // Step Bottom Navigation Controls (Previous and Next only, with Complete Routine on last step)
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 12.dp),
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      if (currentStepIndex > 0) {
        OutlinedButton(
          onClick = { viewModel.prevMorningStep() },
          modifier = Modifier
            .weight(1f)
            .height(52.dp),
          shape = RoundedCornerShape(26.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, SageBorder)
        ) {
          Icon(Icons.Default.ArrowBack, contentDescription = null, tint = TextDeepForest, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Previous",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.SemiBold,
              color = TextDeepForest
            )
          )
        }
      }

      SoftBlueButton(
        onClick = {
          if (currentStepIndex < steps.size - 1) {
            viewModel.nextMorningStep()
          } else {
            // Mark morning routine complete (+1 streak & updates calendar)
            viewModel.completeMorningRoutine()
            showCompletionGreeting = true
            viewModel.showToast("Miracle Morning complete! Have a radiant day ✨")
          }
        },
        modifier = Modifier
          .weight(1f)
          .testTag("morning_step_next_btn")
      ) {
        Text(
          text = if (currentStepIndex < steps.size - 1) "Next" else "Complete Routine ✨",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = ForestButtonText
          )
        )
        Spacer(modifier = Modifier.width(6.dp))
        Icon(Icons.Default.ArrowForward, contentDescription = null, tint = ForestButtonText, modifier = Modifier.size(16.dp))
      }
    }
  }

  // Celebratory Completion Greeting Dialog (Returns to Home!)
  if (showCompletionGreeting) {
    AlertDialog(
      onDismissRequest = {
        showCompletionGreeting = false
        viewModel.setMorningStep(0)
        onNavigateToHome()
      },
      containerColor = SageCardSurface,
      icon = {
        Box(
          modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
            .background(LimePillAccent.copy(alpha = 0.3f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.WbSunny,
            contentDescription = null,
            tint = LimePillDark,
            modifier = Modifier.size(38.dp)
          )
        }
      },
      title = {
        Text(
          text = "Miracle Morning Complete! 🌅",
          style = MaterialTheme.typography.headlineSmall.copy(
            fontWeight = FontWeight.Bold,
            color = TextDeepForest,
            textAlign = TextAlign.Center
          ),
          modifier = Modifier.fillMaxWidth()
        )
      },
      text = {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = "Congratulations! You have completed all 6 sacred morning rituals. Your chakras, aura, and mind are tuned to peace, health, and abundant blessings.",
            style = MaterialTheme.typography.bodyMedium.copy(
              color = TextMediumSage,
              textAlign = TextAlign.Center,
              lineHeight = 22.sp
            )
          )
          Spacer(modifier = Modifier.height(14.dp))
          Text(
            text = "Returning to Home in a moment...",
            style = MaterialTheme.typography.labelSmall.copy(
              color = TextMutedSage,
              fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
          )
        }
      },
      confirmButton = {
        SoftBlueButton(
          onClick = {
            showCompletionGreeting = false
            viewModel.setMorningStep(0)
            onNavigateToHome()
          },
          modifier = Modifier.fillMaxWidth()
        ) {
          Text("Return to Home ✨", color = ForestButtonText, fontWeight = FontWeight.Bold)
        }
      }
    )
  }

  // Add Gratitude Blessing Dialog
  if (showAddDialog) {
    AlertDialog(
      onDismissRequest = { showAddDialog = false },
      containerColor = SageCardSurface,
      title = {
        Text(
          text = "Add Daily Blessing (The Magic)",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextDeepForest)
        )
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text(
            text = "Rhonda Byrne formula: 'I am truly blessed to have [WHAT], because [WHY].'",
            style = MaterialTheme.typography.bodySmall.copy(color = TextMediumSage)
          )
          OutlinedTextField(
            value = newBlessingWhat,
            onValueChange = { newBlessingWhat = it },
            label = { Text("What are you blessed to have?") },
            placeholder = { Text("e.g. peaceful morning stillness") },
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = newBlessingWhy,
            onValueChange = { newBlessingWhy = it },
            label = { Text("Because... (The Reason)") },
            placeholder = { Text("e.g. it gives clarity to design my dream life") },
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (newBlessingWhat.isNotBlank()) {
              viewModel.addGratitudeItem(newBlessingWhat, newBlessingWhy.ifBlank { "it enriches my soul" })
              newBlessingWhat = ""
              newBlessingWhy = ""
              showAddDialog = false
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = ForestButtonBg)
        ) {
          Text("Add Blessing", color = ForestButtonText, fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { showAddDialog = false }) {
          Text("Cancel", color = TextDeepForest)
        }
      }
    )
  }
}

@Composable
fun BrahmaMuhurtaAwakeningContent() {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      AuraCard(borderColor = LimePillAccent) {
        Text(
          text = "The Creator's Hour (4:30 AM – 5:00 AM)",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = TextDeepForest
          )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "Waking up during Brahma Muhurta (1 hour and 36 minutes before sunrise) aligns human biorhythms with pure cosmic sattva. The pineal gland naturally releases melatonin and serotonin, opening the mind to pristine subconscious impression.",
          style = MaterialTheme.typography.bodyMedium.copy(color = TextMediumSage, lineHeight = 22.sp)
        )
      }
    }

    item {
      AuraCard(borderColor = SageBorder) {
        Text(
          text = "Sacred Awakening Steps",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = TextDeepForest
          )
        )
        Spacer(modifier = Modifier.height(8.dp))
        val sequence = listOf(
          "1. Open eyes gently and look at both palms together in reverence.",
          "2. Step onto the earth with deep gratitude for another miracle day.",
          "3. Maintain mindful noble silence without opening social media.",
          "4. Sit in a comfortable meditative posture facing East or North-East."
        )
        sequence.forEach { item ->
          Text(
            text = item,
            style = MaterialTheme.typography.bodySmall.copy(color = TextMediumSage),
            modifier = Modifier.padding(vertical = 4.dp)
          )
        }
      }
    }
  }
}

@Composable
fun WaterManifestationContent(
  waterIntent: String,
  waterSips: Int,
  onSip: () -> Unit,
  onReset: () -> Unit,
  onUpdateIntent: (String) -> Unit
) {
  var isEditingIntent by remember { mutableStateOf(false) }
  var editedIntent by remember { mutableStateOf(waterIntent) }

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(14.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    item {
      Box(
        modifier = Modifier
          .size(150.dp)
          .clip(CircleShape)
          .background(SageCardElevated)
          .border(2.dp, ForestButtonBg, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(
            imageVector = Icons.Default.WaterDrop,
            contentDescription = null,
            tint = ForestButtonBg,
            modifier = Modifier.size(44.dp)
          )
          Text(
            text = "$waterSips of 8 Sips",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = TextDeepForest
            )
          )
          Text(
            text = "Cellular Infusion",
            style = MaterialTheme.typography.labelSmall.copy(color = TextMutedSage)
          )
        }
      }
    }

    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally)
      ) {
        (1..8).forEach { sip ->
          val filled = waterSips >= sip
          Box(
            modifier = Modifier
              .size(34.dp)
              .clip(CircleShape)
              .background(if (filled) ForestButtonBg else SageCardElevated)
              .border(1.5.dp, if (filled) LimePillAccent else SageBorder, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "$sip",
              style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                color = if (filled) ForestButtonText else TextMediumSage
              )
            )
          }
        }
      }
    }

    item {
      AuraCard(borderColor = SageBorder) {
        Text(
          text = "Dr. Masaru Emoto Water Alchemy",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = TextDeepForest
          )
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "Water molecules record electromagnetic vibrational intention. Cupping your glass and speaking love and gratitude restructures the water into radiant hexagonal crystals before you drink.",
          style = MaterialTheme.typography.bodySmall.copy(color = TextMediumSage)
        )

        Spacer(modifier = Modifier.height(12.dp))
        Text(
          text = "Intention Charged into the Water:",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = TextDeepForest)
        )

        if (isEditingIntent) {
          OutlinedTextField(
            value = editedIntent,
            onValueChange = { editedIntent = it },
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(6.dp))
          SoftBlueButton(
            onClick = {
              onUpdateIntent(editedIntent)
              isEditingIntent = false
            }
          ) {
            Text("Save Water Intention", fontWeight = FontWeight.Bold)
          }
        } else {
          Surface(
            color = SageCardElevated,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.clickable { isEditingIntent = true }
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = "“$waterIntent”",
                style = MaterialTheme.typography.bodyMedium.copy(
                  fontWeight = FontWeight.SemiBold,
                  color = TextDeepForest,
                  fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                ),
                modifier = Modifier.weight(1f)
              )
              Icon(Icons.Default.Edit, contentDescription = "Edit Intention", tint = ForestButtonBg, modifier = Modifier.size(18.dp))
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          SoftBlueButton(
            onClick = onSip,
            modifier = Modifier.weight(1.3f).testTag("water_drink_sip_btn")
          ) {
            Icon(Icons.Default.LocalDrink, contentDescription = null, tint = ForestButtonText)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Drink Sip ($waterSips/8)", fontWeight = FontWeight.Bold)
          }

          OutlinedButton(
            onClick = onReset,
            shape = RoundedCornerShape(26.dp),
            modifier = Modifier.height(52.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, SageBorder)
          ) {
            Text("Refill Glass", color = TextDeepForest)
          }
        }
      }
    }
  }
}

@Composable
fun PositiveWisdomContent() {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      AuraCard(borderColor = LimePillAccent) {
        Text(
          text = "Today's Positive Note (Rhonda Byrne)",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = TextDeepForest
          )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "“When you are grateful, it is impossible to have negative feelings. Gratitude dissolves fear, creates instant magnetic harmony, and opens the door to effortless abundance.”",
          style = MaterialTheme.typography.bodyMedium.copy(
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
            color = TextDeepForest,
            lineHeight = 22.sp
          )
        )
      }
    }

    item {
      AuraCard(borderColor = SageBorder) {
        Text(
          text = "Dr. Joe Dispenza Mind Calibration",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = TextDeepForest
          )
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "“To create a new personal reality, you must change your personality. Train your brain to feel the elevated emotions of your fulfilled future right now in the morning silence.”",
          style = MaterialTheme.typography.bodySmall.copy(color = TextMediumSage, lineHeight = 20.sp)
        )
      }
    }
  }
}

@Composable
fun TenGratitudeContent(
  gratitudeList: List<GratitudeItem>,
  onToggle: (Int) -> Unit,
  onOpenAdd: () -> Unit
) {
  val completedCount = gratitudeList.count { it.isCompleted }

  Column(modifier = Modifier.fillMaxSize()) {
    AuraCard(
      modifier = Modifier.fillMaxWidth(),
      borderColor = SageBorder
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
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
            text = "$completedCount of 10 Blessings Completed",
            style = MaterialTheme.typography.bodySmall.copy(color = TextMediumSage)
          )
        }

        IconButton(
          onClick = onOpenAdd,
          modifier = Modifier.testTag("add_blessing_button")
        ) {
          Icon(Icons.Default.AddCircle, contentDescription = "Add Blessing", tint = ForestButtonBg)
        }
      }

      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = "Say each blessing with heartfelt emotion and repeat: 'Thank you, thank you, thank you!'",
        style = MaterialTheme.typography.labelSmall.copy(color = TextMutedSage)
      )
    }

    Spacer(modifier = Modifier.height(10.dp))

    LazyColumn(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f),
      verticalArrangement = Arrangement.spacedBy(8.dp),
      contentPadding = PaddingValues(bottom = 16.dp)
    ) {
      items(gratitudeList, key = { it.id }) { item ->
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
              width = if (item.isCompleted) 1.5.dp else 0.5.dp,
              color = if (item.isCompleted) ForestButtonBg else SageBorder,
              shape = RoundedCornerShape(16.dp)
            )
            .clickable { onToggle(item.id) }
            .testTag("blessing_item_${item.id}"),
          colors = CardDefaults.cardColors(
            containerColor = if (item.isCompleted) SageCardElevated else SageCardSurface
          )
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            verticalAlignment = Alignment.Top
          ) {
            Checkbox(
              checked = item.isCompleted,
              onCheckedChange = { onToggle(item.id) },
              colors = CheckboxDefaults.colors(
                checkedColor = ForestButtonBg,
                checkmarkColor = ForestButtonText
              )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "${item.id}. I am truly blessed to have ${item.blessing},",
                style = MaterialTheme.typography.bodyMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = TextDeepForest
                )
              )
              Text(
                text = "because ${item.reason}.",
                style = MaterialTheme.typography.bodySmall.copy(color = TextMediumSage)
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "Thank you, thank you, thank you! ✨",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = if (item.isCompleted) ForestButtonBg else TextMutedSage
                )
              )
            }
          }
        }
      }
    }
  }
}
