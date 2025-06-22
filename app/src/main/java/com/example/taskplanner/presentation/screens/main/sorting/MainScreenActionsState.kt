package com.example.taskplanner.presentation.screens.main.sorting

import com.example.taskplanner.domain.model.SortConfig

sealed interface MainScreenActionsState {
    data object Initial : MainScreenActionsState

    data class Default(
        val sortConfig: SortConfig,
        val isSorterVisible: Boolean
    ) : MainScreenActionsState
}
