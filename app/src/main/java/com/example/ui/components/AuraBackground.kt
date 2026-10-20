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
  val infiniteTransition = rememberInfiniteTransition(label = "auraGlow")
  val pulseFraction by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 6000, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "auraPulse"
  )

  val softPink = Color(0xFFFDF2F8)
  val softRoseBlush = Color(0xFFFCE7F3)
  val softBlue = Color(0xFFF0F9FF)
  val celestialSky = Color(0xFFE0F2FE)

  val gradientBrush = Brush.verticalGradient(
    colors = listOf(
      softPink,
      softRoseBlush.copy(alpha = 0.8f + pulseFraction * 0.2f),
      softBlue.copy(alpha = 0.9f),
      celestialSky
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
  cornerRadius: Dp = 20.dp,
  backgroundColor: Color = SurfaceGlass,
  borderColor: Color = CardBorder,
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
        .padding(16.dp)
    ) {
      content()
    }
  }
}
