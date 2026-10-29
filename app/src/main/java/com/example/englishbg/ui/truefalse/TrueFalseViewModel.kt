package com.example.englishbg.ui.truefalse

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.englishbg.EnglishApp
import com.example.englishbg.data.ALL_WORDS
import com.example.englishbg.data.GRADE3_WORDS
import com.example.englishbg.data.WordPair
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TrueFalseUiState(
    val englishWord: String = "",
    val shownBulgarian: String = "",
    val isActuallyCorrect: Boolean = false,
    val userAnswer: Boolean? = null,
    val wasCorrect: Boolean? = null,
    val questionIndex: Int = 0,
    val totalQuestions: Int = 10,
    val score: Int = 0,
    val isComplete: Boolean = false,
)

private data class Question(
    val english: String,
    val shownBulgarian: String,
    val isCorrectPair: Boolean,
)

class TrueFalseViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle,
) : AndroidViewModel(application) {

    private val repo = (application as EnglishApp).activityRepository
    private val grade: Int = savedStateHandle.get<Int>("grade") ?: 2
    private val wordList: List<WordPair> = if (grade == 3) GRADE3_WORDS else ALL_WORDS
    private val total = minOf(10, wordList.size)
    private var questions: List<Question> = emptyList()

    private val _uiState = MutableStateFlow(TrueFalseUiState())
    val uiState: StateFlow<TrueFalseUiState> = _uiState.asStateFlow()

    init { loadRound() }

    fun loadRound() {
        val words = wordList.shuffled().take(total)
        questions = words.map { pair ->
            val showCorrect = listOf(true, false).random()
            val shownBulgarian = if (showCorrect) {
                pair.bulgarian
            } else {
                wordList.filter { it.english != pair.english }.random().bulgarian
            }
            Question(pair.english.lowercase(), shownBulgarian, showCorrect)
        }
        _uiState.value = TrueFalseUiState(totalQuestions = total)
        loadQuestion(0)
    }

    private fun loadQuestion(index: Int) {
        if (index >= questions.size) {
            _uiState.value = _uiState.value.copy(isComplete = true)
            return
        }
        val q = questions[index]
        _uiState.value = _uiState.value.copy(
            englishWord = q.english,
            shownBulgarian = q.shownBulgarian,
            isActuallyCorrect = q.isCorrectPair,
            userAnswer = null,
            wasCorrect = null,
            questionIndex = index,
        )
    }

    fun onAnswer(userSaysCorrect: Boolean) {
        val state = _uiState.value
        if (state.userAnswer != null || state.isComplete) return
        val wasRight = userSaysCorrect == state.isActuallyCorrect
        val newScore = state.score + if (wasRight) 1 else 0
        _uiState.value = state.copy(
            userAnswer = userSaysCorrect,
            wasCorrect = wasRight,
            score = newScore,
        )
        viewModelScope.launch {
            delay(950L)
            val nextIndex = state.questionIndex + 1
            if (nextIndex >= questions.size) {
                repo.recordExercisePlay("truefalse")
            }
            loadQuestion(nextIndex)
        }
    }
}
