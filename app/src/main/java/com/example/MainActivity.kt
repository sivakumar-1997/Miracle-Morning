package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AuraBackground
import com.example.ui.components.BottomAuraBar
import com.example.ui.components.SupabaseSyncModal
import com.example.ui.components.TopAuraBar
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppDestination
import com.example.ui.viewmodel.ManifestationViewModel
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        val vm: ManifestationViewModel = viewModel()
        val currentDestination by vm.currentDestination.collectAsState()
        val user by vm.userProfile.collectAsState()
        val cloudState by vm.cloudState.collectAsState()
        val toastMessage by vm.toastMessage.collectAsState()
        val legalInitialTab by vm.legalInitialTab.collectAsState()
        var showCloudSyncModal by remember { mutableStateOf(false) }

        LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
          vm.refreshTimeZoneAndDate()
        }

        // Handle physical/system back press
        BackHandler(enabled = currentDestination != AppDestination.HOME) {
          if (!vm.navigateBack()) {
            vm.navigateTo(AppDestination.HOME)
          }
        }

        // Auto-dismiss toast
        LaunchedEffect(toastMessage) {
          if (toastMessage != null) {
            delay(3500)
            vm.clearToast()
          }
        }

        AuraBackground {
          Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            topBar = {
              if (currentDestination != AppDestination.AUTH && currentDestination != AppDestination.ONBOARDING) {
                TopAuraBar(
                  title = currentDestination.label,
                  currentDestination = currentDestination,
                  onNavigateToPricing = { vm.navigateTo(AppDestination.PRICING) },
                  onNavigateBack = if (currentDestination != AppDestination.HOME) {
                    { vm.navigateBack() }
                  } else null,
                  cloudState = cloudState,
                  onOpenCloudSync = { showCloudSyncModal = true }
                )
              }
            },
            bottomBar = {
              if (currentDestination != AppDestination.AUTH && currentDestination != AppDestination.ONBOARDING) {
                BottomAuraBar(
                  currentDestination = currentDestination,
                  onSelect = { dest -> vm.navigateTo(dest) }
                )
              }
            }
          ) { innerPadding ->
            Box(
              modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
            ) {
              AnimatedContent(
                targetState = currentDestination,
                label = "screen_transition"
              ) { dest ->
                when (dest) {
                  AppDestination.AUTH -> {
                    AuthScreen(
                      viewModel = vm,
                      onAuthSuccess = { vm.navigateTo(AppDestination.HOME) },
                      onProceedToOnboarding = { vm.navigateTo(AppDestination.ONBOARDING) }
                    )
                  }
                  AppDestination.ONBOARDING -> {
                    OnboardingScreen(
                      viewModel = vm,
                      onComplete = { vm.navigateTo(AppDestination.HOME) }
                    )
                  }
                  AppDestination.HOME -> {
                    HomeScreen(
                      viewModel = vm,
                      onNavigate = { target -> vm.navigateTo(target) }
                    )
                  }
                  AppDestination.MORNING_ROUTINE -> {
                    MorningRoutineScreen(
                      viewModel = vm,
                      onNavigateToHome = { vm.navigateTo(AppDestination.HOME) }
                    )
                  }
                  AppDestination.NIGHT_ROUTINE -> {
                    NightRoutineScreen(
                      viewModel = vm
                    )
                  }
                  AppDestination.STREAKS -> {
                    StreaksScreen(
                      viewModel = vm
                    )
                  }
                  AppDestination.BOOKS -> {
                    BooksScreen(
                      viewModel = vm
                    )
                  }
                  AppDestination.PRICING -> {
                    PricingScreen(
                      viewModel = vm
                    )
                  }
                  AppDestination.LEGAL -> {
                    LegalScreen(
                      initialTab = legalInitialTab
                    )
                  }
                }
              }

              // In-App Toast Banner
              if (toastMessage != null) {
                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter)
                ) {
                  Surface(
                    color = ForestButtonBg,
                    shape = RoundedCornerShape(14.dp),
                    shadowElevation = 6.dp,
                    border = androidx.compose.foundation.BorderStroke(1.dp, LimePillAccent)
                  ) {
                    Text(
                      text = toastMessage ?: "",
                      color = ForestButtonText,
                      style = MaterialTheme.typography.bodyMedium,
                      modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                  }
                }
              }
              if (showCloudSyncModal) {
                SupabaseSyncModal(
                  viewModel = vm,
                  onDismiss = { showCloudSyncModal = false }
                )
              }
            }
          }
        }
      }
    }
  }
}
