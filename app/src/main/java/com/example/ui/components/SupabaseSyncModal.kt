package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.screens.LegalTab
import com.example.ui.screens.LegalViewerDialog
import com.example.ui.theme.*
import com.example.ui.viewmodel.ManifestationViewModel

@Composable
fun SupabaseSyncModal(
  viewModel: ManifestationViewModel,
  onDismiss: () -> Unit
) {
  val cloudState by viewModel.cloudState.collectAsState()
  val userProfile by viewModel.userProfile.collectAsState()
  val completedDates by viewModel.completedDates.collectAsState()

  var authMode by remember { mutableStateOf("login") } // "login" | "signup"

  var email by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var passwordVisible by remember { mutableStateOf(false) }
  var authError by remember { mutableStateOf<String?>(null) }
  var isSubmitting by remember { mutableStateOf(false) }
  var activeLegalTab by remember { mutableStateOf<LegalTab?>(null) }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.92f)
        .widthIn(max = 460.dp)
        .clip(RoundedCornerShape(24.dp))
        .testTag("supabase_sync_modal"),
      color = SageMistLight,
      border = androidx.compose.foundation.BorderStroke(1.5.dp, SageBorder)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState())
          .padding(20.dp)
      ) {
        // Header Row (User-friendly title)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(
              imageVector = if (cloudState.isAuthenticated) Icons.Default.VerifiedUser else Icons.Default.AccountCircle,
              contentDescription = null,
              tint = ForestButtonBg,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Sacred Account & Sync",
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
              .testTag("close_sync_modal_btn")
          ) {
            Icon(Icons.Default.Close, contentDescription = "Close Account Modal", tint = TextDeepForest, modifier = Modifier.size(18.dp))
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (cloudState.isAuthenticated && cloudState.user != null) {
          Surface(
            color = SageCardElevated,
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, SageBorder),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Text(
                text = "Signed in as",
                style = MaterialTheme.typography.labelSmall.copy(color = TextMediumSage)
              )
              Text(
                text = cloudState.user?.email ?: "",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = TextDeepForest
                )
              )
              Spacer(modifier = Modifier.height(10.dp))
              Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                  text = "🔥 Streak: ${userProfile.streakDays} Days",
                  style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = TextDeepForest)
                )
                Text(
                  text = "📅 Completed: ${completedDates.size} Days",
                  style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = TextDeepForest)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            SoftBlueButton(
              onClick = {
                isSubmitting = true
                viewModel.syncNowWithCloud { isSubmitting = false }
              },
              enabled = !isSubmitting && !cloudState.isSyncing,
              modifier = Modifier
                .weight(1f)
                .testTag("sb_sync_now_btn")
            ) {
              Icon(Icons.Default.Sync, contentDescription = null, tint = ForestButtonText, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = if (isSubmitting || cloudState.isSyncing) "Syncing..." else "Sync Now",
                fontWeight = FontWeight.Bold,
                color = ForestButtonText
              )
            }

            OutlinedButton(
              onClick = { viewModel.signOutSupabase() },
              modifier = Modifier
                .height(52.dp)
                .minimumInteractiveComponentSize()
                .testTag("sb_signout_btn"),
              shape = RoundedCornerShape(26.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFCDD2))
            ) {
              Text("Sign Out", color = Color(0xFFD32F2F), fontWeight = FontWeight.SemiBold)
            }
          }
        } else if (!cloudState.isConfigured) {
          Surface(
            color = SageCardElevated,
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, SageBorder),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Text(
                text = "Local Sanctuary Active",
                style = MaterialTheme.typography.titleSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = TextDeepForest
                )
              )
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = "Your streaks, 10 daily blessings, 8-sip water alchemy, and night reflections are saved safely on this device.",
                style = MaterialTheme.typography.bodySmall.copy(color = TextMediumSage, lineHeight = 18.sp)
              )
            }
          }
        } else {
          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
              selected = authMode == "login",
              onClick = { authMode = "login"; authError = null },
              label = { Text("Sign In") },
              modifier = Modifier.minimumInteractiveComponentSize()
            )
            FilterChip(
              selected = authMode == "signup",
              onClick = { authMode = "signup"; authError = null },
              label = { Text("Create Account") },
              modifier = Modifier.minimumInteractiveComponentSize()
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedTextField(
            value = email,
            onValueChange = { email = it; authError = null },
            label = { Text("Email Address") },
            placeholder = { Text("you@example.com") },
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("sb_auth_email_input"),
            shape = RoundedCornerShape(12.dp)
          )

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedTextField(
            value = password,
            onValueChange = { password = it; authError = null },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
              IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                  imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                  contentDescription = "Toggle Password"
                )
              }
            },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("sb_auth_password_input"),
            shape = RoundedCornerShape(12.dp)
          )

          if (authError != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = authError ?: "",
              style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFFD32F2F))
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          SoftBlueButton(
            onClick = {
              val e = email.trim()
              val p = password
              if (e.isBlank() || p.isBlank()) {
                authError = "Please enter both email and password."
                return@SoftBlueButton
              }
              isSubmitting = true
              authError = null
              if (authMode == "signup") {
                viewModel.signUpSupabase(
                  email = e,
                  password = p,
                  onSuccess = { isSubmitting = false },
                  onError = { msg -> isSubmitting = false; authError = msg }
                )
              } else {
                viewModel.signInSupabase(
                  email = e,
                  password = p,
                  onSuccess = { isSubmitting = false },
                  onError = { msg -> isSubmitting = false; authError = msg }
                )
              }
            },
            enabled = !isSubmitting,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("sb_auth_submit_btn")
          ) {
            Text(
              text = when {
                isSubmitting -> "Connecting..."
                authMode == "signup" -> "Create Free Account"
                else -> "Sign In & Sync"
              },
              fontWeight = FontWeight.Bold,
              color = ForestButtonText
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically
        ) {
          TextButton(
            onClick = { activeLegalTab = LegalTab.PRIVACY },
            modifier = Modifier
              .minimumInteractiveComponentSize()
              .testTag("sync_modal_privacy_btn")
          ) {
            Text(
              text = "Privacy Policy",
              style = MaterialTheme.typography.labelSmall.copy(
                color = TextMediumSage,
                fontWeight = FontWeight.SemiBold
              )
            )
          }
          Text("•", style = MaterialTheme.typography.labelSmall.copy(color = TextMutedSage))
          TextButton(
            onClick = { activeLegalTab = LegalTab.TERMS },
            modifier = Modifier
              .minimumInteractiveComponentSize()
              .testTag("sync_modal_terms_btn")
          ) {
            Text(
              text = "Terms & Conditions",
              style = MaterialTheme.typography.labelSmall.copy(
                color = TextMediumSage,
                fontWeight = FontWeight.SemiBold
              )
            )
          }
        }
      }
    }
  }

  activeLegalTab?.let { tab ->
    LegalViewerDialog(
      initialTab = tab,
      onDismiss = { activeLegalTab = null }
    )
  }
}
