package com.example.androidweeklyplanner.presentation.screens.main.week

import androidx.lifecycle.viewModelScope
import com.example.androidweeklyplanner.domain.interactor.notification.NotificationEventBus
import com.example.androidweeklyplanner.domain.repository.WeekRepository
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
class WeekScreenViewModel @Inject constructor(
    private val weekRepository: WeekRepository,
    private val notificationEventBus: NotificationEventBus
) : BaseViewModel<WeekScreenState, WeekScreenAction>() {

    private val _state = MutableStateFlow<WeekScreenState>(WeekScreenState.Initial)
    override val state: StateFlow<WeekScreenState> = _state.asStateFlow()

    private val calendarVisibility: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            calendarVisibility
                .combine(weekRepository.getWeek()) { isCalendarVisible, week ->
                    WeekScreenState.Default(
                        week = week,
                        isCalendarVisible = isCalendarVisible
                    )
                }
                .collect { newState ->
                    _state.update { newState }
                }
        }

        viewModelScope.launch {
            notificationEventBus.events.collect {
                calendarVisibility.emit(false)
            }
        }
    }

    override suspend fun execute(action: WeekScreenAction) {
        when (action) {
            WeekScreenAction.SelectNextWeek -> weekRepository.setNextWeek()
            WeekScreenAction.SelectPreviousWeek -> weekRepository.setPreviousWeek()
            is WeekScreenAction.SetCalendarVisibility ->
                calendarVisibility.emit(action.opened)
            is WeekScreenAction.SetDate -> weekRepository.setWeek(action.date)
        }
    }
}
