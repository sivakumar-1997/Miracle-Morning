package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val SageOrganicLightScheme = lightColorScheme(
  primary = ForestButtonBg,
  onPrimary = ForestButtonText,
  primaryContainer = SageCardElevated,
  onPrimaryContainer = TextDeepForest,
  secondary = LimePillAccent,
  onSecondary = ForestButtonBg,
  secondaryContainer = SageMistDarker,
  onSecondaryContainer = TextDeepForest,
  tertiary = MintFreshAccent,
  onTertiary = ForestButtonBg,
  background = SageCreamBg,
  onBackground = TextDeepForest,
  surface = SageCardSurface,
  onSurface = TextDeepForest,
  surfaceVariant = SageCardElevated,
  onSurfaceVariant = TextMediumSage,
  outline = SageBorder
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = false, // Set to false to match the light sage theme in reference image_dc3f9eb0.png
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colorScheme = SageOrganicLightScheme,
    typography = Typography,
    content = content
  )
}
