package com.example.courseworkandroidweeklyplanner.presentation

import androidx.annotation.StringRes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.courseworkandroidweeklyplanner.R
import com.example.courseworkandroidweeklyplanner.domain.model.DayType
import com.example.courseworkandroidweeklyplanner.domain.model.Priority
import com.example.courseworkandroidweeklyplanner.domain.model.SortType
import com.example.courseworkandroidweeklyplanner.domain.model.Week
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
private val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

fun dateToString(localDate: LocalDate): String = localDate.format(dateFormatter)

fun timeToString(time: LocalTime): String = time.format(timeFormatter)

fun convertToLocalDate(dateMillis: Long): LocalDate = Instant
    .ofEpochMilli(dateMillis)
    .atZone(ZoneId.systemDefault())
    .toLocalDate()

fun dateToString(week: Week): String =
    "${dateToString(week.start)} - " +
            dateToString(week.end)

@OptIn(ExperimentalMaterial3Api::class)
object PastOrPresentSelectableDates : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        val selectedDate = Instant.ofEpochMilli(utcTimeMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        val today = LocalDate.now()
        return !selectedDate.isBefore(today)
    }

    override fun isSelectableYear(year: Int): Boolean {
        return year >= LocalDate.now().year
    }
}

val NavBackStackEntry.lifecycleIsResumed: Boolean
    get() = this.lifecycle.currentState == Lifecycle.State.RESUMED

fun NavController.atomicBack(entry: NavBackStackEntry) {
    if (entry.lifecycleIsResumed) {
        this.popBackStack()
    }
}

@get:StringRes
val Priority.description: Int
    get() = when (this) {
        Priority.HIGH -> R.string.description_high
        Priority.BASIC -> R.string.description_basic
        Priority.LOW -> R.string.description_low
    }

@get:StringRes
val SortType.description: Int
    get() = when (this) {
        SortType.INCREASE -> R.string.description_sort_priority_ascending
        SortType.DECREASE -> R.string.description_sort_priority_descending
        SortType.STANDARD -> R.string.description_no_sorting
    }

@get:StringRes
val DayType.description: Int
    get() = when (this) {
        DayType.MONDAY -> R.string.day_monday
        DayType.TUESDAY -> R.string.day_tuesday
        DayType.WEDNESDAY -> R.string.day_wednesday
        DayType.THURSDAY -> R.string.day_thursday
        DayType.FRIDAY -> R.string.day_friday
        DayType.SATURDAY -> R.string.day_saturday
        DayType.SUNDAY -> R.string.day_sunday
    }

suspend inline fun <T, R> Iterable<T>.asyncMap(
    crossinline transform: suspend (value: T) -> R
): Iterable<R> = this.map { value ->
    coroutineScope {
        async {
            transform(value)
        }
    }
}.awaitAll().asIterable()
