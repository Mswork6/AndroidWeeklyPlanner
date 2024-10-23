package com.example.courseworkandroidweeklyplanner.presentation.screens.main.tasks

import com.example.courseworkandroidweeklyplanner.domain.model.Day
import com.example.courseworkandroidweeklyplanner.domain.model.Task

sealed interface TasksScreenAction {
    data class ExpandDay(val day: Day) : TasksScreenAction

    sealed interface TaskDialogAction : TasksScreenAction {
        data class Open(val task: Task) : TaskDialogAction

        data object Close : TaskDialogAction
    }

    data class ToggleTaskStatus(val task: Task) : TasksScreenAction

    data class DeleteTask(val task: Task) : TasksScreenAction
}