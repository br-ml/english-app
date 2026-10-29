package com.example.englishbg.ui.listen

import android.app.Application
import android.speech.tts.TextToSpeech
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.englishbg.EnglishApp
import com.example.englishbg.data.ALL_CATEGORIES
import com.example.englishbg.data.ALL_WORDS
import com.example.englishbg.data.GRADE3_CATEGORIES
import com.example.englishbg.data.GRADE3_WORDS
import com.example.englishbg.data.WordPair
import com.example.englishbg.navigation.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

data class ListenUiState(
    val currentWord: WordPair? = null,
    val options: List<String> = emptyList(),
    val wrongOptions: Set<String> = emptySet(),
    val correctOption: String? = null,
    val isCorrect: Boolean? = null,
    val questionIndex: Int = 0,
    val totalQuestions: Int = 5,
    val correctCount: Int = 0,
    val isComplete: Boolean = false,
    val isTtsReady: Boolean = false,
    val categoryName: String = "",
)

class ListenViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle,
) : AndroidViewModel(application) {

    private val categoryIndex: Int? = savedStateHandle.get<Int>(Screen.ListenCategory.ARG)
    private val grade: Int = savedStateHandle.get<Int>("grade") ?: 2
    private val categories = if (grade == 3) GRADE3_CATEGORIES else ALL_CATEGORIES
    private val allWords = if (grade == 3) GRADE3_WORDS else ALL_WORDS
    private val repo = (application as EnglishApp).activityRepository
    private var roundWords: List<WordPair> = emptyList()
    private lateinit var tts: TextToSpeech

    private val _uiState = MutableStateFlow(ListenUiState())
    val uiState: StateFlow<ListenUiState> = _uiState.asStateFlow()

    init {
        tts = TextToSpeech(application) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.language = Locale.US
                tts.setSpeechRate(0.49f)
                _uiState.value = _uiState.value.copy(isTtsReady = true)
            }
        }
        loadRound()
    }

    fun loadRound() {
        val (words, catName) = when (categoryIndex) {
            null -> allWords.shuffled().take(5) to "Listen & Choose"
            else -> {
                val cat = categories[categoryIndex]
                cat.words.shuffled().take(minOf(5, cat.words.size)) to cat.name
            }
        }
        roundWords = words
        _uiState.value = ListenUiState(
            totalQuestions = words.size,
            categoryName = catName,
            isTtsReady = _uiState.value.isTtsReady,
        )
        loadQuestion(0)
    }

    private fun loadQuestion(index: Int) {
        if (index >= roundWords.size) {
            _uiState.value = _uiState.value.copy(isComplete = true)
            return
        }
        val correct = roundWords[index]
        val distractors = allWords
            .filter { it.english != correct.english }
            .shuffled()
            .take(3)
        val options = (listOf(correct) + distractors)
            .shuffled()
            .map { it.english.lowercase() }

        _uiState.value = _uiState.value.copy(
            currentWord = correct,
            options = options,
            wrongOptions = emptySet(),
            correctOption = null,
            isCorrect = null,
            questionIndex = index,
        )
    }

    fun speak() {
        val word = _uiState.value.currentWord?.english ?: return
        if (_uiState.value.isTtsReady) {
            tts.speak(word, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    fun onOptionSelected(option: String) {
        val state = _uiState.value
        if (option in state.wrongOptions || state.correctOption != null || state.isComplete) return
        val correct = state.currentWord?.english?.lowercase() == option
        if (correct) {
            val firstAttempt = state.wrongOptions.isEmpty()
            val newCorrectCount = state.correctCount + if (firstAttempt) 1 else 0
            _uiState.value = state.copy(
                correctOption = option,
                isCorrect = true,
                correctCount = newCorrectCount,
            )
            viewModelScope.launch {
                delay(900L)
                val nextIndex = state.questionIndex + 1
                if (nextIndex >= roundWords.size) {
                    val score = (newCorrectCount * 100) / roundWords.size
                    if (categoryIndex != null) {
                        if (grade == 3) repo.recordExercisePlay("listen_random_g3")
                        else repo.recordListenCompletion(categoryIndex, score)
                    } else {
                        repo.recordExercisePlay(if (grade == 3) "listen_random_g3" else "listen_random")
                    }
                }
                loadQuestion(nextIndex)
            }
        } else {
            _uiState.value = state.copy(
                wrongOptions = state.wrongOptions + option,
            )
        }
    }

    override fun onCleared() {
        tts.stop()
        tts.shutdown()
        super.onCleared()
    }
}
