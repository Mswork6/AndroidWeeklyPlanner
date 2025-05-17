package com.example.courseworkandroidweeklyplanner.presentation.screens.main.week

import com.example.courseworkandroidweeklyplanner.domain.model.Week

sealed interface WeekScreenState {
    data object Initial : WeekScreenState

    data class Default(
        val week: Week,
        val isCalendarVisible: Boolean
    ) : WeekScreenState
}
