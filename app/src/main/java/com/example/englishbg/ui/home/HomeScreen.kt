package com.example.englishbg.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onCategoryMode: () -> Unit,
    onRandomMode: () -> Unit,
    onListenMode: () -> Unit,
    onTrueFalse: () -> Unit,
    onUnscramble: () -> Unit,
    onProgress: () -> Unit,
    viewModel: HomeViewModel = viewModel(),
) {
    val progress by viewModel.progress.collectAsState()

    Scaffold(containerColor = Color(0xFFFFF8F0)) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(20.dp))

            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        Brush.horizontalGradient(listOf(Color(0xFF6BCB77), Color(0xFF4D96FF)))
                    )
                    .padding(vertical = 24.dp, horizontal = 16.dp),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("⭐ 🌟 ⭐", fontSize = 28.sp)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "English Practice",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        "Choose an exercise!",
                        fontSize = 15.sp,
                        color = Color.White.copy(alpha = 0.9f),
                    )
                    if (progress.practiced > 0) {
                        Spacer(Modifier.height(10.dp))
                        LinearProgressIndicator(
                            progress = { progress.practiced.toFloat() / progress.total },
                            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(4.dp)),
                            color = Color.White,
                            trackColor = Color.White.copy(alpha = 0.3f),
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "${progress.practiced} of ${progress.total} categories · ${progress.threeStars} ★★★",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.9f),
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Row 1
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                ExerciseCard(
                    emoji = "🃏",
                    title = "Match",
                    subtitle = "By category",
                    color = Color(0xFFD6EAFF),
                    onClick = onCategoryMode,
                    modifier = Modifier.weight(1f),
                )
                ExerciseCard(
                    emoji = "🔀",
                    title = "Match",
                    subtitle = "Random words",
                    color = Color(0xFFD6F5E3),
                    onClick = onRandomMode,
                    modifier = Modifier.weight(1f),
                )
            }

            Spacer(Modifier.height(12.dp))

            // Row 2
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                ExerciseCard(
                    emoji = "🎧",
                    title = "Listen",
                    subtitle = "Hear & choose",
                    color = Color(0xFFFFEFCC),
                    onClick = onListenMode,
                    modifier = Modifier.weight(1f),
                )
                ExerciseCard(
                    emoji = "✅",
                    title = "True or False",
                    subtitle = "Right translation?",
                    color = Color(0xFFFFE4E4),
                    onClick = onTrueFalse,
                    modifier = Modifier.weight(1f),
                )
            }

            Spacer(Modifier.height(12.dp))

            // Row 3 — full width
            ExerciseCard(
                emoji = "🔤",
                title = "Unscramble",
                subtitle = "Fix the jumbled letters",
                color = Color(0xFFECDAFF),
                onClick = onUnscramble,
                modifier = Modifier.fillMaxWidth(),
                horizontal = true,
            )

            Spacer(Modifier.height(12.dp))

            // Progress button
            ExerciseCard(
                emoji = "📊",
                title = "Progress",
                subtitle = "See how you're doing",
                color = Color(0xFFDFF5E3),
                onClick = onProgress,
                modifier = Modifier.fillMaxWidth(),
                horizontal = true,
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExerciseCard(
    emoji: String,
    title: String,
    subtitle: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    horizontal: Boolean = false,
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.height(if (horizontal) 88.dp else 130.dp),
    ) {
        if (horizontal) {
            Row(
                modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(emoji, fontSize = 36.sp)
                Column {
                    Text(title, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1A1A2E))
                    Text(subtitle, fontSize = 13.sp, color = Color(0xFF555577))
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(emoji, fontSize = 36.sp)
                Spacer(Modifier.height(6.dp))
                Text(title, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1A1A2E))
                Text(subtitle, fontSize = 12.sp, color = Color(0xFF555577))
            }
        }
    }
}
