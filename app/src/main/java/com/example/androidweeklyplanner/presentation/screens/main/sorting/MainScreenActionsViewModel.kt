package com.example.androidweeklyplanner.presentation.screens.main.sorting

import androidx.lifecycle.viewModelScope
import com.example.androidweeklyplanner.domain.interactor.notification.NotificationEventBus
import com.example.androidweeklyplanner.domain.repository.SortRepository
import com.example.androidweeklyplanner.presentation.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenActionsViewModel @Inject constructor(
    private val sortRepository: SortRepository,
    private val notificationEventBus: NotificationEventBus,
) : BaseViewModel<MainScreenActionsState, MainScreenAction>() {
    private val _state: MutableStateFlow<MainScreenActionsState> =
        MutableStateFlow(MainScreenActionsState.Initial)
    override val state: StateFlow<MainScreenActionsState> = _state.asStateFlow()

    private val sorterVisibility: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            sorterVisibility
                .combine(sortRepository.getSortConfig()) { isSorterVisible, config ->
                    MainScreenActionsState.Default(
                        sortConfig = config,
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

    override suspend fun execute(action: MainScreenAction) = when (action) {
        is MainScreenAction.SetSorterVisibility -> sorterVisibility.emit(action.opened)
        is MainScreenAction.SetSort -> sortRepository.setSortConfig(action.sortConfig)
    }
}