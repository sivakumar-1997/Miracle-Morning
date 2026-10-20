package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val SoftPinkBlueLightScheme = lightColorScheme(
  primary = RosePrimary,
  onPrimary = WhitePure,
  primaryContainer = RoseMist,
  onPrimaryContainer = RoseDeep,
  secondary = BlueDeep,
  onSecondary = WhitePure,
  secondaryContainer = BlueMist,
  onSecondaryContainer = BlueNavy,
  tertiary = AuraGold,
  onTertiary = TextDark,
  tertiaryContainer = AuraGoldSoft,
  onTertiaryContainer = TextDark,
  background = RoseSoft,
  onBackground = TextDark,
  surface = WhitePure,
  onSurface = TextDark,
  surfaceVariant = BlueSoft,
  onSurfaceVariant = TextMuted,
  outline = CardBorder,
)

private val SoftPinkBlueDarkScheme = darkColorScheme(
  primary = RoseBlush,
  onPrimary = RoseDeep,
  primaryContainer = RoseDeep,
  onPrimaryContainer = RoseMist,
  secondary = BlueSky,
  onSecondary = BlueNavy,
  secondaryContainer = BlueDeep,
  onSecondaryContainer = BlueMist,
  tertiary = AuraGold,
  onTertiary = TextDark,
  background = RoseDarkSurface,
  onBackground = WhitePure,
  surface = Color(0xFF281C25),
  onSurface = WhitePure,
  surfaceVariant = Color(0xFF1E2836),
  onSurfaceVariant = BlueSky,
  outline = Color(0x33EC4899),
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // We prefer our curated aesthetic so soft pink and light blue shine through consistently
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    darkTheme -> SoftPinkBlueDarkScheme
    else -> SoftPinkBlueLightScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
