package com.example.courseworkandroidweeklyplanner.presentation.screens.main.sorting

import com.example.courseworkandroidweeklyplanner.domain.model.SortType

sealed interface SearchScreenState {
    data object Initial : SearchScreenState

    data class Default(
        val sort: SortType,
        val isSorterVisible: Boolean
    ) : SearchScreenState
}
