package com.example.taskplanner.presentation.screens.list.tasks

import com.example.taskplanner.domain.model.Task

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