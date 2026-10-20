package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import com.example.data.model.CompetitorPlan
import com.example.ui.components.AuraCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.ManifestationViewModel

@Composable
fun PricingScreen(
  viewModel: ManifestationViewModel
) {
  val competitorPlans = viewModel.competitorPlans
  var selectedTier by remember { mutableStateOf("annual") } // monthly, annual, lifetime

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    contentPadding = PaddingValues(top = 10.dp, bottom = 28.dp)
  ) {
    // Header
    item {
      AuraCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = RosePrimary
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.fillMaxWidth()
        ) {
          Surface(
            color = RoseMist,
            shape = RoundedCornerShape(12.dp)
          ) {
            Text(
              text = "60% More Affordable than Competitors",
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
              style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                color = RoseDeep
              )
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = "Competitive Pricing Sanctum",
            style = MaterialTheme.typography.headlineMedium.copy(
              fontWeight = FontWeight.Bold,
              color = RoseDeep,
              textAlign = TextAlign.Center
            )
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = "Stella & Perla charge premium rates for basic affirmations. AuraMagic provides the entire morning-to-night manifestation system at an accessible rate.",
            style = MaterialTheme.typography.bodySmall.copy(
              color = TextMuted,
              textAlign = TextAlign.Center
            )
          )
        }
      }
    }

    // Direct Price Comparison Cards (Stella vs Perla vs AuraMagic)
    item {
      Text(
        text = "Market Price Comparison",
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold,
          color = TextDark
        )
      )

      Spacer(modifier = Modifier.height(10.dp))

      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        competitorPlans.forEach { plan ->
          val isOurApp = plan.appName == "AuraMagic"
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(16.dp))
              .border(
                width = if (isOurApp) 2.dp else 1.dp,
                color = if (isOurApp) RosePrimary else CardBorder,
                shape = RoundedCornerShape(16.dp)
              ),
            colors = CardDefaults.cardColors(
              containerColor = if (isOurApp) RoseMist.copy(alpha = 0.4f) else WhitePure
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = plan.appName,
                    style = MaterialTheme.typography.titleLarge.copy(
                      fontWeight = FontWeight.Bold,
                      color = if (isOurApp) RoseDeep else TextDark
                    )
                  )
                  if (isOurApp) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                      color = RosePrimary,
                      shape = RoundedCornerShape(8.dp)
                    ) {
                      Text(
                        text = "OUR APP ★",
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                          color = WhitePure,
                          fontWeight = FontWeight.Bold
                        )
                      )
                    }
                  }
                }

                Surface(
                  color = if (isOurApp) BlueMist else Color(0xFFF1F5F9),
                  shape = RoundedCornerShape(8.dp)
                ) {
                  Text(
                    text = plan.marketStatus,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                      color = if (isOurApp) BlueDeep else TextMuted,
                      fontWeight = FontWeight.Bold
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
                      color = if (isOurApp) RosePrimary else TextDark
                    )
                  )
                  Text("Monthly Plan", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                }

                Column(horizontalAlignment = Alignment.End) {
                  Text(
                    text = plan.yearlyPrice,
                    style = MaterialTheme.typography.titleMedium.copy(
                      fontWeight = FontWeight.Bold,
                      color = if (isOurApp) RoseDeep else TextDark
                    )
                  )
                  Text("Annual Plan", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              Text(
                text = "Key Inclusions:",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = BlueDeep
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
                    tint = if (isOurApp) RosePrimary else Color(0xFF10B981),
                    modifier = Modifier.size(14.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = pro,
                    style = MaterialTheme.typography.bodySmall.copy(color = TextDark)
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
                      style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                    )
                  }
                }
              }
            }
          }
        }
      }
    }

    // AuraMagic Plan Selector
    item {
      AuraCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = AuraGold
      ) {
        Text(
          text = "Select Your AuraMagic Tier",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = RoseDeep
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
              .clip(RoundedCornerShape(12.dp))
              .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) RosePrimary else CardBorder,
                shape = RoundedCornerShape(12.dp)
              )
              .clickable { selectedTier = id },
            colors = CardDefaults.cardColors(
              containerColor = if (isSelected) RoseMist else WhitePure
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
                colors = RadioButtonDefaults.colors(selectedColor = RosePrimary)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text(
                  text = title,
                  style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) RoseDeep else TextDark
                  )
                )
                Text(
                  text = priceText,
                  style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Button(
          onClick = {
            viewModel.showToast("AuraMagic Pro active! Welcome to abundance ✨")
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag("pricing_subscribe_button"),
          shape = RoundedCornerShape(16.dp),
          colors = ButtonDefaults.buttonColors(containerColor = RosePrimary)
        ) {
          Text(
            text = "Start 7-Day Free Trial ($3.99/mo after)",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = WhitePure
            )
          )
        }
      }
    }
  }
}
