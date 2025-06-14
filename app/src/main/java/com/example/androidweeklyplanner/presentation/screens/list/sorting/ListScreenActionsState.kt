package com.example.androidweeklyplanner.presentation.screens.list.sorting

import com.example.androidweeklyplanner.domain.model.Category
import com.example.androidweeklyplanner.domain.model.Difficulty
import com.example.androidweeklyplanner.domain.model.Priority
import com.example.androidweeklyplanner.domain.model.SortType
import java.time.LocalDate

sealed interface ListScreenActionsState {
    data object Initial : ListScreenActionsState

    data class Default(
        val startDate: LocalDate?,
        val endDate: LocalDate?,
        val selectedPriorities: Set<Priority>,
        val selectedDifficulties: Set<Difficulty>,
        val selectedCategories: Set<Category>,
        val sortPriorityOrder: SortType,
        val sortDifficultyOrder: SortType,
        val isDatePickerOpened: Boolean,
        val isPriorityFilterDialogOpened: Boolean,
        val isDifficultyFilterDialogOpened: Boolean,
        val isCategoryFilterDialogOpened: Boolean,
        val isSortDialogOpened: Boolean,
    ) : ListScreenActionsState
}