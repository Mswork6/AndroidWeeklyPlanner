package com.example.androidweeklyplanner.presentation.screens.filter

import com.example.androidweeklyplanner.domain.model.Category
import com.example.androidweeklyplanner.domain.model.Difficulty
import com.example.androidweeklyplanner.domain.model.Priority
import com.example.androidweeklyplanner.domain.model.SortType
import java.time.LocalDate

sealed interface FilterScreenActions {
    data class SetDateFilter(val startDate: Long?, val endDate: Long?) : FilterScreenActions
    data class SetPriorityFilter(val priorityFilter: Set<Priority>) : FilterScreenActions
    data class SetDifficultyFilter(val difficultyFilter: Set<Difficulty>) : FilterScreenActions
    data class SetCategoryFilter(val categoryFilter: Set<Category>) : FilterScreenActions
    data class SetPriorityOrder(val priorityOrder: SortType) : FilterScreenActions
    data class SetDifficultyOrder(val difficultyOrder: SortType) : FilterScreenActions
    data class SetDatePickerVisibility(val opened: Boolean) : FilterScreenActions
    data class SetPriorityPickerVisibility(val opened: Boolean) : FilterScreenActions
    data class SetDifficultyPickerVisibility(val opened: Boolean) : FilterScreenActions
    data class SetCategoryPickerVisibility(val opened: Boolean) : FilterScreenActions
    data object ResetValues : FilterScreenActions
    data object Save : FilterScreenActions
}