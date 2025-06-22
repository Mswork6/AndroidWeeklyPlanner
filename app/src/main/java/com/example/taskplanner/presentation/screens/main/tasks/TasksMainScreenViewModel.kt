package com.example.taskplanner.presentation.screens.main.tasks

import androidx.lifecycle.viewModelScope
import com.example.taskplanner.data.CelebratedDatesDataStore
import com.example.taskplanner.domain.eventBus.CelebrationEventBus
import com.example.taskplanner.domain.interactor.notification.NotificationEventBus
import com.example.taskplanner.domain.interactor.saver.TaskInteractor
import com.example.taskplanner.domain.model.Task
import com.example.taskplanner.domain.usecase.GetDaysUseCase
import com.example.taskplanner.domain.usecase.ToggleTaskStatusUseCase
import com.example.taskplanner.presentation.core.BaseViewModel
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
class TasksMainScreenViewModel @Inject constructor(
    private val getDays: GetDaysUseCase,
    private val toggleTaskStatus: ToggleTaskStatusUseCase,
    private val taskInteractor: TaskInteractor,
    private val celebratedDatesDataStore: CelebratedDatesDataStore,
    private val notificationEventBus: NotificationEventBus,
    private val celebrationEventBus: CelebrationEventBus
) : BaseViewModel<TasksMainScreenState, TasksMainScreenAction>() {
    private val _state: MutableStateFlow<TasksMainScreenState> = MutableStateFlow(TasksMainScreenState.Initial)
    override val state: StateFlow<TasksMainScreenState> = _state.asStateFlow()

    private val _playingDates = MutableStateFlow<Set<LocalDate>>(emptySet())

    private val actionDialogTask: MutableStateFlow<Task?> = MutableStateFlow(null)

    init {
        viewModelScope.launch {
            celebrationEventBus.events.collect { date ->
                _playingDates.update { it + date }
            }
        }

        viewModelScope.launch {
            combine(
                getDays(),
                celebratedDatesDataStore.celebratedDatesFlow,
                _playingDates,
                actionDialogTask
            ) { days, celebratedSet, playingSet, selectedTask ->
                    TasksMainScreenState.Default(
                        days = days,
                        celebratedDates = celebratedSet,
                        playingDates = playingSet,
                        dialogState = when (selectedTask) {
                            null -> TasksMainScreenState.TaskScreenDialogState.None
                            else -> TasksMainScreenState.TaskScreenDialogState.Opened(selectedTask)
                        }
                    )
            }.collect { screenState ->
                _state.update {
                    screenState
                }
            }
        }

        // Подписка на уведомления
        viewModelScope.launch {
            notificationEventBus.events.collect {
                actionDialogTask.value = null
            }
        }
    }

    override suspend fun execute(action: TasksMainScreenAction) = when (action) {
        is TasksMainScreenAction.ExpandDay -> {}
        is TasksMainScreenAction.TaskDialogAction -> actionDialogTask.update {
            when (action) {
                is TasksMainScreenAction.TaskDialogAction.Close -> null
                is TasksMainScreenAction.TaskDialogAction.Open -> action.task
            }
        }
        is TasksMainScreenAction.ToggleTaskStatus -> toggleTaskStatus(action.task)
        is TasksMainScreenAction.DeleteTask -> taskInteractor.deleteTask(action.task)
        is TasksMainScreenAction.StopEncouragingAnimation -> {
            _playingDates.update { it - action.date }
        }
    }
}