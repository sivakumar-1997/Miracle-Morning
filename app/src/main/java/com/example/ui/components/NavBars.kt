package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppDestination

@Composable
fun TopAuraBar(
  title: String,
  currentDestination: AppDestination,
  isWireframeOverlay: Boolean,
  onToggleWireframe: () -> Unit,
  onNavigateToWireframeScreen: () -> Unit,
  onNavigateToPricing: () -> Unit,
  onNavigateBack: (() -> Unit)? = null
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .statusBarsPadding()
      .padding(horizontal = 16.dp, vertical = 10.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      if (onNavigateBack != null) {
        IconButton(
          onClick = onNavigateBack,
          modifier = Modifier
            .minimumInteractiveComponentSize()
            .testTag("nav_back_button")
        ) {
          Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Navigate Back",
            tint = RoseDeep
          )
        }
        Spacer(modifier = Modifier.width(4.dp))
      }
      Column {
        Text(
          text = title,
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            color = RoseDeep
          )
        )
        Text(
          text = "Aura & Manifestation Sanctum",
          style = MaterialTheme.typography.labelMedium.copy(
            color = BlueDeep
          )
        )
      }
    }

    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      // 2D Wireframe Quick Toggle / Viewer
      Button(
        onClick = onNavigateToWireframeScreen,
        colors = ButtonDefaults.buttonColors(
          containerColor = if (currentDestination == AppDestination.WIREFRAME_VIEWER) WireframeBlue else BlueMist
        ),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
        modifier = Modifier
          .height(36.dp)
          .testTag("wireframe_toggle_button")
      ) {
        Icon(
          imageVector = Icons.Default.Architecture,
          contentDescription = "2D Wireframe",
          modifier = Modifier.size(16.dp),
          tint = if (currentDestination == AppDestination.WIREFRAME_VIEWER) WhitePure else BlueDeep
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = "2D Wireframe",
          style = MaterialTheme.typography.labelMedium.copy(
            fontWeight = FontWeight.Bold,
            color = if (currentDestination == AppDestination.WIREFRAME_VIEWER) WhitePure else BlueDeep
          )
        )
      }

      // Pricing vs Stella & Perla badge button
      IconButton(
        onClick = onNavigateToPricing,
        modifier = Modifier
          .size(36.dp)
          .clip(CircleShape)
          .background(RoseMist)
          .border(1.dp, CardBorder, CircleShape)
          .testTag("pricing_icon_button")
      ) {
        Icon(
          imageVector = Icons.Default.WorkspacePremium,
          contentDescription = "Pricing Comparison vs Stella and Perla",
          tint = RosePrimary,
          modifier = Modifier.size(18.dp)
        )
      }
    }
  }
}

data class NavItem(
  val destination: AppDestination,
  val label: String,
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector
)

@Composable
fun BottomAuraBar(
  currentDestination: AppDestination,
  onSelect: (AppDestination) -> Unit
) {
  val items = listOf(
    NavItem(AppDestination.HOME, "Home", Icons.Filled.Home, Icons.Outlined.Home),
    NavItem(AppDestination.MORNING_ROUTINE, "Morning", Icons.Filled.WbSunny, Icons.Outlined.WbSunny),
    NavItem(AppDestination.NIGHT_ROUTINE, "Night", Icons.Filled.NightsStay, Icons.Outlined.NightsStay),
    NavItem(AppDestination.STREAKS, "Streaks", Icons.Filled.DateRange, Icons.Outlined.DateRange),
    NavItem(AppDestination.BOOKS, "Books", Icons.Filled.AutoStories, Icons.Outlined.AutoStories)
  )

  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .navigationBarsPadding(),
    color = SurfaceGlass,
    tonalElevation = 6.dp,
    shadowElevation = 8.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(68.dp)
        .padding(horizontal = 8.dp),
      horizontalArrangement = Arrangement.SpaceAround,
      verticalAlignment = Alignment.CenterVertically
    ) {
      items.forEach { item ->
        val isSelected = currentDestination == item.destination
        val activeColor = RosePrimary
        val inactiveColor = TextMuted

        Column(
          modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .clickable { onSelect(item.destination) }
            .testTag("nav_item_${item.destination.name.lowercase()}"),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .background(if (isSelected) RoseMist else Color.Transparent)
              .padding(horizontal = 14.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
              contentDescription = item.label,
              tint = if (isSelected) activeColor else inactiveColor,
              modifier = Modifier.size(22.dp)
            )
          }
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = item.label,
            style = MaterialTheme.typography.labelMedium.copy(
              fontSize = 11.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              color = if (isSelected) activeColor else inactiveColor
            )
          )
        }
      }
    }
  }
}
