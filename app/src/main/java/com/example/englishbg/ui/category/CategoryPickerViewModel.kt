package com.example.englishbg.ui.category

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishbg.EnglishApp
import com.example.englishbg.data.CategoryStats
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class CategoryPickerViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = (application as EnglishApp).activityRepository

    val allStats: StateFlow<Map<Int, CategoryStats>> = repo.allStatsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())
}
