package com.example.androidweeklyplanner.presentation.screens.main.tasks

import androidx.lifecycle.viewModelScope
import com.example.androidweeklyplanner.data.CelebratedDatesDataStore
import com.example.androidweeklyplanner.domain.NotificationEventBus
import com.example.androidweeklyplanner.domain.interactor.saver.TaskInteractor
import com.example.androidweeklyplanner.domain.model.Task
import com.example.androidweeklyplanner.domain.usecase.GetDaysUseCase
import com.example.androidweeklyplanner.domain.usecase.ToggleTaskStatusUseCase
import com.example.androidweeklyplanner.presentation.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksMainScreenViewModel @Inject constructor(
    private val getDays: GetDaysUseCase,
    private val toggleTaskStatus: ToggleTaskStatusUseCase,
    private val taskInteractor: TaskInteractor,
    private val celebratedDatesDataStore: CelebratedDatesDataStore,
    private val notificationEventBus: NotificationEventBus
) : BaseViewModel<TasksMainScreenState, TasksMainScreenAction>() {
    private val _state: MutableStateFlow<TasksMainScreenState> = MutableStateFlow(TasksMainScreenState.Initial)
    override val state: StateFlow<TasksMainScreenState> = _state.asStateFlow()

    private val actionDialogTask: MutableStateFlow<Task?> = MutableStateFlow(null)

    init {
        viewModelScope.launch {
            getDays()
                .combine(celebratedDatesDataStore.celebratedDatesFlow) { days, celebratedSet ->
                    days to celebratedSet
                }
                .combine(actionDialogTask) { (days, celebratedSet), selectedTask ->
                    TasksMainScreenState.Default(
                        days = days,
                        celebratedDates = celebratedSet,
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

        // Подписка на событие уведомления
        viewModelScope.launch {
            notificationEventBus.events.collect {
                // Закрытие любойго открытого диалога
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
        is TasksMainScreenAction.ToggleTaskStatus -> {
            toggleTaskStatus(action.task)

            val date = action.task.date
            val daysNow = getDays().first()
            val dayNow = daysNow.find { it.date == date }
            val allDone = dayNow?.tasks?.all { it.isDone } ?: false

            if (!allDone) {
                celebratedDatesDataStore.unmarkDateCelebrated(date)
            } else {}
        }
        is TasksMainScreenAction.DeleteTask -> taskInteractor.deleteTask(action.task)
        is TasksMainScreenAction.CelebrateDay ->
            celebratedDatesDataStore.markDateCelebrated(action.date)
        is TasksMainScreenAction.UncelebrateDay -> {
            celebratedDatesDataStore.unmarkDateCelebrated(action.date)
        }
    }
}