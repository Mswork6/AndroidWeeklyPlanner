package com.example.courseworkandroidweeklyplanner.presentation.screens.main.sorting

import com.example.courseworkandroidweeklyplanner.domain.model.SortType
import java.time.LocalDate

sealed interface SearchScreenAction {
    data class SetSorterVisibility(val opened: Boolean) : SearchScreenAction

    data class SetSort(val sort: SortType) : SearchScreenAction
}