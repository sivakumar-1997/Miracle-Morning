package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AuraCard
import com.example.ui.components.SoftBlueButton
import com.example.ui.theme.*
import com.example.ui.viewmodel.ManifestationViewModel

@Composable
fun PricingScreen(
  viewModel: ManifestationViewModel
) {
  val competitorPlans = viewModel.competitorPlans
  var selectedTier by remember { mutableStateOf("annual") }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 18.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    contentPadding = PaddingValues(top = 10.dp, bottom = 28.dp)
  ) {
    // Header
    item {
      AuraCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = SoftBluePrimary
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.fillMaxWidth()
        ) {
          Surface(
            color = SageCardElevated,
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, SageBorder)
          ) {
            Text(
              text = "60% More Affordable than Competitors",
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
              style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextDeepForest
              )
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = "Competitive Pricing Sanctum",
            style = MaterialTheme.typography.headlineMedium.copy(
              fontWeight = FontWeight.Bold,
              color = TextDeepForest,
              textAlign = TextAlign.Center
            )
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = "Stella & Perla charge premium rates for basic affirmations. Miracle Mornings delivers the complete morning-to-night manifestation system at an accessible rate.",
            style = MaterialTheme.typography.bodySmall.copy(
              color = TextMediumSage,
              textAlign = TextAlign.Center
            )
          )
        }
      }
    }

    // Direct Price Comparison Cards (Stella vs Perla vs Miracle Mornings)
    item {
      Text(
        text = "Market Price Comparison",
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold,
          color = TextDeepForest
        )
      )

      Spacer(modifier = Modifier.height(10.dp))

      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        competitorPlans.forEach { plan ->
          val isOurApp = plan.appName == "Miracle Mornings"
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(18.dp))
              .border(
                width = if (isOurApp) 2.dp else 1.dp,
                color = if (isOurApp) ForestButtonBg else SageBorder,
                shape = RoundedCornerShape(18.dp)
              ),
            colors = CardDefaults.cardColors(
              containerColor = if (isOurApp) SageCardElevated else SageCardSurface
            )
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier.weight(1f)
                ) {
                  Text(
                    text = plan.appName,
                    style = MaterialTheme.typography.titleLarge.copy(
                      fontWeight = FontWeight.Bold,
                      color = TextDeepForest
                    )
                  )
                  if (isOurApp) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                      color = ForestButtonBg,
                      shape = RoundedCornerShape(8.dp)
                    ) {
                      Text(
                        text = "OUR APP ★",
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                          color = ForestButtonText,
                          fontWeight = FontWeight.Bold,
                          fontSize = 10.sp
                        )
                      )
                    }
                  }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                  color = if (isOurApp) SageCardSurface else SageCardElevated,
                  shape = RoundedCornerShape(8.dp),
                  border = androidx.compose.foundation.BorderStroke(0.5.dp, SageBorder)
                ) {
                  Text(
                    text = plan.marketStatus,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                      color = TextDeepForest,
                      fontWeight = FontWeight.Bold,
                      fontSize = 11.sp
                    )
                  )
                }
              }

              Spacer(modifier = Modifier.height(8.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Column {
                  Text(
                    text = plan.monthlyPrice,
                    style = MaterialTheme.typography.titleMedium.copy(
                      fontWeight = FontWeight.Bold,
                      color = ForestButtonBg
                    )
                  )
                  Text("Monthly Plan", style = MaterialTheme.typography.labelSmall.copy(color = TextMediumSage))
                }

                Column(horizontalAlignment = Alignment.End) {
                  Text(
                    text = plan.yearlyPrice,
                    style = MaterialTheme.typography.titleMedium.copy(
                      fontWeight = FontWeight.Bold,
                      color = TextDeepForest
                    )
                  )
                  Text("Annual Plan", style = MaterialTheme.typography.labelSmall.copy(color = TextMediumSage))
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              Text(
                text = "Key Inclusions:",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = TextDeepForest
                )
              )

              plan.pros.take(4).forEach { pro ->
                Row(
                  modifier = Modifier.padding(vertical = 2.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = ForestButtonBg,
                    modifier = Modifier.size(14.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = pro,
                    style = MaterialTheme.typography.bodySmall.copy(color = TextMediumSage)
                  )
                }
              }

              if (plan.cons.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                plan.cons.take(2).forEach { con ->
                  Row(
                    modifier = Modifier.padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Icon(
                      imageVector = Icons.Default.Close,
                      contentDescription = null,
                      tint = Color(0xFFEF4444),
                      modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                      text = con,
                      style = MaterialTheme.typography.bodySmall.copy(color = TextMutedSage)
                    )
                  }
                }
              }
            }
          }
        }
      }
    }

    // Miracle Mornings Plan Selector
    item {
      AuraCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = SageBorder
      ) {
        Text(
          text = "Select Your Miracle Mornings Tier",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = TextDeepForest
          )
        )
        Spacer(modifier = Modifier.height(10.dp))

        listOf(
          Triple("monthly", "Monthly Tier", "$3.99 / month • Cancel anytime"),
          Triple("annual", "Annual Sacred Pass", "$24.99 / year ($2.08/mo) • Save 48%"),
          Triple("lifetime", "Early Bird Lifetime", "$39.99 one-time • Forever Access")
        ).forEach { (id, title, priceText) ->
          val isSelected = selectedTier == id
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp)
              .clip(RoundedCornerShape(14.dp))
              .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) ForestButtonBg else SageBorder,
                shape = RoundedCornerShape(14.dp)
              )
              .clickable { selectedTier = id },
            colors = CardDefaults.cardColors(
              containerColor = if (isSelected) SageCardElevated else SageCardSurface
            )
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              RadioButton(
                selected = isSelected,
                onClick = { selectedTier = id },
                colors = RadioButtonDefaults.colors(
                  selectedColor = ForestButtonBg,
                  unselectedColor = TextMediumSage
                ),
                modifier = Modifier.minimumInteractiveComponentSize()
              )
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text(
                  text = title,
                  style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextDeepForest
                  )
                )
                Text(
                  text = priceText,
                  style = MaterialTheme.typography.bodySmall.copy(color = TextMediumSage)
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        SoftBlueButton(
          onClick = {
            viewModel.showToast("Miracle Mornings Pro active! Welcome to abundance ✨")
          },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("pricing_subscribe_button")
        ) {
          Text(
            text = "Start 7-Day Free Trial ($3.99/mo after)",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = ForestButtonText
            )
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically
        ) {
          TextButton(
            onClick = { viewModel.openLegal(LegalTab.PRIVACY) },
            modifier = Modifier
              .minimumInteractiveComponentSize()
              .testTag("pricing_privacy_btn")
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
            onClick = { viewModel.openLegal(LegalTab.TERMS) },
            modifier = Modifier
              .minimumInteractiveComponentSize()
              .testTag("pricing_terms_btn")
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
}
