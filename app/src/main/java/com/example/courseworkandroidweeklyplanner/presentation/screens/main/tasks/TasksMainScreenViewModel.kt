package com.example.courseworkandroidweeklyplanner.presentation.screens.main.tasks

import androidx.lifecycle.viewModelScope
import com.example.courseworkandroidweeklyplanner.domain.NotificationEventBus
import com.example.courseworkandroidweeklyplanner.domain.interactor.saver.TaskInteractor
import com.example.courseworkandroidweeklyplanner.domain.model.Task
import com.example.courseworkandroidweeklyplanner.domain.usecase.GetDaysUseCase
import com.example.courseworkandroidweeklyplanner.domain.usecase.ToggleTaskStatusUseCase
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
class TasksMainScreenViewModel @Inject constructor(
    private val getDays: GetDaysUseCase,
    private val toggleTaskStatus: ToggleTaskStatusUseCase,
    private val taskInteractor: TaskInteractor,
    private val notificationEventBus: NotificationEventBus
) : BaseViewModel<TasksMainScreenState, TasksMainScreenAction>() {
    private val _state: MutableStateFlow<TasksMainScreenState> = MutableStateFlow(TasksMainScreenState.Initial)
    override val state: StateFlow<TasksMainScreenState> = _state.asStateFlow()

    private val actionDialogTask: MutableStateFlow<Task?> = MutableStateFlow(null)

    init {
        viewModelScope.launch {
            getDays().combine(actionDialogTask) { days, selectedTask ->
                TasksMainScreenState.Default(
                    days = days,
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
        is TasksMainScreenAction.ToggleTaskStatus -> toggleTaskStatus(action.task)
        is TasksMainScreenAction.DeleteTask -> taskInteractor.deleteTask(action.task)
    }
}