package com.example.englishbg.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "activity")

val EXERCISE_KEYS = listOf("match_category", "match_random", "listen_random", "truefalse", "unscramble")

val EXERCISE_LABELS = mapOf(
    "match_category" to "Match by Category",
    "match_random"   to "Random Match",
    "listen_random"  to "Listen & Choose",
    "truefalse"      to "True or False",
    "unscramble"     to "Unscramble",
)

val EXERCISE_EMOJIS = mapOf(
    "match_category" to "🃏",
    "match_random"   to "🔀",
    "listen_random"  to "🎧",
    "truefalse"      to "✅",
    "unscramble"     to "🔤",
)

val GRADE3_EXERCISE_KEYS = listOf("match_category_g3", "match_random_g3", "listen_random_g3", "truefalse_g3", "unscramble_g3", "grammar_there_is_are_g3", "grammar_imperative_g3", "grammar_likes_g3")

val GRADE3_EXERCISE_LABELS = mapOf(
    "match_category_g3"        to "Match by Category",
    "match_random_g3"          to "Random Match",
    "listen_random_g3"         to "Listen & Choose",
    "truefalse_g3"             to "True or False",
    "unscramble_g3"            to "Unscramble",
    "grammar_there_is_are_g3"  to "Grammar: There is/are",
    "grammar_imperative_g3"    to "Grammar: Imperatives",
    "grammar_likes_g3"         to "Grammar: Likes/Doesn't like",
)

val GRADE3_EXERCISE_EMOJIS = mapOf(
    "match_category_g3"        to "🃏",
    "match_random_g3"          to "🔀",
    "listen_random_g3"         to "🎧",
    "truefalse_g3"             to "✅",
    "unscramble_g3"            to "🔤",
    "grammar_there_is_are_g3"  to "📝",
    "grammar_imperative_g3"    to "📝",
    "grammar_likes_g3"         to "🧡",
)

data class CategoryStats(
    val timesPlayed: Int = 0,
    val bestScore: Int = 0,
) {
    val stars: Int get() = when {
        timesPlayed == 0 -> 0
        bestScore >= 80   -> 3
        bestScore >= 60   -> 2
        else              -> 1
    }
}

class ActivityRepository(private val context: Context) {

    fun allStatsFlow(): Flow<Map<Int, CategoryStats>> = context.dataStore.data.map { prefs ->
        ALL_CATEGORIES.indices.associate { i ->
            i to CategoryStats(
                timesPlayed = prefs[intPreferencesKey("played_$i")] ?: 0,
                bestScore   = prefs[intPreferencesKey("best_$i")]   ?: 0,
            )
        }
    }

    fun exercisePlaysFlow(): Flow<Map<String, Int>> = context.dataStore.data.map { prefs ->
        EXERCISE_KEYS.associateWith { key ->
            prefs[intPreferencesKey("ex_$key")] ?: 0
        }
    }

    fun exercisePlaysGrade3Flow(): Flow<Map<String, Int>> = context.dataStore.data.map { prefs ->
        GRADE3_EXERCISE_KEYS.associateWith { key ->
            prefs[intPreferencesKey("ex_$key")] ?: 0
        }
    }

    suspend fun recordCompletion(categoryIndex: Int, scorePct: Int) {
        context.dataStore.edit { prefs ->
            val playedKey = intPreferencesKey("played_$categoryIndex")
            val bestKey   = intPreferencesKey("best_$categoryIndex")
            prefs[playedKey] = (prefs[playedKey] ?: 0) + 1
            val current = prefs[bestKey] ?: 0
            if (scorePct > current) prefs[bestKey] = scorePct
            val exKey = intPreferencesKey("ex_match_category")
            prefs[exKey] = (prefs[exKey] ?: 0) + 1
        }
    }

    suspend fun recordListenCompletion(categoryIndex: Int, scorePct: Int) {
        context.dataStore.edit { prefs ->
            val playedKey = intPreferencesKey("played_$categoryIndex")
            val bestKey   = intPreferencesKey("best_$categoryIndex")
            prefs[playedKey] = (prefs[playedKey] ?: 0) + 1
            val current = prefs[bestKey] ?: 0
            if (scorePct > current) prefs[bestKey] = scorePct
            val exKey = intPreferencesKey("ex_listen_random")
            prefs[exKey] = (prefs[exKey] ?: 0) + 1
        }
    }

    suspend fun recordExercisePlay(exerciseKey: String) {
        context.dataStore.edit { prefs ->
            val key = intPreferencesKey("ex_$exerciseKey")
            prefs[key] = (prefs[key] ?: 0) + 1
        }
    }

    suspend fun resetAll() {
        context.dataStore.edit { it.clear() }
    }
}
