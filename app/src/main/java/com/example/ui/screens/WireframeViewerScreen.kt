package com.example.ui.screens

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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AuraCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppDestination
import com.example.ui.viewmodel.ManifestationViewModel

data class WireframeScreenSpec(
  val id: String,
  val title: String,
  val category: String,
  val destinationTarget: AppDestination,
  val wireframeDescription: String,
  val components: List<String>,
  val userActions: List<String>
)

@Composable
fun WireframeViewerScreen(
  viewModel: ManifestationViewModel,
  onNavigateToScreen: (AppDestination) -> Unit
) {
  val wireframeSpecs = listOf(
    WireframeScreenSpec(
      id = "auth",
      title = "1. Auth & Credentials (Login / Register / Reset)",
      category = "Authentication",
      destinationTarget = AppDestination.AUTH,
      wireframeDescription = "2D wireframe for user authentication. Displays brand aura logo, mode tabs (Login, Register, Forgot Password), form input fields (Email, Password, Name), and direct recovery link dispatch.",
      components = listOf(
        "[Frame: Auth_Container 360x800]",
        "  ├── [Header: Sacred_Aura_Emblem 80x80 Circle]",
        "  ├── [Text: AppTitle_AuraMagic (28sp Serif)]",
        "  ├── [SegmentedControl: Login | Register | Reset]",
        "  ├── [TextField: Input_Email (Rounded 14dp)]",
        "  ├── [TextField: Input_Password (Visibility toggle)]",
        "  ├── [Link: Forgot_Password_Text]",
        "  └── [PrimaryButton: Enter_Sanctum_Button]"
      ),
      userActions = listOf(
        "User selects 'Register' -> Enters Name & Email -> Moves directly to Onboarding.",
        "User clicks 'Forgot Password' -> Dispatches secure reset link.",
        "User clicks 'Instant Demo' -> Enters directly as practitioner."
      )
    ),
    WireframeScreenSpec(
      id = "onboarding",
      title = "2. Onboarding Questionnaire Flow",
      category = "User Alignment",
      destinationTarget = AppDestination.ONBOARDING,
      wireframeDescription = "4-step alignment questionnaire immediately following registration. Collects spiritual nickname, current vibrational mood, primary intention, and Brahma Muhurta awakening schedule.",
      components = listOf(
        "[Frame: Onboarding_StepWizard 360x800]",
        "  ├── [LinearIndicator: Step_1_to_4_Progress]",
        "  ├── [QuestionCard: 'How May We Call You?' (Nickname)]",
        "  ├── [Grid: Mood_Selection_Cards (Grateful / Calm / Energetic / Stressed)]",
        "  ├── [RadioGroup: Intention_Selection (Abundance / Health / Love)]",
        "  ├── [Scheduler: Brahma_Muhurta_Wake_Time (04:30 AM default)]",
        "  └── [PrimaryButton: Next_Step / Finish]"
      ),
      userActions = listOf(
        "Selects nickname or quick chips ('Siva', 'Aura').",
        "Selects vibration frequency.",
        "Confirms 4:30 AM Brahma Muhurta hour."
      )
    ),
    WireframeScreenSpec(
      id = "home",
      title = "3. Home Dashboard & Daily Sanctum",
      category = "Core Flow",
      destinationTarget = AppDestination.HOME,
      wireframeDescription = "Main hub of AuraMagic. Displays daily greeting, current vibration status, Brahma Muhurta countdown, quick action cards for morning rituals, water manifestation widget, and 10 blessings progress.",
      components = listOf(
        "[Frame: Home_Dashboard_Scrollable 360x800]",
        "  ├── [HeaderCard: Greeting, Nickname, Streak Fire & Aura Bloom]",
        "  ├── [Banner: Brahma_Muhurta_Countdown (4:30 AM Creator's Hour)]",
        "  ├── [ActionCard: Morning_Rituals_Progress_Bar (3/6 Steps)]",
        "  ├── [Widget: Water_Manifestation_Sip_Tracker (3/7 sips)]",
        "  ├── [Widget: Rhonda_Byrne_10_Blessings_Summary]",
        "  ├── [Widget: 528Hz_Solfeggio_Frequency_Player]",
        "  ├── [TeaserCard: Night_Routine_Magic_Rock]",
        "  └── [ComparisonBanner: AuraMagic vs Stella/Perla]"
      ),
      userActions = listOf(
        "Tap 'Enter Morning Ritual Flow' -> Opens guided stepped sequence.",
        "Tap 'Take 1 Sip' -> Increments cellular hydration intention.",
        "Tap '528Hz Tone' -> Toggles ambient frequency player."
      )
    ),
    WireframeScreenSpec(
      id = "morning_routine",
      title = "4. Morning Routine (Brahma Muhurta to 10 Blessings)",
      category = "Ritual Streamline",
      destinationTarget = AppDestination.MORNING_ROUTINE,
      wireframeDescription = "The core streamlined morning sequence. 6 sequential steps: 1. Awakening at 4:30 AM -> 2. Aura Cleansing (ஆரா சுத்தி) -> 3. Cosmic Prayer -> 4. Water Manifestation -> 5. Frequency Tuning & Book Notes -> 6. 10 Gratitude Practices.",
      components = listOf(
        "[Frame: Morning_Routine_Stepper 360x800]",
        "  ├── [HorizontalStepTabs: 1 2 3 4 5 6]",
        "  ├── [Header: Step_Title & Tamil_Subtitle & Duration]",
        "  ├── [StepView_1: Brahma Muhurta Cosmic Context & Karaagre Vasate]",
        "  ├── [StepView_2: Aura Brush Instructions & 4-7-8 Animated Breath Circle]",
        "  ├── [StepView_3: Universal Prayer of Surrender & Tamil Sankalpam]",
        "  ├── [StepView_4: Glass of Water with 2D Ripple & 7-Sip Counter]",
        "  ├── [StepView_5: Solfeggio 528Hz / 432Hz Selector & Positive Notes]",
        "  ├── [StepView_6: 10 Gratitude List with 'Thank you, thank you, thank you']",
        "  └── [FooterControls: Previous_Button | Next_Step_Button]"
      ),
      userActions = listOf(
        "Practitioner completes each ritual with timer / guided prompts.",
        "Checks off 10 blessings.",
        "Advances seamlessly through the 6 stages."
      )
    ),
    WireframeScreenSpec(
      id = "water_manifestation",
      title = "5. Water Manifestation Alchemy (Dr. Masaru Emoto)",
      category = "Specialized Ritual",
      destinationTarget = AppDestination.MORNING_ROUTINE,
      wireframeDescription = "Focused 2D blueprint of the water manifestation glass ritual. Place glass in front, hold with both hands, whisper/imprint vibration, and take 7 mindful sips.",
      components = listOf(
        "[Frame: Water_Manifestation_Canvas]",
        "  ├── [Illustration: Glass_Of_Water with Hexagonal_Crystal_Field]",
        "  ├── [Counter: '3 of 7 Mindful Sips Taken']",
        "  ├── [EditableCard: Spoken_Intention ('Infinite Health & Abundance')]",
        "  ├── [Instruction: Palm_Cupping & Hexagonal_Resonance_Science]",
        "  └── [ButtonRow: Drink_1_Sip_Button | Refill_Glass_Button]"
      ),
      userActions = listOf(
        "Edit or customize intention whispered into water.",
        "Tap 'Drink Sip' -> Triggers cellular intention confirmation."
      )
    ),
    WireframeScreenSpec(
      id = "night_routine",
      title = "6. Night Routine & The Magic Rock",
      category = "Evening Reflection",
      destinationTarget = AppDestination.NIGHT_ROUTINE,
      wireframeDescription = "Night twilight protocol inspired by Rhonda Byrne's 'The Magic'. Step 1: Read tomorrow's exercise in advance. Step 2: Hold Magic Rock, review day's events, pick best blessing, say 'Thank you!' before sleep.",
      components = listOf(
        "[Frame: Night_Routine_Sanctum 360x800]",
        "  ├── [TwilightHeader: Dark_Mode_Moon_Theme]",
        "  ├── [PreviewCard: Tomorrow_Exercise_Day_2_The_Magic_Rock]",
        "  ├── [VisualCard: Bedside_Magic_Rock_Talisman]",
        "  ├── [TextField: Input_Best_Blessing_Of_The_Day]",
        "  └── [PrimaryButton: Seal_With_'Thank_You!'_Before_Sleep]"
      ),
      userActions = listOf(
        "Reads preview of Day 2 practice.",
        "Enters today's greatest highlight.",
        "Submits night reflection to advance streak."
      )
    ),
    WireframeScreenSpec(
      id = "streaks",
      title = "7. Monthly Streaks & Aura Bloom Calendar",
      category = "Gamification & Habit",
      destinationTarget = AppDestination.STREAKS,
      wireframeDescription = "Monthly calendar tracking consistency for every day of the month. Displays morning Brahma completion, night Magic Rock completion, total blessings count, and Aura Radiance levels.",
      components = listOf(
        "[Frame: Streaks_Month_Matrix 360x800]",
        "  ├── [Header: 18_Consecutive_Days_Fire_Badge]",
        "  ├── [MetricRow: 94% Morning | 88% Night | 180+ Blessings]",
        "  ├── [MonthGrid: 31_Day_Calendar_Cells (October 2026)]",
        "  ├── [Legend: Morning_Dot (Pink) | Night_Dot (Blue)]",
        "  └── [ProgressionBar: Aura_Bloom_Level_4_to_Level_5]"
      ),
      userActions = listOf(
        "Views monthly calendar completion.",
        "Checks progress toward Level 5 Cosmic Luminary."
      )
    ),
    WireframeScreenSpec(
      id = "books",
      title = "8. Manifestation & Brain Frequency Books Library",
      category = "Wisdom & Reference",
      destinationTarget = AppDestination.BOOKS,
      wireframeDescription = "Curated literature reference library. Details classic texts: The Magic (2012), The Secret (2006), Breaking the Habit of Being Yourself (2012), Ask and It Is Given (2004), Water Messages (2004), and Subconscious Mind (1963).",
      components = listOf(
        "[Frame: Books_Wisdom_Library 360x800]",
        "  ├── [CategoryChips: All | Rhonda Byrne | Neuroscience | Water]",
        "  ├── [BookCard_List: Title, Author, Year, Category_Badge]",
        "  ├── [Tag: Brain_Wave_Frequency (Alpha/Theta/Solfeggio)]",
        "  └── [ModalDialog: Full_Book_Summary & Daily_Practice_Integration]"
      ),
      userActions = listOf(
        "Filter by author or scientific category.",
        "Tap book to view full summary, quote, and brain tuning mechanism."
      )
    ),
    WireframeScreenSpec(
      id = "pricing",
      title = "9. Pricing Matrix vs Stella & Perla",
      category = "Monetization & Strategy",
      destinationTarget = AppDestination.PRICING,
      wireframeDescription = "Competitive price point comparison showing AuraMagic at $3.99/mo ($24.99/yr) vs Stella ($9.99/mo) and Perla ($7.99/mo). Highlights Stella's declining market momentum and AuraMagic's comprehensive 6-ritual system.",
      components = listOf(
        "[Frame: Pricing_Comparison_Sanctum 360x800]",
        "  ├── [Badge: '60% More Affordable Than Competitors']",
        "  ├── [Card_Stella: $9.99/mo (Expensive, Stagnant)]",
        "  ├── [Card_Perla: $7.99/mo (Basic Affirmations Only)]",
        "  ├── [Card_AuraMagic: $3.99/mo (Complete Brahma & Water Rituals)]",
        "  ├── [TierSelector: Monthly | Annual ($2.08/mo) | Lifetime Early Bird]",
        "  └── [SubscribeButton: Start_7_Day_Free_Trial]"
      ),
      userActions = listOf(
        "Compares feature matrix against Stella & Perla.",
        "Selects monthly or annual discounted pass.",
        "Activates trial."
      )
    )
  )

  var selectedSpecIndex by remember { mutableIntStateOf(0) }
  val activeSpec = wireframeSpecs[selectedSpecIndex]

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    contentPadding = PaddingValues(top = 10.dp, bottom = 28.dp)
  ) {
    // 2D Wireframe & Figma Blueprint Header
    item {
      AuraCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = WireframeBlue
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(46.dp)
              .clip(CircleShape)
              .background(WireframeBlue.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Architecture,
              contentDescription = null,
              tint = WireframeBlue,
              modifier = Modifier.size(26.dp)
            )
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "2D Wireframe & Architecture Blueprint",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = WireframeBlue
              )
            )
            Text(
              text = "Interactive UI/UX Blueprints, Figma Frames & System Flow",
              style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(color = Color(0xFFCBD5E1), thickness = 0.5.dp)
        Spacer(modifier = Modifier.height(10.dp))

        // Figma Project URL & Reference Card
        Surface(
          color = WireframeBg,
          shape = RoundedCornerShape(12.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, WireframeLine.copy(alpha = 0.5f))
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Figma Wireframe Canvas Link:",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = WireframeText
                )
              )
              Surface(
                color = Color(0xFFEFF6FF),
                shape = RoundedCornerShape(6.dp)
              ) {
                Text(
                  text = "Official Spec",
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                  style = MaterialTheme.typography.labelSmall.copy(
                    color = WireframeBlue,
                    fontWeight = FontWeight.Bold
                  )
                )
              }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "https://www.figma.com/design/auramagic-manifestation-wireframe",
              style = MaterialTheme.typography.bodySmall.copy(
                fontFamily = FontFamily.Monospace,
                color = WireframeBlue,
                fontWeight = FontWeight.SemiBold
              )
            )

            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "Viewport: 360 × 800 dp • 8dp Grid System • M3 Dynamic Tokens\nColors: Soft Rose (#FDF2F8/#EC4899) & Serene Celestial Blue (#F0F9FF/#0284C7)",
              style = MaterialTheme.typography.labelSmall.copy(
                color = TextMuted,
                lineHeight = 16.sp
              )
            )
          }
        }
      }
    }

    // Horizontal Screen Selector Tabs
    item {
      Text(
        text = "Select Screen to Inspect Wireframe:",
        style = MaterialTheme.typography.titleSmall.copy(
          fontWeight = FontWeight.Bold,
          color = TextDark
        )
      )

      Spacer(modifier = Modifier.height(8.dp))

      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        items(wireframeSpecs.size) { index ->
          val spec = wireframeSpecs[index]
          val isSelected = index == selectedSpecIndex
          FilterChip(
            selected = isSelected,
            onClick = { selectedSpecIndex = index },
            label = { Text(spec.title.take(18) + "...") },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = WireframeBlue,
              selectedLabelColor = WhitePure
            )
          )
        }
      }
    }

    // Active Wireframe Screen Inspector Box
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .border(1.5.dp, WireframeBlue, RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = WireframeBg)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = activeSpec.title,
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = WireframeText
                )
              )
              Text(
                text = "Figma Frame ID: frame_${activeSpec.id}_360x800",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontFamily = FontFamily.Monospace,
                  color = WireframeBlue
                )
              )
            }

            Button(
              onClick = { onNavigateToScreen(activeSpec.destinationTarget) },
              colors = ButtonDefaults.buttonColors(containerColor = RosePrimary),
              shape = RoundedCornerShape(10.dp),
              contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Icon(Icons.Default.Launch, contentDescription = null, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Try Live Screen", fontSize = 12.sp)
            }
          }

          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = activeSpec.wireframeDescription,
            style = MaterialTheme.typography.bodySmall.copy(color = TextDark, lineHeight = 18.sp)
          )

          Spacer(modifier = Modifier.height(14.dp))

          // 2D Architectural Wireframe Layout Preview
          Text(
            text = "2D Architectural Component Blueprint:",
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold,
              color = WireframeBlue
            )
          )

          Spacer(modifier = Modifier.height(6.dp))

          Surface(
            color = WhitePure,
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1))
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              activeSpec.components.forEach { comp ->
                Text(
                  text = comp,
                  style = MaterialTheme.typography.bodySmall.copy(
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = Color(0xFF1E293B)
                  ),
                  modifier = Modifier.padding(vertical = 2.dp)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // User Journey Actions
          Text(
            text = "Interaction Flow & State Transitions:",
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold,
              color = WireframeText
            )
          )

          Spacer(modifier = Modifier.height(6.dp))

          activeSpec.userActions.forEach { action ->
            Row(
              modifier = Modifier.padding(vertical = 3.dp),
              verticalAlignment = Alignment.Top
            ) {
              Icon(
                imageVector = Icons.Default.DoubleArrow,
                contentDescription = null,
                tint = WireframeBlue,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = action,
                style = MaterialTheme.typography.bodySmall.copy(color = TextDark)
              )
            }
          }
        }
      }
    }

    // Full End-to-End User Flow Architecture Diagram
    item {
      AuraCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = BlueSky
      ) {
        Text(
          text = "Complete Application Flow Architecture",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = BlueDeep
          )
        )
        Spacer(modifier = Modifier.height(8.dp))

        val flowSteps = listOf(
          "1. Auth Screen: Login, Register, Forgot Password",
          "2. Onboarding Questionnaire: Nickname, Current Vibration, Intention, 4:30 AM Wake Schedule",
          "3. Home Dashboard: Brahma Muhurta Countdown, Quick Actions, Audio Frequency, Teasers",
          "4. Morning Flow Step 1: Brahma Muhurta Awakening (பிரம்ம முகூர்த்தம்) 4:30 AM",
          "5. Morning Flow Step 2: Aura Cleansing (ஆரா சுத்தி / பிராண சுத்தி) & Breathwork",
          "6. Morning Flow Step 3: Sacred Universal Prayer & Alignment",
          "7. Morning Flow Step 4: Water Manifestation Alchemy (Dr. Masaru Emoto 7 Sips)",
          "8. Morning Flow Step 5: Positive Notes & 528Hz Solfeggio Brain Frequency Tuning",
          "9. Morning Flow Step 6: 10 Gratitude Practices (The Magic by Rhonda Byrne)",
          "10. Daytime Alignment: Affirmations, Books Library, Streaks Tracker",
          "11. Night Sanctum Step 1: Preview Tomorrow's Gratitude Practice Prior",
          "12. Night Sanctum Step 2: Hold Magic Rock, Recall Best Blessing, Say 'Thank You' before sleep",
          "13. Monthly Streaks Calendar: 31-Day consistency tracking & Aura Bloom evolution",
          "14. Pricing Matrix: $3.99/mo (60% more affordable than Stella $9.99/mo & Perla $7.99/mo)"
        )

        flowSteps.forEach { step ->
          Text(
            text = "• $step",
            style = MaterialTheme.typography.bodySmall.copy(
              color = TextDark,
              lineHeight = 20.sp
            ),
            modifier = Modifier.padding(vertical = 2.dp)
          )
        }
      }
    }
  }
}
