package com.example.courseworkandroidweeklyplanner.presentation.screens.list.tasks

import com.example.courseworkandroidweeklyplanner.domain.model.Task

sealed interface TasksListScreenAction {
    /** Открыть/закрыть диалог по задаче */
    sealed interface TaskDialogAction : TasksListScreenAction {
        data class Open(val task: Task) : TaskDialogAction
        object Close : TaskDialogAction
    }

    data class ToggleTaskStatus(val task: Task) : TasksListScreenAction
    data class DeleteTask(val task: Task) : TasksListScreenAction
}