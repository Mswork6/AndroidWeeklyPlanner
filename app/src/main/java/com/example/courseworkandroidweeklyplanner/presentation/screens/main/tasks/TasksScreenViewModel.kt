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
class TasksScreenViewModel @Inject constructor(
    private val getDays: GetDaysUseCase,
    private val toggleTaskStatus: ToggleTaskStatusUseCase,
    private val taskInteractor: TaskInteractor,
    private val notificationEventBus: NotificationEventBus
) : BaseViewModel<TasksScreenState, TasksScreenAction>() {
    private val _state: MutableStateFlow<TasksScreenState> = MutableStateFlow(TasksScreenState.Initial)
    override val state: StateFlow<TasksScreenState> = _state.asStateFlow()

    private val actionDialogTask: MutableStateFlow<Task?> = MutableStateFlow(null)

    init {
        viewModelScope.launch {
            getDays().combine(actionDialogTask) { days, selectedTask ->
                TasksScreenState.Default(
                    days = days,
                    dialogState = when (selectedTask) {
                        null -> TasksScreenState.TaskScreenDialogState.None
                        else -> TasksScreenState.TaskScreenDialogState.Opened(selectedTask)
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

    override suspend fun execute(action: TasksScreenAction) = when (action) {
        is TasksScreenAction.ExpandDay -> {}
        is TasksScreenAction.TaskDialogAction -> actionDialogTask.update {
            when (action) {
                is TasksScreenAction.TaskDialogAction.Close -> null
                is TasksScreenAction.TaskDialogAction.Open -> action.task
            }
        }
        is TasksScreenAction.ToggleTaskStatus -> toggleTaskStatus(action.task)
        is TasksScreenAction.DeleteTask -> taskInteractor.deleteTask(action.task)
    }
}