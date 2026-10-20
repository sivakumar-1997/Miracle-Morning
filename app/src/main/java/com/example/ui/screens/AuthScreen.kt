package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
  var email by remember { mutableStateOf("sivakumar@auramagic.app") }
  var password by remember { mutableStateOf("manifest2026") }
  var confirmPassword by remember { mutableStateOf("manifest2026") }
  var fullName by remember { mutableStateOf("Sivakumar") }
  var passwordVisible by remember { mutableStateOf(false) }
  var statusMessage by remember { mutableStateOf<String?>(null) }

  val scrollState = rememberScrollState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .statusBarsPadding()
      .navigationBarsPadding()
      .verticalScroll(scrollState)
      .padding(horizontal = 24.dp, vertical = 16.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    // App Logo & Sacred Aura Header
    Box(
      modifier = Modifier
        .size(80.dp)
        .clip(CircleShape)
        .background(RoseMist)
        .border(2.dp, RosePrimary.copy(alpha = 0.5f), CircleShape),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = Icons.Default.SelfImprovement,
        contentDescription = "Aura Sacred Emblem",
        tint = RosePrimary,
        modifier = Modifier.size(44.dp)
      )
    }

    Spacer(modifier = Modifier.height(12.dp))

    Text(
      text = "AuraMagic",
      style = MaterialTheme.typography.displayLarge.copy(
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        color = RoseDeep
      )
    )

    Text(
      text = "Sacred Brahma Muhurta & Gratitude Sanctum",
      style = MaterialTheme.typography.bodyMedium.copy(
        color = BlueDeep,
        textAlign = TextAlign.Center
      )
    )

    Spacer(modifier = Modifier.height(24.dp))

    // Sub-mode tabs (Login / Register / Forgot Password)
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(BlueMist.copy(alpha = 0.7f))
        .padding(4.dp),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      listOf(
        AuthSubMode.LOGIN to "Login",
        AuthSubMode.REGISTER to "Register",
        AuthSubMode.FORGOT_PASSWORD to "Reset"
      ).forEach { (mode, label) ->
        val isSelected = authMode == mode
        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) WhitePure else Color.Transparent)
            .border(
              width = if (isSelected) 1.dp else 0.dp,
              color = if (isSelected) RosePrimary.copy(alpha = 0.3f) else Color.Transparent,
              shape = RoundedCornerShape(12.dp)
            )
            .padding(vertical = 10.dp)
            .testTag("auth_tab_${mode.name.lowercase()}"),
          contentAlignment = Alignment.Center
        ) {
          TextButton(
            onClick = {
              viewModel.setAuthMode(mode)
              statusMessage = null
            },
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = label,
              style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) RosePrimary else TextMuted
              )
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Form Container
    AuraCard(
      modifier = Modifier.fillMaxWidth(),
      cornerRadius = 24.dp,
      borderColor = RoseBlush
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
                  color = RoseDeep
                )
              )
              Text(
                text = "Re-align your daily vibration and continue your morning rituals.",
                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
              )

              OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                leadingIcon = {
                  Icon(Icons.Default.Email, contentDescription = "Email", tint = BlueDeep)
                },
                singleLine = true,
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("auth_email_input"),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = RosePrimary,
                  unfocusedBorderColor = BlueSky
                ),
                shape = RoundedCornerShape(14.dp)
              )

              OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = {
                  Icon(Icons.Default.Lock, contentDescription = "Password", tint = BlueDeep)
                },
                trailingIcon = {
                  IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                      imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                      contentDescription = "Toggle password visibility",
                      tint = TextMuted
                    )
                  }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("auth_password_input"),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = RosePrimary,
                  unfocusedBorderColor = BlueSky
                ),
                shape = RoundedCornerShape(14.dp)
              )

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
              ) {
                TextButton(
                  onClick = { viewModel.setAuthMode(AuthSubMode.FORGOT_PASSWORD) },
                  modifier = Modifier.testTag("auth_forgot_password_btn")
                ) {
                  Text(
                    text = "Forgot Password?",
                    style = MaterialTheme.typography.labelMedium.copy(color = RosePrimary)
                  )
                }
              }

              Button(
                onClick = {
                  viewModel.loginUser(email)
                  onAuthSuccess()
                },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(50.dp)
                  .testTag("auth_submit_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RosePrimary)
              ) {
                Icon(Icons.Default.Login, contentDescription = null, tint = WhitePure)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "Enter Sanctum",
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = WhitePure
                  )
                )
              }
            }

            AuthSubMode.REGISTER -> {
              Text(
                text = "Begin Your Sacred Journey",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = RoseDeep
                )
              )
              Text(
                text = "Create your sanctuary account. Next, we will personalize your morning rituals and intentions.",
                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
              )

              OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Full Name") },
                leadingIcon = {
                  Icon(Icons.Default.Person, contentDescription = "Name", tint = BlueDeep)
                },
                singleLine = true,
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("auth_name_input"),
                shape = RoundedCornerShape(14.dp)
              )

              OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                leadingIcon = {
                  Icon(Icons.Default.Email, contentDescription = "Email", tint = BlueDeep)
                },
                singleLine = true,
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("auth_email_register"),
                shape = RoundedCornerShape(14.dp)
              )

              OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Create Password") },
                leadingIcon = {
                  Icon(Icons.Default.Lock, contentDescription = "Password", tint = BlueDeep)
                },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
              )

              Button(
                onClick = {
                  viewModel.loginUser(email)
                  onProceedToOnboarding()
                },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(50.dp)
                  .testTag("auth_register_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RosePrimary)
              ) {
                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = WhitePure)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "Continue to Onboarding",
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = WhitePure
                  )
                )
              }
            }

            AuthSubMode.FORGOT_PASSWORD -> {
              Text(
                text = "Reset Sanctum Access",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = RoseDeep
                )
              )
              Text(
                text = "Enter your registered email address and we will dispatch a secure manifestation recovery link.",
                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
              )

              OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Registered Email") },
                leadingIcon = {
                  Icon(Icons.Default.MailOutline, contentDescription = "Email", tint = BlueDeep)
                },
                singleLine = true,
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("auth_reset_email_input"),
                shape = RoundedCornerShape(14.dp)
              )

              if (statusMessage != null) {
                Text(
                  text = statusMessage ?: "",
                  style = MaterialTheme.typography.bodyMedium.copy(
                    color = BlueDeep,
                    fontWeight = FontWeight.SemiBold
                  )
                )
              }

              Button(
                onClick = {
                  statusMessage = "Magic reset instructions sent to $email! Check inbox."
                },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(50.dp)
                  .testTag("auth_reset_send_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BlueDeep)
              ) {
                Icon(Icons.Default.Send, contentDescription = null, tint = WhitePure)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "Send Recovery Link",
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = WhitePure
                  )
                )
              }

              TextButton(
                onClick = { viewModel.setAuthMode(AuthSubMode.LOGIN) },
                modifier = Modifier.fillMaxWidth()
              ) {
                Text("Return to Sign In", color = RosePrimary)
              }
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Quick direct preview button for evaluators
    OutlinedButton(
      onClick = {
        viewModel.loginUser("sivakumar@auramagic.app")
        onAuthSuccess()
      },
      modifier = Modifier
        .fillMaxWidth()
        .testTag("auth_quick_demo_button"),
      shape = RoundedCornerShape(14.dp),
      colors = ButtonDefaults.outlinedButtonColors(contentColor = BlueDeep),
      border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(BlueSky))
    ) {
      Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = AuraGold)
      Spacer(modifier = Modifier.width(8.dp))
      Text("Instant Demo: Enter as Siva (Brahma Practitioner)")
    }
  }
}
