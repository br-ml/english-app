package com.example.englishbg.ui.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.englishbg.data.ALL_CATEGORIES
import com.example.englishbg.data.CategoryStats
import com.example.englishbg.data.GRADE3_CATEGORIES

private val categoryEmojis = listOf(
    "🌈", "🔢", "📅", "⛅", "🍂",
    "👨‍👩‍👧", "🐾", "🎮", "🏫", "👋",
    "📍", "🔷", "🍎", "🥗", "🌱",
    "⚽", "✨", "😊", "🌟",
    "📝", "👤", "🏷️",
)

private val cardColors = listOf(
    Color(0xFFFFE4E4), // soft red
    Color(0xFFFFEFCC), // soft yellow
    Color(0xFFD6F5E3), // soft green
    Color(0xFFD6EAFF), // soft blue
    Color(0xFFFFE4CC), // soft orange
    Color(0xFFECDAFF), // soft purple
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryPickerScreen(
    grade: Int,
    onCategorySelected: (Int) -> Unit,
    onBack: () -> Unit,
    viewModel: CategoryPickerViewModel = viewModel(),
) {
    val allStats by viewModel.allStats.collectAsState()
    val categories = if (grade == 3) GRADE3_CATEGORIES else ALL_CATEGORIES

    Scaffold(
        containerColor = Color(0xFFFFF8F0),
        topBar = {
            TopAppBar(
                title = { Text("Choose a Category", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize().padding(padding),
        ) {
            itemsIndexed(categories) { index, category ->
                val bgColor = cardColors[index % cardColors.size]
                val emoji = categoryEmojis.getOrElse(index) { "📖" }
                val stats = if (grade == 2) allStats[index] ?: CategoryStats() else CategoryStats()
                Card(
                    onClick = { onCategorySelected(index) },
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.fillMaxWidth().heightIn(min = 96.dp),
                    colors = CardDefaults.cardColors(containerColor = bgColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(emoji, fontSize = 28.sp)
                        Text(
                            text = category.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1A2E),
                        )
                        Text(
                            text = "${category.words.size} words",
                            fontSize = 12.sp,
                            color = Color(0xFF555577),
                        )
                        if (stats.timesPlayed > 0) {
                            Text(
                                text = starsText(stats.stars),
                                fontSize = 14.sp,
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun starsText(stars: Int): String = when (stars) {
    3 -> "★★★"
    2 -> "★★☆"
    else -> "★☆☆"
}
