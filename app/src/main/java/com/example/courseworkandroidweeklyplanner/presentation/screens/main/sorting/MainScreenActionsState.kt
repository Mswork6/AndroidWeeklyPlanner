package com.example.courseworkandroidweeklyplanner.presentation.screens.main.sorting

import com.example.courseworkandroidweeklyplanner.domain.model.SortType

sealed interface MainScreenActionsState {
    data object Initial : MainScreenActionsState

    data class Default(
        val sort: SortType,
        val isSorterVisible: Boolean
    ) : MainScreenActionsState
}
