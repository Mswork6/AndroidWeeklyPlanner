package com.example.androidweeklyplanner.domain.interactor.builder

import com.example.androidweeklyplanner.domain.model.Task

sealed interface TaskBuilderState {
    data object Initial : TaskBuilderState

    data class Default(val task: Task): TaskBuilderState

    data object Invalid : TaskBuilderState
}