package com.example.courseworkandroidweeklyplanner.presentation.screens.list.sorting

import com.example.courseworkandroidweeklyplanner.domain.model.SortType

sealed interface ListScreenActionsState {
    data object Initial : ListScreenActionsState

    data class Default(
        val sort: SortType,
        val isSorterVisible: Boolean
    ) : ListScreenActionsState
}