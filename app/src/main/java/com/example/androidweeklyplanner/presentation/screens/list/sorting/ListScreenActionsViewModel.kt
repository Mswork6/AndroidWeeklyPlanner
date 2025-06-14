package com.example.androidweeklyplanner.presentation.screens.list.sorting

import androidx.lifecycle.viewModelScope
import com.example.androidweeklyplanner.domain.NotificationEventBus
import com.example.androidweeklyplanner.domain.model.Category
import com.example.androidweeklyplanner.domain.model.Difficulty
import com.example.androidweeklyplanner.domain.model.Priority
import com.example.androidweeklyplanner.domain.model.SortType
import com.example.androidweeklyplanner.domain.repository.FilterRepository
import com.example.androidweeklyplanner.domain.repository.SortRepository
import com.example.androidweeklyplanner.presentation.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ListScreenActionsViewModel @Inject constructor(
    private val filterRepository: FilterRepository,
    private val sortRepository: SortRepository,
    private val notificationEventBus: NotificationEventBus,
) : BaseViewModel<ListScreenActionsState, ListScreenAction>() {
    private val _state: MutableStateFlow<ListScreenActionsState> =
        MutableStateFlow(ListScreenActionsState.Initial)
    override val state: StateFlow<ListScreenActionsState> = _state.asStateFlow()


    init {
        viewModelScope.launch {
            combine(
                filterRepository.getFilterConfig(),
                sortRepository.getSortConfig()
            ) { filterConfig, sortConfig ->
                ListScreenActionsState.Default(
                    startDate = filterConfig.startDate,
                    endDate = filterConfig.endDate,
                    selectedPriorities = filterConfig.priorityFilter,
                    selectedDifficulties = filterConfig.difficultyFilter,
                    selectedCategories = filterConfig.categoryFilter,
                    sortPriorityOrder = sortConfig.priorityOrder,
                    sortDifficultyOrder = sortConfig.difficultyOrder,
                    isDatePickerOpened = false,
                    isPriorityFilterDialogOpened = false,
                    isDifficultyFilterDialogOpened = false,
                    isCategoryFilterDialogOpened = false,
                    isSortDialogOpened = false
                )

            }.collect { screenState ->
                _state.update { screenState }
            }
        }

        // Подписка на событие уведомления
        viewModelScope.launch {
            notificationEventBus.events.collect {
                setPriorityPickerVisibility(false)
                setDifficultyPickerVisibility(false)
                setCategoryPickerVisibility(false)
            }
        }
    }

    override suspend fun execute(action: ListScreenAction) = when (action) {
        is ListScreenAction.SetStartDate -> setStartDate(action.startDate)
        is ListScreenAction.SetEndDate -> setEndDate(action.endDate)
        is ListScreenAction.SetPriorityFilter -> setPriorityFilter(action.priorityFilter)
        is ListScreenAction.SetDifficultyFilter -> setDifficultyFilter(action.difficultyFilter)
        is ListScreenAction.SetCategoryFilter -> setCategoryFilter(action.categoryFilter)
        is ListScreenAction.SetPriorityOrder -> setPriorityOrder(action.priorityOrder)
        is ListScreenAction.SetDifficultyOrder -> setDifficultyOrder(action.difficultyOrder)
        is ListScreenAction.SetPriorityPickerVisibility -> setPriorityPickerVisibility(action.opened)
        is ListScreenAction.SetDifficultyPickerVisibility -> setDifficultyPickerVisibility(action.opened)
        is ListScreenAction.SetCategoryPickerVisibility -> setCategoryPickerVisibility(action.opened)
        is ListScreenAction.Save -> save()


    }

    private fun setStartDate(startDate: LocalDate?) {
        _state.update {
            when (it) {
                is ListScreenActionsState.Default -> it.copy(startDate = startDate)
                else -> it
            }
        }
    }

    private fun setEndDate(endDate: LocalDate?) {
        _state.update {
            when (it) {
                is ListScreenActionsState.Default -> it.copy(endDate = endDate)
                else -> it
            }
        }
    }

    private fun setPriorityFilter(priorityFilter: Set<Priority>) {
        _state.update {
            when (it) {
                is ListScreenActionsState.Default -> it.copy(selectedPriorities = priorityFilter)
                else -> it
            }
        }
    }

    private fun setDifficultyFilter(difficultyFilter: Set<Difficulty>) {
        _state.update {
            when (it) {
                is ListScreenActionsState.Default -> it.copy(selectedDifficulties = difficultyFilter)
                else -> it
            }
        }
    }

    private fun setCategoryFilter(categoryFilter: Set<Category>) {
        _state.update {
            when (it) {
                is ListScreenActionsState.Default -> it.copy(selectedCategories = categoryFilter)
                else -> it
            }
        }
    }

    private fun setPriorityOrder(priorityOrder: SortType) {
        _state.update {
            when (it) {
                is ListScreenActionsState.Default -> it.copy(sortPriorityOrder = priorityOrder)
                else -> it
            }
        }
    }

    private fun setDifficultyOrder(difficultyOrder: SortType) {
        _state.update {
            when (it) {
                is ListScreenActionsState.Default -> it.copy(sortDifficultyOrder = difficultyOrder)
                else -> it
            }
        }
    }

    private fun setPriorityPickerVisibility(opened: Boolean) {
        _state.update {
            when (it) {
                is ListScreenActionsState.Default -> it.copy(isPriorityFilterDialogOpened = opened)
                else -> it
            }
        }
    }

    private fun setDifficultyPickerVisibility(opened: Boolean) {
        _state.update {
            when (it) {
                is ListScreenActionsState.Default -> it.copy(isDifficultyFilterDialogOpened = opened)
                else -> it
            }
        }
    }

    private fun setCategoryPickerVisibility(opened: Boolean) {
        _state.update {
            when (it) {
                is ListScreenActionsState.Default -> it.copy(isCategoryFilterDialogOpened = opened)
                else -> it
            }
        }
    }

    private suspend fun save() {}


}
