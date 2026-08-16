package com.example.englishbg.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishbg.EnglishApp
import com.example.englishbg.data.ALL_CATEGORIES
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class ProgressSummary(
    val practiced: Int = 0,
    val total: Int = ALL_CATEGORIES.size,
    val threeStars: Int = 0,
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = (application as EnglishApp).activityRepository

    val progress: StateFlow<ProgressSummary> = repo.allStatsFlow()
        .map { stats ->
            ProgressSummary(
                practiced   = stats.values.count { it.timesPlayed > 0 },
                threeStars  = stats.values.count { it.stars == 3 },
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProgressSummary())
}
