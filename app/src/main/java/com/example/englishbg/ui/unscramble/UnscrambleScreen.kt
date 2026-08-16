package com.example.englishbg.ui.unscramble

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.englishbg.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun UnscrambleScreen(
    viewModel: UnscrambleViewModel = viewModel(),
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val wordLength = uiState.correctAnswer.length

    Scaffold(
        containerColor = Color(0xFFFFF8F0),
        topBar = {
            TopAppBar(
                title = { Text("Unscramble", fontWeight = FontWeight.Bold) },
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
                            text = "${uiState.questionIndex + 1} / ${uiState.totalQuestions}",
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
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                Spacer(Modifier.height(4.dp))

                // Bulgarian hint
                Text(
                    text = "🇧🇬  ${uiState.bulgarianHint}",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF1A1A2E),
                )

                Text(
                    text = "Spell the English word:",
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                // Answer boxes row (picked tiles)
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    repeat(wordLength) { i ->
                        val tile = uiState.pickedTiles.getOrNull(i)
                        val boxColor = when {
                            tile == null -> Color.Transparent
                            uiState.checkResult == CheckResult.Correct -> Color(0xFF6BCB77)
                            uiState.checkResult == CheckResult.Wrong -> Color(0xFFFF4444)
                            else -> Color(0xFF4D96FF)
                        }
                        val borderColor = when {
                            uiState.checkResult == CheckResult.Correct -> Color(0xFF6BCB77)
                            uiState.checkResult == CheckResult.Wrong -> Color(0xFFFF4444)
                            tile != null -> Color(0xFF4D96FF)
                            else -> Color(0xFFB0BEC5)
                        }
                        Box(
                            modifier = Modifier
                                .padding(3.dp)
                                .size(44.dp)
                                .background(boxColor, RoundedCornerShape(10.dp))
                                .border(2.dp, borderColor, RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center,
                        ) {
                            if (tile != null) {
                                Text(
                                    text = tile.char.toString(),
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                )
                            }
                        }
                    }
                }

                // Show correct answer on wrong
                if (uiState.checkResult == CheckResult.Wrong) {
                    Text(
                        text = "✅  ${uiState.correctAnswer}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32),
                        textAlign = TextAlign.Center,
                    )
                }

                Spacer(Modifier.height(8.dp))

                // Available letter tiles
                FlowRow(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    uiState.availableTiles.forEach { tile ->
                        LetterChip(
                            letter = tile.char.toString().uppercase(),
                            color = Color(0xFFFFD93D),
                            enabled = uiState.checkResult == null,
                            onClick = { viewModel.onTileAvailableTapped(tile) },
                        )
                    }
                }

                // Picked tiles (tappable to remove)
                if (uiState.pickedTiles.isNotEmpty() && uiState.checkResult == null) {
                    Text(
                        text = "Tap a letter below to remove it:",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        uiState.pickedTiles.forEach { tile ->
                            LetterChip(
                                letter = tile.char.toString().uppercase(),
                                color = Color(0xFFB0BEC5),
                                enabled = true,
                                onClick = { viewModel.onTilePickedTapped(tile) },
                            )
                        }
                    }
                }
            }

            // Success overlay
            AnimatedVisibility(
                visible = uiState.isComplete,
                enter = fadeIn(animationSpec = tween(300)),
                exit = fadeOut(animationSpec = tween(200)),
            ) {
                UnscrambleSuccessOverlay(
                    total = uiState.totalQuestions,
                    onPlayAgain = { viewModel.loadRound() },
                    onBack = onBack,
                )
            }
        }
    }
}

@Composable
private fun LetterChip(
    letter: String,
    color: Color,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        color = color,
        modifier = Modifier
            .padding(4.dp)
            .size(52.dp),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = letter,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1A1A2E),
            )
        }
    }
}

@Composable
private fun UnscrambleSuccessOverlay(
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
                Text("🔤 Great job! 🔤", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)
                Text(
                    "You unscrambled all $total words!",
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
