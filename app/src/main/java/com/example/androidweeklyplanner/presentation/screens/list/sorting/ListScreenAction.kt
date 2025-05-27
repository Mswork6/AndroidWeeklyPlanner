package com.example.androidweeklyplanner.presentation.screens.list.sorting

import com.example.androidweeklyplanner.domain.model.SortType

sealed interface ListScreenAction {
    data class SetSorterVisibility(val opened: Boolean) : ListScreenAction

    data class SetSort(val sort: SortType) : ListScreenAction
}