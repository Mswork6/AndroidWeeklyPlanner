package com.example.androidweeklyplanner.presentation.screens.main.week

import com.example.androidweeklyplanner.domain.model.Week

sealed interface WeekScreenState {
    data object Initial : WeekScreenState

    data class Default(
        val week: Week,
        val isCalendarVisible: Boolean
    ) : WeekScreenState
}
