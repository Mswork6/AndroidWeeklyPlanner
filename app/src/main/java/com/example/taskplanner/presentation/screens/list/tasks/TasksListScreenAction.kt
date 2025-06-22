package com.example.taskplanner.presentation.screens.list.tasks

import com.example.taskplanner.domain.model.Task

sealed interface TasksListScreenAction {
    sealed interface TaskDialogAction : TasksListScreenAction {
        data class Open(val task: Task) : TaskDialogAction
        object Close : TaskDialogAction
    }

    data class ToggleTaskStatus(val task: Task) : TasksListScreenAction
    data class DeleteTask(val task: Task) : TasksListScreenAction
}