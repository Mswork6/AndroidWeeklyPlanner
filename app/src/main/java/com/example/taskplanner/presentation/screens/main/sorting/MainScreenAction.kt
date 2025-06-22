package com.example.taskplanner.presentation.screens.main.sorting

import com.example.taskplanner.domain.model.SortConfig

sealed interface MainScreenAction {
    data class SetSorterVisibility(val opened: Boolean) : MainScreenAction

    data class SetSort(val sortConfig: SortConfig) : MainScreenAction
}