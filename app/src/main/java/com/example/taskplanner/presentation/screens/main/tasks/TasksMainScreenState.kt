package com.example.taskplanner.presentation.screens.main.tasks

import com.example.taskplanner.domain.model.Day
import com.example.taskplanner.domain.model.Task
import java.time.LocalDate

sealed interface TasksMainScreenState {
    data object Initial : TasksMainScreenState

    data class Default(
        val days: List<Day>,
        val celebratedDates: Set<LocalDate>,
        val playingDates: Set<LocalDate>,
        val dialogState: TaskScreenDialogState
    ) : TasksMainScreenState

    sealed interface TaskScreenDialogState {
        data object None : TaskScreenDialogState

        data class Opened(val task: Task) : TaskScreenDialogState
    }
}