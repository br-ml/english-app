package com.example.englishbg

import android.app.Application
import com.example.englishbg.data.ActivityRepository

class EnglishApp : Application() {
    val activityRepository by lazy { ActivityRepository(this) }
}
