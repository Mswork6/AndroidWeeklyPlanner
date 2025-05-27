package com.example.androidweeklyplanner.presentation.screens.list.sorting

import com.example.androidweeklyplanner.domain.model.SortType

sealed interface ListScreenActionsState {
    data object Initial : ListScreenActionsState

    data class Default(
        val sort: SortType,
        val isSorterVisible: Boolean
    ) : ListScreenActionsState
}