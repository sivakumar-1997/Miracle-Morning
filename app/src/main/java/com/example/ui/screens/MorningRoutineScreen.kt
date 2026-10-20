package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GratitudeItem
import com.example.data.model.MorningRoutineStep
import com.example.ui.components.AuraCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.ManifestationViewModel

@Composable
fun MorningRoutineScreen(
  viewModel: ManifestationViewModel
) {
  val currentStepIndex by viewModel.activeMorningStepIndex.collectAsState()
  val steps = MorningRoutineStep.values()
  val currentStep = steps[currentStepIndex]

  val gratitudeList by viewModel.gratitudeList.collectAsState()
  val waterSips by viewModel.waterSipsTaken.collectAsState()
  val waterIntent by viewModel.waterIntent.collectAsState()
  val selectedFrequency by viewModel.selectedFrequencyHz.collectAsState()
  val isFrequencyPlaying by viewModel.isFrequencyPlaying.collectAsState()

  var newBlessingWhat by remember { mutableStateOf("") }
  var newBlessingWhy by remember { mutableStateOf("") }
  var showAddDialog by remember { mutableStateOf(false) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
  ) {
    // Stepper Tab Selector (Horizontal scrollable pill tabs)
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp),
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      steps.forEachIndexed { index, step ->
        val isSelected = index == currentStepIndex
        val isDone = index < currentStepIndex
        Box(
          modifier = Modifier
            .weight(1f)
            .height(34.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(
              when {
                isSelected -> RosePrimary
                isDone -> BlueSky
                else -> BlueMist.copy(alpha = 0.5f)
              }
            )
            .clickable { viewModel.setMorningStep(index) }
            .testTag("morning_step_tab_$index"),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "${index + 1}",
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold,
              color = if (isSelected) WhitePure else if (isDone) BlueDeep else TextMuted
            )
          )
        }
      }
    }

    // Step Header Card
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(18.dp)),
      colors = CardDefaults.cardColors(containerColor = RoseMist.copy(alpha = 0.6f))
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(RosePrimary),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "${currentStep.stepNumber}",
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Bold,
              color = WhitePure
            )
          )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = currentStep.title,
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = RoseDeep
            )
          )
          Text(
            text = currentStep.tamilSubtitle,
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.SemiBold,
              color = BlueDeep
            )
          )
        }
        Surface(
          color = WhitePure,
          shape = RoundedCornerShape(10.dp)
        ) {
          Text(
            text = "~${currentStep.durationMinutes} min",
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall.copy(
              color = RoseDeep,
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
          AuraCleansingContent()
        }
        MorningRoutineStep.SACRED_PRAYER -> {
          SacredPrayerContent()
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
        MorningRoutineStep.BRAIN_FREQUENCY_TUNING -> {
          BrainFrequencyContent(
            selectedHz = selectedFrequency,
            isPlaying = isFrequencyPlaying,
            onSelectHz = { viewModel.setFrequency(it) },
            onToggleAudio = { viewModel.toggleFrequencyAudio() }
          )
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

    // Step Bottom Navigation Controls (Previous / Next)
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
            .height(48.dp),
          shape = RoundedCornerShape(14.dp)
        ) {
          Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Previous")
        }
      }

      Button(
        onClick = {
          if (currentStepIndex < steps.size - 1) {
            viewModel.nextMorningStep()
          } else {
            viewModel.showToast("Congratulations! Morning Brahma Manifestation complete ✨")
          }
        },
        modifier = Modifier
          .weight(if (currentStepIndex > 0) 1.5f else 1f)
          .height(48.dp)
          .testTag("morning_step_next_btn"),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = RosePrimary)
      ) {
        Text(
          text = if (currentStepIndex < steps.size - 1) "Next: ${steps[currentStepIndex + 1].title.take(18)}..." else "Complete Morning Routine ✨",
          style = MaterialTheme.typography.titleSmall.copy(
            fontWeight = FontWeight.Bold,
            color = WhitePure
          )
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
      }
    }
  }

  // Add Gratitude Blessing Dialog
  if (showAddDialog) {
    AlertDialog(
      onDismissRequest = { showAddDialog = false },
      title = {
        Text(
          text = "Add Daily Blessing (The Magic)",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = RoseDeep)
        )
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text(
            text = "Rhonda Byrne formula: 'I am truly blessed to have [WHAT], because [WHY].'",
            style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
          )
          OutlinedTextField(
            value = newBlessingWhat,
            onValueChange = { newBlessingWhat = it },
            label = { Text("What are you blessed to have?") },
            placeholder = { Text("e.g. peaceful morning silence") },
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
          colors = ButtonDefaults.buttonColors(containerColor = RosePrimary)
        ) {
          Text("Add Blessing")
        }
      },
      dismissButton = {
        TextButton(onClick = { showAddDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }
}

// ----------------- SUB-COMPONENTS FOR EACH MORNING STEP -----------------

@Composable
fun BrahmaMuhurtaAwakeningContent() {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      AuraCard(borderColor = AuraGold) {
        Text(
          text = "The Creator's Hour • பிரம்ம முகூர்த்தம்",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = RoseDeep
          )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "Waking up exactly between 4:30 AM and 5:00 AM aligns you with the Brahmam (Universal Consciousness). At this precise period, the planetary energy has the highest concentration of nascent oxygen, and the pineal gland naturally secretes melatonin and serotonin for divine stillness.",
          style = MaterialTheme.typography.bodyMedium.copy(color = TextDark, lineHeight = 22.sp)
        )
      }
    }

    item {
      AuraCard(borderColor = BlueSky) {
        Text(
          text = "Awakening Ritual Sequence",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = BlueDeep
          )
        )
        Spacer(modifier = Modifier.height(8.dp))
        val sequence = listOf(
          "1. Open eyes gently and view both palms joined together (Karaagre Vasate Lakshmi).",
          "2. Step right foot onto earth with reverence and unspoken gratitude.",
          "3. Light a gentle lamp or candle; maintain noble silence without phones or social media.",
          "4. Sit in comfortable posture facing East or North-East."
        )
        sequence.forEach { item ->
          Text(
            text = item,
            style = MaterialTheme.typography.bodySmall.copy(color = TextDark),
            modifier = Modifier.padding(vertical = 4.dp)
          )
        }
      }
    }
  }
}

@Composable
fun AuraCleansingContent() {
  val infiniteTransition = rememberInfiniteTransition(label = "breath")
  val breathScale by infiniteTransition.animateFloat(
    initialValue = 0.85f,
    targetValue = 1.15f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 4000, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "breathScale"
  )

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(14.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    item {
      // Animated breathing and aura sweep visualizer
      Box(
        modifier = Modifier
          .size(140.dp)
          .scale(breathScale)
          .clip(CircleShape)
          .background(
            Brush.radialGradient(
              colors = listOf(RoseMist, BlueSky, Color.Transparent)
            )
          )
          .border(2.dp, RosePrimary.copy(alpha = 0.5f), CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(
            imageVector = Icons.Default.Air,
            contentDescription = null,
            tint = RoseDeep,
            modifier = Modifier.size(36.dp)
          )
          Text(
            text = "Prana Breath",
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              color = RoseDeep
            )
          )
        }
      }
    }

    item {
      AuraCard(borderColor = RoseBlush) {
        Text(
          text = "Aura Cleansing (ஆரா சுத்தி)",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = RoseDeep
          )
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "Your electromagnetic aura extends 2 to 3 feet around your body. Throughout sleep, residual emotional static and astral dust accumulate.",
          style = MaterialTheme.typography.bodyMedium.copy(color = TextDark)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
          text = "Instructions for Sweeping:",
          style = MaterialTheme.typography.labelLarge.copy(color = BlueDeep)
        )
        Text(
          text = "• Rub your palms together briskly until warm prana tingling begins.\n• Sweep both hands 2 inches above your crown downward to your shoulders, arms, and torso.\n• Visualize pure golden-rose light burning away any lingering tension, anxiety, or heavy attachments.\n• Breathe in for 4 seconds, hold for 4 seconds, exhale for 4 seconds with a sigh of relief.",
          style = MaterialTheme.typography.bodySmall.copy(color = TextDark, lineHeight = 20.sp)
        )
      }
    }
  }
}

@Composable
fun SacredPrayerContent() {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      AuraCard(borderColor = BlueDeep) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.SelfImprovement, contentDescription = null, tint = RosePrimary)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Sacred Prayer of Universal Surrender",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = RoseDeep
            )
          )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Surface(
          color = RoseSoft,
          shape = RoundedCornerShape(12.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, RoseMist)
        ) {
          Text(
            text = "“O Divine Supreme Consciousness, Source of all Light and Breath,\nI place this new day at Your sacred feet.\nCleanse my mind of fear, scarcity, and doubt.\nFill my heart with reverence, unconditional love, and infinite gratitude.\nMay every thought I think, every word I speak, and every action I take be a vessel of blessing to the world.\nI surrender and I receive with open arms.”",
            modifier = Modifier.padding(14.dp),
            style = MaterialTheme.typography.bodyMedium.copy(
              fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
              color = TextDark,
              lineHeight = 22.sp
            )
          )
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(
          text = "Tamil Sankalpam (சங்கல்பம்):",
          style = MaterialTheme.typography.labelMedium.copy(color = BlueDeep, fontWeight = FontWeight.Bold)
        )
        Text(
          text = "“இறைவனின் பேரருளால் இன்று அனைத்தும் நன்மையாகவே நடக்கிறது. நான் ஆரோக்கியமும், அமைதியும், எல்லையற்ற செல்வமும் நிறைந்திருக்கிறேன். நன்றி, நன்றி, நன்றி!”",
          style = MaterialTheme.typography.bodySmall.copy(color = TextDark, lineHeight = 20.sp)
        )
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
      // 2D Water Glass Visualizer with subtle animated ripples
      Box(
        modifier = Modifier
          .size(160.dp)
          .clip(CircleShape)
          .background(
            Brush.verticalGradient(
              colors = listOf(BlueMist, BlueSky)
            )
          )
          .border(2.dp, BlueDeep, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(
            imageVector = Icons.Default.WaterDrop,
            contentDescription = null,
            tint = BlueDeep,
            modifier = Modifier.size(48.dp)
          )
          Text(
            text = "$waterSips of 7 Sips",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = BlueDeep
            )
          )
          Text(
            text = "Cellular Infusion",
            style = MaterialTheme.typography.labelSmall.copy(color = TextDark)
          )
        }
      }
    }

    item {
      AuraCard(borderColor = BlueDeep) {
        Text(
          text = "Dr. Masaru Emoto Water Alchemy",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = BlueDeep
          )
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "Water is a living crystalline memory tape. As demonstrated by Dr. Emoto, holding a glass of pure water between your palms and feeling pure gratitude physically rearranges the water into hexagonal radiant crystals.",
          style = MaterialTheme.typography.bodySmall.copy(color = TextDark)
        )

        Spacer(modifier = Modifier.height(12.dp))
        Text(
          text = "Intention Charged into the Water:",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = RoseDeep)
        )

        if (isEditingIntent) {
          OutlinedTextField(
            value = editedIntent,
            onValueChange = { editedIntent = it },
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(6.dp))
          Button(
            onClick = {
              onUpdateIntent(editedIntent)
              isEditingIntent = false
            },
            colors = ButtonDefaults.buttonColors(containerColor = BlueDeep)
          ) {
            Text("Save Water Intention")
          }
        } else {
          Surface(
            color = BlueSoft,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.clickable { isEditingIntent = true }
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = "“$waterIntent”",
                style = MaterialTheme.typography.bodyMedium.copy(
                  fontWeight = FontWeight.SemiBold,
                  color = BlueDeep,
                  fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                ),
                modifier = Modifier.weight(1f)
              )
              Icon(Icons.Default.Edit, contentDescription = "Edit Intention", tint = BlueDeep, modifier = Modifier.size(18.dp))
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Button(
            onClick = onSip,
            modifier = Modifier
              .weight(1f)
              .height(48.dp)
              .testTag("water_drink_sip_btn"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BlueDeep)
          ) {
            Icon(Icons.Default.LocalDrink, contentDescription = null, tint = WhitePure)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Drink Sip ($waterSips/7)")
          }

          OutlinedButton(
            onClick = onReset,
            shape = RoundedCornerShape(14.dp)
          ) {
            Text("Refill Glass")
          }
        }
      }
    }
  }
}

@Composable
fun BrainFrequencyContent(
  selectedHz: Int,
  isPlaying: Boolean,
  onSelectHz: (Int) -> Unit,
  onToggleAudio: () -> Unit
) {
  val frequencies = listOf(
    Pair(528, "528 Hz • Miracle & DNA Repair (Love)"),
    Pair(432, "432 Hz • Universal Harmony & Calm"),
    Pair(639, "639 Hz • Heart Connection & Relationships"),
    Pair(741, "741 Hz • Awakening Intuition & Vision"),
    Pair(396, "396 Hz • Liberating Fear & Guilt")
  )

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      AuraCard(borderColor = RosePrimary) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Column {
            Text(
              text = "Solfeggio Brain Frequency Tuning",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = RoseDeep
              )
            )
            Text(
              text = "Shift into Alpha (8-12 Hz) & Theta (4-8 Hz) states",
              style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
            )
          }

          FilledIconButton(
            onClick = onToggleAudio,
            colors = IconButtonDefaults.filledIconButtonColors(
              containerColor = if (isPlaying) RosePrimary else BlueDeep
            )
          ) {
            Icon(
              imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
              contentDescription = "Toggle Audio",
              tint = WhitePure
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        frequencies.forEach { (hz, title) ->
          val isSelected = hz == selectedHz
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp)
              .clip(RoundedCornerShape(12.dp))
              .border(
                width = if (isSelected) 1.5.dp else 0.5.dp,
                color = if (isSelected) RosePrimary else CardBorder,
                shape = RoundedCornerShape(12.dp)
              )
              .clickable { onSelectHz(hz) },
            colors = CardDefaults.cardColors(
              containerColor = if (isSelected) RoseMist else WhitePure
            )
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                  color = if (isSelected) RoseDeep else TextDark
                )
              )
              if (isSelected && isPlaying) {
                Text(
                  text = "Playing ♪",
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = RosePrimary
                  )
                )
              }
            }
          }
        }
      }
    }

    item {
      AuraCard(borderColor = BlueSky) {
        Text(
          text = "Positive Note of the Day (The Magic)",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = BlueDeep
          )
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "“Reflect on your present blessings, of which every man has many; not on your past misfortunes, of which all men have some.” — Charles Dickens (quoted in The Magic by Rhonda Byrne)",
          style = MaterialTheme.typography.bodyMedium.copy(
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
            color = TextDark,
            lineHeight = 22.sp
          )
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
      borderColor = RosePrimary
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Count Your 10 Blessings",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = RoseDeep
            )
          )
          Text(
            text = "$completedCount of 10 Blessings Felt & Acknowledged",
            style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
          )
        }

        IconButton(
          onClick = onOpenAdd,
          modifier = Modifier.testTag("add_blessing_button")
        ) {
          Icon(Icons.Default.AddCircle, contentDescription = "Add Blessing", tint = RosePrimary)
        }
      }

      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = "Say each one aloud or in your mind, and repeat with deep feeling: 'Thank you, thank you, thank you!'",
        style = MaterialTheme.typography.labelSmall.copy(color = BlueDeep)
      )
    }

    Spacer(modifier = Modifier.height(8.dp))

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
            .clip(RoundedCornerShape(14.dp))
            .border(
              width = if (item.isCompleted) 1.5.dp else 0.5.dp,
              color = if (item.isCompleted) RosePrimary else CardBorder,
              shape = RoundedCornerShape(14.dp)
            )
            .clickable { onToggle(item.id) }
            .testTag("blessing_item_${item.id}"),
          colors = CardDefaults.cardColors(
            containerColor = if (item.isCompleted) RoseMist.copy(alpha = 0.5f) else WhitePure
          )
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            verticalAlignment = Alignment.Top
          ) {
            Checkbox(
              checked = item.isCompleted,
              onCheckedChange = { onToggle(item.id) },
              colors = CheckboxDefaults.colors(checkedColor = RosePrimary)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "${item.id}. I am truly blessed to have ${item.blessing},",
                style = MaterialTheme.typography.bodyMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = if (item.isCompleted) RoseDeep else TextDark
                )
              )
              Text(
                text = "because ${item.reason}.",
                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "Thank you, thank you, thank you! ✨",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = if (item.isCompleted) RosePrimary else BlueDeep
                )
              )
            }
          }
        }
      }
    }
  }
}
