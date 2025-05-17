package com.example.courseworkandroidweeklyplanner.presentation.screens.main.sorting

import androidx.lifecycle.viewModelScope
import com.example.courseworkandroidweeklyplanner.domain.NotificationEventBus
import com.example.courseworkandroidweeklyplanner.domain.repository.SortRepository
import com.example.courseworkandroidweeklyplanner.domain.repository.WeekRepository
import com.example.courseworkandroidweeklyplanner.presentation.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val sortRepository: SortRepository,
    private val notificationEventBus: NotificationEventBus,
) : BaseViewModel<SearchScreenState, SearchScreenAction>() {
    private val _state: MutableStateFlow<SearchScreenState> =
        MutableStateFlow(SearchScreenState.Initial)
    override val state: StateFlow<SearchScreenState> = _state.asStateFlow()

    private val sorterVisibility: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            sorterVisibility
                .combine(sortRepository.getSort()) { isSorterVisible, sort ->
                    SearchScreenState.Default(
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

    override suspend fun execute(action: SearchScreenAction) = when (action) {
        is SearchScreenAction.SetSorterVisibility -> sorterVisibility.emit(action.opened)
        is SearchScreenAction.SetSort -> sortRepository.setSort(action.sort)
    }
}