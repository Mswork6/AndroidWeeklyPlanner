package com.example.taskplanner.presentation.screens.main.week

import com.example.taskplanner.domain.model.Week

sealed interface WeekScreenState {
    data object Initial : WeekScreenState

    data class Default(
        val week: Week,
        val isCalendarVisible: Boolean
    ) : WeekScreenState
}
