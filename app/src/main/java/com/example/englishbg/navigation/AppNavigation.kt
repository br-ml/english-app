package com.example.englishbg.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.englishbg.ui.category.CategoryPickerScreen
import com.example.englishbg.ui.home.HomeScreen
import com.example.englishbg.ui.listen.ListenScreen
import com.example.englishbg.ui.matching.MatchingScreen
import com.example.englishbg.ui.progress.ProgressScreen
import com.example.englishbg.ui.truefalse.TrueFalseScreen
import com.example.englishbg.ui.unscramble.UnscrambleScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object CategoryPicker : Screen("category_picker")
    object ListenCategoryPicker : Screen("listen_category_picker")
    object MatchingRandom : Screen("matching_random")
    object MatchingCategory : Screen("matching_category/{categoryIndex}") {
        fun createRoute(index: Int) = "matching_category/$index"
        const val ARG = "categoryIndex"
    }
    object ListenRandom : Screen("listen_random")
    object ListenCategory : Screen("listen_category/{categoryIndex}") {
        fun createRoute(index: Int) = "listen_category/$index"
        const val ARG = "categoryIndex"
    }
    object TrueFalse : Screen("truefalse")
    object Unscramble : Screen("unscramble")
    object Progress : Screen("progress")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Home.route) {

        composable(Screen.Home.route) {
            HomeScreen(
                onCategoryMode  = { navController.navigate(Screen.CategoryPicker.route) },
                onRandomMode    = { navController.navigate(Screen.MatchingRandom.route) },
                onListenMode    = { navController.navigate(Screen.ListenRandom.route) },
                onTrueFalse     = { navController.navigate(Screen.TrueFalse.route) },
                onUnscramble    = { navController.navigate(Screen.Unscramble.route) },
                onProgress      = { navController.navigate(Screen.Progress.route) },
            )
        }

        composable(Screen.CategoryPicker.route) {
            CategoryPickerScreen(
                onCategorySelected = { index ->
                    navController.navigate(Screen.MatchingCategory.createRoute(index))
                },
                onBack = { navController.popBackStack() },
            )
        }

        composable(Screen.MatchingRandom.route) {
            MatchingScreen(onBack = { navController.popBackStack() })
        }

        composable(
            route = Screen.MatchingCategory.route,
            arguments = listOf(navArgument(Screen.MatchingCategory.ARG) { type = NavType.IntType })
        ) {
            MatchingScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.ListenRandom.route) {
            ListenScreen(onBack = { navController.popBackStack() })
        }

        composable(
            route = Screen.ListenCategory.route,
            arguments = listOf(navArgument(Screen.ListenCategory.ARG) { type = NavType.IntType })
        ) {
            ListenScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.TrueFalse.route) {
            TrueFalseScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.Unscramble.route) {
            UnscrambleScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.Progress.route) {
            ProgressScreen(onBack = { navController.popBackStack() })
        }
    }
}
