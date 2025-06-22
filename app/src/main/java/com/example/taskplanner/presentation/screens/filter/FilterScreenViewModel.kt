package com.example.taskplanner.presentation.screens.filter

import androidx.lifecycle.viewModelScope
import com.example.taskplanner.domain.FilterScreenSortRepo
import com.example.taskplanner.domain.interactor.notification.NotificationEventBus
import com.example.taskplanner.domain.model.Category
import com.example.taskplanner.domain.model.Difficulty
import com.example.taskplanner.domain.model.FilterConfig
import com.example.taskplanner.domain.model.Priority
import com.example.taskplanner.domain.model.SortConfig
import com.example.taskplanner.domain.model.SortType
import com.example.taskplanner.domain.repository.FilterRepository
import com.example.taskplanner.domain.repository.SortRepository
import com.example.taskplanner.presentation.convertToLocalDate
import com.example.taskplanner.presentation.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterScreenViewModel @Inject constructor(
    private val filterRepository: FilterRepository,
    @FilterScreenSortRepo private val sortRepository: SortRepository,
    private val notificationEventBus: NotificationEventBus,
) : BaseViewModel<FilterScreenState, FilterScreenActions>() {
    private val _state: MutableStateFlow<FilterScreenState> =
        MutableStateFlow(FilterScreenState.Initial)
    override val state: StateFlow<FilterScreenState> = _state.asStateFlow()


    init {
        viewModelScope.launch {
            combine(
                filterRepository.getFilterConfig(),
                sortRepository.getSortConfig()
            ) { filterConfig, sortConfig ->
                FilterScreenState.Default(
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

    override suspend fun execute(action: FilterScreenActions) = when (action) {
        is FilterScreenActions.SetDateFilter -> setDateFilter(action.startDate, action.endDate)
        is FilterScreenActions.SetPriorityFilter -> setPriorityFilter(action.priorityFilter)
        is FilterScreenActions.SetDifficultyFilter -> setDifficultyFilter(action.difficultyFilter)
        is FilterScreenActions.SetCategoryFilter -> setCategoryFilter(action.categoryFilter)
        is FilterScreenActions.SetPriorityOrder -> setPriorityOrder(action.priorityOrder)
        is FilterScreenActions.SetDifficultyOrder -> setDifficultyOrder(action.difficultyOrder)
        is FilterScreenActions.SetDatePickerVisibility -> setDatePickerVisibility(action.opened)
        is FilterScreenActions.SetPriorityPickerVisibility -> setPriorityPickerVisibility(action.opened)
        is FilterScreenActions.SetDifficultyPickerVisibility -> setDifficultyPickerVisibility(action.opened)
        is FilterScreenActions.SetCategoryPickerVisibility -> setCategoryPickerVisibility(action.opened)
        is FilterScreenActions.ResetValues -> resetValues()
        is FilterScreenActions.Save -> save()

    }

    private fun setDateFilter(startDate: Long?, endDate: Long?) {
        _state.update {
            when (it) {
                is FilterScreenState.Default -> it.copy(
                    startDate = startDate?.let { convertToLocalDate(startDate) },
                    endDate = endDate?.let { convertToLocalDate(endDate) }
                )

                else -> it
            }
        }
    }

    private fun setPriorityFilter(priorityFilter: Set<Priority>) {
        _state.update {
            when (it) {
                is FilterScreenState.Default -> it.copy(selectedPriorities = priorityFilter)
                else -> it
            }
        }
    }

    private fun setDifficultyFilter(difficultyFilter: Set<Difficulty>) {
        _state.update {
            when (it) {
                is FilterScreenState.Default -> it.copy(selectedDifficulties = difficultyFilter)
                else -> it
            }
        }
    }

    private fun setCategoryFilter(categoryFilter: Set<Category>) {
        _state.update {
            when (it) {
                is FilterScreenState.Default -> it.copy(selectedCategories = categoryFilter)
                else -> it
            }
        }
    }

    private fun setPriorityOrder(priorityOrder: SortType) {
        _state.update {
            when (it) {
                is FilterScreenState.Default -> it.copy(sortPriorityOrder = priorityOrder)
                else -> it
            }
        }
    }

    private fun setDifficultyOrder(difficultyOrder: SortType) {
        _state.update {
            when (it) {
                is FilterScreenState.Default -> it.copy(sortDifficultyOrder = difficultyOrder)
                else -> it
            }
        }
    }

    private fun setDatePickerVisibility(opened: Boolean) {
        _state.update {
            when (it) {
                is FilterScreenState.Default -> it.copy(isDatePickerOpened = opened)
                else -> it
            }
        }
    }

    private fun setPriorityPickerVisibility(opened: Boolean) {
        _state.update {
            when (it) {
                is FilterScreenState.Default -> it.copy(isPriorityFilterDialogOpened = opened)
                else -> it
            }
        }
    }

    private fun setDifficultyPickerVisibility(opened: Boolean) {
        _state.update {
            when (it) {
                is FilterScreenState.Default -> it.copy(isDifficultyFilterDialogOpened = opened)
                else -> it
            }
        }
    }

    private fun setCategoryPickerVisibility(opened: Boolean) {
        _state.update {
            when (it) {
                is FilterScreenState.Default -> it.copy(isCategoryFilterDialogOpened = opened)
                else -> it
            }
        }
    }

    private fun resetValues() {
        _state.update {
            when (it) {
                is FilterScreenState.Default -> it.copy(
                    startDate = null,
                    endDate = null,
                    selectedPriorities = emptySet(),
                    selectedDifficulties = emptySet(),
                    selectedCategories = emptySet(),
                    sortPriorityOrder = SortType.STANDARD,
                    sortDifficultyOrder = SortType.STANDARD
                )

                else -> it
            }
        }
    }

    private suspend fun save() {
        val state = state.value
        if (state is FilterScreenState.Default) {
            val filterConfig = FilterConfig(
                startDate = state.startDate,
                endDate = state.endDate,
                priorityFilter = state.selectedPriorities,
                difficultyFilter = state.selectedDifficulties,
                categoryFilter = state.selectedCategories
            )
            val sortConfig = SortConfig(
                priorityOrder = state.sortPriorityOrder,
                difficultyOrder = state.sortDifficultyOrder
            )
            filterRepository.setFilterConfig(filterConfig)
            sortRepository.setSortConfig(sortConfig)
        }
    }
}

