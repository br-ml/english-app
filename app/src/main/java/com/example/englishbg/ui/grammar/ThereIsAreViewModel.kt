package com.example.englishbg.ui.grammar

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishbg.EnglishApp
import com.example.englishbg.data.THERE_IS_ARE_SENTENCES
import com.example.englishbg.data.ThereIsAreSentence
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ThereIsAreUiState(
    val sentence: ThereIsAreSentence? = null,
    val userChosenPlural: Boolean? = null,
    val isCorrect: Boolean? = null,
    val questionIndex: Int = 0,
    val totalQuestions: Int = 10,
    val score: Int = 0,
    val isComplete: Boolean = false,
)

class ThereIsAreViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = (application as EnglishApp).activityRepository
    private val total = 10
    private var questions: List<ThereIsAreSentence> = emptyList()

    private val _uiState = MutableStateFlow(ThereIsAreUiState())
    val uiState: StateFlow<ThereIsAreUiState> = _uiState.asStateFlow()

    init { loadRound() }

    fun loadRound() {
        questions = THERE_IS_ARE_SENTENCES.shuffled().take(total)
        _uiState.value = ThereIsAreUiState(totalQuestions = total)
        loadQuestion(0)
    }

    private fun loadQuestion(index: Int) {
        if (index >= questions.size) {
            _uiState.value = _uiState.value.copy(isComplete = true)
            return
        }
        _uiState.value = _uiState.value.copy(
            sentence = questions[index],
            userChosenPlural = null,
            isCorrect = null,
            questionIndex = index,
        )
    }

    fun onAnswer(userChosenPlural: Boolean) {
        val state = _uiState.value
        if (state.userChosenPlural != null || state.isComplete) return
        val correct = userChosenPlural == (state.sentence?.isPlural ?: false)
        val newScore = state.score + if (correct) 1 else 0
        _uiState.value = state.copy(
            userChosenPlural = userChosenPlural,
            isCorrect = correct,
            score = newScore,
        )
        viewModelScope.launch {
            delay(950L)
            val nextIndex = state.questionIndex + 1
            if (nextIndex >= questions.size) {
                repo.recordExercisePlay("grammar_there_is_are_g3")
            }
            loadQuestion(nextIndex)
        }
    }
}
