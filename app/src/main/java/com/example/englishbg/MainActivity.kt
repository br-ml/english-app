package com.example.englishbg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.englishbg.navigation.AppNavigation
import com.example.englishbg.ui.theme.EnglishBGTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EnglishBGTheme {
                AppNavigation()
            }
        }
    }
}
