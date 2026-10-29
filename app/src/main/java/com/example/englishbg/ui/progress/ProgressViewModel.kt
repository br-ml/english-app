package com.example.englishbg.ui.progress

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishbg.EnglishApp
import com.example.englishbg.data.ALL_CATEGORIES
import com.example.englishbg.data.CategoryStats
import com.example.englishbg.data.EXERCISE_EMOJIS
import com.example.englishbg.data.EXERCISE_KEYS
import com.example.englishbg.data.EXERCISE_LABELS
import com.example.englishbg.data.GRADE3_EXERCISE_EMOJIS
import com.example.englishbg.data.GRADE3_EXERCISE_KEYS
import com.example.englishbg.data.GRADE3_EXERCISE_LABELS
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CategoryProgress(
    val index: Int,
    val name: String,
    val emoji: String,
    val stats: CategoryStats,
)

data class ExerciseStat(
    val key: String,
    val label: String,
    val emoji: String,
    val timesPlayed: Int,
)

data class ProgressUiState(
    val categories: List<CategoryProgress> = emptyList(),
    val grade2Exercises: List<ExerciseStat> = emptyList(),
    val grade3Exercises: List<ExerciseStat> = emptyList(),
)

class ProgressViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = (application as EnglishApp).activityRepository

    private val emojis = listOf(
        "🌈", "🔢", "📅", "⛅", "🍂",
        "👨‍👩‍👧", "🐾", "🎮", "🏫", "👋",
        "📍", "🔷", "🍎", "🥗", "🌱",
        "⚽", "✨", "😊", "🌟",
        "📝", "👤", "🏷️",
    )

    val uiState: StateFlow<ProgressUiState> = combine(
        repo.allStatsFlow(),
        repo.exercisePlaysFlow(),
        repo.exercisePlaysGrade3Flow(),
    ) { statsMap, exercisePlays, exercisePlaysG3 ->
        ProgressUiState(
            categories = ALL_CATEGORIES.mapIndexed { i, cat ->
                CategoryProgress(
                    index = i,
                    name  = cat.name,
                    emoji = emojis.getOrElse(i) { "📖" },
                    stats = statsMap[i] ?: CategoryStats(),
                )
            },
            grade2Exercises = EXERCISE_KEYS.map { key ->
                ExerciseStat(
                    key         = key,
                    label       = EXERCISE_LABELS[key] ?: key,
                    emoji       = EXERCISE_EMOJIS[key] ?: "🎯",
                    timesPlayed = exercisePlays[key] ?: 0,
                )
            },
            grade3Exercises = GRADE3_EXERCISE_KEYS.map { key ->
                ExerciseStat(
                    key         = key,
                    label       = GRADE3_EXERCISE_LABELS[key] ?: key,
                    emoji       = GRADE3_EXERCISE_EMOJIS[key] ?: "🎯",
                    timesPlayed = exercisePlaysG3[key] ?: 0,
                )
            },
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProgressUiState())

    fun resetAll() {
        viewModelScope.launch { repo.resetAll() }
    }
}
