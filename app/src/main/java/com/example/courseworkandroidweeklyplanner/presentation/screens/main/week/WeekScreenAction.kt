package com.example.courseworkandroidweeklyplanner.presentation.screens.main.week

sealed interface WeekScreenAction {
    data object SelectPreviousWeek : WeekScreenAction

    data object SelectNextWeek : WeekScreenAction
}
