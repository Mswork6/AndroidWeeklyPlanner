package com.example.taskplanner.presentation.screens.filter

import com.example.taskplanner.domain.model.Category
import com.example.taskplanner.domain.model.Difficulty
import com.example.taskplanner.domain.model.Priority
import com.example.taskplanner.domain.model.SortType
import java.time.LocalDate

sealed interface FilterScreenState {
    data object Initial : FilterScreenState

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
    ) : FilterScreenState
}