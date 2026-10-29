package com.example.englishbg.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.englishbg.ui.category.CategoryPickerScreen
import com.example.englishbg.ui.home.Grade2HomeScreen
import com.example.englishbg.ui.home.HomeScreen
import com.example.englishbg.ui.listen.ListenScreen
import com.example.englishbg.ui.matching.MatchingScreen
import com.example.englishbg.ui.progress.ProgressScreen
import com.example.englishbg.ui.truefalse.TrueFalseScreen
import com.example.englishbg.ui.unscramble.UnscrambleScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Grade2Home : Screen("grade2_home")
    object CategoryPicker : Screen("category_picker/{grade}") {
        fun createRoute(grade: Int) = "category_picker/$grade"
        const val ARG_GRADE = "grade"
    }
    object MatchingRandom : Screen("matching_random/{grade}") {
        fun createRoute(grade: Int) = "matching_random/$grade"
        const val ARG_GRADE = "grade"
    }
    object MatchingCategory : Screen("matching_category/{grade}/{categoryIndex}") {
        fun createRoute(grade: Int, index: Int) = "matching_category/$grade/$index"
        const val ARG_GRADE = "grade"
        const val ARG = "categoryIndex"
    }
    object ListenRandom : Screen("listen_random/{grade}") {
        fun createRoute(grade: Int) = "listen_random/$grade"
        const val ARG_GRADE = "grade"
    }
    object ListenCategory : Screen("listen_category/{grade}/{categoryIndex}") {
        fun createRoute(grade: Int, index: Int) = "listen_category/$grade/$index"
        const val ARG_GRADE = "grade"
        const val ARG = "categoryIndex"
    }
    object TrueFalse : Screen("truefalse/{grade}") {
        fun createRoute(grade: Int) = "truefalse/$grade"
        const val ARG_GRADE = "grade"
    }
    object Unscramble : Screen("unscramble/{grade}") {
        fun createRoute(grade: Int) = "unscramble/$grade"
        const val ARG_GRADE = "grade"
    }
    object Progress : Screen("progress")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Home.route) {

        composable(Screen.Home.route) {
            HomeScreen(
                onGrade2      = { navController.navigate(Screen.Grade2Home.route) },
                onGrade3Match = { navController.navigate(Screen.MatchingRandom.createRoute(3)) },
                onGrade3Listen = { navController.navigate(Screen.ListenRandom.createRoute(3)) },
                onGrade3TrueFalse = { navController.navigate(Screen.TrueFalse.createRoute(3)) },
                onGrade3Unscramble = { navController.navigate(Screen.Unscramble.createRoute(3)) },
                onGrade3Category = { navController.navigate(Screen.CategoryPicker.createRoute(3)) },
                onProgress    = { navController.navigate(Screen.Progress.route) },
            )
        }

        composable(Screen.Grade2Home.route) {
            Grade2HomeScreen(
                onCategoryMode = { navController.navigate(Screen.CategoryPicker.createRoute(2)) },
                onRandomMode   = { navController.navigate(Screen.MatchingRandom.createRoute(2)) },
                onListenMode   = { navController.navigate(Screen.ListenRandom.createRoute(2)) },
                onTrueFalse    = { navController.navigate(Screen.TrueFalse.createRoute(2)) },
                onUnscramble   = { navController.navigate(Screen.Unscramble.createRoute(2)) },
                onProgress     = { navController.navigate(Screen.Progress.route) },
                onBack         = { navController.popBackStack() },
            )
        }

        composable(
            route = Screen.CategoryPicker.route,
            arguments = listOf(navArgument(Screen.CategoryPicker.ARG_GRADE) { type = NavType.IntType })
        ) { backEntry ->
            val grade = backEntry.arguments?.getInt(Screen.CategoryPicker.ARG_GRADE) ?: 2
            CategoryPickerScreen(
                grade = grade,
                onCategorySelected = { index ->
                    navController.navigate(Screen.MatchingCategory.createRoute(grade, index))
                },
                onBack = { navController.popBackStack() },
            )
        }

        composable(
            route = Screen.MatchingRandom.route,
            arguments = listOf(navArgument(Screen.MatchingRandom.ARG_GRADE) { type = NavType.IntType })
        ) {
            MatchingScreen(onBack = { navController.popBackStack() })
        }

        composable(
            route = Screen.MatchingCategory.route,
            arguments = listOf(
                navArgument(Screen.MatchingCategory.ARG_GRADE) { type = NavType.IntType },
                navArgument(Screen.MatchingCategory.ARG) { type = NavType.IntType },
            )
        ) {
            MatchingScreen(onBack = { navController.popBackStack() })
        }

        composable(
            route = Screen.ListenRandom.route,
            arguments = listOf(navArgument(Screen.ListenRandom.ARG_GRADE) { type = NavType.IntType })
        ) {
            ListenScreen(onBack = { navController.popBackStack() })
        }

        composable(
            route = Screen.ListenCategory.route,
            arguments = listOf(
                navArgument(Screen.ListenCategory.ARG_GRADE) { type = NavType.IntType },
                navArgument(Screen.ListenCategory.ARG) { type = NavType.IntType },
            )
        ) {
            ListenScreen(onBack = { navController.popBackStack() })
        }

        composable(
            route = Screen.TrueFalse.route,
            arguments = listOf(navArgument(Screen.TrueFalse.ARG_GRADE) { type = NavType.IntType })
        ) {
            TrueFalseScreen(onBack = { navController.popBackStack() })
        }

        composable(
            route = Screen.Unscramble.route,
            arguments = listOf(navArgument(Screen.Unscramble.ARG_GRADE) { type = NavType.IntType })
        ) {
            UnscrambleScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.Progress.route) {
            ProgressScreen(onBack = { navController.popBackStack() })
        }
    }
}
