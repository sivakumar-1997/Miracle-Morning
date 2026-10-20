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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AuraBackground
import com.example.ui.components.BottomAuraBar
import com.example.ui.components.TopAuraBar
import com.example.ui.screens.*
import com.example.ui.theme.BlueDeep
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.RosePrimary
import com.example.ui.theme.WhitePure
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
        val isWireframeOverlay by vm.isWireframeOverlay.collectAsState()
        val toastMessage by vm.toastMessage.collectAsState()

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
                  isWireframeOverlay = isWireframeOverlay,
                  onToggleWireframe = { vm.toggleWireframeOverlay() },
                  onNavigateToWireframeScreen = { vm.navigateTo(AppDestination.WIREFRAME_VIEWER) },
                  onNavigateToPricing = { vm.navigateTo(AppDestination.PRICING) },
                  onNavigateBack = if (currentDestination != AppDestination.HOME) {
                    { vm.navigateBack() }
                  } else null
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
                      viewModel = vm
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
                  AppDestination.WIREFRAME_VIEWER -> {
                    WireframeViewerScreen(
                      viewModel = vm,
                      onNavigateToScreen = { target -> vm.navigateTo(target) }
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
                    color = BlueDeep,
                    shape = RoundedCornerShape(14.dp),
                    shadowElevation = 6.dp
                  ) {
                    Text(
                      text = toastMessage ?: "",
                      color = WhitePure,
                      style = MaterialTheme.typography.bodyMedium,
                      modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
