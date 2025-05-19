package com.example.courseworkandroidweeklyplanner.presentation.screens.list.tasks

import com.example.courseworkandroidweeklyplanner.domain.model.Task

sealed interface TasksListScreenState {
    object Initial : TasksListScreenState

    data class Default(
        val tasks: List<Task>,
        val dialogState: TaskDialogState
    ) : TasksListScreenState

    sealed interface TaskDialogState {
        object None : TaskDialogState
        data class Opened(val task: Task) : TaskDialogState
    }
}