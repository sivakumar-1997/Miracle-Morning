package com.example.ui.screens

import androidx.compose.animation.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AuraCard
import com.example.ui.components.SoftBlueButton
import com.example.ui.components.SupabaseSyncModal
import com.example.ui.theme.*
import com.example.ui.viewmodel.AuthSubMode
import com.example.ui.viewmodel.ManifestationViewModel

@Composable
fun AuthScreen(
  viewModel: ManifestationViewModel,
  onAuthSuccess: () -> Unit,
  onProceedToOnboarding: () -> Unit
) {
  val authMode by viewModel.authMode.collectAsState()
  val cloudState by viewModel.cloudState.collectAsState()
  var email by remember { mutableStateOf("siva@miraclemornings.app") }
  var password by remember { mutableStateOf("miracle2026") }
  var fullName by remember { mutableStateOf("Sivakumar") }
  var passwordVisible by remember { mutableStateOf(false) }
  var statusMessage by remember { mutableStateOf<String?>(null) }
  var authError by remember { mutableStateOf<String?>(null) }
  var isSubmitting by remember { mutableStateOf(false) }
  var showCloudModal by remember { mutableStateOf(false) }

  val scrollState = rememberScrollState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .statusBarsPadding()
      .navigationBarsPadding()
      .verticalScroll(scrollState)
      .padding(horizontal = 24.dp, vertical = 20.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    // Ethereal Emblem
    Box(
      modifier = Modifier
        .size(80.dp)
        .clip(CircleShape)
        .background(CozySurfaceElevated)
        .border(1.5.dp, SoftBluePrimary.copy(alpha = 0.6f), CircleShape),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = Icons.Default.SelfImprovement,
        contentDescription = "Miracle Mornings Emblem",
        tint = SoftBluePrimary,
        modifier = Modifier.size(44.dp)
      )
    }

    Spacer(modifier = Modifier.height(14.dp))

    Text(
      text = "Miracle Mornings",
      style = MaterialTheme.typography.displayLarge.copy(
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        color = TextDeepForest
      )
    )

    Text(
      text = "Your Sacred Morning Manifestation Sanctuary",
      style = MaterialTheme.typography.bodyMedium.copy(
        color = TextMediumSage,
        textAlign = TextAlign.Center
      )
    )

    Spacer(modifier = Modifier.height(24.dp))

    // Sub-mode tabs (Login / Sign Up / Forgot Password)
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(SageCardElevated)
        .border(1.dp, SageBorder, RoundedCornerShape(16.dp))
        .padding(4.dp),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      listOf(
        AuthSubMode.LOGIN to "Login",
        AuthSubMode.REGISTER to "Sign Up",
        AuthSubMode.FORGOT_PASSWORD to "Reset"
      ).forEach { (mode, label) ->
        val isSelected = authMode == mode
        Box(
          modifier = Modifier
            .weight(1f)
            .heightIn(min = 48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) ForestButtonBg else Color.Transparent)
            .clickable {
              viewModel.setAuthMode(mode)
              statusMessage = null
            }
            .testTag("auth_tab_${mode.name.lowercase()}"),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = label,
            style = MaterialTheme.typography.labelLarge.copy(
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              color = if (isSelected) ForestButtonText else TextMediumSage
            )
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Form Container
    AuraCard(
      modifier = Modifier.fillMaxWidth(),
      cornerRadius = 24.dp,
      borderColor = SageBorder
    ) {
      AnimatedContent(
        targetState = authMode,
        label = "auth_form_anim"
      ) { targetMode ->
        Column(
          modifier = Modifier.fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          when (targetMode) {
            AuthSubMode.LOGIN -> {
              Text(
                text = "Welcome Back to Stillness",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = TextDeepForest
                )
              )
              Text(
                text = "Enter your credentials to continue your morning routines.",
                style = MaterialTheme.typography.bodySmall.copy(color = TextMediumSage)
              )

              OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                leadingIcon = {
                  Icon(Icons.Default.Email, contentDescription = "Email", tint = TextMediumSage)
                },
                singleLine = true,
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("auth_email_input"),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = ForestButtonBg,
                  unfocusedBorderColor = SageBorder,
                  focusedTextColor = TextDeepForest,
                  unfocusedTextColor = TextDeepForest,
                  focusedLabelColor = ForestButtonBg,
                  unfocusedLabelColor = TextMediumSage
                ),
                shape = RoundedCornerShape(14.dp)
              )

              OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = {
                  Icon(Icons.Default.Lock, contentDescription = "Password", tint = TextMediumSage)
                },
                trailingIcon = {
                  IconButton(
                    onClick = { passwordVisible = !passwordVisible },
                    modifier = Modifier.minimumInteractiveComponentSize()
                  ) {
                    Icon(
                      imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                      contentDescription = "Toggle password visibility",
                      tint = TextMediumSage
                    )
                  }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("auth_password_input"),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = ForestButtonBg,
                  unfocusedBorderColor = SageBorder,
                  focusedTextColor = TextDeepForest,
                  unfocusedTextColor = TextDeepForest,
                  focusedLabelColor = ForestButtonBg,
                  unfocusedLabelColor = TextMediumSage
                ),
                shape = RoundedCornerShape(14.dp)
              )

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
              ) {
                TextButton(
                  onClick = { viewModel.setAuthMode(AuthSubMode.FORGOT_PASSWORD) },
                  modifier = Modifier
                    .minimumInteractiveComponentSize()
                    .testTag("auth_forgot_password_btn")
                ) {
                  Text(
                    text = "Forgot Password?",
                    style = MaterialTheme.typography.labelMedium.copy(
                      color = TextDeepForest,
                      fontWeight = FontWeight.SemiBold
                    )
                  )
                }
              }

              if (authError != null) {
                Text(
                  text = authError ?: "",
                  style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFFD32F2F))
                )
              }

              SoftBlueButton(
                onClick = {
                  authError = null
                  if (cloudState.isConfigured) {
                    isSubmitting = true
                    viewModel.signInSupabase(
                      email = email.trim(),
                      password = password,
                      onSuccess = {
                        isSubmitting = false
                        onAuthSuccess()
                      },
                      onError = { msg ->
                        isSubmitting = false
                        authError = msg
                      }
                    )
                  } else {
                    viewModel.loginUser(email)
                    onAuthSuccess()
                  }
                },
                enabled = !isSubmitting,
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("auth_submit_button")
              ) {
                Icon(Icons.Default.Login, contentDescription = null, tint = ForestButtonText)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = if (isSubmitting) "Connecting..." else if (cloudState.isConfigured) "Sign In & Cloud Sync" else "Login to Sanctuary",
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = ForestButtonText
                  )
                )
              }
            }

            AuthSubMode.REGISTER -> {
              Text(
                text = "Create Your Account",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = TextDeepForest
                )
              )
              Text(
                text = "Sign up to begin your personalized manifestation routines.",
                style = MaterialTheme.typography.bodySmall.copy(color = TextMediumSage)
              )

              OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Full Name") },
                leadingIcon = {
                  Icon(Icons.Default.Person, contentDescription = "Name", tint = TextMediumSage)
                },
                singleLine = true,
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("auth_name_input"),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = ForestButtonBg,
                  unfocusedBorderColor = SageBorder,
                  focusedTextColor = TextDeepForest,
                  unfocusedTextColor = TextDeepForest,
                  focusedLabelColor = ForestButtonBg,
                  unfocusedLabelColor = TextMediumSage
                ),
                shape = RoundedCornerShape(14.dp)
              )

              OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                leadingIcon = {
                  Icon(Icons.Default.Email, contentDescription = "Email", tint = TextMediumSage)
                },
                singleLine = true,
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("auth_email_register"),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = ForestButtonBg,
                  unfocusedBorderColor = SageBorder,
                  focusedTextColor = TextDeepForest,
                  unfocusedTextColor = TextDeepForest,
                  focusedLabelColor = ForestButtonBg,
                  unfocusedLabelColor = TextMediumSage
                ),
                shape = RoundedCornerShape(14.dp)
              )

              OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = {
                  Icon(Icons.Default.Lock, contentDescription = "Password", tint = TextMediumSage)
                },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = ForestButtonBg,
                  unfocusedBorderColor = SageBorder,
                  focusedTextColor = TextDeepForest,
                  unfocusedTextColor = TextDeepForest,
                  focusedLabelColor = ForestButtonBg,
                  unfocusedLabelColor = TextMediumSage
                ),
                shape = RoundedCornerShape(14.dp)
              )

              if (authError != null) {
                Text(
                  text = authError ?: "",
                  style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFFD32F2F))
                )
              }

              SoftBlueButton(
                onClick = {
                  authError = null
                  if (cloudState.isConfigured) {
                    isSubmitting = true
                    viewModel.registerUser(fullName, email)
                    viewModel.signUpSupabase(
                      email = email.trim(),
                      password = password,
                      onSuccess = {
                        isSubmitting = false
                        onProceedToOnboarding()
                      },
                      onError = { msg ->
                        isSubmitting = false
                        authError = msg
                      }
                    )
                  } else {
                    viewModel.registerUser(fullName, email)
                    onProceedToOnboarding()
                  }
                },
                enabled = !isSubmitting,
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("auth_register_button")
              ) {
                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = ForestButtonText)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = if (isSubmitting) "Creating Account..." else "Create Account & Start (0d Streak)",
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = ForestButtonText
                  )
                )
              }
            }

            AuthSubMode.FORGOT_PASSWORD -> {
              Text(
                text = "Reset Your Password",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = TextDeepForest
                )
              )
              Text(
                text = "Enter your registered email address to receive password reset instructions.",
                style = MaterialTheme.typography.bodySmall.copy(color = TextMediumSage)
              )

              OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Registered Email") },
                leadingIcon = {
                  Icon(Icons.Default.MailOutline, contentDescription = "Email", tint = TextMediumSage)
                },
                singleLine = true,
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("auth_reset_email_input"),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = ForestButtonBg,
                  unfocusedBorderColor = SageBorder,
                  focusedTextColor = TextDeepForest,
                  unfocusedTextColor = TextDeepForest,
                  focusedLabelColor = ForestButtonBg,
                  unfocusedLabelColor = TextMediumSage
                ),
                shape = RoundedCornerShape(14.dp)
              )

              if (statusMessage != null) {
                Text(
                  text = statusMessage ?: "",
                  style = MaterialTheme.typography.bodyMedium.copy(
                    color = ForestButtonBg,
                    fontWeight = FontWeight.SemiBold
                  )
                )
              }

              SoftBlueButton(
                onClick = {
                  if (cloudState.isConfigured) {
                    viewModel.recoverPasswordSupabase(email.trim()) { msg ->
                      statusMessage = msg
                    }
                  } else {
                    statusMessage = "Reset instructions sent to $email! Please check your inbox."
                  }
                },
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("auth_reset_send_button")
              ) {
                Icon(Icons.Default.Send, contentDescription = null, tint = ForestButtonText)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "Send Reset Link",
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = ForestButtonText
                  )
                )
              }

              TextButton(
                onClick = { viewModel.setAuthMode(AuthSubMode.LOGIN) },
                modifier = Modifier
                  .fillMaxWidth()
                  .minimumInteractiveComponentSize()
              ) {
                Text("Return to Login", color = TextDeepForest, fontWeight = FontWeight.SemiBold)
              }
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(18.dp))

    // First-Time User Login (Starts with 0 Streaks)
    Button(
      onClick = {
        viewModel.loginUser("new.practitioner@miraclemornings.app", isFirstTime = true)
        onAuthSuccess()
      },
      modifier = Modifier
        .fillMaxWidth()
        .height(52.dp)
        .minimumInteractiveComponentSize()
        .testTag("auth_first_time_login_btn"),
      shape = RoundedCornerShape(26.dp),
      colors = ButtonDefaults.buttonColors(containerColor = ForestButtonBg)
    ) {
      Icon(Icons.Default.PersonAdd, contentDescription = null, tint = ForestButtonText)
      Spacer(modifier = Modifier.width(8.dp))
      Text("First-Time User Login (Starts with 0 Streaks)", color = ForestButtonText, fontWeight = FontWeight.Bold)
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Existing Practitioner Demo (18d Streak)
    OutlinedButton(
      onClick = {
        viewModel.loginUser("siva@miraclemornings.app", isFirstTime = false)
        onAuthSuccess()
      },
      modifier = Modifier
        .fillMaxWidth()
        .height(52.dp)
        .minimumInteractiveComponentSize()
        .testTag("auth_quick_demo_button"),
      shape = RoundedCornerShape(26.dp),
      border = androidx.compose.foundation.BorderStroke(1.5.dp, ForestButtonBg)
    ) {
      Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = ForestButtonBg)
      Spacer(modifier = Modifier.width(8.dp))
      Text("Existing Practitioner Demo (Siva, 18d Streak)", color = ForestButtonBg, fontWeight = FontWeight.SemiBold)
    }

    Spacer(modifier = Modifier.height(10.dp))

    TextButton(
      onClick = { showCloudModal = true },
      modifier = Modifier
        .minimumInteractiveComponentSize()
        .testTag("auth_configure_supabase_btn")
    ) {
      Icon(
        imageVector = if (cloudState.isConfigured) Icons.Default.CloudDone else Icons.Default.CloudQueue,
        contentDescription = null,
        tint = ForestButtonBg,
        modifier = Modifier.size(18.dp)
      )
      Spacer(modifier = Modifier.width(6.dp))
      Text(
        text = if (cloudState.isConfigured) "Supabase Cloud Sync Connected" else "Configure Supabase Cloud Sync",
        color = TextDeepForest,
        fontWeight = FontWeight.SemiBold
      )
    }
  }

  if (showCloudModal) {
    SupabaseSyncModal(
      viewModel = viewModel,
      onDismiss = { showCloudModal = false }
    )
  }
}
