package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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

  var activeTab by remember(cloudState.isConfigured) {
    mutableStateOf(if (cloudState.isConfigured) "auth" else "settings")
  }
  var authMode by remember { mutableStateOf("login") } // "login" | "signup"

  var projectUrl by remember(cloudState.projectUrl) { mutableStateOf(cloudState.projectUrl) }
  var anonKey by remember(cloudState.anonKey) { mutableStateOf(cloudState.anonKey) }
  var settingsError by remember { mutableStateOf<String?>(null) }

  var email by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var passwordVisible by remember { mutableStateOf(false) }
  var authError by remember { mutableStateOf<String?>(null) }
  var isSubmitting by remember { mutableStateOf(false) }

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
        // Header Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(
              imageVector = if (cloudState.isAuthenticated) Icons.Default.CloudDone else Icons.Default.CloudQueue,
              contentDescription = null,
              tint = ForestButtonBg,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Supabase Cloud Sync",
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
            Icon(Icons.Default.Close, contentDescription = "Close Cloud Sync", tint = TextDeepForest, modifier = Modifier.size(18.dp))
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Tab Switcher (Auth / Project Settings)
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(SageCardElevated)
            .border(1.dp, SageBorder, RoundedCornerShape(14.dp))
            .padding(4.dp),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          val authLabel = if (cloudState.isAuthenticated) "Account" else "Sign In / Up"
          listOf("auth" to authLabel, "settings" to "Project Settings").forEach { (tabId, label) ->
            val selected = activeTab == tabId
            Box(
              modifier = Modifier
                .weight(1f)
                .heightIn(min = 44.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(if (selected) ForestButtonBg else Color.Transparent)
                .clickable { activeTab = tabId }
                .testTag("sync_modal_tab_$tabId"),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = label,
                style = MaterialTheme.typography.labelLarge.copy(
                  fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                  color = if (selected) ForestButtonText else TextMediumSage
                )
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (activeTab == "settings") {
          Text(
            text = "Connect the same Supabase project you use on the Web app for seamless cross-device cloud sync.",
            style = MaterialTheme.typography.bodySmall.copy(color = TextMediumSage, lineHeight = 18.sp)
          )

          Spacer(modifier = Modifier.height(12.dp))

          OutlinedTextField(
            value = projectUrl,
            onValueChange = { projectUrl = it; settingsError = null },
            label = { Text("Project URL") },
            placeholder = { Text("https://xyzcompany.supabase.co") },
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("sb_url_input"),
            shape = RoundedCornerShape(12.dp)
          )

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedTextField(
            value = anonKey,
            onValueChange = { anonKey = it; settingsError = null },
            label = { Text("Anon Public Key") },
            placeholder = { Text("eyJhbGciOiJIUzI1NiIsInR5c...") },
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("sb_key_input"),
            shape = RoundedCornerShape(12.dp)
          )

          if (settingsError != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = settingsError ?: "",
              style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFFD32F2F))
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            SoftBlueButton(
              onClick = {
                val u = projectUrl.trim()
                val k = anonKey.trim()
                if (!u.startsWith("http") || k.isBlank()) {
                  settingsError = "Please enter a valid Supabase URL and Anon Public Key."
                } else {
                  viewModel.configureSupabase(u, k)
                  activeTab = "auth"
                }
              },
              modifier = Modifier
                .weight(1f)
                .testTag("save_sb_settings_btn")
            ) {
              Text("Save & Connect", fontWeight = FontWeight.Bold, color = ForestButtonText)
            }

            if (cloudState.isConfigured) {
              OutlinedButton(
                onClick = {
                  viewModel.disconnectSupabase()
                  projectUrl = ""
                  anonKey = ""
                },
                modifier = Modifier
                  .height(52.dp)
                  .minimumInteractiveComponentSize(),
                shape = RoundedCornerShape(26.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFCDD2))
              ) {
                Text("Disconnect", color = Color(0xFFD32F2F), fontWeight = FontWeight.SemiBold)
              }
            }
          }

          Spacer(modifier = Modifier.height(12.dp))
          Surface(
            color = SageCardElevated,
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, SageBorder)
          ) {
            Text(
              text = "Use the same supabase_schema.sql database as your Vercel web app to sync streaks, 10 gratitudes, 8-sip water alchemy, and night reflections.",
              modifier = Modifier.padding(10.dp),
              style = MaterialTheme.typography.labelSmall.copy(color = TextMediumSage, lineHeight = 16.sp)
            )
          }
        } else {
          // AUTH / ACCOUNT TAB
          if (cloudState.isAuthenticated && cloudState.user != null) {
            Surface(
              color = SageCardElevated,
              shape = RoundedCornerShape(14.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, SageBorder),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(14.dp)) {
                Text(
                  text = "Logged in as",
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
          } else {
            if (!cloudState.isConfigured) {
              Text(
                text = "Enter your Supabase Project URL and Anon Key in Project Settings first to enable sign-in.",
                style = MaterialTheme.typography.bodySmall.copy(color = TextMediumSage)
              )
              Spacer(modifier = Modifier.height(12.dp))
              SoftBlueButton(
                onClick = { activeTab = "settings" },
                modifier = Modifier.fillMaxWidth()
              ) {
                Text("Open Project Settings", fontWeight = FontWeight.Bold, color = ForestButtonText)
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
          }
        }
      }
    }
  }
}
