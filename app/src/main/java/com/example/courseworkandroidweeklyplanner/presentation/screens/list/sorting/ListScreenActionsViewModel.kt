package com.example.courseworkandroidweeklyplanner.presentation.screens.list.sorting

import androidx.lifecycle.viewModelScope
import com.example.courseworkandroidweeklyplanner.domain.NotificationEventBus
import com.example.courseworkandroidweeklyplanner.domain.repository.SortRepository
import com.example.courseworkandroidweeklyplanner.presentation.core.BaseViewModel
import com.example.courseworkandroidweeklyplanner.presentation.screens.main.sorting.MainScreenAction
import com.example.courseworkandroidweeklyplanner.presentation.screens.main.sorting.MainScreenActionsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListScreenActionsViewModel @Inject constructor(
    private val sortRepository: SortRepository,
    private val notificationEventBus: NotificationEventBus,
) : BaseViewModel<ListScreenActionsState, ListScreenAction>() {
    private val _state: MutableStateFlow<ListScreenActionsState> =
        MutableStateFlow(ListScreenActionsState.Initial)
    override val state: StateFlow<ListScreenActionsState> = _state.asStateFlow()

    private val sorterVisibility: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            sorterVisibility
                .combine(sortRepository.getSort()) { isSorterVisible, sort ->
                    ListScreenActionsState.Default(
                        sort = sort,
                        isSorterVisible = isSorterVisible
                    )
                }
                .collect { screenState ->
                    _state.update { screenState }
                }
        }

        // Подписка на событие уведомления
        viewModelScope.launch {
            notificationEventBus.events.collect {
                sorterVisibility.emit(false)
            }
        }
    }

    override suspend fun execute(action: ListScreenAction) = when (action) {
        is ListScreenAction.SetSorterVisibility -> sorterVisibility.emit(action.opened)
        is ListScreenAction.SetSort -> sortRepository.setSort(action.sort)
    }
}