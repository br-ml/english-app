package com.example.englishbg.ui.grammar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.englishbg.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImperativeScreen(
    viewModel: ImperativeViewModel = viewModel(),
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val answered = uiState.userChoseOption1 != null
    val q = uiState.question

    fun optionColor(isOption1: Boolean): Color {
        if (!answered) return if (isOption1) Color(0xFF4D96FF) else Color(0xFF845EF7)
        val correctIsThis = q?.correctIsOption1 == isOption1
        val userChoseThis = uiState.userChoseOption1 == isOption1
        return when {
            correctIsThis -> Color(0xFF6BCB77)
            userChoseThis -> Color(0xFFFF4444)
            else -> if (isOption1) Color(0xFF4D96FF) else Color(0xFF845EF7)
        }
    }

    Scaffold(
        containerColor = Color(0xFFFFF8F0),
        topBar = {
            TopAppBar(
                title = { Text("Imperatives", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.padding(end = 12.dp),
                    ) {
                        Text(
                            text = "${uiState.score} / ${uiState.totalQuestions}",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                    }
                },
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                // Prompt card
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = q?.item?.bulgarian ?: "",
                            fontSize = 34.sp,
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center,
                            color = Color(0xFF1A1A2E),
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = "Choose the correct English imperative",
                            fontSize = 13.sp,
                            color = Color(0xFF888899),
                            textAlign = TextAlign.Center,
                        )
                    }
                }

                Spacer(Modifier.height(32.dp))

                // Option 1
                Button(
                    onClick = { viewModel.onAnswer(true) },
                    enabled = !answered,
                    modifier = Modifier.fillMaxWidth().height(68.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = optionColor(true),
                        disabledContainerColor = optionColor(true),
                    ),
                ) {
                    Text(
                        q?.option1 ?: "",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                    )
                }

                Spacer(Modifier.height(14.dp))

                // Option 2
                Button(
                    onClick = { viewModel.onAnswer(false) },
                    enabled = !answered,
                    modifier = Modifier.fillMaxWidth().height(68.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = optionColor(false),
                        disabledContainerColor = optionColor(false),
                    ),
                ) {
                    Text(
                        q?.option2 ?: "",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            AnimatedVisibility(
                visible = uiState.isComplete,
                enter = fadeIn(animationSpec = tween(300)),
                exit = fadeOut(animationSpec = tween(200)),
            ) {
                ImperativeSuccessOverlay(
                    score = uiState.score,
                    total = uiState.totalQuestions,
                    onPlayAgain = { viewModel.loadRound() },
                    onBack = onBack,
                )
            }
        }
    }
}

@Composable
private fun ImperativeSuccessOverlay(
    score: Int,
    total: Int,
    onPlayAgain: () -> Unit,
    onBack: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xCC000000)),
        contentAlignment = Alignment.Center,
    ) {
        ElevatedCard(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.padding(horizontal = 36.dp),
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Image(
                    painter = painterResource(R.drawable.brani3),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(140.dp).clip(CircleShape),
                )
                val emoji = if (score >= total * 0.8) "🌟" else "👍"
                Text("$emoji Well done Raya! $emoji", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                Text(
                    "You got $score out of $total!",
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = onPlayAgain,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(14.dp),
                ) { Text("Play Again", fontSize = 18.sp, fontWeight = FontWeight.Bold) }
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(14.dp),
                ) { Text("Back to Menu", fontSize = 18.sp) }
            }
        }
    }
}
