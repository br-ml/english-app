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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
fun Grade2HomeScreen(
    onCategoryMode: () -> Unit,
    onRandomMode: () -> Unit,
    onListenMode: () -> Unit,
    onTrueFalse: () -> Unit,
    onUnscramble: () -> Unit,
    onProgress: () -> Unit,
    onBack: () -> Unit,
    viewModel: HomeViewModel = viewModel(),
) {
    val progress by viewModel.progress.collectAsState()

    Scaffold(
        containerColor = Color(0xFFFFF8F0),
        topBar = {
            TopAppBar(
                title = { Text("2nd Grade", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(12.dp))

            // Progress header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        Brush.horizontalGradient(listOf(Color(0xFFFF922B), Color(0xFFFFD93D)))
                    )
                    .padding(vertical = 20.dp, horizontal = 16.dp),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🏫 2nd Grade", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    Text(
                        "Choose an exercise!",
                        fontSize = 14.sp,
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

            Spacer(Modifier.height(20.dp))

            // Row 1
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                GradeExerciseCard(
                    emoji = "🃏",
                    title = "Match",
                    subtitle = "By category",
                    color = Color(0xFFD6EAFF),
                    onClick = onCategoryMode,
                    modifier = Modifier.weight(1f),
                )
                GradeExerciseCard(
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
                GradeExerciseCard(
                    emoji = "🎧",
                    title = "Listen",
                    subtitle = "Hear & choose",
                    color = Color(0xFFFFEFCC),
                    onClick = onListenMode,
                    modifier = Modifier.weight(1f),
                )
                GradeExerciseCard(
                    emoji = "✅",
                    title = "True or False",
                    subtitle = "Right translation?",
                    color = Color(0xFFFFE4E4),
                    onClick = onTrueFalse,
                    modifier = Modifier.weight(1f),
                )
            }

            Spacer(Modifier.height(12.dp))

            GradeExerciseCard(
                emoji = "🔤",
                title = "Unscramble",
                subtitle = "Fix the jumbled letters",
                color = Color(0xFFECDAFF),
                onClick = onUnscramble,
                modifier = Modifier.fillMaxWidth(),
                horizontal = true,
            )

            Spacer(Modifier.height(12.dp))

            GradeExerciseCard(
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
