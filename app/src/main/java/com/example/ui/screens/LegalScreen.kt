package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.LegalDocuments
import com.example.ui.components.AuraCard
import com.example.ui.theme.*

enum class LegalTab(val label: String) {
  PRIVACY("Privacy Policy"),
  TERMS("Terms & Conditions")
}

@Composable
fun LegalScreen(
  initialTab: LegalTab = LegalTab.PRIVACY
) {
  var selectedTab by remember(initialTab) { mutableStateOf(initialTab) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 18.dp)
  ) {
    LegalContentBody(
      selectedTab = selectedTab,
      onSelectTab = { selectedTab = it },
      modifier = Modifier.fillMaxSize()
    )
  }
}

@Composable
fun LegalViewerDialog(
  initialTab: LegalTab = LegalTab.PRIVACY,
  onDismiss: () -> Unit
) {
  var selectedTab by remember(initialTab) { mutableStateOf(initialTab) }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.94f)
        .fillMaxHeight(0.86f)
        .widthIn(max = 560.dp)
        .clip(RoundedCornerShape(24.dp))
        .testTag("legal_viewer_dialog"),
      color = SageMistLight,
      border = androidx.compose.foundation.BorderStroke(1.5.dp, SageBorder)
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(18.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(
              imageVector = if (selectedTab == LegalTab.PRIVACY) Icons.Default.PrivacyTip else Icons.Default.Gavel,
              contentDescription = null,
              tint = ForestButtonBg,
              modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = selectedTab.label,
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                color = TextDeepForest
              )
            )
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier
              .minimumInteractiveComponentSize()
              .size(36.dp)
              .clip(CircleShape)
              .background(SageCardElevated)
              .testTag("close_legal_dialog_btn")
          ) {
            Icon(Icons.Default.Close, contentDescription = "Close Legal View", tint = TextDeepForest, modifier = Modifier.size(18.dp))
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        LegalContentBody(
          selectedTab = selectedTab,
          onSelectTab = { selectedTab = it },
          modifier = Modifier.weight(1f)
        )
      }
    }
  }
}

@Composable
private fun LegalContentBody(
  selectedTab: LegalTab,
  onSelectTab: (LegalTab) -> Unit,
  modifier: Modifier = Modifier
) {
  val sections = if (selectedTab == LegalTab.PRIVACY) {
    LegalDocuments.privacySections
  } else {
    LegalDocuments.termsSections
  }

  Column(modifier = modifier) {
    // Segmented Tab Row
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(14.dp))
        .background(SageCardElevated)
        .border(1.dp, SageBorder, RoundedCornerShape(14.dp))
        .padding(4.dp),
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      LegalTab.values().forEach { tab ->
        val isSelected = selectedTab == tab
        Box(
          modifier = Modifier
            .weight(1f)
            .heightIn(min = 48.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) ForestButtonBg else Color.Transparent)
            .clickable { onSelectTab(tab) }
            .testTag("legal_tab_${tab.name.lowercase()}"),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = tab.label,
            style = MaterialTheme.typography.labelLarge.copy(
              fontWeight = FontWeight.Bold,
              color = if (isSelected) ForestButtonText else TextMediumSage
            )
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.spacedBy(10.dp),
      contentPadding = PaddingValues(bottom = 24.dp)
    ) {
      item {
        AuraCard(
          modifier = Modifier.fillMaxWidth(),
          borderColor = LimePillAccent
        ) {
          Text(
            text = if (selectedTab == LegalTab.PRIVACY) {
              "Miracle Mornings Privacy Policy"
            } else {
              "Miracle Mornings Terms & Conditions"
            },
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = TextDeepForest
            )
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Effective Date: ${LegalDocuments.EFFECTIVE_DATE} • Contact: ${LegalDocuments.CONTACT_EMAIL}",
            style = MaterialTheme.typography.labelSmall.copy(color = TextMediumSage)
          )
        }
      }

      items(sections) { section ->
        AuraCard(
          modifier = Modifier.fillMaxWidth(),
          borderColor = SageBorder
        ) {
          Text(
            text = section.heading,
            style = MaterialTheme.typography.titleSmall.copy(
              fontWeight = FontWeight.Bold,
              color = TextDeepForest
            )
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = section.body,
            style = MaterialTheme.typography.bodySmall.copy(
              color = TextMediumSage,
              lineHeight = 20.sp
            )
          )
        }
      }
    }
  }
}
