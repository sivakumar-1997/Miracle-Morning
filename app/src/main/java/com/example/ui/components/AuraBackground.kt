package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*

@Composable
fun AuraBackground(
  modifier: Modifier = Modifier,
  content: @Composable BoxScope.() -> Unit
) {
  val infiniteTransition = rememberInfiniteTransition(label = "sageAuraGlow")
  val pulseFraction by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 8000, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "sageAuraPulse"
  )

  // Warm pale sage & creamy eucalyptus tones from Dribbble reference image_dc3f9eb0.png
  val gradientBrush = Brush.verticalGradient(
    colors = listOf(
      SageMistLight,
      SageCreamBg,
      SageMistDarker.copy(alpha = 0.6f + pulseFraction * 0.15f),
      SageCreamBg
    )
  )

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(gradientBrush)
  ) {
    content()
  }
}

@Composable
fun AuraCard(
  modifier: Modifier = Modifier,
  cornerRadius: Dp = 22.dp,
  backgroundColor: Color = SageCardSurface,
  borderColor: Color = SageBorder,
  content: @Composable ColumnScope.() -> Unit
) {
  Card(
    modifier = modifier
      .clip(RoundedCornerShape(cornerRadius))
      .border(1.dp, borderColor, RoundedCornerShape(cornerRadius)),
    shape = RoundedCornerShape(cornerRadius),
    colors = CardDefaults.cardColors(containerColor = backgroundColor),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(18.dp)
    ) {
      content()
    }
  }
}

/**
 * Dark Forest Green Pill Button (as shown in reference image_dc3f9eb0.png "Buy Now", "Continue")
 */
@Composable
fun SoftBlueButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  content: @Composable RowScope.() -> Unit
) {
  Button(
    onClick = onClick,
    enabled = enabled,
    modifier = modifier
      .height(52.dp),
    shape = RoundedCornerShape(26.dp), // Sleek pill shape
    colors = ButtonDefaults.buttonColors(
      containerColor = ForestButtonBg,
      contentColor = ForestButtonText,
      disabledContainerColor = SageCardElevated,
      disabledContentColor = TextMutedSage
    ),
    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp, pressedElevation = 5.dp)
  ) {
    content()
  }
}
