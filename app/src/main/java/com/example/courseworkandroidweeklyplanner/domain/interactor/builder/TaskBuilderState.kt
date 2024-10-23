package com.example.courseworkandroidweeklyplanner.domain.interactor.builder

import com.example.courseworkandroidweeklyplanner.domain.model.Task

sealed interface TaskBuilderState {
    data object Initial : TaskBuilderState

    data class Default(val task: Task): TaskBuilderState

    data object Invalid : TaskBuilderState
}