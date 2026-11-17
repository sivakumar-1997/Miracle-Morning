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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.remote.SupabaseCloudState
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppDestination

@Composable
fun TopAuraBar(
  title: String,
  currentDestination: AppDestination,
  onNavigateToPricing: () -> Unit,
  onNavigateBack: (() -> Unit)? = null,
  cloudState: SupabaseCloudState? = null,
  onOpenCloudSync: (() -> Unit)? = null
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .statusBarsPadding()
      .padding(horizontal = 20.dp, vertical = 12.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row(
      modifier = Modifier.weight(1f),
      verticalAlignment = Alignment.CenterVertically
    ) {
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
            tint = TextPrimaryCozy
          )
        }
        Spacer(modifier = Modifier.width(6.dp))
      }
      Column(modifier = Modifier.weight(1f, fill = false)) {
        Text(
          text = title,
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            color = TextPrimaryCozy
          ),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
        Text(
          text = "Miracle Mornings • Sacred Routine",
          style = MaterialTheme.typography.labelSmall.copy(
            color = SoftBlueLight
          ),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }
    }

    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      if (onOpenCloudSync != null) {
        val isAuthed = cloudState?.isAuthenticated == true
        val label = cloudState?.statusLabel ?: "Cloud Sync"
        Surface(
          color = if (isAuthed) ForestButtonBg else SageCardElevated,
          shape = RoundedCornerShape(20.dp),
          border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isAuthed) LimePillAccent else SageBorder
          ),
          modifier = Modifier
            .heightIn(min = 48.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { onOpenCloudSync() }
            .testTag("cloud_sync_badge")
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = if (isAuthed) Icons.Default.CloudDone else Icons.Default.CloudQueue,
              contentDescription = "Supabase Cloud Sync",
              tint = if (isAuthed) ForestButtonText else TextDeepForest,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = label,
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = if (isAuthed) ForestButtonText else TextDeepForest
              ),
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }
        }
      }

      // Pricing / Pass badge button
      IconButton(
        onClick = onNavigateToPricing,
        modifier = Modifier
          .minimumInteractiveComponentSize()
          .size(48.dp)
          .clip(CircleShape)
          .background(SageCardElevated)
          .border(1.dp, SageBorder, CircleShape)
          .testTag("pricing_icon_button")
      ) {
        Icon(
          imageVector = Icons.Default.WorkspacePremium,
          contentDescription = "Pricing Comparison vs Stella and Perla",
          tint = TextDeepForest,
          modifier = Modifier.size(22.dp)
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
    color = SageCardSurface,
    tonalElevation = 6.dp,
    shadowElevation = 8.dp,
    border = androidx.compose.foundation.BorderStroke(1.dp, SageBorder)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(72.dp)
        .padding(horizontal = 8.dp),
      horizontalArrangement = Arrangement.SpaceAround,
      verticalAlignment = Alignment.CenterVertically
    ) {
      items.forEach { item ->
        val isSelected = currentDestination == item.destination

        Column(
          modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .minimumInteractiveComponentSize()
            .clickable { onSelect(item.destination) }
            .testTag("nav_item_${item.destination.name.lowercase()}"),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(16.dp))
              .background(if (isSelected) ForestButtonBg else Color.Transparent)
              .padding(horizontal = 16.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
              contentDescription = item.label,
              tint = if (isSelected) ForestButtonText else TextMutedSage,
              modifier = Modifier.size(22.dp)
            )
          }
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = item.label,
            style = MaterialTheme.typography.labelSmall.copy(
              fontSize = 11.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              color = if (isSelected) TextDeepForest else TextMutedSage
            )
          )
        }
      }
    }
  }
}
