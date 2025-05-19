package com.example.courseworkandroidweeklyplanner.presentation.screens.list.sorting

import com.example.courseworkandroidweeklyplanner.domain.model.SortType

sealed interface SearchListScreenAction {
    data class SetSorterVisibility(val opened: Boolean) : SearchListScreenAction

    data class SetSort(val sort: SortType) : SearchListScreenAction
}