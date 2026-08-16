package com.example.englishbg.ui.unscramble

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishbg.EnglishApp
import com.example.englishbg.data.ALL_WORDS
import com.example.englishbg.data.WordPair
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LetterTile(val id: Int, val char: Char)

enum class CheckResult { Correct, Wrong }

data class UnscrambleUiState(
    val bulgarianHint: String = "",
    val correctAnswer: String = "",
    val availableTiles: List<LetterTile> = emptyList(),
    val pickedTiles: List<LetterTile> = emptyList(),
    val checkResult: CheckResult? = null,
    val questionIndex: Int = 0,
    val totalQuestions: Int = 5,
    val isComplete: Boolean = false,
)

class UnscrambleViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = (application as EnglishApp).activityRepository
    private val total = 5
    private val suitable = ALL_WORDS.filter { ' ' !in it.english && it.english.length in 3..10 }
    private var roundWords = listOf<WordPair>()

    private val _uiState = MutableStateFlow(UnscrambleUiState())
    val uiState: StateFlow<UnscrambleUiState> = _uiState.asStateFlow()

    init { loadRound() }

    fun loadRound() {
        roundWords = suitable.shuffled().take(total)
        _uiState.value = UnscrambleUiState(totalQuestions = total)
        loadQuestion(0)
    }

    private fun loadQuestion(index: Int) {
        if (index >= roundWords.size) {
            _uiState.value = _uiState.value.copy(isComplete = true)
            return
        }
        val word = roundWords[index]
        val answer = word.english.lowercase()
        var tiles = answer.mapIndexed { i, c -> LetterTile(i, c) }.shuffled()
        if (tiles.map { it.char }.joinToString("") == answer && answer.length > 1) {
            tiles = tiles.reversed()
        }
        _uiState.value = _uiState.value.copy(
            bulgarianHint = word.bulgarian,
            correctAnswer = answer,
            availableTiles = tiles,
            pickedTiles = emptyList(),
            checkResult = null,
            questionIndex = index,
        )
    }

    fun onTileAvailableTapped(tile: LetterTile) {
        val state = _uiState.value
        if (state.checkResult != null) return
        val newAvailable = state.availableTiles - tile
        val newPicked = state.pickedTiles + tile

        if (newAvailable.isEmpty()) {
            val typed = newPicked.map { it.char }.joinToString("")
            val result = if (typed == state.correctAnswer) CheckResult.Correct else CheckResult.Wrong
            _uiState.value = state.copy(
                availableTiles = newAvailable,
                pickedTiles = newPicked,
                checkResult = result,
            )
            viewModelScope.launch {
                delay(if (result == CheckResult.Correct) 900L else 1500L)
                val nextIndex = state.questionIndex + 1
                if (nextIndex >= roundWords.size) {
                    repo.recordExercisePlay("unscramble")
                }
                loadQuestion(nextIndex)
            }
        } else {
            _uiState.value = state.copy(availableTiles = newAvailable, pickedTiles = newPicked)
        }
    }

    fun onTilePickedTapped(tile: LetterTile) {
        val state = _uiState.value
        if (state.checkResult != null) return
        _uiState.value = state.copy(
            availableTiles = state.availableTiles + tile,
            pickedTiles = state.pickedTiles - tile,
        )
    }
}
