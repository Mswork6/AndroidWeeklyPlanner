package com.example.taskplanner.presentation.screens.main.week

sealed interface WeekScreenAction {
    data object SelectPreviousWeek : WeekScreenAction

    data object SelectNextWeek : WeekScreenAction

    data class SetCalendarVisibility(val opened: Boolean) : WeekScreenAction

    data class SetDate(val dateInMillis: Long) : WeekScreenAction
}
