package com.example.androidweeklyplanner.presentation.screens.main.sorting

import com.example.androidweeklyplanner.domain.model.SortType

sealed interface MainScreenAction {
    data class SetSorterVisibility(val opened: Boolean) : MainScreenAction

    data class SetSort(val sort: SortType) : MainScreenAction
}