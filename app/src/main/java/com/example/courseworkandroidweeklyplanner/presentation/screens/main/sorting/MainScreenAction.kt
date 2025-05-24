package com.example.courseworkandroidweeklyplanner.presentation.screens.main.sorting

import com.example.courseworkandroidweeklyplanner.domain.model.SortType

sealed interface MainScreenAction {
    data class SetSorterVisibility(val opened: Boolean) : MainScreenAction

    data class SetSort(val sort: SortType) : MainScreenAction
}