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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onGrade2: () -> Unit,
    onGrade3Grammar: () -> Unit,
    onGrade3Match: () -> Unit,
    onGrade3Listen: () -> Unit,
    onGrade3TrueFalse: () -> Unit,
    onGrade3Unscramble: () -> Unit,
    onGrade3Category: () -> Unit,
    onProgress: () -> Unit,
) {
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
                        "Избери клас!",
                        fontSize = 15.sp,
                        color = Color.White.copy(alpha = 0.9f),
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // Grade 3 section label
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "🆕  3rd Grade",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1A1A2E),
                )
            }

            Spacer(Modifier.height(10.dp))

            // Grammar — top of 3rd grade
            GradeExerciseCard(
                emoji = "📝",
                title = "Grammar",
                subtitle = "There is / There are",
                color = Color(0xFFE8F4FD),
                onClick = onGrade3Grammar,
                modifier = Modifier.fillMaxWidth(),
                horizontal = true,
            )

            Spacer(Modifier.height(12.dp))

            // Grade 3 exercises — row 1
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                GradeExerciseCard(
                    emoji = "🔀",
                    title = "Match",
                    subtitle = "Random words",
                    color = Color(0xFFD6F5E3),
                    onClick = onGrade3Match,
                    modifier = Modifier.weight(1f),
                )
                GradeExerciseCard(
                    emoji = "🎧",
                    title = "Listen",
                    subtitle = "Hear & choose",
                    color = Color(0xFFFFEFCC),
                    onClick = onGrade3Listen,
                    modifier = Modifier.weight(1f),
                )
            }

            Spacer(Modifier.height(12.dp))

            // Grade 3 exercises — row 2
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                GradeExerciseCard(
                    emoji = "✅",
                    title = "True or False",
                    subtitle = "Right translation?",
                    color = Color(0xFFFFE4E4),
                    onClick = onGrade3TrueFalse,
                    modifier = Modifier.weight(1f),
                )
                GradeExerciseCard(
                    emoji = "🔤",
                    title = "Unscramble",
                    subtitle = "Fix the letters",
                    color = Color(0xFFECDAFF),
                    onClick = onGrade3Unscramble,
                    modifier = Modifier.weight(1f),
                )
            }

            Spacer(Modifier.height(12.dp))

            // Grade 3 by category
            GradeExerciseCard(
                emoji = "🃏",
                title = "Match by Category",
                subtitle = "Choose a category",
                color = Color(0xFFD6EAFF),
                onClick = onGrade3Category,
                modifier = Modifier.fillMaxWidth(),
                horizontal = true,
            )

            Spacer(Modifier.height(20.dp))

            // Divider label
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "🏫  2nd Grade",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1A1A2E),
                )
            }

            Spacer(Modifier.height(10.dp))

            GradeExerciseCard(
                emoji = "📚",
                title = "2nd Grade",
                subtitle = "2nd grade exercises",
                color = Color(0xFFFFF0D6),
                onClick = onGrade2,
                modifier = Modifier.fillMaxWidth(),
                horizontal = true,
            )

            Spacer(Modifier.height(12.dp))

            GradeExerciseCard(
                emoji = "📊",
                title = "Прогрес",
                subtitle = "Виж резултатите",
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
internal fun GradeExerciseCard(
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
