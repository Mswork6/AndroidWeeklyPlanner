package com.example.courseworkandroidweeklyplanner.presentation.screens.main.tasks

import com.example.courseworkandroidweeklyplanner.domain.model.Day
import com.example.courseworkandroidweeklyplanner.domain.model.Task

sealed interface TasksScreenState {
    data object Initial : TasksScreenState

    data class Default(
        val days: List<Day>,
        val dialogState: TaskScreenDialogState
    ) : TasksScreenState

    sealed interface TaskScreenDialogState {
        data object None : TaskScreenDialogState

        data class Opened(val task: Task) : TaskScreenDialogState
    }
}