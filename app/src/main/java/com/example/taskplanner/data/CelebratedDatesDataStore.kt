package com.example.taskplanner.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

private val Context.dataStore by preferencesDataStore(
    name = "celebrated_dates"
)

class CelebratedDatesDataStore(private val context: Context) {
    companion object {
        private val DATES_KEY = stringSetPreferencesKey("celebrated_dates_set")
    }

    val celebratedDatesFlow: Flow<Set<LocalDate>> = context.dataStore.data
        .map { prefs ->
            prefs[DATES_KEY]
                ?.mapNotNull { runCatching { LocalDate.parse(it) }.getOrNull() }
                ?.toSet()
                ?: emptySet()
        }

    suspend fun markDateCelebrated(date: LocalDate) {
        context.dataStore.edit { prefs ->
            val current = prefs[DATES_KEY] ?: emptySet()
            prefs[DATES_KEY] = current + date.toString()
        }
    }

    suspend fun unmarkDateCelebrated(date: LocalDate) {
        context.dataStore.edit { prefs ->
            val current = prefs[DATES_KEY] ?: emptySet()
            prefs[DATES_KEY] = current - date.toString()
        }
    }
}