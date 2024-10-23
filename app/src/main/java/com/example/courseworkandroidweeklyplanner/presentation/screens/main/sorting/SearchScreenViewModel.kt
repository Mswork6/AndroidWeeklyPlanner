package com.example.courseworkandroidweeklyplanner.presentation.screens.main.sorting

import androidx.lifecycle.viewModelScope
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
    private val weekRepository: WeekRepository,
    private val sortRepository: SortRepository
) : BaseViewModel<SearchScreenState, SearchScreenAction>() {
    private val _state: MutableStateFlow<SearchScreenState> = MutableStateFlow(SearchScreenState.Initial)
    override val state: StateFlow<SearchScreenState> = _state.asStateFlow()

    private val calendarVisibility: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val sorterVisibility: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            calendarVisibility
                .combine(sorterVisibility) { isCalendarVisible, isSorterVisible ->
                    Pair(isCalendarVisible, isSorterVisible)
                }.combine(sortRepository.getSort()) { (isCalendarVisible, isSorterVisible), sort ->
                    SearchScreenState.Default(
                        sort = sort,
                        isCalendarVisible = isCalendarVisible,
                        isSorterVisible = isSorterVisible
                    )
                }.collect { screenState ->
                    _state.update {
                        screenState
                    }
                }
        }
    }

    override suspend fun execute(action: SearchScreenAction) = when (action) {
        is SearchScreenAction.SetCalendarVisibility -> calendarVisibility.emit(action.opened)
        is SearchScreenAction.SetSorterVisibility -> sorterVisibility.emit(action.opened)
        is SearchScreenAction.SetDate -> weekRepository.setWeek(action.date)
        is SearchScreenAction.SetSort -> sortRepository.setSort(action.sort)
    }
}