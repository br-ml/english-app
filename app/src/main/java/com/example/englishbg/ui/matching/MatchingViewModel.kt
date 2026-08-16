package com.example.englishbg.ui.matching

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.englishbg.EnglishApp
import com.example.englishbg.data.ALL_CATEGORIES
import com.example.englishbg.data.ALL_WORDS
import com.example.englishbg.navigation.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class MatchCardState { Idle, Selected, Matched, WrongFlash }

data class CardUiState(
    val text: String,
    val pairIndex: Int,
    val colorIndex: Int,
    val cardState: MatchCardState = MatchCardState.Idle,
)

data class MatchingUiState(
    val englishCards: List<CardUiState> = emptyList(),
    val bulgarianCards: List<CardUiState> = emptyList(),
    val selectedEnglishIndex: Int? = null,
    val matchedCount: Int = 0,
    val wrongAttempts: Int = 0,
    val totalPairs: Int = 0,
    val isComplete: Boolean = false,
    val categoryName: String = "",
)

class MatchingViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle,
) : AndroidViewModel(application) {

    private val categoryIndex: Int? = savedStateHandle.get<Int>(Screen.MatchingCategory.ARG)
    private val repo = (application as EnglishApp).activityRepository

    private val _uiState = MutableStateFlow(MatchingUiState())
    val uiState: StateFlow<MatchingUiState> = _uiState.asStateFlow()

    init { loadRound() }

    fun loadRound() {
        val (words, catName) = when (categoryIndex) {
            null -> ALL_WORDS.shuffled().take(5) to "Random Words"
            else -> {
                val cat = ALL_CATEGORIES[categoryIndex]
                cat.words.shuffled().take(minOf(5, cat.words.size)) to cat.name
            }
        }

        val total = words.size
        val englishCards = words
            .mapIndexed { i, wp -> CardUiState(wp.english.lowercase(), pairIndex = i, colorIndex = i) }
            .shuffled()
        val bulgarianCards = words
            .mapIndexed { i, wp -> CardUiState(wp.bulgarian, pairIndex = i, colorIndex = i) }
            .shuffled()

        _uiState.value = MatchingUiState(
            englishCards = englishCards,
            bulgarianCards = bulgarianCards,
            totalPairs = total,
            categoryName = catName,
        )
    }

    fun onEnglishCardTapped(index: Int) {
        val state = _uiState.value
        val card = state.englishCards[index]
        if (card.cardState == MatchCardState.Matched ||
            card.cardState == MatchCardState.WrongFlash
        ) return

        _uiState.value = state.copy(
            englishCards = state.englishCards.mapIndexed { i, c ->
                when {
                    c.cardState == MatchCardState.Matched -> c
                    i == index -> c.copy(cardState = MatchCardState.Selected)
                    else -> c.copy(cardState = MatchCardState.Idle)
                }
            },
            selectedEnglishIndex = index,
        )
    }

    fun onBulgarianCardTapped(index: Int) {
        val state = _uiState.value
        val bulg = state.bulgarianCards[index]
        val selIdx = state.selectedEnglishIndex ?: return

        if (bulg.cardState == MatchCardState.Matched ||
            bulg.cardState == MatchCardState.WrongFlash
        ) return

        val eng = state.englishCards[selIdx]

        if (eng.pairIndex == bulg.pairIndex) {
            val newMatched = state.matchedCount + 1
            val complete = newMatched == state.totalPairs
            _uiState.value = state.copy(
                englishCards = state.englishCards.mapIndexed { i, c ->
                    if (i == selIdx) c.copy(cardState = MatchCardState.Matched) else c
                },
                bulgarianCards = state.bulgarianCards.mapIndexed { i, c ->
                    if (i == index) c.copy(cardState = MatchCardState.Matched) else c
                },
                selectedEnglishIndex = null,
                matchedCount = newMatched,
                isComplete = complete,
            )
            if (complete && categoryIndex != null) {
                viewModelScope.launch {
                    val wrong = _uiState.value.wrongAttempts
                    val score = maxOf(0, 100 - wrong * 20)
                    repo.recordCompletion(categoryIndex, score)
                }
            } else if (complete && categoryIndex == null) {
                viewModelScope.launch { repo.recordExercisePlay("match_random") }
            }
        } else {
            _uiState.value = state.copy(
                englishCards = state.englishCards.mapIndexed { i, c ->
                    if (i == selIdx) c.copy(cardState = MatchCardState.WrongFlash) else c
                },
                bulgarianCards = state.bulgarianCards.mapIndexed { i, c ->
                    if (i == index) c.copy(cardState = MatchCardState.WrongFlash) else c
                },
                selectedEnglishIndex = null,
                wrongAttempts = state.wrongAttempts + 1,
            )
            viewModelScope.launch {
                delay(600L)
                _uiState.update { s ->
                    s.copy(
                        englishCards = s.englishCards.map {
                            if (it.cardState == MatchCardState.WrongFlash)
                                it.copy(cardState = MatchCardState.Idle) else it
                        },
                        bulgarianCards = s.bulgarianCards.map {
                            if (it.cardState == MatchCardState.WrongFlash)
                                it.copy(cardState = MatchCardState.Idle) else it
                        },
                    )
                }
            }
        }
    }

    fun onPlayAgain() { loadRound() }
}
