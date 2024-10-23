package com.example.courseworkandroidweeklyplanner.presentation.screens.main.week

import androidx.lifecycle.viewModelScope
import com.example.courseworkandroidweeklyplanner.domain.repository.WeekRepository
import com.example.courseworkandroidweeklyplanner.presentation.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeekScreenViewModel @Inject constructor(
    private val weekRepository: WeekRepository
) : BaseViewModel<WeekScreenState, WeekScreenAction>() {
    private val _state: MutableStateFlow<WeekScreenState> = MutableStateFlow(WeekScreenState.Initial)
    override val state: StateFlow<WeekScreenState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            weekRepository.getWeek().collect { week ->
                _state.update {
                    WeekScreenState.Default(week)
                }
            }
        }
    }

    override suspend fun execute(action: WeekScreenAction) = when (action) {
        WeekScreenAction.SelectNextWeek -> weekRepository.setNextWeek()
        WeekScreenAction.SelectPreviousWeek -> weekRepository.setPreviousWeek()
    }
}
