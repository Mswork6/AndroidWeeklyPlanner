package com.example.taskplanner.presentation.screens.main.tasks

import com.example.taskplanner.domain.model.Day
import com.example.taskplanner.domain.model.Task
import java.time.LocalDate

sealed interface TasksMainScreenAction {
    data class ExpandDay(val day: Day) : TasksMainScreenAction

    sealed interface TaskDialogAction : TasksMainScreenAction {
        data class Open(val task: Task) : TaskDialogAction

        data object Close : TaskDialogAction
    }

    data class ToggleTaskStatus(val task: Task) : TasksMainScreenAction

    data class DeleteTask(val task: Task) : TasksMainScreenAction

    data class StopEncouragingAnimation(val date: LocalDate) : TasksMainScreenAction
}