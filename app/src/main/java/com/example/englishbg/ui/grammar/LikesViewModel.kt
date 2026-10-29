package com.example.englishbg.ui.grammar

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishbg.EnglishApp
import com.example.englishbg.data.LIKES_SENTENCES
import com.example.englishbg.data.LikesItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LikesUiState(
    val item: LikesItem? = null,
    val userChoseLikes: Boolean? = null,
    val isCorrect: Boolean? = null,
    val questionIndex: Int = 0,
    val totalQuestions: Int = 10,
    val score: Int = 0,
    val isComplete: Boolean = false,
)

class LikesViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = (application as EnglishApp).activityRepository
    private val total = minOf(10, LIKES_SENTENCES.size)
    private var questions: List<LikesItem> = emptyList()

    private val _uiState = MutableStateFlow(LikesUiState())
    val uiState: StateFlow<LikesUiState> = _uiState.asStateFlow()

    init { loadRound() }

    fun loadRound() {
        questions = LIKES_SENTENCES.shuffled().take(total)
        _uiState.value = LikesUiState(totalQuestions = total)
        loadQuestion(0)
    }

    private fun loadQuestion(index: Int) {
        if (index >= questions.size) {
            _uiState.value = _uiState.value.copy(isComplete = true)
            return
        }
        _uiState.value = _uiState.value.copy(
            item = questions[index],
            userChoseLikes = null,
            isCorrect = null,
            questionIndex = index,
        )
    }

    fun onAnswer(userChoseLikes: Boolean) {
        val state = _uiState.value
        if (state.userChoseLikes != null || state.isComplete) return
        val correct = userChoseLikes == (state.item?.likes ?: false)
        val newScore = state.score + if (correct) 1 else 0
        _uiState.value = state.copy(
            userChoseLikes = userChoseLikes,
            isCorrect = correct,
            score = newScore,
        )
        viewModelScope.launch {
            delay(950L)
            val nextIndex = state.questionIndex + 1
            if (nextIndex >= questions.size) {
                repo.recordExercisePlay("grammar_likes_g3")
            }
            loadQuestion(nextIndex)
        }
    }
}
