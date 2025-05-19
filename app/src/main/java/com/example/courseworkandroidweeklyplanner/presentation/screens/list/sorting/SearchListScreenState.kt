package com.example.courseworkandroidweeklyplanner.presentation.screens.list.sorting

import com.example.courseworkandroidweeklyplanner.domain.model.SortType

sealed interface SearchListScreenState {
    data object Initial : SearchListScreenState

    data class Default(
        val sort: SortType,
        val isSorterVisible: Boolean
    ) : SearchListScreenState
}