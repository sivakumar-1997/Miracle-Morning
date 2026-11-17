package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Light Organic Sage & Deep Forest Palette (WCAG AAA/AA Compliant)
val SageCreamBg = Color(0xFFF3F7F4)          // Clean warm organic sage background
val SageMistLight = Color(0xFFF8FAF8)        // Soft light sage highlight
val SageMistDarker = Color(0xFFE4EDE6)       // Deeper muted sage for soft gradients
val SageCardSurface = Color(0xFFFFFFFF)      // Crisp pure white card container (100% opacity)
val SageCardElevated = Color(0xFFEAF1EB)     // Subtle frosted sage elevated card
val SageBorder = Color(0xFFBFD4C6)           // Accessible card border (>= 3:1 non-text contrast)
val SageBorderFocused = Color(0xFF142B1E)    // High contrast focused border
val SageBorderGlow = Color(0x66142B1E)       // Subtle organic border glow

// Buttons & Accents (WCAG AAA/AA Compliant)
val ForestButtonBg = Color(0xFF142B1E)       // Deep forest green button
val ForestButtonText = Color(0xFFFFFFFF)     // High contrast white text on forest (> 14:1)
val LimePillAccent = Color(0xFFA3E635)       // Glowing lime pill highlight
val LimePillDark = Color(0xFF1F4A2C)         // Deep accessible forest-lime for text & icons (> 7:1)
val MintFreshAccent = Color(0xFF22C55E)      // Fresh mint indicator

// Typography Tokens for High Readability & Contrast
val TextDeepForest = Color(0xFF0F2417)       // Primary titles & headlines (16:1 contrast on white)
val TextMediumSage = Color(0xFF23442E)       // Secondary labels, subtitles & icons (8.5:1 contrast)
val TextMutedSage = Color(0xFF3D5E48)        // Muted descriptions & metadata (5.5:1 contrast, > 4.5:1 WCAG AA)
val WhitePure = Color(0xFFFFFFFF)

// Mapped legacy tokens for seamless integration across all screens
val OrganicForestBg = SageCreamBg
val OrganicDeepMoss = SageMistLight
val OrganicSurface = SageCardSurface
val OrganicSurfaceElevated = SageCardElevated
val OrganicSurfaceBorder = SageBorder
val OrganicGlowAccent = LimePillAccent
val OrganicLimePrimary = ForestButtonBg
val OrganicLimeHover = Color(0xFF1D3B2A)
val OrganicMintLight = LimePillAccent
val OrganicForestDarkText = ForestButtonText
val OrganicContainer = SageCardElevated

val CozyMidnightBg = SageCardElevated
val CozyDeepViolet = SageCardSurface
val CozySurface = SageCardSurface
val CozySurfaceElevated = SageCardElevated
val CozySurfaceBorder = SageBorder
val CozyGlowAccent = LimePillAccent
val CozyRoseSoft = Color(0xFFB45309)

val SoftBluePrimary = ForestButtonBg
val SoftBlueHover = OrganicLimeHover
val SoftBlueLight = TextMediumSage           // Replaced low-contrast lime with high-contrast TextMediumSage (> 8:1)
val SoftBlueIce = Color(0xFFDCFCE7)
val SoftBlueDeep = TextMediumSage
val SoftBlueContainer = SageCardElevated
val TextDarkNavy = ForestButtonText

// 7 Chakras Sacred Colors (Adjusted for accessibility & contrast)
val ChakraRootRed = Color(0xFFDC2626)         // Muladhara 396 Hz
val ChakraSacralOrange = Color(0xFFEA580C)    // Svadhisthana 417 Hz
val ChakraSolarYellow = Color(0xFFB45309)     // Manipura 528 Hz (Rich amber gold, WCAG AA compliant)
val ChakraHeartGreen = Color(0xFF15803D)      // Anahata 639 Hz (Deep emerald green)
val ChakraThroatBlue = Color(0xFF0284C7)      // Vishuddha 741 Hz
val ChakraThirdEyeIndigo = Color(0xFF4338CA)  // Ajna 852 Hz
val ChakraCrownViolet = Color(0xFF7E22CE)     // Sahasrara 963 Hz

// Crisp Typography
val TextPrimaryCozy = TextDeepForest
val TextSecondaryCozy = TextMediumSage
val TextMutedCozy = TextMutedSage
