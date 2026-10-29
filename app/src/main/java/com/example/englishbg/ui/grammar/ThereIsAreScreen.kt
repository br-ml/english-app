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
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.englishbg.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThereIsAreScreen(
    viewModel: ThereIsAreViewModel = viewModel(),
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val answered = uiState.userChosenPlural != null
    val sentence = uiState.sentence

    val isButtonColor = when {
        !answered -> Color(0xFF4D96FF)
        sentence?.isPlural == false -> Color(0xFF6BCB77)        // "is" is correct
        uiState.userChosenPlural == false -> Color(0xFFFF4444)  // user chose "is" but wrong
        else -> Color(0xFF4D96FF)
    }
    val areButtonColor = when {
        !answered -> Color(0xFF845EF7)
        sentence?.isPlural == true -> Color(0xFF6BCB77)         // "are" is correct
        uiState.userChosenPlural == true -> Color(0xFFFF4444)   // user chose "are" but wrong
        else -> Color(0xFF845EF7)
    }

    Scaffold(
        containerColor = Color(0xFFFFF8F0),
        topBar = {
            TopAppBar(
                title = { Text("There is / There are", fontWeight = FontWeight.Bold) },
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
        Box(
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
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
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(color = Color(0xFF1A1A2E))) { append("There ") }
                                withStyle(
                                    SpanStyle(
                                        color = Color(0xFF4D96FF),
                                        fontWeight = FontWeight.ExtraBold,
                                    )
                                ) { append("___") }
                                withStyle(SpanStyle(color = Color(0xFF1A1A2E))) {
                                    append(" ${sentence?.after ?: ""}")
                                }
                            },
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            lineHeight = 36.sp,
                        )
                        if (sentence != null) {
                            Text(
                                text = "💡 ${sentence.hint}",
                                fontSize = 15.sp,
                                color = Color(0xFF666688),
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }

                Spacer(Modifier.height(40.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Button(
                        onClick = { viewModel.onAnswer(false) },
                        enabled = !answered,
                        modifier = Modifier.weight(1f).height(72.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = isButtonColor,
                            disabledContainerColor = isButtonColor,
                        ),
                    ) {
                        Text("is", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    }
                    Button(
                        onClick = { viewModel.onAnswer(true) },
                        enabled = !answered,
                        modifier = Modifier.weight(1f).height(72.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = areButtonColor,
                            disabledContainerColor = areButtonColor,
                        ),
                    ) {
                        Text("are", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    }
                }
            }

            AnimatedVisibility(
                visible = uiState.isComplete,
                enter = fadeIn(animationSpec = tween(300)),
                exit = fadeOut(animationSpec = tween(200)),
            ) {
                ThereIsAreSuccessOverlay(
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
private fun ThereIsAreSuccessOverlay(
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
