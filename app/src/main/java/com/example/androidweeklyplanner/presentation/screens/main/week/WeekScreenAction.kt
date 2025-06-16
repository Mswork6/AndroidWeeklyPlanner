package com.example.androidweeklyplanner.presentation.screens.main.week

import java.time.LocalDate

sealed interface WeekScreenAction {
    data object SelectPreviousWeek : WeekScreenAction

    data object SelectNextWeek : WeekScreenAction

    data class SetCalendarVisibility(val opened: Boolean) : WeekScreenAction

    data class SetDate(val dateInMillis: Long) : WeekScreenAction
}
