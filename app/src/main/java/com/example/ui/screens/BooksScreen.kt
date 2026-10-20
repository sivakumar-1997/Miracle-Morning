package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ManifestationBook
import com.example.ui.components.AuraCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.ManifestationViewModel

@Composable
fun BooksScreen(
  viewModel: ManifestationViewModel
) {
  val books = viewModel.books
  var selectedCategory by remember { mutableStateOf("All") }
  val selectedBook by viewModel.selectedBook.collectAsState()

  val categories = listOf("All", "Rhonda Byrne", "Neuroscience & Brain", "Water Alchemy", "Sleep & Subconscious")

  val filteredBooks = remember(selectedCategory) {
    if (selectedCategory == "All") books
    else {
      books.filter { book ->
        when (selectedCategory) {
          "Rhonda Byrne" -> book.author.contains("Rhonda Byrne", ignoreCase = true)
          "Neuroscience & Brain" -> book.category.contains("Neuroscience", ignoreCase = true) || book.brainFrequencyConcept.contains("Wave", ignoreCase = true)
          "Water Alchemy" -> book.title.contains("Water", ignoreCase = true)
          "Sleep & Subconscious" -> book.category.contains("Subconscious", ignoreCase = true) || book.category.contains("Hypnagogic", ignoreCase = true)
          else -> true
        }
      }
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
  ) {
    // Header
    AuraCard(
      modifier = Modifier.fillMaxWidth(),
      borderColor = BlueSky
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(BlueMist),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.AutoStories,
            contentDescription = null,
            tint = BlueDeep,
            modifier = Modifier.size(26.dp)
          )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
          Text(
            text = "Manifestation & Brain Wisdom",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = BlueDeep
            )
          )
          Text(
            text = "Foundational texts on vibration, frequency & subconscious reprogramming",
            style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Filter chips row
    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      items(categories) { category ->
        val isSelected = category == selectedCategory
        FilterChip(
          selected = isSelected,
          onClick = { selectedCategory = category },
          label = { Text(category) },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = RosePrimary,
            selectedLabelColor = WhitePure
          )
        )
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Book Cards List
    LazyColumn(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f),
      verticalArrangement = Arrangement.spacedBy(12.dp),
      contentPadding = PaddingValues(bottom = 24.dp)
    ) {
      items(filteredBooks, key = { it.id }) { book ->
        BookCardItem(
          book = book,
          onClick = { viewModel.selectBook(book) }
        )
      }
    }
  }

  // Book Detail Dialog
  if (selectedBook != null) {
    val book = selectedBook!!
    AlertDialog(
      onDismissRequest = { viewModel.selectBook(null) },
      title = {
        Column {
          Text(
            text = book.title,
            style = MaterialTheme.typography.headlineSmall.copy(
              fontWeight = FontWeight.Bold,
              color = RoseDeep
            )
          )
          Text(
            text = "By ${book.author} • Published in ${book.year}",
            style = MaterialTheme.typography.labelMedium.copy(color = BlueDeep)
          )
        }
      },
      text = {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          item {
            Surface(
              color = RoseMist.copy(alpha = 0.5f),
              shape = RoundedCornerShape(10.dp)
            ) {
              Text(
                text = "“${book.quote}”",
                modifier = Modifier.padding(10.dp),
                style = MaterialTheme.typography.bodySmall.copy(
                  fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                  color = TextDark,
                  lineHeight = 18.sp
                )
              )
            }
          }

          item {
            Text(
              text = "Brain Wave & Frequency Mechanism:",
              style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = BlueDeep
              )
            )
            Text(
              text = book.brainFrequencyConcept,
              style = MaterialTheme.typography.bodySmall.copy(color = TextDark)
            )
          }

          item {
            Text(
              text = "Core Daily Practice for AuraMagic:",
              style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = RosePrimary
              )
            )
            Text(
              text = book.keyPractice,
              style = MaterialTheme.typography.bodySmall.copy(color = TextDark)
            )
          }

          item {
            Text(
              text = "Book Overview:",
              style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = TextDark
              )
            )
            Text(
              text = book.fullSummary,
              style = MaterialTheme.typography.bodySmall.copy(color = TextDark, lineHeight = 20.sp)
            )
          }
        }
      },
      confirmButton = {
        Button(
          onClick = { viewModel.selectBook(null) },
          colors = ButtonDefaults.buttonColors(containerColor = RosePrimary)
        ) {
          Text("Close Guide")
        }
      }
    )
  }
}

@Composable
fun BookCardItem(
  book: ManifestationBook,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
      .clickable(onClick = onClick)
      .testTag("book_item_${book.id}"),
    colors = CardDefaults.cardColors(containerColor = WhitePure),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = book.title,
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = RoseDeep
            )
          )
          Text(
            text = "${book.author} (${book.year})",
            style = MaterialTheme.typography.bodySmall.copy(
              fontWeight = FontWeight.Medium,
              color = BlueDeep
            )
          )
        }

        Surface(
          color = RoseMist,
          shape = RoundedCornerShape(8.dp)
        ) {
          Text(
            text = book.category.take(18),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              color = RoseDeep
            )
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = book.quote,
        style = MaterialTheme.typography.bodySmall.copy(
          fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
          color = TextDark,
          lineHeight = 18.sp
        ),
        maxLines = 2
      )

      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Brain tuning: ${book.brainFrequencyConcept.take(28)}...",
          style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
        )
        Text(
          text = "Read Notes →",
          style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Bold,
            color = RosePrimary
          )
        )
      }
    }
  }
}
