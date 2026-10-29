package com.example.englishbg.ui.grammar

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishbg.EnglishApp
import com.example.englishbg.data.IMPERATIVE_SENTENCES
import com.example.englishbg.data.ImperativeItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

data class ImperativeQuestion(
    val item: ImperativeItem,
    val option1: String,
    val option2: String,
    val correctIsOption1: Boolean,
)

data class ImperativeUiState(
    val question: ImperativeQuestion? = null,
    val userChoseOption1: Boolean? = null,
    val isCorrect: Boolean? = null,
    val questionIndex: Int = 0,
    val totalQuestions: Int = 10,
    val score: Int = 0,
    val isComplete: Boolean = false,
)

class ImperativeViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = (application as EnglishApp).activityRepository
    private val total = minOf(10, IMPERATIVE_SENTENCES.size)
    private var questions: List<ImperativeQuestion> = emptyList()

    private val _uiState = MutableStateFlow(ImperativeUiState())
    val uiState: StateFlow<ImperativeUiState> = _uiState.asStateFlow()

    init { loadRound() }

    fun loadRound() {
        questions = IMPERATIVE_SENTENCES.shuffled().take(total).map { item ->
            if (Random.nextBoolean()) {
                ImperativeQuestion(item, item.correct, item.wrong, correctIsOption1 = true)
            } else {
                ImperativeQuestion(item, item.wrong, item.correct, correctIsOption1 = false)
            }
        }
        _uiState.value = ImperativeUiState(totalQuestions = total)
        loadQuestion(0)
    }

    private fun loadQuestion(index: Int) {
        if (index >= questions.size) {
            _uiState.value = _uiState.value.copy(isComplete = true)
            return
        }
        _uiState.value = _uiState.value.copy(
            question = questions[index],
            userChoseOption1 = null,
            isCorrect = null,
            questionIndex = index,
        )
    }

    fun onAnswer(choseOption1: Boolean) {
        val state = _uiState.value
        if (state.userChoseOption1 != null || state.isComplete) return
        val q = state.question ?: return
        val correct = choseOption1 == q.correctIsOption1
        val newScore = state.score + if (correct) 1 else 0
        _uiState.value = state.copy(
            userChoseOption1 = choseOption1,
            isCorrect = correct,
            score = newScore,
        )
        viewModelScope.launch {
            delay(950L)
            val nextIndex = state.questionIndex + 1
            if (nextIndex >= questions.size) {
                repo.recordExercisePlay("grammar_imperative_g3")
            }
            loadQuestion(nextIndex)
        }
    }
}
