package com.example.englishbg.ui.listen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
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
import com.example.englishbg.ui.theme.pairColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListenScreen(
    viewModel: ListenViewModel = viewModel(),
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = Color(0xFFFFF8F0),
        topBar = {
            TopAppBar(
                title = { Text(uiState.categoryName, fontWeight = FontWeight.Bold, maxLines = 1) },
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
                            text = "${uiState.questionIndex} / ${uiState.totalQuestions}",
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
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                Spacer(Modifier.height(8.dp))

                // Speaker button
                Surface(
                    onClick = { viewModel.speak() },
                    shape = CircleShape,
                    color = if (uiState.isTtsReady)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.size(120.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Hear the word",
                            modifier = Modifier.size(56.dp),
                            tint = if (uiState.isTtsReady) Color.White
                                   else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                Text(
                    text = if (uiState.isTtsReady) "Tap to hear the word"
                           else "Loading voice…",
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Spacer(Modifier.height(4.dp))

                // 2×2 option grid
                val options = uiState.options
                if (options.size == 4) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            OptionCard(
                                text = options[0],
                                state = optionState(uiState, options[0]),
                                modifier = Modifier.weight(1f),
                                onClick = { viewModel.onOptionSelected(options[0]) },
                            )
                            OptionCard(
                                text = options[1],
                                state = optionState(uiState, options[1]),
                                modifier = Modifier.weight(1f),
                                onClick = { viewModel.onOptionSelected(options[1]) },
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            OptionCard(
                                text = options[2],
                                state = optionState(uiState, options[2]),
                                modifier = Modifier.weight(1f),
                                onClick = { viewModel.onOptionSelected(options[2]) },
                            )
                            OptionCard(
                                text = options[3],
                                state = optionState(uiState, options[3]),
                                modifier = Modifier.weight(1f),
                                onClick = { viewModel.onOptionSelected(options[3]) },
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
                ListenSuccessOverlay(
                    totalQuestions = uiState.totalQuestions,
                    onPlayAgain = { viewModel.loadRound() },
                    onBack = onBack,
                )
            }
        }
    }
}

private enum class OptionCardState { Idle, Correct, Wrong }

private fun optionState(state: ListenUiState, option: String): OptionCardState = when {
    option == state.correctOption  -> OptionCardState.Correct
    option in state.wrongOptions   -> OptionCardState.Wrong
    else                           -> OptionCardState.Idle
}

@Composable
private fun OptionCard(
    text: String,
    state: OptionCardState,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val backgroundColor = when (state) {
        OptionCardState.Idle    -> MaterialTheme.colorScheme.surfaceVariant
        OptionCardState.Correct -> pairColors[2]
        OptionCardState.Wrong   -> Color(0xFFFF4444)
    }
    val textColor = when (state) {
        OptionCardState.Wrong -> Color.White
        else                  -> Color.Black
    }

    Card(
        onClick = onClick,
        enabled = state == OptionCardState.Idle,
        modifier = modifier.aspectRatio(2f),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            disabledContainerColor = backgroundColor,
            disabledContentColor = textColor,
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = text,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
            )
        }
    }
}

@Composable
private fun ListenSuccessOverlay(
    totalQuestions: Int,
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
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape),
                )
                Text("Well done Raya!", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
                Text(
                    "You got all $totalQuestions words!",
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = onPlayAgain,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(14.dp),
                ) {
                    Text("Play Again", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(14.dp),
                ) {
                    Text("Back to Menu", fontSize = 18.sp)
                }
            }
        }
    }
}
