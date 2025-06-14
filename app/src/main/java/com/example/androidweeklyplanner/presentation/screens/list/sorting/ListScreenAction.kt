package com.example.androidweeklyplanner.presentation.screens.list.sorting

import com.example.androidweeklyplanner.domain.model.Category
import com.example.androidweeklyplanner.domain.model.Difficulty
import com.example.androidweeklyplanner.domain.model.Priority
import com.example.androidweeklyplanner.domain.model.SortType
import java.time.LocalDate

sealed interface ListScreenAction {
    data class SetStartDate(val startDate: LocalDate?) : ListScreenAction
    data class SetEndDate(val endDate: LocalDate?) : ListScreenAction
    data class SetPriorityFilter(val priorityFilter: Set<Priority>) : ListScreenAction
    data class SetDifficultyFilter(val difficultyFilter: Set<Difficulty>) : ListScreenAction
    data class SetCategoryFilter(val categoryFilter: Set<Category>) : ListScreenAction
    data class SetPriorityOrder(val priorityOrder: SortType) : ListScreenAction
    data class SetDifficultyOrder(val difficultyOrder: SortType) : ListScreenAction
    data class SetPriorityPickerVisibility(val opened: Boolean) : ListScreenAction
    data class SetDifficultyPickerVisibility(val opened: Boolean) : ListScreenAction
    data class SetCategoryPickerVisibility(val opened: Boolean) : ListScreenAction
    data object Save : ListScreenAction
}