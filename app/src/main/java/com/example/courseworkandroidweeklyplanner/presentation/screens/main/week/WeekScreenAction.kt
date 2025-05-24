package com.example.courseworkandroidweeklyplanner.presentation.screens.main.week

import java.time.LocalDate

sealed interface WeekScreenAction {
    data object SelectPreviousWeek : WeekScreenAction

    data object SelectNextWeek : WeekScreenAction

    data class SetCalendarVisibility(val opened: Boolean) : WeekScreenAction

    data class SetDate(val date: LocalDate) : WeekScreenAction
}
