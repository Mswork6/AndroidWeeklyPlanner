package com.example.courseworkandroidweeklyplanner.presentation.screens.task

import com.example.courseworkandroidweeklyplanner.domain.model.Priority

sealed interface TaskScreenAction {
    data class SetName(val name: String) : TaskScreenAction

    data class SetDescription(val description: String) : TaskScreenAction

    data class SetDate(val dateInMillis: Long) : TaskScreenAction

    data class SetPriority(val priority: Priority) : TaskScreenAction

    data class SetTime(val hour: Int?, val minute: Int?) : TaskScreenAction

    data class SetDatePickerVisibility(val opened: Boolean) : TaskScreenAction

    data class SetPriorityPickerVisibility(val opened: Boolean) : TaskScreenAction

    data class SetTimePickerVisibility(val opened: Boolean) : TaskScreenAction

    data object Save : TaskScreenAction
}