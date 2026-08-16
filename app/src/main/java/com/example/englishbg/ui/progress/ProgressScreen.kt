package com.example.englishbg.ui.progress

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.englishbg.data.ALL_CATEGORIES

private val rowColors = listOf(
    Color(0xFFFFE4E4), Color(0xFFFFEFCC), Color(0xFFD6F5E3),
    Color(0xFFD6EAFF), Color(0xFFFFE4CC), Color(0xFFECDAFF),
)

private val barColors = listOf(
    Color(0xFFE57373), Color(0xFFFFB300), Color(0xFF4CAF50),
    Color(0xFF2196F3), Color(0xFFFF7043), Color(0xFF9C27B0),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressScreen(
    onBack: () -> Unit,
    viewModel: ProgressViewModel = viewModel(),
) {
    val state by viewModel.uiState.collectAsState()
    val practiced = state.categories.count { it.stats.timesPlayed > 0 }
    val total = ALL_CATEGORIES.size
    val threeStars = state.categories.count { it.stats.stars == 3 }
    var showResetDialog by remember { mutableStateOf(false) }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset Progress?") },
            text = { Text("This will clear all scores and play counts. This cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.resetAll()
                    showResetDialog = false
                }) {
                    Text("Reset", color = Color(0xFFE53935), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancel")
                }
            },
        )
    }

    Scaffold(
        containerColor = Color(0xFFFFF8F0),
        topBar = {
            TopAppBar(
                title = { Text("Progress", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showResetDialog = true }) {
                        Icon(
                            Icons.Default.DeleteForever,
                            contentDescription = "Reset progress",
                            tint = Color(0xFFE53935),
                        )
                    }
                },
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            // Overall summary
            item {
                Spacer(Modifier.height(8.dp))
                SummaryCard(practiced, total, threeStars)
            }

            // Exercise activity section
            item {
                Spacer(Modifier.height(4.dp))
                Text(
                    "Exercise Activity",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A2E),
                )
            }

            items(state.exercises) { ex ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEEEEFF)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(ex.emoji, fontSize = 26.sp)
                        Spacer(Modifier.width(12.dp))
                        Text(
                            ex.label,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1A1A2E),
                            modifier = Modifier.weight(1f),
                        )
                        if (ex.timesPlayed > 0) {
                            Text(
                                "${ex.timesPlayed}×",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF4D96FF),
                            )
                        } else {
                            Text(
                                "Not played",
                                fontSize = 12.sp,
                                color = Color(0xFF999999),
                            )
                        }
                    }
                }
            }

            // Category breakdown section
            item {
                Spacer(Modifier.height(4.dp))
                Text(
                    "Category Scores",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A2E),
                )
            }

            items(state.categories) { item ->
                val colorIndex = item.index % rowColors.size
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = rowColors[colorIndex]),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(item.emoji, fontSize = 22.sp)
                            Spacer(Modifier.width(10.dp))
                            Text(
                                text = item.name,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1A1A2E),
                                modifier = Modifier.weight(1f),
                            )
                            if (item.stats.timesPlayed > 0) {
                                Text(
                                    text = starsText(item.stats.stars),
                                    fontSize = 14.sp,
                                    color = Color(0xFFFFB300),
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = "${item.stats.bestScore}%",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF333355),
                                )
                            } else {
                                Text(
                                    text = "Not played",
                                    fontSize = 12.sp,
                                    color = Color(0xFF999999),
                                )
                            }
                        }
                        Spacer(Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { item.stats.bestScore / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = barColors[colorIndex],
                            trackColor = Color(0x33000000),
                        )
                        if (item.stats.timesPlayed > 0) {
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "Played ${item.stats.timesPlayed} time${if (item.stats.timesPlayed > 1) "s" else ""}",
                                fontSize = 11.sp,
                                color = Color(0xFF666688),
                            )
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun SummaryCard(practiced: Int, total: Int, threeStars: Int) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E)),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                "Overall Progress",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
            )
            Spacer(Modifier.height(10.dp))
            LinearProgressIndicator(
                progress = { practiced.toFloat() / total },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp)),
                color = Color(0xFF6BCB77),
                trackColor = Color(0x55FFFFFF),
            )
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    "$practiced / $total categories practiced",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.85f),
                )
                Text(
                    "$threeStars ★★★",
                    fontSize = 13.sp,
                    color = Color(0xFFFFD93D),
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

private fun starsText(stars: Int): String = when (stars) {
    3 -> "★★★"
    2 -> "★★☆"
    else -> "★☆☆"
}
