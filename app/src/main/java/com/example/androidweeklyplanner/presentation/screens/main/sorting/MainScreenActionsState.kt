package com.example.androidweeklyplanner.presentation.screens.main.sorting

import com.example.androidweeklyplanner.domain.model.SortConfig
import com.example.androidweeklyplanner.domain.model.SortType

sealed interface MainScreenActionsState {
    data object Initial : MainScreenActionsState

    data class Default(
        val sortConfig: SortConfig,
        val isSorterVisible: Boolean
    ) : MainScreenActionsState
}
